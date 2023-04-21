package sc.fgrid.engine;

import java.net.URL;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.log4j.Logger;
import sc.fgrid.common.JaxbValidationEventHandler;
import sc.fgrid.jsdl.ApplicationType;
import sc.fgrid.jsdl.JobDefinitionType;
import sc.fgrid.jsdl.JobIdentificationType;
import sc.fgrid.jsdl.ObjectFactory;
import sc.fgrid.jsdl.POSIXApplicationType;
import sc.fgrid.jsdl.ResourcesType;
import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * Add functionallity to a sc.fgrid.jsdl.JobDefinition, which is one level below
 * the root class of a JSDL Document for ARC. This class uses the jsdl-noany.xsd
 * to unmarshall/marshall
 */
@javax.xml.bind.annotation.XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "jsdl:JobDefinition_Type")
public class JsdlArc extends JobDefinitionType {

    private Logger log = Logger.getLogger(JsdlArc.class);

    /**
     * If this is set to true, please also recreate files in package
     * sc.fgrid.jsdl according to other schema files.
     */
    private static final boolean marshallFrom3Schemas = false;

    /**
     * from https://jaxb.dev.java.net/faq/index.html : Context is thread save,
     * but the Marshaller and Unmarshaller classes are not thread safe.
     */
    private static JAXBContext jaxbcontext = null;

    /**
     * From javadoc:javax.xml.validation.Schema : A Schema object is thread safe
     * and applications are encouraged to share it across many parsers in many
     * threads.
     */
    private static Schema schema = null;

    public JsdlArc() {
    }

    /**
     * Create an empty JobIdentification object, in case there is none, and
     * return the object in any case for conveniance.
     */
    JobIdentificationType assert_JobIdentification() {
        if (jobDescription.getJobIdentification() == null) {
            ObjectFactory of = new ObjectFactory();
            jobDescription.setJobIdentification(of.createJobIdentificationType());
        }
        return jobDescription.getJobIdentification();
    }

    /**
     * Create an empty Application object, in case there is none, and return the
     * object in any case for conveniance.
     */
    ApplicationType assert_Application() {
        if (jobDescription.getApplication() == null) {
            ObjectFactory of = new ObjectFactory();
            jobDescription.setApplication(of.createApplicationType());
        }
        return jobDescription.getApplication();
    }

    /**
     * Create an empty POSIXApplication object, in case there is none, and
     * return the object in any case for convenience. Will create the
     * surrounding Application object if needed.
     */
    POSIXApplicationType assert_POSIXApplication() {
        ApplicationType a = assert_Application();
        if (a.getPOSIXApplication() == null) {
            ObjectFactory of = new ObjectFactory();
            a.setPOSIXApplication(of.createPOSIXApplicationType());
        }
        return a.getPOSIXApplication();
    }

    /**
     * Create an empty Resources object, in case there is none, and return the
     * object in any case for conveniance.
     */
    ResourcesType assert_Resources() {
        if (jobDescription.getResources() == null) {
            ObjectFactory of = new ObjectFactory();
            jobDescription.setResources(of.createResourcesType());
        }
        return jobDescription.getResources();
    }

    /**
     * This is not implemented. Unfortunately javax.xml.bind.Validate is
     * depreciated and not implemented anymore in JAXB 2.0. The way out would be
     * to write (marshall) a document to a stream and read it back with
     * validation on during unmarshalling. This should be done in this method.
     * 
     * @throws RuntimeException
     *             always
     */
    void validate() {
        throw new RuntimeException("JsdlArc.validate not implemented!");
    }

    /**
     * Create the Context for (un)marshaller for the JSDL with Posix and ARC
     * extensions. Needed to be called only once, anytime before it is used the
     * first time. This method is thread save. Also create a thread safe Schema
     * object for validation.
     */
    private static synchronized void createContextAndSchema() {
        try {
            if (jaxbcontext == null) {
                if (marshallFrom3Schemas) {
                    jaxbcontext = JAXBContext.newInstance("sc.fgrid.jsdl:sc.fgrid.jsdlposix:sc.fgrid.jsdlarc:sc.fgrid.engine");
                } else {
                    jaxbcontext = javax.xml.bind.JAXBContext.newInstance("sc.fgrid.jsdl:sc.fgrid.engine");
                }
            }
            if (schema == null) {
                ClassLoader classLoader = JsdlArc.class.getClassLoader();
                if (marshallFrom3Schemas) {
                    URL schemaURL1 = classLoader.getResource("sc/fgrid/jsdl.xsd");
                    java.net.URL schemaURL2 = classLoader.getResource("sc/fgrid/jsdlposix.xsd");
                    java.net.URL schemaURL3 = classLoader.getResource("sc/fgrid/jsdlarc.xsd");
                    String schema1 = schemaURL1.toExternalForm();
                    String schema2 = schemaURL2.toExternalForm();
                    String schema3 = schemaURL3.toExternalForm();
                    Source source1 = new StreamSource(schema1);
                    Source source2 = new StreamSource(schema2);
                    Source source3 = new StreamSource(schema3);
                    SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
                    Source schemas[] = { source1, source2, source3 };
                    schema = sf.newSchema(schemas);
                } else {
                    java.net.URL schemaURL = classLoader.getResource("sc/fgrid/jsdl-noany.xsd");
                    javax.xml.validation.SchemaFactory sf = javax.xml.validation.SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
                    schema = sf.newSchema(schemaURL);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("Problem creating JAXBContext and Schema for it!", ex);
        }
    }

    /**
     * Create an unmarshaller which ummarshalls the JobDecription object to this
     * class. This unmarshaller is validating. This method is thread save, but
     * the returned object is not thread save, create a new unmarshaller for
     * each Thread!
     * 
     * @param validate
     *            set true to enable validation against schema
     * @param sb
     *            sink for error messages during parsing
     */
    static Unmarshaller createUnmarshaller(boolean validate, StringBuffer sb) {
        createContextAndSchema();
        try {
            Unmarshaller unmarshaller = jaxbcontext.createUnmarshaller();
            unmarshaller.setSchema(schema);
            ValidationEventHandler veh = new JaxbValidationEventHandler(sb, null, null);
            unmarshaller.setEventHandler(veh);
            if (validate) {
                unmarshaller.setSchema(schema);
            }
            return unmarshaller;
        } catch (Exception ex) {
            throw new RuntimeException("Problem creating unmarshaler for Jsdl - ARC !", ex);
        }
    }

    /**
     * Create a marshaller which marshalls the JobDecription object to this
     * class. This marshaller is not validating! This method is thread save, but
     * the returned object is not thread save, create a new marshaller for each
     * Thread!
     * 
     * @param validate
     *            set true to enable validation against schema
     */
    static Marshaller createMarshaller(boolean validate) {
        createContextAndSchema();
        try {
            Marshaller marshaller = jaxbcontext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
            marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NamespacePrefixMapper() {

                /**
                         * @see com.sun.xml.bind.marshaller.NamespacePrefixMapper#getPreferredPrefix(
                         *      java.lang.String, java.lang.String, boolean)
                         */
                @Override
                public String getPreferredPrefix(String namespace, String sug, boolean rp) {
                    if ("http://schemas.ggf.org/jsdl/2005/11/jsdl".equals(namespace)) return "jsdl";
                    if ("http://schemas.ggf.org/jsdl/2005/11/jsdl-posix".equals(namespace)) return "jsdl-posix";
                    if ("http://www.nordugrid.org/ws/schemas/jsdl-arc".equals(namespace)) return "jsdl-arc";
                    return sug;
                }
            });
            marshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
            if (validate) {
                marshaller.setSchema(schema);
            }
            return marshaller;
        } catch (Exception ex) {
            throw new RuntimeException("Problem creating marshaler for jsdl for arc!", ex);
        }
    }
}
