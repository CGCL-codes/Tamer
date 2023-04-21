package com.juryrig.couchweather;

import java.util.Calendar;
import java.util.Collection;

@SuppressWarnings("serial")
public class IndoorHumidityPane extends AbstractTrendPane {

    private Settings settings;

    private WeatherDB dbConnection;

    public IndoorHumidityPane(Settings settings, WeatherDB dbConnection) {
        this.settings = settings;
        this.dbConnection = dbConnection;
        initialize("Indoor Humidity Range", "Indoor Humidity", settings);
    }

    @Override
    public double convertDataPoint(double data) {
        return data;
    }

    @Override
    public String displayData(double data) {
        return settings.humidityDisplay(data);
    }

    @Override
    public double getDataPoint(Conditions conditions) {
        return conditions.getHumidityIndoors();
    }

    @Override
    public Collection<Conditions> getAllData() {
        return dbConnection.getHistoricalData();
    }

    @Override
    public Calendar getOldestDate() {
        return (Calendar) dbConnection.getOldestDate().clone();
    }

    @Override
    public double invalidValue() {
        return Double.MIN_VALUE;
    }

    @Override
    public double getMinimumRange() {
        return settings.getHumidMinRange();
    }

    @Override
    public boolean haveData() {
        return dbConnection.haveData();
    }

    @Override
    public String getOldestLoaded() {
        return dbConnection.getOldestLoadedString();
    }

    @Override
    public boolean doSecondDataSet() {
        return false;
    }

    @Override
    public double getSecondDataPoint(Conditions conditions) {
        return Double.MIN_VALUE;
    }
}
