package de.denkselbst.sentrick.sbd.rulebased;

public class SingleAssignmentVariable<T> {

    private T val;

    public boolean bind(T value) {
        if (value == null) return false; else if (val == null) {
            val = value;
            return true;
        } else return val.equals(value);
    }

    public T value() {
        return val;
    }

    public boolean isBound() {
        return val != null;
    }
}
