package org.jaffa.presentation.portlet.widgets.controller.exceptions;

import org.jaffa.exceptions.CustomRuntimeException;

/** This exception is thrown if the XML fragment passed to the controller is malformed
 */
public class XmlStructureRuntimeException extends CustomRuntimeException {

    /** Default constructor
     */
    public XmlStructureRuntimeException() {
    }

    /** Constructs the exception with the specified detail message
     * @param msg the detail message
     */
    public XmlStructureRuntimeException(String msg) {
        super(msg);
    }

    /** Constructs the exception with the specified detail message and nested exception
     * @param msg the detail message
     * @param sourceException the nested exception
     */
    public XmlStructureRuntimeException(String msg, Throwable sourceException) {
        super(msg, sourceException);
    }
}
