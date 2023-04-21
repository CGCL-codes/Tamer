package com.memoire.bu;

import java.awt.event.ActionEvent;

/**
 * An ActionListener for the monitor.
 * 
 * @author Fred Deniger
 * @version $Id: BuTimerActionListenerMonitor.java,v 1.1 2004-06-01 11:20:25 deniger Exp $
 */
public class BuTimerActionListenerMonitor extends BuTimerActionListenerAbstract {

    /**
   * @param _m the target monitor.
   */
    public BuTimerActionListenerMonitor(BuMonitorAbstract _m) {
        super(_m);
    }

    /**
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
    public void actionPerformed(ActionEvent _e) {
        BuMonitorAbstract m = getMonitor();
        if (m == null) {
            return;
        }
        if (m.refreshState()) {
            BuUpdateGUI.repaintLater(m);
            m.setToolTipText(m.getToolTipText());
        }
    }

    private BuMonitorAbstract getMonitor() {
        Object o = getTarget();
        if (o != null) return (BuMonitorAbstract) o;
        return null;
    }
}
