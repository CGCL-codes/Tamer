package org.strophe.gui;

import java.awt.*;
import org.strophe.sph.*;

public final class GuiDisplayFieldUnit implements SphUnit {

    TextField textField;

    SphObjectScope scope;

    SphEventListener eventListener = new SphEventListener();

    public GuiDisplayFieldUnit(SphObjectScope scope) {
        this.scope = scope;
        this.textField = new TextField();
        this.textField.setEditable(false);
    }

    protected Object getObject(int index) {
        return scope.sphObjectScope_getObject(5, index);
    }

    protected Object nvl(int index, Object nullValue) {
        Object o = getObject(index);
        if (o != null) return o;
        return nullValue;
    }

    protected static final String nullString = "";

    protected static final Integer nullInteger = new Integer(0);

    protected String getString(int index) {
        return (String) nvl(index, nullString);
    }

    protected Integer getInteger(int index) {
        return (Integer) nvl(index, nullInteger);
    }

    protected int getint(int index) {
        return getInteger(index).intValue();
    }

    protected boolean isNull(int index) {
        return getObject(index) == null;
    }

    public Object sphUnit_compute() {
        if (!eventListener.isValid()) {
            eventListener.startComputing();
            String text = getString(0);
            SphDispatch location = (SphDispatch) getObject(1);
            SphDispatch dimension = (SphDispatch) getObject(2);
            int x = ((Integer) location.sphDispatch_getObject(0)).intValue();
            int y = ((Integer) location.sphDispatch_getObject(1)).intValue();
            int w = ((Integer) dimension.sphDispatch_getObject(0)).intValue();
            int h = ((Integer) dimension.sphDispatch_getObject(1)).intValue();
            textField.resize(w, h);
            textField.move(x, y);
            String s = textField.getText();
            if (!text.equals(s)) textField.setText(text);
            eventListener.stopComputing();
        }
        eventListener.takeListener();
        return textField;
    }
}
