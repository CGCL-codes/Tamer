package org.atricore.idbus.kernel.main.mediation.camel.logging;

import org.apache.camel.Message;
import java.util.Collection;

/**
 * @author <a href="mailto:sgonzalez@atricore.org">Sebastian Gonzalez Oyuela</a>
 * @version $Id: MediationLogger.java 1359 2009-07-19 16:57:57Z sgonzalez $
 */
public interface MediationLogger {

    void logIncomming(Message message);

    void logOutgoing(Message messasge);

    void logFault(Message faultMessage);

    Collection<LogMessageBuilder> getMessageBuilders();
}
