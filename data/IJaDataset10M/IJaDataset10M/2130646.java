package dev.weather;

import java.util.HashMap;
import java.util.Map;
import flex.messaging.FlexContext;
import flex.messaging.FlexSession;
import flex.messaging.MessageException;

public class WeatherService {

    public WeatherInfo getWeatherInfo(String zipCode) {
        Integer.parseInt(zipCode);
        WeatherInfo weatherInfo = new WeatherInfo();
        try {
            if (zipCode.startsWith("0")) {
                weatherInfo = getBostonWeather();
            } else if (zipCode.startsWith("9")) {
                weatherInfo = getSanFranciscoWeather();
            } else {
                weatherInfo = getDummyWeather();
            }
        } catch (NumberFormatException nfe) {
            weatherInfo = getDummyWeather();
        }
        return weatherInfo;
    }

    public String getTemperature(String zipCode) {
        return getWeatherInfo(zipCode).getTemperature();
    }

    public Map getWeatherMap(String zipCode) {
        HashMap map = new HashMap();
        WeatherInfo info = getWeatherInfo(zipCode);
        map.put("location", info.getLocation());
        map.put("temperature", info.getTemperature());
        map.put("forecast", info.getForecast());
        return map;
    }

    public String getTemperatureWithClose(String zipCode) {
        FlexSession session = FlexContext.getFlexSession();
        return getTemperature(zipCode);
    }

    public String generateMessageExceptionWithExtendedData(String extraData) {
        MessageException me = new MessageException("Testing extendedData.");
        Map extendedData = new HashMap();
        extendedData.put("extraData", extraData);
        me.setExtendedData(extendedData);
        me.setCode("999");
        me.setDetails("Some custom details.");
        throw me;
    }

    private WeatherInfo getBostonWeather() {
        WeatherInfo weatherInfo = new WeatherInfo();
        weatherInfo.setLocation("Boston, MA");
        weatherInfo.setTemperature("80");
        weatherInfo.setForecast("Sunny");
        weatherInfo.setExtendedForecast("sunny", "sunny", "sunny", "cloudy", "cloudy");
        return weatherInfo;
    }

    private WeatherInfo getSanFranciscoWeather() {
        WeatherInfo weatherInfo = new WeatherInfo();
        weatherInfo.setLocation("San Francisco, CA");
        weatherInfo.setTemperature("50");
        weatherInfo.setForecast("Rainy");
        weatherInfo.setExtendedForecast("cloudy", "cloudy", "cloudy", "rainy", "rainy");
        return weatherInfo;
    }

    private WeatherInfo getDummyWeather() {
        WeatherInfo weatherInfo = new WeatherInfo();
        weatherInfo.setLocation("Any Where, XY");
        weatherInfo.setTemperature("70");
        weatherInfo.setForecast("Clear");
        return weatherInfo;
    }
}
