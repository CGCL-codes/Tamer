package org.mobicents.slee.resource.diameter.sh.events.avp.userdata;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each  Java content interface and Java element interface 
 * generated in the org.mobicents.slee.resource.diameter.sh.events.avp.userdata package. 
 * <p>An ObjectFactory allows you to programatically construct new instances of the Java representation 
 * for XML content. The Java representation of XML content can consist of schema derived interfaces 
 * and classes representing the binding of schema type definitions, element declarations and model 
 * groups.  Factory methods for each of these are provided in this class.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
@XmlRegistry
public class ObjectFactory {

    private static final QName _ShData_QNAME = new QName("", "Sh-Data");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes
     * for package: org.mobicents.slee.resource.diameter.sh.events.avp.userdata
     * 
     */
    public ObjectFactory() {
    }

    public TChargingInformation createChargingInformation() {
        return new TChargingInformation();
    }

    public TTrigger createTrigger() {
        return new TTrigger();
    }

    public TSePoTriExtension createSePoTriExtension() {
        return new TSePoTriExtension();
    }

    public TApplicationServer createApplicationServer() {
        return new TApplicationServer();
    }

    public TIFCs createIFCs() {
        return new TIFCs();
    }

    public TPublicIdentityExtension createPublicIdentityExtension() {
        return new TPublicIdentityExtension();
    }

    public TServiceData createServiceData() {
        return new TServiceData();
    }

    public TShDataExtension2 createShDataExtension2() {
        return new TShDataExtension2();
    }

    public TShIMSDataExtension createShIMSDataExtension() {
        return new TShIMSDataExtension();
    }

    public TShDataExtension createShDataExtension() {
        return new TShDataExtension();
    }

    public TCSLocationInformation createCSLocationInformation() {
        return new TCSLocationInformation();
    }

    public TInitialFilterCriteria createInitialFilterCriteria() {
        return new TInitialFilterCriteria();
    }

    public THeader createHeader() {
        return new THeader();
    }

    public TPublicIdentity createPublicIdentity() {
        return new TPublicIdentity();
    }

    public TSessionDescription createSessionDescription() {
        return new TSessionDescription();
    }

    public TPSLocationInformation createPSLocationInformation() {
        return new TPSLocationInformation();
    }

    public TShData createShData() {
        return new TShData();
    }

    public TShIMSData createShIMSData() {
        return new TShIMSData();
    }

    public TShIMSDataExtension3 createShIMSDataExtension3() {
        return new TShIMSDataExtension3();
    }

    public TShIMSDataExtension2 createShIMSDataExtension2() {
        return new TShIMSDataExtension2();
    }

    public TISDNAddress createISDNAddress() {
        return new TISDNAddress();
    }

    public TExtension createExtension() {
        return new TExtension();
    }

    public TPublicIdentityExtension2 createPublicIdentityExtension2() {
        return new TPublicIdentityExtension2();
    }

    public TDSAI createDSAI() {
        return new TDSAI();
    }

    public TTransparentData createTransparentData() {
        return new TTransparentData();
    }

    public TSePoTri createSePoTri() {
        return new TSePoTri();
    }

    @XmlElementDecl(namespace = "", name = "Sh-Data")
    public JAXBElement<TShData> createShData(TShData value) {
        return new JAXBElement<TShData>(_ShData_QNAME, TShData.class, null, value);
    }
}
