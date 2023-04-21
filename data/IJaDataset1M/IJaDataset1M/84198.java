package net.ar.guia.own.adapters.data;

public class FloatStringAdapter implements StringAdapter {

    public String getStringValue(Object anObject) {
        return anObject.toString();
    }

    public Object getAdaptedValue(String aString) {
        return aString.length() > 0 ? new Float(aString) : null;
    }
}
