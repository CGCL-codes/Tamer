package org.dbe.studio.core.security.error;

/**
 * @author <a href="mailto:chatark@cs.tcd.ie">Khalid Chatar</a>
 * @author <a href="mailto:Dominik.Dahlem@cs.tcd.ie">Dominik Dahlem</a>
 */
public interface IErrorMessageHandler {

    void setErrorMessage(String p_message, int p_severity);

    int getSeverityError();

    int getSeverityOK();

    int getSeverityWarning();

    int getSeverityInfo();
}
