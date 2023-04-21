package playground.tnicolai.urbansim.com.matsim.config;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the playground.tnicolai.urbansim.com.matsim.config package. 
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

    private static final QName _Config_QNAME = new QName("", "config");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: playground.tnicolai.urbansim.com.matsim.config
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ControlerType }
     * 
     */
    public ControlerType createControlerType() {
        return new ControlerType();
    }

    /**
     * Create an instance of {@link ConfigType }
     * 
     */
    public ConfigType createConfigType() {
        return new ConfigType();
    }

    /**
     * Create an instance of {@link PlanCalcScoreType }
     * 
     */
    public PlanCalcScoreType createPlanCalcScoreType() {
        return new PlanCalcScoreType();
    }

    /**
     * Create an instance of {@link UrbansimParameterType }
     * 
     */
    public UrbansimParameterType createUrbansimParameterType() {
        return new UrbansimParameterType();
    }

    /**
     * Create an instance of {@link NetworkType }
     * 
     */
    public NetworkType createNetworkType() {
        return new NetworkType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConfigType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "config")
    public JAXBElement<ConfigType> createConfig(ConfigType value) {
        return new JAXBElement<ConfigType>(_Config_QNAME, ConfigType.class, null, value);
    }
}
