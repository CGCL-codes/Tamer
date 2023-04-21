package com.cibertec.proydawi.utils;

public class Entidad {

    public static final int EINT = 0;

    public static final int EFLOAT = 1;

    public static final int ESTRING = 2;

    public static final int EBOOLEAN = 3;

    public static final int EDOUBLE = 4;

    private String key;

    private Object value;

    private int type;

    public Entidad(String $key, Object $value, int $type) {
        key = $key;
        value = $value;
        type = $type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
