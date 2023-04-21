package net.ar.guia.own.implementation;

import net.ar.guia.own.interfaces.*;

public class DefaultSelectionModeChooser implements SelectionModeChooser {

    protected SelectionMode selectionMode = SelectionMode.SINGLE_SELECTION;

    public SelectionMode getSelectionMode() {
        return selectionMode;
    }

    public void setSelectionMode(SelectionMode aSelectionMode) {
        selectionMode = aSelectionMode;
    }
}
