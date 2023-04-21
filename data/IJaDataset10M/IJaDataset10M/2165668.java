package at.nullpointer.trayrss.configuration.timeframes;

/**
 * Represents the days TrayRSS should monitor the feeds
 * 
 * @author thefake
 *
 */
public class Week {

    private boolean mon, tue, wed, thu, fri, sat, sun;

    public void setMon(boolean mon) {
        this.mon = mon;
    }

    public boolean isMon() {
        return this.mon;
    }

    public void setTue(boolean tue) {
        this.tue = tue;
    }

    public boolean isTue() {
        return this.tue;
    }

    public void setWed(boolean wed) {
        this.wed = wed;
    }

    public boolean isWed() {
        return this.wed;
    }

    public void setThu(boolean thu) {
        this.thu = thu;
    }

    public boolean isThu() {
        return this.thu;
    }

    public void setFri(boolean fri) {
        this.fri = fri;
    }

    public boolean isFri() {
        return this.fri;
    }

    public void setSat(boolean sat) {
        this.sat = sat;
    }

    public boolean isSat() {
        return this.sat;
    }

    public void setSun(boolean sun) {
        this.sun = sun;
    }

    public boolean isSun() {
        return this.sun;
    }
}
