package org.tigr.microarray.mev;

import java.awt.Component;
import java.util.ArrayList;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import org.tigr.microarray.mev.action.ActionManager;

public class MultipleArrayToolbar extends JToolBar {

    /**
     * Construct a <code>MultipleArrayToolbar</code> using
     * specified action manager.
     * @see ActionManager
     */
    public MultipleArrayToolbar(ActionManager manager) {
        addAlgorithmActions(manager);
    }

    /**
     * Adds actions into the toolbar.
     */
    private int algorithmCount(ActionManager manager) {
        int count = 0;
        Action action;
        while ((action = manager.getAction(ActionManager.ANALYSIS_ACTION + String.valueOf(count))) != null) {
            count++;
        }
        return count;
    }

    private void addAlgorithmActions(ActionManager manager) {
        int index = 0;
        Action action;
        String[] category = { "Clustering", "Statistics", "Classification", "Data Reduction", "Meta Analysis", "Visualization", "Miscellaneous", "ben" };
        for (int i = 0; i < category.length; i++) {
            while ((action = manager.getAction(ActionManager.ANALYSIS_ACTION + String.valueOf(index))) != null) {
                if ((action.getValue(ActionManager.CATEGORY)).equals(category[i])) {
                    if (this.algorithmCount(manager) == TMEV.getCustomerAnalysis().length) {
                        if (TMEV.getCustomerAnalysis()[index] == 1) add(action);
                    } else {
                        TMEV.initCustomerAnalysis(this.algorithmCount(manager));
                        index--;
                    }
                }
                index++;
            }
            this.addSeparator();
            index = 0;
        }
    }

    /**
     * Overriden from JToolBar.
     */
    public JButton add(Action a) {
        JButton button = super.add(a);
        button.setActionCommand((String) a.getValue(Action.ACTION_COMMAND_KEY));
        button.setIcon((Icon) a.getValue(ActionManager.LARGE_ICON));
        button.setFocusPainted(false);
        return button;
    }

    /**
     * Returns an array of buttons with the same action command.
     */
    private AbstractButton[] getButtons(String command) {
        ArrayList list = new ArrayList();
        Component[] components = getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof AbstractButton) {
                if (((AbstractButton) components[i]).getActionCommand().equals(command)) list.add(components[i]);
            }
        }
        return (AbstractButton[]) list.toArray(new AbstractButton[list.size()]);
    }

    /**
     * Sets state of buttons with specified action command.
     */
    private void setEnable(String command, boolean enable) {
        AbstractButton[] buttons = getButtons(command);
        if (buttons == null || buttons.length < 1) {
            return;
        }
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setEnabled(enable);
        }
    }

    /**
     * Disables some buttons according to specified state.
     */
    public void systemDisable(int state) {
        switch(state) {
            case TMEV.SYSTEM:
                setEnable(ActionManager.LOAD_DB_COMMAND, false);
                setEnable(ActionManager.LOAD_FILE_COMMAND, false);
                setEnable(ActionManager.LOAD_EXPRESSION_COMMAND, false);
                setEnable(ActionManager.LOAD_DIRECTORY_COMMAND, false);
                break;
            case TMEV.DATA_AVAILABLE:
                setEnable(ActionManager.LOAD_DB_COMMAND, false);
                setEnable(ActionManager.SAVE_IMAGE_COMMAND, false);
                setEnable(ActionManager.PRINT_IMAGE_COMMAND, false);
                setEnable(ActionManager.ANALYSIS_COMMAND, false);
                break;
            case TMEV.DB_AVAILABLE:
                setEnable(ActionManager.LOAD_DB_COMMAND, false);
                break;
            case TMEV.DB_LOGIN:
                setEnable(ActionManager.LOAD_DB_COMMAND, false);
                break;
        }
    }

    /**
     * Enables some buttons according to specified state.
     */
    public void systemEnable(int state) {
        switch(state) {
            case TMEV.SYSTEM:
                setEnable(ActionManager.LOAD_FILE_COMMAND, true);
                setEnable(ActionManager.LOAD_DIRECTORY_COMMAND, true);
                setEnable(ActionManager.LOAD_EXPRESSION_COMMAND, true);
                break;
            case TMEV.DATA_AVAILABLE:
                setEnable(ActionManager.SAVE_IMAGE_COMMAND, true);
                setEnable(ActionManager.PRINT_IMAGE_COMMAND, true);
                setEnable(ActionManager.ANALYSIS_COMMAND, true);
                break;
            case TMEV.DB_AVAILABLE:
                break;
            case TMEV.DB_LOGIN:
                setEnable(ActionManager.LOAD_DB_COMMAND, true);
                break;
        }
    }
}
