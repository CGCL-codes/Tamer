package de.shandschuh.jaolt.gui.listener.core.tag;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;
import de.shandschuh.jaolt.gui.core.TagsJPanel;
import de.shandschuh.jaolt.gui.core.tag.AddTagJPanel;

public class AddTagTextFieldFocusListener implements FocusListener {

    private TagsJPanel tagsJPanel;

    private AddTagJPanel addTagJPanel;

    public AddTagTextFieldFocusListener(TagsJPanel tagsJPanel, AddTagJPanel addTagJPanel) {
        this.tagsJPanel = tagsJPanel;
        this.addTagJPanel = addTagJPanel;
    }

    public void focusGained(FocusEvent e) {
    }

    public void focusLost(FocusEvent e) {
        JTextField textField = (JTextField) e.getSource();
        if (textField.getText().trim().length() > 0) {
            tagsJPanel.addTag(textField.getText().trim());
            textField.setText("");
        }
        addTagJPanel.setEditMode(false);
    }
}
