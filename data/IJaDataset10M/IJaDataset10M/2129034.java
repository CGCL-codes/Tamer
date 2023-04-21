package org.dolmen.core.lang;

/**
 * To listen Exceptions
 *
 * @since 0.2
 * @since 4 march 2009
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte 
 *
 */
public interface ExceptionListener {

    /**
	 * Called when a exception occurs
	 * @param exception the exception
	 */
    public void exceptionOccurs(Throwable exception);
}
