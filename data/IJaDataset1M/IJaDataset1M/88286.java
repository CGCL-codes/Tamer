package net.jxta.impl.endpoint.transportMeter;

import net.jxta.endpoint.EndpointAddress;
import net.jxta.util.documentSerializable.DocumentSerializable;
import net.jxta.util.documentSerializable.DocumentSerializableUtilities;
import net.jxta.util.documentSerializable.DocumentSerializationException;
import net.jxta.document.Element;
import net.jxta.document.TextElement;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

/**
 * The Metric for a single Transport
 **/
public class TransportMetric implements DocumentSerializable {

    private String protocol;

    private EndpointAddress endpointAddress;

    private HashMap<EndpointAddress, TransportBindingMetric> transportBindingMetrics = new HashMap<EndpointAddress, TransportBindingMetric>();

    public TransportMetric(TransportMeter transportMeter) {
        this.endpointAddress = transportMeter.getEndpointAddress();
        this.protocol = transportMeter.getProtocol();
    }

    public TransportMetric() {
    }

    public TransportMetric(TransportMetric prototype) {
        this.endpointAddress = prototype.endpointAddress;
        this.protocol = prototype.protocol;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TransportMetric) {
            TransportMetric other = (TransportMetric) obj;
            return protocol.equals(other.protocol) && endpointAddress.equals(other.endpointAddress);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return endpointAddress.hashCode();
    }

    public EndpointAddress getEndpointAddress() {
        return endpointAddress;
    }

    public String getProtocol() {
        return protocol;
    }

    public synchronized void addTransportBindingMetric(TransportBindingMetric transportBindingMetric) {
        transportBindingMetrics.put(transportBindingMetric.getEndpointAddress(), transportBindingMetric);
    }

    public TransportBindingMetric getTransportBindingMetric(EndpointAddress endpointAddress) {
        return transportBindingMetrics.get(endpointAddress);
    }

    public TransportBindingMetric getTransportBindingMetric(TransportBindingMetric prototype) {
        return getTransportBindingMetric(prototype.getEndpointAddress());
    }

    public Iterator<TransportBindingMetric> getTransportBindingMetrics() {
        return transportBindingMetrics.values().iterator();
    }

    public int getTransportBindingMetricsCount() {
        return transportBindingMetrics.size();
    }

    public void serializeTo(Element element) throws DocumentSerializationException {
        DocumentSerializableUtilities.addString(element, "endpointAddress", endpointAddress.toString());
        DocumentSerializableUtilities.addString(element, "protocol", protocol);
        for (Iterator<TransportBindingMetric> i = getTransportBindingMetrics(); i.hasNext(); ) {
            TransportBindingMetric transportBindingMetric = i.next();
            DocumentSerializableUtilities.addDocumentSerializable(element, "binding", transportBindingMetric);
        }
    }

    public void initializeFrom(Element element) throws DocumentSerializationException {
        for (Enumeration e = element.getChildren(); e.hasMoreElements(); ) {
            Element childElement = (TextElement) e.nextElement();
            String tagName = (String) childElement.getKey();
            if (tagName.equals("endpointAddress")) {
                String endpointAddressString = DocumentSerializableUtilities.getString(childElement);
                endpointAddress = new EndpointAddress(endpointAddressString);
            } else if (tagName.equals("protocol")) {
                protocol = DocumentSerializableUtilities.getString(childElement);
            } else if (tagName.equals("binding")) {
                TransportBindingMetric transportBindingMetric = (TransportBindingMetric) DocumentSerializableUtilities.getDocumentSerializable(childElement, TransportBindingMetric.class);
                transportBindingMetrics.put(transportBindingMetric.getEndpointAddress(), transportBindingMetric);
            }
        }
    }

    void mergeMetrics(TransportMetric otherTransportMetric) {
        for (Iterator<TransportBindingMetric> i = otherTransportMetric.getTransportBindingMetrics(); i.hasNext(); ) {
            TransportBindingMetric otherTransportBindingMetric = i.next();
            TransportBindingMetric transportBindingMetric = getTransportBindingMetric(otherTransportBindingMetric.getEndpointAddress());
            if (transportBindingMetric == null) {
                transportBindingMetric = new TransportBindingMetric(otherTransportBindingMetric);
                addTransportBindingMetric(transportBindingMetric);
            }
            transportBindingMetric.mergeMetrics(otherTransportBindingMetric);
        }
    }
}
