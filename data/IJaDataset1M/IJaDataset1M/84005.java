package net.ar.guia.own.adapters.data;

public class BooleanStringAdapter implements StringAdapter {

    public String getStringValue(Object anObject) {
        return anObject.toString();
    }

    public Object getAdaptedValue(String aString) {
        return new Boolean(aString);
    }
}
