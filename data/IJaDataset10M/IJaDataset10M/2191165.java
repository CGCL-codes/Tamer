package net.metasimian.test.axis2;

import junit.framework.TestCase;

public class WeatherForecastStubTest extends TestCase {

    public void testGetWeatherByZipCode() throws Exception {
        WeatherForecastStub ws = new WeatherForecastStub();
        WeatherForecastStub.GetWeatherByZipCode request = new WeatherForecastStub.GetWeatherByZipCode();
        request.setZipCode("97233");
        WeatherForecastStub.GetWeatherByZipCodeResponse response = ws.GetWeatherByZipCode(request);
        System.out.println("response.getGetWeatherByZipCodeResult().getLatitude(): " + response.getGetWeatherByZipCodeResult().getLatitude());
        System.out.println("response.getGetWeatherByZipCodeResult().getLongitude(): " + response.getGetWeatherByZipCodeResult().getLongitude());
        System.out.println("response.getGetWeatherByZipCodeResult().getFipsCode(): " + response.getGetWeatherByZipCodeResult().getFipsCode());
    }
}
