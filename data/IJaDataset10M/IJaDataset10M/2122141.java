package org.nicocube.airain.domain.client.gamedate;

public abstract class AbstractGameDate {

    protected AbstractGameDate() {
    }

    public enum Season {

        Spring, Summer, Autumn, Winter
    }

    protected static final int game_hours_seconds = 3600;

    protected static final int game_day_seconds = 24 * game_hours_seconds;

    protected static final int game_moon_seconds = 28 * game_day_seconds;

    protected static final long game_year_seconds = 12 * game_moon_seconds;

    public abstract long getTimestamp();

    protected abstract void setTimestamp(long timestamp);

    public abstract Season getSeason();

    public abstract int getHours();

    public abstract int getMoonDays();

    public abstract long getTotalDays();

    public abstract long getTotalMoons();

    public abstract int getYearDays();

    public abstract int getYearMoons();

    public abstract long getYears();
}
