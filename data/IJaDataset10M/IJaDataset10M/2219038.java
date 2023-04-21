package uk.ac.ebi.pride.gui.action.impl;

import uk.ac.ebi.pride.gui.PrideInspectorContext;
import uk.ac.ebi.pride.gui.action.PrideAction;
import uk.ac.ebi.pride.gui.desktop.Desktop;
import uk.ac.ebi.pride.gui.task.impl.OpenDatabaseSearchPaneTask;
import uk.ac.ebi.pride.gui.utils.DefaultGUIBlocker;
import uk.ac.ebi.pride.gui.utils.GUIBlocker;
import uk.ac.ebi.pride.util.InternetChecker;
import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Open database action will open a connection PRIDE public instance.
 * <p/>
 * User: rwang
 * Date: 11-Feb-2010
 * Time: 11:49:36
 */
public class OpenDatabaseAction extends PrideAction {

    public OpenDatabaseAction(String desc, Icon icon) {
        super(desc, icon);
        setAccelerator(java.awt.event.KeyEvent.VK_P, ActionEvent.CTRL_MASK);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (InternetChecker.check()) {
            PrideInspectorContext context = (PrideInspectorContext) Desktop.getInstance().getDesktopContext();
            OpenDatabaseSearchPaneTask newTask = new OpenDatabaseSearchPaneTask();
            newTask.setGUIBlocker(new DefaultGUIBlocker(newTask, GUIBlocker.Scope.NONE, null));
            context.addTask(newTask);
        } else {
            String msg = Desktop.getInstance().getDesktopContext().getProperty("internet.connection.warning.message");
            String shortMsg = Desktop.getInstance().getDesktopContext().getProperty("internet.connection.warning.short.message");
            JOptionPane.showMessageDialog(Desktop.getInstance().getMainComponent(), msg, shortMsg, JOptionPane.WARNING_MESSAGE);
        }
    }
}
