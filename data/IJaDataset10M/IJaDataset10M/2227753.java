package com.traxel.lumbermill.event;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class EventListenerStatus extends JPanel implements PropertyChangeListener {

    private final EventListener LISTENER;

    private final JTextField STATUS_FIELD = new JTextField(10), TYPE_FIELD = new JTextField(5), HOST_FIELD = new JTextField(10), PORT_FIELD = new JTextField(5);

    private final JLabel STATUS_LABEL = new JLabel("Listener Status: ", JLabel.TRAILING), TYPE_LABEL = new JLabel("  Type: ", JLabel.TRAILING), HOST_LABEL = new JLabel("  Host: ", JLabel.TRAILING), PORT_LABEL = new JLabel("  Port: ", JLabel.TRAILING);

    public EventListenerStatus(EventListener listener) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        LISTENER = listener;
        STATUS_LABEL.setLabelFor(STATUS_FIELD);
        TYPE_LABEL.setLabelFor(TYPE_FIELD);
        HOST_LABEL.setLabelFor(HOST_FIELD);
        PORT_LABEL.setLabelFor(PORT_FIELD);
        add(STATUS_LABEL);
        add(STATUS_FIELD);
        add(TYPE_LABEL);
        add(TYPE_FIELD);
        add(HOST_LABEL);
        add(HOST_FIELD);
        add(PORT_LABEL);
        add(PORT_FIELD);
        STATUS_FIELD.setEditable(false);
        STATUS_FIELD.setText(LISTENER.getStatus());
        TYPE_FIELD.setEditable(false);
        TYPE_FIELD.setText(LISTENER.getType());
        HOST_FIELD.setEditable(false);
        HOST_FIELD.setText(LISTENER.getHost());
        PORT_FIELD.setEditable(false);
        PORT_FIELD.setText(LISTENER.getPortString());
        LISTENER.addPropertyListener(this);
    }

    public void propertyChange(PropertyChangeEvent event) {
        if (LISTENER.equals(event.getSource())) {
            String property;
            Object newValue;
            property = event.getPropertyName();
            newValue = event.getNewValue();
            if (EventListener.STATUS_PROPERTY.equals(property)) {
                if (newValue == null) {
                    newValue = "";
                }
                STATUS_FIELD.setText(newValue.toString());
            }
        }
    }
}
