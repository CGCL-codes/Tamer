package com.jawise.serviceadapter.test.svc.soap.cars;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import javax.xml.namespace.QName;
import org.codehaus.xfire.XFireRuntimeException;
import org.codehaus.xfire.aegis.AegisBindingProvider;
import org.codehaus.xfire.annotations.AnnotationServiceFactory;
import org.codehaus.xfire.annotations.jsr181.Jsr181WebAnnotations;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.jaxb2.JaxbTypeRegistry;
import org.codehaus.xfire.service.Endpoint;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.soap.AbstractSoapBinding;
import org.codehaus.xfire.transport.TransportManager;

public class CarRentalServiceAClient {

    private static XFireProxyFactory proxyFactory = new XFireProxyFactory();

    private HashMap endpoints = new HashMap();

    private Service service0;

    public CarRentalServiceAClient() {
        create0();
        Endpoint CarRentalServiceAPortTypeLocalEndpointEP = service0.addEndpoint(new QName("http://soap.svc.test.serviceadapter.jawise.com", "CarRentalServiceAPortTypeLocalEndpoint"), new QName("http://soap.svc.test.serviceadapter.jawise.com", "CarRentalServiceAPortTypeLocalBinding"), "xfire.local://CarRentalServiceA");
        endpoints.put(new QName("http://soap.svc.test.serviceadapter.jawise.com", "CarRentalServiceAPortTypeLocalEndpoint"), CarRentalServiceAPortTypeLocalEndpointEP);
        Endpoint CarRentalServiceAHttpPortEP = service0.addEndpoint(new QName("http://soap.svc.test.serviceadapter.jawise.com", "CarRentalServiceAHttpPort"), new QName("http://soap.svc.test.serviceadapter.jawise.com", "CarRentalServiceAHttpBinding"), "http://localhost:8080/serviceadapter-testservices/soa/CarRentalServiceA");
        endpoints.put(new QName("http://soap.svc.test.serviceadapter.jawise.com", "CarRentalServiceAHttpPort"), CarRentalServiceAHttpPortEP);
    }

    public Object getEndpoint(Endpoint endpoint) {
        try {
            return proxyFactory.create((endpoint).getBinding(), (endpoint).getUrl());
        } catch (MalformedURLException e) {
            throw new XFireRuntimeException("Invalid URL", e);
        }
    }

    public Object getEndpoint(QName name) {
        Endpoint endpoint = ((Endpoint) endpoints.get((name)));
        if ((endpoint) == null) {
            throw new IllegalStateException("No such endpoint!");
        }
        return getEndpoint((endpoint));
    }

    public Collection getEndpoints() {
        return endpoints.values();
    }

    private void create0() {
        TransportManager tm = (org.codehaus.xfire.XFireFactory.newInstance().getXFire().getTransportManager());
        HashMap props = new HashMap();
        props.put("annotations.allow.interface", true);
        AnnotationServiceFactory asf = new AnnotationServiceFactory(new Jsr181WebAnnotations(), tm, new AegisBindingProvider(new JaxbTypeRegistry()));
        asf.setBindingCreationEnabled(false);
        service0 = asf.create((com.jawise.serviceadapter.test.svc.soap.cars.CarRentalServiceAPortType.class), props);
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://soap.svc.test.serviceadapter.jawise.com", "CarRentalServiceAHttpBinding"), "http://schemas.xmlsoap.org/soap/http");
        }
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://soap.svc.test.serviceadapter.jawise.com", "CarRentalServiceAPortTypeLocalBinding"), "urn:xfire:transport:local");
        }
    }

    public CarRentalServiceAPortType getCarRentalServiceAPortTypeLocalEndpoint() {
        return ((CarRentalServiceAPortType) (this).getEndpoint(new QName("http://soap.svc.test.serviceadapter.jawise.com", "CarRentalServiceAPortTypeLocalEndpoint")));
    }

    public CarRentalServiceAPortType getCarRentalServiceAPortTypeLocalEndpoint(String url) {
        CarRentalServiceAPortType var = getCarRentalServiceAPortTypeLocalEndpoint();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

    public CarRentalServiceAPortType getCarRentalServiceAHttpPort() {
        return ((CarRentalServiceAPortType) (this).getEndpoint(new QName("http://soap.svc.test.serviceadapter.jawise.com", "CarRentalServiceAHttpPort")));
    }

    public CarRentalServiceAPortType getCarRentalServiceAHttpPort(String url) {
        CarRentalServiceAPortType var = getCarRentalServiceAHttpPort();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }
}
