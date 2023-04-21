package com.mscg.main.util.ui.contextmenu.cutandpaste;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.text.JTextComponent;

public abstract class GenericCutAndPasteAction extends JMenuItem implements ActionListener {

    private static final long serialVersionUID = -4813393845482453769L;

    protected JTextComponent comp;

    public GenericCutAndPasteAction(JTextComponent comp, String name) {
        super(name);
        this.comp = comp;
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!comp.hasFocus()) comp.requestFocus();
    }

    public abstract boolean checkEnabled();
}
