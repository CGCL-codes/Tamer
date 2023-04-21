package net.sf.opengroove.client;

import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

/**
 * The Swing equivalent of {@link AMenuItem}
 * 
 * @author Alexander Boyd
 * 
 */
public abstract class IMenuItem extends JMenuItem implements ActionListener {

    public IMenuItem(String s) {
        super(s);
        addActionListener(this);
    }
}
