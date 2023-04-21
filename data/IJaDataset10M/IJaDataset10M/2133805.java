package net.sourceforge.zyps.userinterfaces;

import java.awt.*;
import java.util.*;

/**
 * Test class.
 * Presents several checkboxes.
 * 
 * @author Jay McGavren
 */
public class SelectionPanel extends Panel {

    private static final long serialVersionUID = -9206901297728642819L;

    private HashMap selections = new HashMap();

    public void addSelection(String value) {
        Checkbox checkBox = new Checkbox(value);
        this.selections.put(value, checkBox);
        this.add(checkBox);
    }

    public void removeSelection(String value) throws NoSuchElementException {
        Checkbox checkBox = (Checkbox) this.selections.remove(value);
        if (checkBox == null) throw new NoSuchElementException(value + " not present for " + this);
        this.remove(checkBox);
    }

    public Iterator selectionIterator() {
        return selections.keySet().iterator();
    }

    /**
	 * Constructor.
	 * Sets up layout for this panel.
	 */
    public SelectionPanel() {
        setLayout(new GridLayout(0, 1));
    }

    /**
	 * Determine whether a given selection is selected or not.
	 * 
	 * @param selection Name of the selection
	 * 
	 * @return true if selected, false if not
	 * 
	 * @throws NoSuchElementException Indicates there was no selection by the given name
	 */
    public boolean isSelected(String selection) throws NoSuchElementException {
        Checkbox checkbox = (Checkbox) selections.get(selection);
        if (checkbox == null) throw new NoSuchElementException(selection + " not present for " + this);
        return checkbox.getState();
    }
}
