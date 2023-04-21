package com.carsongee.jsshmacro;

import java.util.Collections;
import java.util.Vector;

/**
 * An extension to the AbstractListModel that holds references to MacroDataModel
 * objects, as well as ensuring duplicate macros (IDed by their name) are not
 * added. 
 * @author Carson Gee
 */
public class MacroListModel extends javax.swing.AbstractListModel implements javax.swing.ComboBoxModel {

    private Object selectedItem;

    private Vector<MacroDataModel> macros = new Vector<MacroDataModel>();

    public int getSize() {
        return macros.size();
    }

    public Object getElementAt(int i) {
        return macros.get(i).getMacroName();
    }

    /**
     * Attempts to add a macro to the list.  If a macro is already in the list
     * with the same name as that being added, it returns false and does not
     * add the macro
     * @param newMacro The macro to be added
     * @return False if the macro already exists in the list, true if it has
     *         been added successfully.
     */
    public boolean addMacro(MacroDataModel newMacro) {
        for (int i = 0; i < macros.size(); i++) {
            if (newMacro.getMacroName().equals(macros.get(i).getMacroName())) return false;
        }
        macros.add(newMacro);
        Collections.sort(macros);
        fireContentsChanged(this, 0, getSize());
        return true;
    }

    /**
     * Removes the macro at the index specified from the list
     * @param index The 0-based index of the macro to be removed
     */
    public void removeMacro(int index) {
        macros.remove(index);
        fireContentsChanged(this, 0, getSize());
    }

    /**
     * Because getElementAt just returns the name of the macro, this method
     * is provided for getting the actual MacroDataModel object at the specified
     * index from the list
     * @param index The 0-based index where the macro resides
     * @return The MacroDataModel object found at the index specified or null
     *         if it is not in the list.
     */
    public MacroDataModel getMacro(int index) {
        try {
            return macros.get(index);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return null;
    }

    /**
     * Returns the macro object with the name specified.  Because this list
     * model requires unique names for macros this method will return 1 or 0
     * objects
     * @param name The string name that identifies the macro to be retrieved
     *        from the list.
     * @return The MacroDataModel object with the name specified or null if it
     *         is not in the list.
     */
    public MacroDataModel getMacro(String name) {
        for (int i = 0; i < macros.size(); i++) {
            if (macros.get(i).getMacroName().equals(name)) return macros.get(i);
        }
        return null;
    }

    /**
     * Returns a Vector of all the MacroDataModel objects in this list
     * @return 
     */
    public Vector<MacroDataModel> getMacros() {
        return macros;
    }

    /**
     * Sets the list of vectors contained in this list
     * @param macros 
     */
    public void setMacros(Vector<MacroDataModel> macros) {
        this.macros = macros;
        this.fireContentsChanged(this, this.getMacros().size(), 0);
    }

    public Object getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Object val) {
        if (getMacro((String) val) != null) selectedItem = val; else selectedItem = null;
    }
}
