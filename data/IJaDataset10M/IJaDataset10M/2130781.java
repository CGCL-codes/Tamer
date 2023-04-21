package logic;

public class Event implements java.lang.Comparable, java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private int time, pinThatWasJustUpdated, updateTo;

    Gate fromGate;

    Event(int time, Gate fromGate, int pinThatWasJustUpdated, int updateTo) {
        this.time = time;
        this.fromGate = fromGate;
        this.pinThatWasJustUpdated = pinThatWasJustUpdated;
        this.updateTo = updateTo;
    }

    public int getTime() {
        return time;
    }

    public void display() {
        String tidiedUpGateName = fromGate.toString().substring(fromGate.toString().indexOf('.') + 1, fromGate.toString().indexOf('@'));
        System.out.println("Time: " + time + "\tGate: " + tidiedUpGateName + fromGate.uniqueGateID() + "\tPinUpdated: " + pinThatWasJustUpdated + "\tUpdateTo: " + updateTo);
    }

    public Gate getFromGate() {
        return fromGate;
    }

    public int getGateUniqueID() {
        return fromGate.uniqueGateID();
    }

    public int isUpdatingToValue() {
        return updateTo;
    }

    public int pinThatWasJustUpdated() {
        return pinThatWasJustUpdated;
    }

    public int compareTo(Object e) {
        Event that = (Event) e;
        int a = this.getTime();
        int b = that.getTime();
        if (a > b) return 1;
        if (a < b) return -1;
        return 0;
    }
}
