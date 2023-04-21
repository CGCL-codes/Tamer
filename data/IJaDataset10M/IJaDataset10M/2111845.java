package org.fao.fenix.communication.webservice;

import javax.xml.stream.XMLStreamException;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.fao.fenix.communication.server.FenixCommunicationModule;

public class ServicesToCommunication {

    public static String NAMESPACE = "http://webservice.communication.fenix.fao.org/xsd";

    public static FenixCommunicationModule communicationModule;

    public ServicesToCommunication() {
        communicationModule = new FenixCommunicationModule();
    }

    public OMElement ping(OMElement element) throws XMLStreamException {
        element.build();
        element.detach();
        OMElement symbolTargetPeer = element.getFirstElement();
        String targetPeer = symbolTargetPeer.getText();
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace(NAMESPACE, "ns");
        OMElement method = fac.createOMElement("pingResponse", omNs);
        OMElement value = fac.createOMElement("return", omNs);
        communicationModule = new FenixCommunicationModule();
        String isOn = String.valueOf(communicationModule.pingPeer(targetPeer));
        value.addChild(fac.createOMText(value, isOn));
        method.addChild(value);
        return method;
    }
}
