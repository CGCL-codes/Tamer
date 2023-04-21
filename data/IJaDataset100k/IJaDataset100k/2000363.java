package de.benedetto;

import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import de.benedetto.i18n.*;

class UIDefaultsDialog extends InternalStatusFrame implements ActionListener {

    UILookandFeelPanel lfPane;

    UIFontsPanel fontsPane;

    UIDefaultsDialog(String title) {
        super(title);
        JTabbedPane tabbedPane = new JTabbedPane();
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        JPanel buttonPane = new JPanel();
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        createButtonPane(buttonPane);
        lfPane = new UILookandFeelPanel();
        tabbedPane.add(BenedettoMessages.getString("title.look_and_feel"), lfPane);
        fontsPane = new UIFontsPanel();
        tabbedPane.add(BenedettoMessages.getString("title.fonts"), fontsPane);
        setSize(getPreferredSize());
    }

    private void createButtonPane(JPanel parent) {
        parent.setLayout(new BoxLayout(parent, BoxLayout.X_AXIS));
        JButton b_ok = new JButton(BenedettoMessages.getString("button.ok"));
        parent.add(b_ok);
        b_ok.setActionCommand("201");
        b_ok.addActionListener(this);
        JButton b_abbruch = new JButton(BenedettoMessages.getString("button.cancel"));
        parent.add(b_abbruch);
        b_abbruch.setActionCommand("202");
        b_abbruch.addActionListener(this);
        JButton b_uebernehmen = new JButton(BenedettoMessages.getString("button.apply"));
        parent.add(b_uebernehmen);
        b_uebernehmen.setActionCommand("203");
        b_uebernehmen.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        Integer action = new Integer(e.getActionCommand());
        switch(action.intValue()) {
            case 201:
                lfPane.updateOptions();
                fontsPane.updateOptions();
                setVisible(false);
                break;
            case 202:
                setVisible(false);
                break;
            case 203:
                lfPane.updateOptions();
                fontsPane.updateOptions();
                Benedetto.benedetto.getUserOptions().setFonts();
                Benedetto.benedetto.getUserOptions().setLookAndFeel();
                SwingUtilities.updateComponentTreeUI(Benedetto.benedetto);
                break;
        }
    }
}
