package net.adrianromero.tpv.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.event.EventListenerList;
import net.adrianromero.tpv.forms.AppConfig;

/**
 * VisorTimer provides a singleton timer to control how long the customer
 * display shows the last output after the transaction was closed.
 * 
 * @author hans
 */
public class VisorTimer {

    private static Timer vTimer;

    private static VisorTimer instance = null;

    private EventListenerList listeners = new EventListenerList();

    private static boolean inactive = false;

    private VisorTimer(int delay) {
        if (delay == 0) {
            inactive = true;
        } else {
            vTimer = new Timer(delay, new TimeoutListener());
            vTimer.setRepeats(false);
        }
    }

    public static VisorTimer getInstance() {
        if (instance == null) {
            int delay;
            try {
                delay = Integer.parseInt(AppConfig.getInstance().getProperty("startMsgDelay"));
            } catch (NumberFormatException numberFormatException) {
                delay = 0;
            }
            instance = new VisorTimer(delay * 1000);
        }
        return instance;
    }

    public void start() {
        if (inactive) {
            return;
        }
        vTimer.start();
    }

    public void stop() {
        if (inactive) {
            return;
        }
        vTimer.stop();
    }

    /**
     * Add an ActionListener
     * @param listener
     */
    public void addActionListener(ActionListener listener) {
        listeners.add(ActionListener.class, listener);
    }

    /**
     * Remove an ActionListener
     * @param listener
     */
    public void removeActionListener(ActionListener listener) {
        listeners.remove(ActionListener.class, listener);
    }

    /**
     * Fire ReturnToLOgin to all registered listeners
     * @param event Action event to be delivered
     */
    protected synchronized void fireTimoutAction(ActionEvent event) {
        for (ActionListener l : listeners.getListeners(ActionListener.class)) {
            l.actionPerformed(event);
        }
    }

    private class TimeoutListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            fireTimoutAction(e);
        }
    }
}
