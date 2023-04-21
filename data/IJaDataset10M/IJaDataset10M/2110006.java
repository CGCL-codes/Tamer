package de.grogra.greenlab.ui.elements;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;
import de.grogra.greenlab.GreenLabGUI;

/**
 * The TextField
 * 
 * @author Cong Ding
 * 
 */
public class GreenLabTextField extends JTextField implements FocusListener {

    private static final long serialVersionUID = 1654445337973914974L;

    private String keyString = "defaultTooltip";

    public GreenLabTextField(String keyString) {
        super();
        this.keyString = keyString;
        addFocusListener(this);
        final String key = this.keyString;
        addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                GreenLabGUI.tooltipPanel.updateValue(key);
            }
        });
    }

    @Override
    public void focusGained(FocusEvent arg0) {
        GreenLabGUI.tooltipPanel.updateValue(keyString);
    }

    @Override
    public void focusLost(FocusEvent arg0) {
    }
}
