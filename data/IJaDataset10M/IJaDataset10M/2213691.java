package com.sanctuary.interfaces;

import java.io.Serializable;

public interface ObjectSerializer extends Serializable {

    public Object toObject(String in);

    public String toString(Object in);
}
