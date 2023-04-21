package org.ungoverned.radical.action;

import java.awt.event.*;
import javax.swing.*;
import org.ungoverned.radical.Radical;

public class OpenAction extends AbstractAction {

    private Radical m_radical = null;

    public OpenAction(Radical radical) {
        super("Open...");
        m_radical = radical;
    }

    public void actionPerformed(ActionEvent event) {
        m_radical.open();
    }
}
