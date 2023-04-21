package org.apache.axis2.jaxws.message.headers;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

    private static final QName _ConfigHeader1_QNAME = new QName("http://headers.message.jaxws.axis2.apache.org/types4", "ConfigHeader1");

    private static final QName _ConfigHeader3_QNAME = new QName("http://headers.message.jaxws.axis2.apache.org/types4", "ConfigHeader3");

    private static final QName _ConfigHeader2_QNAME = new QName("http://headers.message.jaxws.axis2.apache.org/types4", "ConfigHeader2");

    private static final QName _ConfigBody_QNAME = new QName("http://headers.message.jaxws.axis2.apache.org/types4", "ConfigBody");

    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ConfigHeader }
     * 
     */
    public ConfigHeader createConfigHeader() {
        return new ConfigHeader();
    }

    /**
     * Create an instance of {@link ConfigHeader }
     * 
     */
    public ConfigBody createConfigBody() {
        return new ConfigBody();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConfigBody }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://headers.message.jaxws.axis2.apache.org/types4", name = "ConfigBody")
    public JAXBElement<ConfigBody> createConfigBody(ConfigBody value) {
        return new JAXBElement<ConfigBody>(_ConfigBody_QNAME, ConfigBody.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConfigHeader }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://headers.message.jaxws.axis2.apache.org/types4", name = "ConfigHeader1")
    public JAXBElement<ConfigHeader> createConfigHeader1(ConfigHeader value) {
        return new JAXBElement<ConfigHeader>(_ConfigHeader1_QNAME, ConfigHeader.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConfigHeader }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://headers.message.jaxws.axis2.apache.org/types4", name = "ConfigHeader3")
    public JAXBElement<ConfigHeader> createConfigHeader3(ConfigHeader value) {
        return new JAXBElement<ConfigHeader>(_ConfigHeader3_QNAME, ConfigHeader.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConfigHeader }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://headers.message.jaxws.axis2.apache.org/types4", name = "ConfigHeader2")
    public JAXBElement<ConfigHeader> createConfigHeader2(ConfigHeader value) {
        return new JAXBElement<ConfigHeader>(_ConfigHeader2_QNAME, ConfigHeader.class, null, value);
    }
}
