package net.sf.wwusmart.algorithms.framework;

import java.io.Serializable;
import java.util.Vector;

/**
 * To offer a Parameter to select one of multiple options (Combo Box or Radio Buttons).
 * If choices are known at compile time, it is preferable to create a enum
 * instead of using this class.
 *
 * @author thilo
 */
public abstract class Choice implements Serializable {

    private int choice = 0;

    public abstract void refresh(Algorithm algorithm);

    public abstract Vector<String> getStrings();

    public abstract Vector<? extends Object> getElements();

    public void setIndex(int val) {
        choice = val;
    }

    public int getIndex() {
        return choice;
    }

    public String getCurrentString() {
        if (getStrings() == null || getStrings().size() == 0) {
            return "";
        }
        return getStrings().get(choice);
    }

    public Object getCurrentElement() {
        if (getElements() == null || getElements().size() == 0) {
            return null;
        }
        return getElements().get(choice);
    }

    @Override
    public String toString() {
        return getCurrentString();
    }
}
