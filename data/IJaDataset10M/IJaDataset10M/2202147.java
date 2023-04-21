package barsuift.simLife.time;

import javax.xml.bind.annotation.XmlRootElement;
import barsuift.simLife.State;

@XmlRootElement
public class DateHandlerState implements State {

    private SimLifeDateState date;

    public DateHandlerState() {
        super();
        this.date = new SimLifeDateState();
    }

    public DateHandlerState(SimLifeDateState date) {
        super();
        this.date = date;
    }

    public SimLifeDateState getDate() {
        return date;
    }

    public void setDate(SimLifeDateState date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        DateHandlerState other = (DateHandlerState) obj;
        if (date == null) {
            if (other.date != null) return false;
        } else if (!date.equals(other.date)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "DateHandlerState [date=" + date + "]";
    }
}
