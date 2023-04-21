package yapgen.base.type;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author riccardo
 */
public class DurationType implements Type {

    private Long seconds;

    public void setYears(int years) {
        this.seconds = (long) (12 * 30 * 24 * 3600 * years);
    }

    public void setMonths(int months) {
        this.seconds = (long) (30 * 24 * 3600 * months);
    }

    public void setDays(int days) {
        this.seconds = (long) (24 * 3600 * days);
    }

    public void setHours(int hours) {
        this.seconds = (long) (3600 * hours);
    }

    public void setMinutes(int minutes) {
        this.seconds = (long) (60 * minutes);
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    public int getYears() {
        return (int) (seconds / (12 * 30 * 24 * 3600));
    }

    public int getMonths() {
        return (int) (seconds / (30 * 24 * 3600));
    }

    public int getDays() {
        return (int) (seconds / (24 * 3600));
    }

    public int getHours() {
        return (int) (seconds / 3600);
    }

    public int getMinutes() {
        return (int) (seconds / 60);
    }

    public long getSeconds() {
        return seconds;
    }

    @Override
    public void valueFromString(String stringValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String valueToString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getValue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object clone() {
        Object ret = null;
        try {
            ret = super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(DurationType.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    @Override
    public String toXml() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
