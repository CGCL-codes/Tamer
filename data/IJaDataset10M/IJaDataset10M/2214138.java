package ch.fork.AdHocRailway.domain.turnouts;

public class TurnoutAddress {

    private int address;

    private int bus;

    private boolean switched;

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TurnoutAddress t = (TurnoutAddress) o;
        if (address != t.getAddress()) return false;
        if (bus != t.getBus()) return false;
        if (switched != t.isSwitched()) return false;
        return true;
    }

    public int hashCode() {
        return address + bus;
    }

    public String toString() {
        return "[" + bus + "|" + address + "|" + switched + "]";
    }

    /** default constructor */
    public TurnoutAddress() {
    }

    /** full constructor */
    public TurnoutAddress(int address, int bus, boolean switched) {
        this.address = address;
        this.bus = bus;
        this.switched = switched;
    }

    public int getAddress() {
        return this.address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getBus() {
        return this.bus;
    }

    public void setBus(int bus) {
        this.bus = bus;
    }

    public boolean isSwitched() {
        return this.switched;
    }

    public void setSwitched(boolean switched) {
        this.switched = switched;
    }
}
