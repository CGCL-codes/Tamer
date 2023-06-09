package org.softsmithy.lib.swing.action;

import java.util.*;
import org.softsmithy.lib.swing.event.*;

/**
 * The CustomizerActionManager enables and disables Actions when the current 
 * selection of customizers changes. With each action a set of properties can be specified 
 * (the set can be empty). 
 * If all selected customizers support all specified properties of an Action, 
 * the Action is enabled else disabled.
 * If the selection is empty, all registered Actions are disabled. 
 *
 * The CustomizerActionManager must be registered as a CustomizerSelectionListener
 * to a SelectionManager of a JCustomizerPane.
 *
 * @author  puce
 */
public class CustomizerActionManager implements CustomizerSelectionListener {

    private final Set actions = new HashSet();

    /** Creates a new instance of CustomizerActionManager */
    public CustomizerActionManager() {
    }

    public void selectionChanged(CustomizerSelectionEvent e) {
        Set commonProperties = e.getCommonCustomizableProperties();
        for (Iterator i = actions.iterator(); i.hasNext(); ) {
            CustomizerAction action = (CustomizerAction) i.next();
            if (e.getSelectedCustomizers().size() > 0) {
                action.setEnabled(commonProperties.containsAll(action.getNeededCustomizableProperties()));
            } else {
                action.setEnabled(false);
            }
        }
    }

    public void addAction(CustomizerAction action) {
        actions.add(action);
    }

    public void removeAction(CustomizerAction action) {
        actions.remove(action);
    }
}
