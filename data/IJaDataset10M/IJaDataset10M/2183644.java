package org.dhcc.gui.charcreation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import javax.swing.JScrollPane;
import org.dhcc.UserChar;
import org.dhcc.gui.modules.CharCreation.StepListener;
import org.dhcc.utils.gui.DHCCDialog;
import org.swixml.SwingEngine;

public class Entry implements CreationStep {

    public SwingEngine swix = new SwingEngine();

    private ActionListener externalListener;

    private ActionListener internalListener = new EntryListener();

    private DHCCDialog entryDialog;

    private static final String guiPath = "/org/dhcc/gui/charcreation/entry_question.xml";

    public void displayStep() {
        if (externalListener == null) {
            System.out.println("Listener not set");
            return;
        }
        swix.getTaglib().registerTag("dhccdialog", DHCCDialog.class);
        try {
            entryDialog = (DHCCDialog) swix.render(getClass().getResource(guiPath));
            swix.setActionListener(entryDialog, internalListener);
            swix.getRootComponent().setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserChar save() {
        return null;
    }

    public void setCharacter(UserChar userChar) {
    }

    public void setListener(ActionListener listener) {
        this.externalListener = listener;
    }

    public void updateGui() {
    }

    public void setTargetComponent(JScrollPane target) {
    }

    class EntryListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            entryDialog.dispose();
            if (e.getActionCommand().equalsIgnoreCase("nextStep")) {
                externalListener.actionPerformed(e);
            }
        }
    }

    public void setParameters(UserChar userChar, StepListener listener, JScrollPane target) {
        this.externalListener = listener;
    }

    public void update(Observable o, Object arg) {
    }
}
