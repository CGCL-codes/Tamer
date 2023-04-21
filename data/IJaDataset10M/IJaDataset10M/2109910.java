package k9;

class TimeDate extends TimePoint {

    boolean relative = false;

    boolean mars_time = false;

    ;

    static int MarsSecondsPerDay = 24 * 3600;

    static int EarthSecondsPerDay = 24 * 3600;

    static int SecondsPerHour = 3600;

    static int SecondsPerMinute = 60;

    boolean isrelative() {
        return relative;
    }

    boolean isMarsTime() {
        return mars_time;
    }

    public TimeDate(long secs, int msec) {
        super(secs, msec);
    }

    public TimeDate(long secs, int msec, boolean xrelative) {
        super(secs, msec);
        relative = xrelative;
    }

    public TimeDate(long val) {
        super(val);
    }

    public TimeDate() {
    }

    public TimeDate(int val, int val2, boolean xrelative) {
        super(val, val2);
        relative = xrelative;
    }

    public TimeDate(int val, int val2) {
        super(val, val2);
    }

    public TimeDate(double val) {
        super(val);
    }

    public TimeDate(int sym) {
        super(sym);
    }

    public TimeDate(TimeDate td) {
        initTimePointFromTimePoint(td, SymbolicTimeUndefined);
        relative = td.relative;
    }
}
