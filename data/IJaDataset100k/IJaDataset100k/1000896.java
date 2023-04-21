package org.compiere.pos;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import org.compiere.util.*;

/**
 * POS Keyboard Focus Manager
 * 
 * @author Comunidad de Desarrollo OpenXpertya 
 *         *Basado en Codigo Original Modificado, Revisado y Optimizado de:
 *         *Copyright (c) Jorg Janke
 * @version $Id: PosKeyboardFocusManager.java,v 1.2 2004/07/11 19:50:12 jjanke
 *          Exp $
 */
public class PosKeyboardFocusManager extends DefaultKeyboardFocusManager implements ActionListener {

    /**
	 * PosKeyboardFocusManager
	 */
    public PosKeyboardFocusManager() {
        super();
    }

    /** FirstIn First Out List */
    private LinkedList<KeyEvent> m_fifo = new LinkedList<KeyEvent>();

    /** Last Key Type */
    private long m_lastWhen = 0;

    /** Timer */
    private javax.swing.Timer m_timer = null;

    /**	Logger			*/
    private static CLogger log = CLogger.getCLogger(PosKeyboardFocusManager.class);

    /**
	 * Dispose
	 */
    public void dispose() {
        if (m_timer != null) m_timer.stop();
        m_timer = null;
        if (m_fifo != null) m_fifo.clear();
        m_fifo = null;
    }

    /**
	 * Start Timer
	 */
    public void start() {
        int delay = 200;
        log.fine("PosKeyboardFocusManager.start - " + delay);
        if (m_timer == null) m_timer = new javax.swing.Timer(delay, this);
        if (!m_timer.isRunning()) m_timer.start();
    }

    /**
	 * Stop Timer
	 */
    public void stop() {
        log.fine("PosKeyboardFocusManager.stop - " + m_timer);
        if (m_timer != null) m_timer.stop();
    }

    /***************************************************************************
	 * Dispatch Key Event - queue
	 * 
	 * @param eevent
	 *            event
	 * @return true
	 */
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getID() == KeyEvent.KEY_PRESSED) {
            m_lastWhen = event.getWhen();
        }
        if (m_timer == null) super.dispatchKeyEvent(event); else m_fifo.add(event);
        return true;
    }

    /**
	 * Action Performed - unqueue
	 * 
	 * @param e
	 *            event
	 */
    public void actionPerformed(ActionEvent e) {
        if (m_timer == null) return;
        while (m_fifo.size() > 0) {
            KeyEvent event = (KeyEvent) m_fifo.removeFirst();
            super.dispatchKeyEvent(event);
        }
    }
}
