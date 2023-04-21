package net.sf.amemailchecker.gui.notification.window.modal;

import net.sf.amemailchecker.gui.notification.panel.ExtendedInfoPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MultipleNotificationWindow extends CaptionedNotificationWindow {

    private List<ExtendedInfoPanel> notifications = new ArrayList<ExtendedInfoPanel>();

    private JLabel emptyMessage;

    public MultipleNotificationWindow(Window displayOn) {
        super(displayOn);
        construct();
    }

    protected void construct() {
        super.construct();
        JScrollPane contentScroll = new JScrollPane();
        contentScroll.getViewport().add(contentPanel);
        contentScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        getContentPane().add(contentScroll, BorderLayout.CENTER);
        constructActionButton();
        actionButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                notifications.clear();
                contentPanel.removeAll();
                setVisible(false);
            }
        });
        JPanel footer = new JPanel();
        footer.add(actionButton);
        getContentPane().add(footer, BorderLayout.SOUTH);
        emptyMessage = new JLabel();
        emptyMessage.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        emptyMessage.setHorizontalAlignment(JLabel.CENTER);
        emptyMessage.setAlignmentY(JLabel.CENTER_ALIGNMENT);
        emptyMessage.setVerticalAlignment(JLabel.CENTER);
    }

    public void setVisible(boolean visible) {
        if (!containsNotifications()) contentPanel.add(emptyMessage);
        setSize(400, 300);
        locate();
        super.setVisible(visible);
    }

    public void addNotification(String message, String detailedMessage) {
        ExtendedInfoPanel label = new ExtendedInfoPanel(message, detailedMessage);
        label.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        notifications.add(label);
        contentPanel.add(label);
    }

    public boolean containsNotifications() {
        return notifications.size() > 0;
    }

    public int getNotificationsSize() {
        return notifications.size();
    }

    public void setEmptyMessage(String message) {
        emptyMessage.setText(message);
    }
}
