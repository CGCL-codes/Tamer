package jgnash.ui.option;

import java.awt.event.*;
import javax.swing.*;
import jgnash.ui.reminder.RecurringPanel;
import jgnash.ui.util.UIResource;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/** Reminder options panel.
 * <p>
 * $Id: ReminderOptions.java 675 2008-06-17 01:36:01Z ccavanaugh $
 * 
 * @author Craig Cavanaugh
 */
public class ReminderOptions extends JPanel implements ActionListener, OptionConstants {

    private UIResource rb = (UIResource) UIResource.get();

    private JCheckBox confirmDeleteButton;

    public ReminderOptions() {
        layoutMainPanel();
        confirmDeleteButton.addActionListener(this);
    }

    private void initComponents() {
        confirmDeleteButton = new JCheckBox(rb.getString("Button.ConfirmReminderDelete"));
        confirmDeleteButton.setSelected(RecurringPanel.isConfirmReminderDeleteEnabled());
    }

    private void layoutMainPanel() {
        initComponents();
        FormLayout layout = new FormLayout("right:p, 4dlu, max(75dlu;p):g", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, this);
        builder.setRowGroupingEnabled(true);
        builder.setDefaultDialogBorder();
        builder.append(confirmDeleteButton, 3);
    }

    /** Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == confirmDeleteButton) {
            RecurringPanel.setConfirmReminderDeleteEnabled(confirmDeleteButton.isSelected());
        }
    }
}
