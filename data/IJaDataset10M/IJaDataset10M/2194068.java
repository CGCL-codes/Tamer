package info.monami.osgi.osgi4ami.service.communication;

import info.monami.osgi.osgi4ami.service.ServiceListener;
import java.util.List;

/**
 *  <p style="margin-top: 0">
 *          This cluster proposes methods related generic communication of the basic service, 
 *          which should be mandatory to services intended to use UCH.
 *        </p>
 */
public interface GenericCommunicationServiceListener extends ServiceListener {

    public void sendInformationReport(Object listenerObject, String variable, String value);

    public void sendInformationReport(Object listenerObject, List variables, List values);
}
