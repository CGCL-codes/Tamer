package com.kitten.dao;

import java.util.Observable;

public class KittenDropSQLTextDao extends Observable {

    private String dropSQLText;

    private boolean dropAtCursorLocation = false;

    public String getDropSQLText() {
        return dropSQLText;
    }

    public void setDropSQLText(String dropSQLText) {
        this.dropSQLText = dropSQLText;
        this.setChanged();
        this.notifyObservers(this);
    }

    public boolean isDropAtCursorLocation() {
        return dropAtCursorLocation;
    }

    public void setDropAtCursorLocation(boolean dropAtCursorLocation) {
        this.dropAtCursorLocation = dropAtCursorLocation;
    }
}
