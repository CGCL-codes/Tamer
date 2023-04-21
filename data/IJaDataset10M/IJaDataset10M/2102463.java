package org.perfectday.threads;

import org.apache.log4j.Logger;

/**
 * Esta clase es usada para realizar operaciones en otro grupo de hebras distinto
 * permitiendo as� la comunicaci�n entre dos hebras principales sin causar interbloqueo
 *
 * Adicionalmente este comando ya asegura la integridad del sistema ya que
 * toda excepci�n es recogida
 * @author Miguel Angel Lopez Montellano (alakat@gmail.com)
 */
public abstract class Command implements Runnable {

    private static Logger logger = Logger.getLogger(Command.class);

    @Override
    public void run() {
        try {
            trueRun();
        } catch (Throwable t) {
            logger.fatal(t.getMessage(), t);
        }
    }

    /**
     * Este m�todo es el que realiza la ejecuci�n real. Las operaciones
     * que se ejecuten en este m�todo ser�n lanzadas en hebras independientes
     * @throws java.lang.Exception
     */
    public abstract void trueRun() throws Exception;
}
