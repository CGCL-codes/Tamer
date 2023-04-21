package org.myrpg.holy_factory;

import java.io.*;

/**
 * Logs exceptions.
 *
 * @author	St�phane Lemaire
 */
public class ExceptionLogger {

    /**
	 * Logs any exception to an object that implements the Loggable interface.
	 *
	 * @param	ex	the exception.
	 * @param	logger	the object.
	 */
    public static void log(Throwable ex, Loggable logger) {
        StringWriter writer;
        writer = new StringWriter();
        ex.printStackTrace(new PrintWriter(writer));
        logger.log(writer.toString());
    }
}
