package com.loribel.commons.swing.border;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.AbstractButton;
import javax.swing.JComponent;

/**
 * Title border with button component (JCheckBox, ...).
 */
public class GB_ButtonTitledPanel extends GB_CompTitledPanel implements ItemListener {

    private AbstractButton titleButton;

    public GB_ButtonTitledPanel() {
        super();
    }

    public GB_ButtonTitledPanel(AbstractButton a_titleButton) {
        super(a_titleButton);
    }

    public void setTitleComponent(JComponent a_titleComponent) {
        unregisterButton();
        super.setTitleComponent(a_titleComponent);
        registerButton(titleComponent);
    }

    protected void unregisterButton() {
        if (titleButton == null) {
            return;
        }
        titleButton.removeItemListener(this);
        titleButton = null;
    }

    protected void registerButton(JComponent a_titleComponent) {
        if (a_titleComponent == null) {
            return;
        }
        if (a_titleComponent instanceof AbstractButton) {
            titleButton = (AbstractButton) a_titleComponent;
            titleButton.addItemListener(this);
        }
    }

    /**
     * Update the state of panel according to the state of Button.
     * If titleButton.isSelected() do nothing, otherwise, set the content pane disabled.
     */
    public void updateState() {
        if (!titleButton.isSelected()) {
            itemStateChanged(null);
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (titleButton == null) {
            return;
        }
    }
}
