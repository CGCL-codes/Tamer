package ru.vsu.cs.piit.vokod.view;

import java.awt.Container;
import java.awt.event.ActionListener;
import javax.swing.JDialog;

/**
 *
 * @author Галчонок
 */
public class MyDialog extends JDialog {

    EmptyPanelForDialog panelForDialog = new EmptyPanelForDialog();

    public MyDialog(Container content) {
        this.getRootPane().setContentPane(panelForDialog);
        panelForDialog.setContentPane(content);
        this.setSize(content.getPreferredSize().width, content.getPreferredSize().height + 70);
        this.setResizable(false);
    }

    public MyDialog(Container content, ActionListener OkListener, ActionListener CancelListener) {
        this(content);
        addOkActionListener(OkListener);
        addCancelActionListener(CancelListener);
    }

    @Override
    public void setContentPane(Container contentPane) {
        panelForDialog.setContentPane(contentPane);
    }

    public void addOkActionListener(ActionListener l) {
        panelForDialog.addOkActionListener(l);
    }

    public void addCancelActionListener(ActionListener l) {
        panelForDialog.addCancelActionListener(l);
    }
}
