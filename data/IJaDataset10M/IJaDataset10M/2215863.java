package de.laures.cewolf.taglib;

/**
 * Contains some base constants to avoid explicit dependancy to concrete chart 
 * implementation's constant values. The constants of this class also serve as
 * the base contract for data exchange between sub packages.
 * @author  Guido Laures
 */
public interface ChartConstants {

    int AREA = 0;

    int AREA_XY = 1;

    int HORIZONTAL_BAR = 2;

    int HORIZONTAL_BAR_3D = 3;

    int LINE = 4;

    int PIE = 5;

    int SCATTER = 6;

    int STACKED_HORIZONTAL_BAR = 7;

    int STACKED_VERTICAL_BAR = 8;

    int STACKED_VERTICAL_BAR_3D = 9;

    int TIME_SERIES = 10;

    int VERTICAL_BAR = 11;

    int VERTICAL_BAR_3D = 12;

    int XY = 13;

    int CANDLE_STICK = 14;

    int HIGH_LOW = 15;

    int GANTT = 16;

    int WIND = 17;

    int VERTICAL_XY_BAR = 18;

    int PIE_3D = 19;

    int OVERLAY_XY = 20;

    int OVERLAY_CATEGORY = 21;

    int COMBINED_XY = 22;

    int METER = 23;

    int STACKED_AREA = 24;

    int BUBBLE = 25;

    int SPLINE = 26;

    int HISTOGRAM = 27;

    int THERMOMETER = 28;

    int DIAL = 29;

    int COMPASS = 30;

    int SPIDERWEB = 31;

    int STACKED_HORIZONTAL_BAR_3D = 32;

    int WAFER = 33;
}
