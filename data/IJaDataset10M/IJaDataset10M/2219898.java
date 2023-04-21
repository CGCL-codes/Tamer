package com.bbn.vessel.author.util.wizard;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * an item that just displays a message and doesn't collect any data
 * @author jostwald
 *
 */
public class MessageItem extends WizardItem {

    /**
     * create a message item with no title
     *
     * @param message message to be displayed in the page
     */
    public MessageItem(String message) {
        this(null, message);
    }

    /**
     * create a message page with the given message
     * @param title frame title
     * @param message message to be displayed in the page
     */
    public MessageItem(String title, String message) {
        super(title, new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel label = new JLabel(message);
        label.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        label.setAlignmentY(JComponent.CENTER_ALIGNMENT);
        add(label, gbc);
    }

    @Override
    public void clearMapFields(WizardState state) {
    }

    @Override
    public void finish(WizardState state) {
    }

    @Override
    public boolean isCompleted() {
        return true;
    }
}
