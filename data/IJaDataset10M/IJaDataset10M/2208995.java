package eu.more.diaball.senddata.glucose.generated;

import java.util.List;
import javax.xml.namespace.QName;
import org.soda.dpws.DPWSException;
import org.soda.dpws.addressing.EndpointReference;
import org.soda.dpws.addressing.UserEndpointReference;
import org.soda.dpws.invocation.Call;
import org.soda.dpws.registry.ServiceEndpoint;

public class SGDNotifier {

    public static final String TNS = "http://www.ist-more.org/SendDataGlucose";

    private static final QName[] PORT_TYPES = { new QName(TNS, "SendDataGlucosePortType") };

    private ServiceEndpoint service;

    public SGDNotifier(ServiceEndpoint service) throws DPWSException {
        this.service = service;
        if (service.getSupportedTypes() != null) {
            List types = service.getSupportedTypes();
            for (int i = 0; i < PORT_TYPES.length; i++) {
                if (types.contains(PORT_TYPES[i])) return;
            }
            throw new DPWSException("The service implements no type that can be notified with the SGDNotifier class.");
        }
    }

    public void notifyNewMeasurementValue(eu.more.diaball.senddata.glucose.generated.GlucoseMeasurement parameters) throws DPWSException {
        String action = "http://www.ist-more.org/SendDataGlucose/SendDataGlucosePortType/NewMeasurementValue";
        List endpoints = service.getSubcribers(action);
        if (endpoints == null) return;
        for (int i = 0; i < endpoints.size(); i++) {
            EndpointReference epr = (EndpointReference) endpoints.get(i);
            Call call = new Call(new UserEndpointReference(epr), SGDWSDLInfoFactory.getWSDLInfo());
            try {
                call.send(action, parameters);
            } catch (DPWSException e) {
            }
        }
    }

    public void notifyNewMeasurementValueEncrypted(eu.more.diaball.senddata.glucose.generated.EncryptedResponse parameters) throws DPWSException {
        String action = "http://www.ist-more.org/SendDataGlucose/SendDataGlucosePortType/NewMeasurementValueEncrypted";
        List endpoints = service.getSubcribers(action);
        if (endpoints == null) return;
        for (int i = 0; i < endpoints.size(); i++) {
            EndpointReference epr = (EndpointReference) endpoints.get(i);
            Call call = new Call(new UserEndpointReference(epr), SGDWSDLInfoFactory.getWSDLInfo());
            try {
                call.send(action, parameters);
            } catch (DPWSException e) {
            }
        }
    }
}
