package com.substanceofcode.tracker.grid;

import javax.microedition.lcdui.Item;

public abstract class CustomizableGridFormatter implements GridFormatter {

    public abstract Item getDataConfiguration(int display_context, int id);

    public abstract void fillPosition(GridPosition pos);

    public abstract GridPosition getPositionFromFields() throws BadFormattedException;
}
