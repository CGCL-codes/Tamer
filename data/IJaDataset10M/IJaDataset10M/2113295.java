package org.subrecord.subconsole.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.subrecord.kit.SubRecordApi;
import org.subrecord.subconsole.SubConsole;

public class LogMenuFactory {

    public static JMenu getLogMenu(final SubConsole console, final SubRecordApi subRecord) {
        JMenu logsMenu = new JMenu("Logs");
        JMenuItem logsOption1 = new JMenuItem("Option 1");
        logsOption1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });
        logsMenu.add(logsOption1);
        return logsMenu;
    }
}
