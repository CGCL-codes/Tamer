package org.freehold.jukebox.service;

/**
 * Serves one goal: to be able to assign the proper name to the application
 * wrapper log channel serving this application.
 *
 *
 * @author Copyright &copy; <a href="mailto:vt@freehold.crocodile.org">Vadim Tkachenko</a> 1995-1999
 * @version $Id: Application.java,v 1.1 2001-01-02 08:21:56 vtt Exp $
 */
public interface Application {

    /**
     * Return the application name as a string.
     */
    public String getApplicationName();
}
