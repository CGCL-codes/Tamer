package org.sodeja.silan;

public class SILIndexedObject implements SILObject {

    private final SILClass type;

    private final SILObject[] values;

    public SILIndexedObject(SILClass type, int size) {
        this.type = type;
        this.values = new SILObject[size];
    }

    public SILIndexedObject(SILClass type, SILObject[] values) {
        this.type = type;
        this.values = values;
    }

    public int length() {
        return values.length;
    }

    public SILObject at(int index) {
        return values[index - 1];
    }

    public void at_put(int index, SILObject obj) {
        values[index - 1] = obj;
    }

    @Override
    public SILClass getType() {
        return type;
    }

    @Override
    public SILObject copy() {
        SILIndexedObject copy = new SILIndexedObject(type, values.length);
        for (int i = 0; i < values.length; i++) {
            copy.values[i] = values[i].copy();
        }
        return copy;
    }

    @Override
    public SILObject get(String reference) {
        int index = Integer.parseInt(reference);
        if (index <= 0 || index > values.length) {
            return null;
        }
        return this.values[index - 1];
    }

    @Override
    public void set(String reference, SILObject value) {
        int index = Integer.parseInt(reference);
        if (index <= 0 || index > values.length) {
            throw new UnsupportedOperationException("Global variable set.");
        }
        this.values[index - 1] = value;
    }
}
