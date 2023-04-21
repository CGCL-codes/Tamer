package org.mcisb.kinetics.sabio.model.experimentalconditions;

import java.math.*;
import javax.xml.bind.*;
import javax.xml.bind.annotation.*;
import javax.xml.namespace.*;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.mcisb.kinetics.sabio.model.experimentalconditions package. 
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

    private static final QName _StartValueTemperature_QNAME = new QName("http://sabio.villa-bosch.de/SABIORK/", "start_Value_Temperature");

    private static final QName _KineticLawID_QNAME = new QName("http://sabio.villa-bosch.de/SABIORK/", "kineticLawID");

    private static final QName _EndValuePH_QNAME = new QName("http://sabio.villa-bosch.de/SABIORK/", "end_Value_pH");

    private static final QName _StartValuePH_QNAME = new QName("http://sabio.villa-bosch.de/SABIORK/", "start_Value_pH");

    private static final QName _EndValueTemperature_QNAME = new QName("http://sabio.villa-bosch.de/SABIORK/", "end_Value_Temperature");

    private static final QName _Buffer_QNAME = new QName("http://sabio.villa-bosch.de/SABIORK/", "buffer");

    private static final QName _Unit_QNAME = new QName("http://sabio.villa-bosch.de/SABIORK/", "unit");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.mcisb.kinetics.sabio.model.experimentalconditions
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Temperature }
     * 
     */
    public Temperature createTemperature() {
        return new Temperature();
    }

    /**
     * Create an instance of {@link ExperimentalConditions }
     * 
     */
    public ExperimentalConditions createExperimentalConditions() {
        return new ExperimentalConditions();
    }

    /**
     * Create an instance of {@link PH }
     * 
     */
    public PH createPH() {
        return new PH();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sabio.villa-bosch.de/SABIORK/", name = "start_Value_Temperature")
    public JAXBElement<BigDecimal> createStartValueTemperature(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_StartValueTemperature_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sabio.villa-bosch.de/SABIORK/", name = "kineticLawID")
    public JAXBElement<Long> createKineticLawID(Long value) {
        return new JAXBElement<Long>(_KineticLawID_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sabio.villa-bosch.de/SABIORK/", name = "end_Value_pH")
    public JAXBElement<BigDecimal> createEndValuePH(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_EndValuePH_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sabio.villa-bosch.de/SABIORK/", name = "start_Value_pH")
    public JAXBElement<BigDecimal> createStartValuePH(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_StartValuePH_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sabio.villa-bosch.de/SABIORK/", name = "end_Value_Temperature")
    public JAXBElement<BigDecimal> createEndValueTemperature(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_EndValueTemperature_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sabio.villa-bosch.de/SABIORK/", name = "buffer")
    public JAXBElement<String> createBuffer(String value) {
        return new JAXBElement<String>(_Buffer_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sabio.villa-bosch.de/SABIORK/", name = "unit")
    public JAXBElement<String> createUnit(String value) {
        return new JAXBElement<String>(_Unit_QNAME, String.class, null, value);
    }
}
