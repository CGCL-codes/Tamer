package bt747.sys.interfaces;

public interface BT747Date {

    /**
     * Advance the time.
     * 
     * @param s
     *                The number of days to advance.
     */
    void advance(final int s);

    /**
     * Return the date in the format usually used in the GPS devices: number
     * of seconds since the epoch 1970.
     * 
     * @return number of seconds since the epoch.
     */
    int dateToUTCepoch1970();

    String getDateString();

    int getYear();

    int getMonth();

    int getDay();
}
