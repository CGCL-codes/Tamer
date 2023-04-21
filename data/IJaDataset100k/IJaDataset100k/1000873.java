package com.techjedi.dragonbot;

/**
 * Interface to the services managing I/O for game error logging. 
 * 
 * @author Doug Bateman
 */
public interface LogService {

    /**
	 * Logs the message
	 * @param msg The message to log.
	 */
    void log(String msg);

    /**
	 * Logs the message and exception.
	 * @param msg The message to log.
	 * @param t The exception to log.
	 */
    void log(String msg, Throwable t);

    /**
	 * Logs the exception.
	 * @param t The exception to log.
	 */
    void log(Throwable t);
}
