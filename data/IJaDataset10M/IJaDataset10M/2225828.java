package com.jawise.serviceadapter.test.svc.soap.computerparts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.jawise.serviceadapter.test.svc.soap package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private static final QName _PartQuantity_QNAME = new QName("http://soap.svc.test.serviceadapter.jawise.com", "quantity");

    private static final QName _PartAmout_QNAME = new QName("http://soap.svc.test.serviceadapter.jawise.com", "amout");

    private static final QName _PartId_QNAME = new QName("http://soap.svc.test.serviceadapter.jawise.com", "id");

    private static final QName _PartDetails_QNAME = new QName("http://soap.svc.test.serviceadapter.jawise.com", "details");

    private static final QName _OrderParts_QNAME = new QName("http://soap.svc.test.serviceadapter.jawise.com", "parts");

    private static final QName _OrderTelephone_QNAME = new QName("http://soap.svc.test.serviceadapter.jawise.com", "telephone");

    private static final QName _OrderTotal_QNAME = new QName("http://soap.svc.test.serviceadapter.jawise.com", "total");

    private static final QName _OrderRequestdate_QNAME = new QName("http://soap.svc.test.serviceadapter.jawise.com", "requestdate");

    private static final QName _OrderEmail_QNAME = new QName("http://soap.svc.test.serviceadapter.jawise.com", "email");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.jawise.serviceadapter.test.svc.soap
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Part }
     * 
     */
    public Part createPart() {
        return new Part();
    }

    /**
     * Create an instance of {@link ArrayOfPart }
     * 
     */
    public ArrayOfPart createArrayOfPart() {
        return new ArrayOfPart();
    }

    /**
     * Create an instance of {@link PurchaseOrder }
     * 
     */
    public PurchaseOrder createPurchaseOrder() {
        return new PurchaseOrder();
    }

    /**
     * Create an instance of {@link Order }
     * 
     */
    public Order createOrder() {
        return new Order();
    }

    /**
     * Create an instance of {@link PurchaseOrderResponse }
     * 
     */
    public PurchaseOrderResponse createPurchaseOrderResponse() {
        return new PurchaseOrderResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.svc.test.serviceadapter.jawise.com", name = "quantity", scope = Part.class)
    public JAXBElement<String> createPartQuantity(String value) {
        return new JAXBElement<String>(_PartQuantity_QNAME, String.class, Part.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.svc.test.serviceadapter.jawise.com", name = "amout", scope = Part.class)
    public JAXBElement<String> createPartAmout(String value) {
        return new JAXBElement<String>(_PartAmout_QNAME, String.class, Part.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.svc.test.serviceadapter.jawise.com", name = "id", scope = Part.class)
    public JAXBElement<String> createPartId(String value) {
        return new JAXBElement<String>(_PartId_QNAME, String.class, Part.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.svc.test.serviceadapter.jawise.com", name = "details", scope = Part.class)
    public JAXBElement<String> createPartDetails(String value) {
        return new JAXBElement<String>(_PartDetails_QNAME, String.class, Part.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfPart }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.svc.test.serviceadapter.jawise.com", name = "parts", scope = Order.class)
    public JAXBElement<ArrayOfPart> createOrderParts(ArrayOfPart value) {
        return new JAXBElement<ArrayOfPart>(_OrderParts_QNAME, ArrayOfPart.class, Order.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.svc.test.serviceadapter.jawise.com", name = "telephone", scope = Order.class)
    public JAXBElement<String> createOrderTelephone(String value) {
        return new JAXBElement<String>(_OrderTelephone_QNAME, String.class, Order.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.svc.test.serviceadapter.jawise.com", name = "total", scope = Order.class)
    public JAXBElement<String> createOrderTotal(String value) {
        return new JAXBElement<String>(_OrderTotal_QNAME, String.class, Order.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.svc.test.serviceadapter.jawise.com", name = "requestdate", scope = Order.class)
    public JAXBElement<String> createOrderRequestdate(String value) {
        return new JAXBElement<String>(_OrderRequestdate_QNAME, String.class, Order.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.svc.test.serviceadapter.jawise.com", name = "id", scope = Order.class)
    public JAXBElement<String> createOrderId(String value) {
        return new JAXBElement<String>(_PartId_QNAME, String.class, Order.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.svc.test.serviceadapter.jawise.com", name = "email", scope = Order.class)
    public JAXBElement<String> createOrderEmail(String value) {
        return new JAXBElement<String>(_OrderEmail_QNAME, String.class, Order.class, value);
    }
}
