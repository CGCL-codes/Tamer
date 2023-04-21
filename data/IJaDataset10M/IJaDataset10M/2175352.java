package barsuift.simLife.time;

import javax.xml.bind.annotation.XmlRootElement;
import barsuift.simLife.State;

@XmlRootElement
public class SimLifeDateState implements State {

    private long value;

    public SimLifeDateState() {
        value = 0;
    }

    public SimLifeDateState(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SimLifeDateState [value=" + value + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (value ^ (value >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        SimLifeDateState other = (SimLifeDateState) obj;
        if (value != other.value) return false;
        return true;
    }
}
