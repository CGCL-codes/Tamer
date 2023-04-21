package hoipolloi;

import java.awt.event.*;
import javax.swing.*;

/**
 * ActionListener for the Gender Combo Box in edit profile to add/remove the
 * maiden name JPanel when the gender is changed to Male from unselected or
 * female.
 *
 * @author  Brandon Buck
 * @since   January 1, 2009
 * @version 1.0
 */
public class GenderComboBoxActionListener implements ActionListener {

    private MainMenu parent;

    private JPanel componentPanel;

    private JPanel maidenPanel;

    /** 
     * Default constructer for GenderComboBoxActionListener.
     *
     * Creates a new instance of GenderComboBoxActionListener containing
     * the maiden item and the panel to add/remove it from. This method also
     * takes in and stores the parent MainMenu so it can update the UI when it
     * changes.
     * 
     * @param parent
     * @param componentPanel
     * @param maidenPanel
     */
    public GenderComboBoxActionListener(MainMenu parent, JPanel componentPanel, JPanel maidenPanel) {
        this.parent = parent;
        this.componentPanel = componentPanel;
        this.maidenPanel = maidenPanel;
    }

    public void actionPerformed(ActionEvent e) {
        JComboBox sourceComboBox = (JComboBox) (e.getSource());
        if (sourceComboBox.getSelectedItem().toString().equals("Male")) {
            componentPanel.remove(maidenPanel);
            parent.updateWindow();
        } else if (sourceComboBox.getSelectedItem().toString().equals("Female")) {
            componentPanel.add(maidenPanel, 5);
            parent.updateWindow();
        }
    }
}
