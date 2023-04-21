package userInterfaceLaag;

import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public abstract class AbstractPanel extends JPanel {

    protected EventListenerList eventListenerList = new EventListenerList();

    public AbstractPanel() {
        this("AbstractPanel");
    }

    public AbstractPanel(String id) {
        setName(id);
    }

    public void addActionListener(EventListener l) {
        eventListenerList.add(EventListener.class, l);
    }

    protected void raiseEvent(ActionEvent e) {
        EventListener[] listeners = eventListenerList.getListeners(EventListener.class);
        for (EventListener l : listeners) {
            ((ActionListener) l).actionPerformed(e);
        }
    }
}
