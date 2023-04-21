package org.jpos.util;

import java.util.EventListener;

/**
 * @author apr@cs.com.uy
 * @version $Id: LogListener.java 2854 2010-01-02 10:34:31Z apr $
 */
public interface LogListener extends EventListener {

    public LogEvent log(LogEvent ev);
}
