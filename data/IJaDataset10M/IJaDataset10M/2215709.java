package org.jazzteam.edu.patterns.observerWeatherStation.main;

import org.jazzteam.edu.patterns.observerWeatherStation.observers.CurrentConditionDisplay;
import org.jazzteam.edu.patterns.observerWeatherStation.observers.ForecastDisplay;
import org.jazzteam.edu.patterns.observerWeatherStation.observers.StatisticsDisplay;
import org.jazzteam.edu.patterns.observerWeatherStation.subject.WeatherData;

/**
 * @author Hor1zont
 *
 */
public class Test {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        WeatherData weatherData = new WeatherData();
        CurrentConditionDisplay currentConditionDisplay = new CurrentConditionDisplay(weatherData);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherData);
        StatisticsDisplay statisticsDisplay = new StatisticsDisplay(weatherData);
        weatherData.setMeasurements(40, 65, 45f);
        weatherData.setMeasurements(60, 75, 40f);
        weatherData.setMeasurements(80, 15, 40.8f);
        weatherData.setMeasurements(100, 65, 45.8f);
        weatherData.setMeasurements(20, 75, 45.1f);
    }
}
