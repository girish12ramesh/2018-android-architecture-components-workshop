package fr.ekito.myweatherapp.view.detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import fr.ekito.myweatherapp.data.repository.WeatherRepository
import fr.ekito.myweatherapp.domain.DailyForecastModel
import fr.ekito.myweatherapp.util.mvvm.RxViewModel
import fr.ekito.myweatherapp.util.rx.SchedulerProvider
import fr.ekito.myweatherapp.util.rx.with
import fr.ekito.myweatherapp.view.ErrorState
import fr.ekito.myweatherapp.view.LoadingState
import fr.ekito.myweatherapp.view.State

class DetailViewModel : RxViewModel() {

    /*
     * Dependencies out of constructor. Will be filled later by DetailActivity
     */
    lateinit var weatherRepository: WeatherRepository
    lateinit var schedulerProvider: SchedulerProvider

    private val mStates = MutableLiveData<State>()
    val states: LiveData<State>
        get() = mStates

    fun getDetail(id: String) {
        mStates.value = LoadingState
        launch {
            weatherRepository.getWeatherDetail(id).with(schedulerProvider).subscribe(
                { detail ->
                    mStates.value = WeatherDetailState(detail)
                }, { error -> mStates.value = ErrorState(error) })
        }
    }

    data class WeatherDetailState(val weather: DailyForecastModel) : State()
}