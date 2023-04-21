package com.sksdpt.kioskjui.gui.forward;

public abstract class DataRunnable implements Runnable {

    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
