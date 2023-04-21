package com.lc.util;

/**
 * Defines a catch-all Error&nbsp;/ Exception handler.
 * @author Laurent Caillette
 * @version $Revision: 1.1.1.1 $  $Date: 2002/02/19 22:12:03 $
 */
public interface IThrowableHandler {

    /**
 * Method called when a Throwable should be handled. <br>
 * Since this method could be potentially called from <i>any</i> thread, it
 * is strongly recommanded to make its implementation synchronized.
 * @param throwable The Throwable to handle.
 * @return <code>true</code> if it was handled, <code>false</code>
 *    if default behavior should be used instead.
 */
    boolean handleThrowable(Throwable throwable);
}
