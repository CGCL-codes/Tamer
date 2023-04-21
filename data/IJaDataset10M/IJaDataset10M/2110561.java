package org.jsresources.apps.midiplayer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JPanel;
import org.jsresources.apps.jmvp.manager.RM;
import org.jsresources.apps.jmvp.manager.swing.ActionManager;
import org.jsresources.apps.jmvp.manager.swing.AMAction;

public class MidiDeviceSelectionDialog extends JDialog {

    private MidiPlayerFrame m_midiPlayerFrame;

    private MidiDeviceSelectionPanel m_midiDeviceSelectionPanel;

    public MidiDeviceSelectionDialog(MidiPlayerFrame midiPlayerFrame) {
        super(midiPlayerFrame, RM.getResourceString("MidiDeviceSelectionDialog.title"));
        m_midiPlayerFrame = midiPlayerFrame;
        createActions(getActionManager());
        WindowListener windowListener = new WindowAdapter() {

            public void windowClosing(WindowEvent we) {
            }
        };
        this.addWindowListener(windowListener);
        this.getContentPane().setLayout(new BorderLayout());
        m_midiDeviceSelectionPanel = new MidiDeviceSelectionPanel(getMidiPlayerModel());
        this.getContentPane().add(m_midiDeviceSelectionPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        AbstractButton button;
        button = getActionManager().createButton("midiDeviceSelectionOkButton");
        buttonPanel.add(button);
        button = getActionManager().createButton("midiDeviceSelectionApplyButton");
        buttonPanel.add(button);
        button = getActionManager().createButton("midiDeviceSelectionCancelButton");
        buttonPanel.add(button);
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        pack();
    }

    private MidiPlayerModel getMidiPlayerModel() {
        return m_midiPlayerFrame.getMidiPlayerModel();
    }

    private void commit() {
        m_midiDeviceSelectionPanel.commit();
    }

    private void close() {
        setVisible(false);
    }

    public ActionManager getActionManager() {
        return m_midiPlayerFrame.getActionManager();
    }

    public void createActions(ActionManager actionManager) {
        actionManager.addAction(new MidiDeviceSelectionOkAction());
        actionManager.addAction(new MidiDeviceSelectionApplyAction());
        actionManager.addAction(new MidiDeviceSelectionCancelAction());
    }

    private class MidiDeviceSelectionOkAction extends AMAction {

        public void actionPerformed(ActionEvent ae) {
            if (Debug.getTraceActions()) {
                Debug.out("MidiDeviceSelectionDialog.MidiDeviceSelectionOkAction.actionPerformed(): called");
            }
            MidiDeviceSelectionDialog.this.commit();
            MidiDeviceSelectionDialog.this.close();
        }
    }

    private class MidiDeviceSelectionApplyAction extends AMAction {

        public void actionPerformed(ActionEvent ae) {
            if (Debug.getTraceActions()) {
                Debug.out("MidiDeviceSelectionDialog.MidiDeviceSelectionApplyAction.actionPerformed(): called");
            }
            MidiDeviceSelectionDialog.this.commit();
        }
    }

    private class MidiDeviceSelectionCancelAction extends AMAction {

        public void actionPerformed(ActionEvent ae) {
            if (Debug.getTraceActions()) {
                Debug.out("MidiDeviceSelectionDialog.MidiDeviceSelectionCancelAction.actionPerformed(): called");
            }
            MidiDeviceSelectionDialog.this.close();
        }
    }
}
