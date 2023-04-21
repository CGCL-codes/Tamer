package com.magicpwd._comp;

/**
 *
 * @author Administrator
 */
public class WButtonGroup extends javax.swing.ButtonGroup {

    private java.util.HashMap<String, javax.swing.ButtonModel> buttonModel;

    public WButtonGroup() {
        buttonModel = new java.util.HashMap<String, javax.swing.ButtonModel>();
    }

    public void add(String actionCommand, javax.swing.AbstractButton button) {
        buttonModel.put(actionCommand, button.getModel());
        add(button);
    }

    public boolean setSelected(String actionCommand, boolean selected) {
        javax.swing.ButtonModel model = buttonModel.get(actionCommand);
        if (model != null) {
            setSelected(model, selected);
            return true;
        }
        return false;
    }

    public boolean setSelected(String actionCommand, boolean selected, String defaultCommand) {
        javax.swing.ButtonModel model = buttonModel.get(actionCommand);
        if (model != null) {
            setSelected(model, selected);
            return true;
        }
        model = buttonModel.get(defaultCommand);
        if (model != null) {
            setSelected(model, selected);
            return true;
        }
        return false;
    }

    public boolean contains(String actionCommand) {
        return buttonModel.containsKey(actionCommand);
    }
}
