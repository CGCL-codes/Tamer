package com.ketralnis.isUpApp;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.BoxLayout;
import java.awt.GridLayout;

public class isUpMonitorConfigEdit extends isUpMonitorConfig implements ActionListener {

    private isUpMonitor m_monitor = null;

    public isUpMonitorConfigEdit(isUpMonitor in) {
        m_monitor = in;
        JPanel totalPane = new JPanel();
        {
            totalPane.setLayout(new BoxLayout(totalPane, BoxLayout.PAGE_AXIS));
            totalPane.add(createContentPanel(m_monitor));
            totalPane.add(createActionPane());
        }
        configPane.getSettings(m_monitor);
        getContentPane().add(totalPane);
        init();
    }

    public JPanel createActionPane() {
        actionPane = new JPanel(new GridLayout(1, 0));
        {
            JButton Save = new JButton("Save");
            {
                String temp = "Saves the isUpLight";
                Save.setMnemonic(KeyEvent.VK_A);
                Save.getAccessibleContext().setAccessibleDescription(temp);
                Save.setToolTipText(temp);
                Save.setActionCommand("save");
                Save.addActionListener(this);
            }
            actionPane.add(Save);
            JButton Cancel = new JButton("Cancel");
            {
                String temp = "Closes this dialog, disregarding changes";
                Cancel.setMnemonic(KeyEvent.VK_C);
                Cancel.getAccessibleContext().setAccessibleDescription(temp);
                Cancel.setToolTipText(temp);
                Cancel.setActionCommand("cancel");
                Cancel.addActionListener(this);
            }
            actionPane.add(Cancel);
        }
        return actionPane;
    }

    public void actionPerformed(ActionEvent e) {
        if ("save".equals(e.getActionCommand())) {
            configPane.applySettings(m_monitor);
            m_monitor.setIsUp(configCards.mkisUp());
            dispose();
        } else if ("cancel".equals(e.getActionCommand())) {
            dispose();
        }
    }
}
