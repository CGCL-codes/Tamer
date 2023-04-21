package com.bluebrim.layout.impl.client.transfer;

public abstract class CoTextOnSomethingOperation extends CoAbstractDropOperation {

    protected static final int DEFAULT_TEXTBOX_HEIGHT = 100;

    protected static final int DEFAULT_TEXTBOX_WIDTH = 200;

    protected static final int DEFAULT_X_POSITION = 10;

    protected static final int DEFAULT_Y_POSITION = 10;

    public String getDescription(String nameOfTarget) {
        return "Sl�pp text p� " + nameOfTarget;
    }
}
