package org.ozoneDB.adminGui.widget;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

public class ButtonPanel extends JPanel {

    /** The listener list. */
    protected EventListenerList listenerList = new EventListenerList();

    /**
     * Default constructor not used.
     */
    private ButtonPanel() {
    }

    /**
     * Overloaded constructor creates the panel buttons.
     *
     * @param buttonName - an array of button names.
     */
    public ButtonPanel(String[] buttonName) {
        super();
        if (buttonName != null) init(buttonName);
    }

    /**
     * This method initializes the button panel.
     *
     * @param buttonName - an array of button names.
     */
    private void init(final String[] buttonName) {
        this.setLayout(new GridBagLayout());
        JButton[] button = new JButton[buttonName.length];
        for (int i = 0; i < buttonName.length; i++) {
            final String name = buttonName[i].toString();
            button[i] = new JButton(name);
            button[i].setPreferredSize(new Dimension(80, 27));
            button[i].setDefaultCapable(true);
            button[i].addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    fireExecute(name);
                }
            });
            this.add(button[i], new GridBagConstraints(i, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 15, 10), 0, 0));
        }
    }

    /**
     * This method allows classes to register for connection events.
     *
     * @param listener - a connection listener.
     */
    public void addConnectionListener(ButtonPanelListener listener) {
        listenerList.add(ButtonPanelListener.class, listener);
    }

    /**
     * This method allows classes to unregister from connection events.
     *
     * @param listener - a connection listener.
     */
    public void removeConnectionListener(ButtonPanelListener listener) {
        listenerList.remove(ButtonPanelListener.class, listener);
    }

    /**
     * This method notifies all when the login process is fired.
     *
     * @param buttonName - the name associated with the button.
     */
    private void fireExecute(String buttonName) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == ButtonPanelListener.class) {
                ((ButtonPanelListener) listeners[i + 1]).buttonExecute(buttonName);
            }
        }
    }
}
