package com.hmbnet.squelch;

import java.io.Serializable;

public class Cell<T> implements Serializable {

    private T myData;

    public Cell(T data) {
        myData = data;
    }

    public T getData() {
        return myData;
    }

    @Override
    public String toString() {
        return myData.toString();
    }
}
