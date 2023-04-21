package net.sf.bt747.waba.system;

import bt747.sys.Settings;
import bt747.sys.interfaces.BT747Date;

/**
 * @author Mario De Weerd
 */
public final class WabaDate extends waba.util.Date implements BT747Date {

    /**
     * 
     */
    public WabaDate() {
        super();
    }

    /**
     * @param sentDay
     * @param sentMonth
     * @param sentYear
     */
    public WabaDate(final int sentDay, final int sentMonth, final int sentYear) {
        super(sentDay, sentMonth, sentYear);
    }

    private static byte dateFormatToWaba(final byte dateFormat) {
        switch(dateFormat) {
            case Settings.DATE_DMY:
                return waba.sys.Settings.DATE_DMY;
            case Settings.DATE_YMD:
                return waba.sys.Settings.DATE_YMD;
            default:
                return waba.sys.Settings.DATE_DMY;
        }
    }

    /**
     * @param strDate
     * @param dateFormat
     */
    public WabaDate(final String strDate, final byte dateFormat) {
        super(strDate, dateFormatToWaba(dateFormat));
    }

    public final String getDateString() {
        return getDate();
    }

    /**
     * Constant offset to correct date from getJulianDay to 1970 epoch.
     */
    private static final int JULIAN_DAY_1_1_1970_OFFSET = (new waba.util.Date(1, 1, 1970)).getJulianDay();

    /**
     * Number of seconds in a day.
     */
    private static final int SECONDS_IN_DAY = 24 * 60 * 60;

    public final int dateToUTCepoch1970() {
        return (getJulianDay() - JULIAN_DAY_1_1_1970_OFFSET) * SECONDS_IN_DAY;
    }
}
