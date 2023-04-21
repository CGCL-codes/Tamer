package org.mcisb.ui.util.table.action;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import org.mcisb.ui.util.*;
import org.mcisb.ui.wizard.parameter.*;
import org.mcisb.util.*;

/**
 *
 * @author Neil Swainston
 */
public class GroupAction extends TableSelectionAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("org.mcisb.ui.util.table.action.messages");

    /**
	 * 
	 */
    private final GroupManager manager;

    /**
	 *
	 * @param table
	 * @param manager
	 */
    public GroupAction(final JTable table, final GroupManager manager) {
        super(table, resourceBundle.getString("GroupAction.title"));
        this.manager = manager;
    }

    @Override
    public void performAction(final ActionEvent e) {
        final String GROUP_NAME = "GROUP_NAME";
        final GenericBean bean = new GenericBean();
        final Map<Object, Object> propertyNameToKey = new HashMap<Object, Object>();
        final Map<Object, Object> options = new LinkedHashMap<Object, Object>();
        final String prompt = resourceBundle.getString("GroupAction.namePrompt");
        propertyNameToKey.put(GROUP_NAME, prompt);
        options.put(prompt, null);
        final DefaultParameterPanel component = new DefaultParameterPanel(resourceBundle.getString("GroupAction.dialogTitle"), options);
        try {
            final Container topLevelAncestor = table.getTopLevelAncestor();
            final JDialog dialog = new JDialog((topLevelAncestor instanceof Frame) ? (Frame) topLevelAncestor : null, true);
            new DefaultParameterApp(dialog, resourceBundle.getString("GroupAction.dialogTitle"), bean, component, propertyNameToKey).show();
            final Object group = bean.getString(GROUP_NAME);
            if (group != null) {
                if (manager.getGroup(group) != null) {
                    JOptionPane.showMessageDialog(dialog, resourceBundle.getString("GroupAction.duplicateErrorMessage"), resourceBundle.getString("GroupAction.error"), JOptionPane.ERROR_MESSAGE);
                }
                manager.addGroup(group, getSelection());
            }
        } catch (Exception ex) {
            final JDialog dialog = new ExceptionComponentFactory().getExceptionDialog(null, resourceBundle.getString("GroupAction.error"), ex);
            ComponentUtils.setLocationCentral(dialog);
            dialog.setVisible(true);
        }
    }
}
