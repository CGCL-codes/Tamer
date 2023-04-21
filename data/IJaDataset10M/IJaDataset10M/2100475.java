package net.sf.amemailchecker.gui.notification.window.tooltip;

import net.sf.amemailchecker.gui.notification.window.BaseNotificationWindow;
import net.sf.amemailchecker.gui.notification.panel.TextMultiLinePanel;
import javax.swing.*;
import java.awt.*;

public class ToolTipNotificationWindow extends BaseNotificationWindow {

    private Component component;

    private String message;

    public ToolTipNotificationWindow(String message, Component component) {
        this.message = message;
        this.component = component;
        init();
    }

    protected void construct() {
        contentPanel.setBackground(theme.getDefaultBackgroundColor());
        contentPanel.setBorder(BorderFactory.createLineBorder(theme.getDefaultBorderColor()));
        contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        if (message != null) contentPanel.add(new TextMultiLinePanel(message, false));
    }

    protected void locate() {
        Point location = component.getLocationOnScreen();
        Dimension size = component.getSize();
        setLocation((int) (location.getX() + size.getWidth() / 2), (int) (location.getY() + size.getHeight() / 2));
    }

    public void showNotification(String message) {
        this.message = message;
        contentPanel.removeAll();
        construct();
        contentPanel.revalidate();
        contentPanel.repaint();
        super.showNotification();
    }
}
