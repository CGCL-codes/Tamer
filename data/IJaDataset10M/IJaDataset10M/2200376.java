package org.openXpertya.server;

import org.openXpertya.util.CLogger;

/**
 * Descripción de Clase
 *
 *
 * @version 2.2, 24.03.06
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class GrupoServidorOXP extends ThreadGroup {

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public static GrupoServidorOXP get() {
        if ((s_group == null) || s_group.isDestroyed()) {
            s_group = new GrupoServidorOXP();
        }
        return s_group;
    }

    /** Descripción de Campos */
    private static GrupoServidorOXP s_group = null;

    /**
     * Constructor de la clase ...
     *
     */
    private GrupoServidorOXP() {
        super("OXPServers");
        setDaemon(true);
        setMaxPriority(Thread.MAX_PRIORITY);
        log.info(getName() + " - Parent=" + getParent());
    }

    /** Descripción de Campos */
    protected CLogger log = CLogger.getCLogger(getClass());

    /**
     * Descripción de Método
     *
     *
     * @param t
     * @param e
     */
    public void uncaughtException(Thread t, Throwable e) {
        log.info("uncaughtException = " + e.toString());
        super.uncaughtException(t, e);
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String toString() {
        return getName();
    }

    /**
     * Descripción de Método
     *
     */
    public void dump() {
        log.fine(getName() + (isDestroyed() ? " (destroyed)" : ""));
        log.fine("- Parent=" + getParent());
        Thread[] list = new Thread[activeCount()];
        log.fine("- Count=" + enumerate(list, true));
        for (int i = 0; i < list.length; i++) {
            log.fine("-- " + list[i]);
        }
    }
}
