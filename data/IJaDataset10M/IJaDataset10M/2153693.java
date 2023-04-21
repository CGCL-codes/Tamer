package com.bluesky.plum.uimodels.render.swing.components.containers.window;

import javax.swing.JComponent;
import javax.swing.JDialog;
import com.bluesky.plum.uimodels.standard.components.containers.Panel;
import com.bluesky.plum.uimodels.standard.components.containers.window.Dialog;
import com.bluesky.plum.uimodels.standard.components.containers.window.Window;

public class SDialog extends Dialog {

    protected JDialog dialog;

    public SDialog() {
        dialog = new JDialog();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void setContentPanel(Panel contentPanel) {
        super.setContentPanel(contentPanel);
        dialog.getContentPane().removeAll();
        dialog.getContentPane().add((JComponent) contentPanel.getNativeComponent());
    }

    @Override
    public void setModal(boolean modal) {
        super.setModal(modal);
        dialog.setModal(modal);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        dialog.setVisible(visible);
    }

    @Override
    public Object getNativeComponent() {
        return dialog;
    }

    @Override
    public void setParentWindow(Window parentWindow) {
        super.setParentWindow(parentWindow);
        dialog = new JDialog((java.awt.Window) parentWindow.getNativeComponent());
    }

    @Override
    public void pack() {
        dialog.pack();
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
        dialog.setTitle(title);
    }
}
