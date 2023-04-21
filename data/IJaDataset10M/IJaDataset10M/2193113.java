package eu.planets_project.tb.api.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import eu.planets_project.tb.api.services.tags.ServiceTag;

/**
 * @author Andrew Lindley, ARC
 * 
 * This interface describes an TestbedService i.e. a Service which has been registered
 * through the Testbed's service registration interface (i.e. building a xmlrequest 
 *  template, invoking it with data and building an Xpath Statement) by using the wsclient classes
 * for actual service execution. The Testbed's Service Registry Impl registeres instances
 * of objects implementing this interface.
 * 
 * Important: A class implementing this interface contains all Information that's required 
 * for being able to invoke an experiment, execute the underlying service, and knows how to
 * extract the resulting data.
 */
public interface TestbedServiceTemplate {

    public String getDescription();

    public void setDescription(String sDescription);

    public String getEndpoint();

    public void setEndpoint(String sURL);

    /**
	 * Takes the Service's Endpoint and if extract = true, fetches and stores the WSDL's content.
	 * @param sURL
	 * @param extract true: fetches the WSDL content and stores it in .setWSDLContent(String content)
	 */
    public void setEndpoint(String sURL, boolean extract);

    public String getName();

    public void setName(String sName);

    /**
	 * Returns a list of all registered service operations.
	 * @return
	 */
    public List<ServiceOperation> getAllServiceOperations();

    /**
	 * Returns a list of all available ServiceOperations of a given Type 
	 * e.g. PA or PC
	 * @param serviceType: ServiceOperation.SERVICE_OPERATION_TYPE_XY
	 * @return
	 */
    public List<ServiceOperation> getAllServiceOperationsByType(String serviceType);

    public List<String> getAllServiceOperationNames();

    /**
	 * @param sName
	 * @return null if operation is not found
	 */
    public ServiceOperation getServiceOperation(String sName);

    /**
	 * @param sOperationName: null not allowed
	 * @param xmlRequestTemplate: null not allowed
	 * @param xPathToOutput: null not allowed
	 */
    public void addServiceOperation(String sOperationName, String xmlRequestTemplate, String xpathToOutput);

    public void addServiceOperation(ServiceOperation operation);

    public void removeServiceOperation(String sOperationName);

    public void setServiceOperations(List<ServiceOperation> operations);

    public boolean isEndpointWSICompliant();

    public void setEndpointWSICompliant(boolean compliant);

    /**
	 * Uses MD5 UUID Generation out of WSDL Content. This has the advantage, services
	 * can be recogized directly by their source. To getUUID either Endpoint or WSDL content need to be already set.
	 * The first time getUUID is called
	 * @return
	 */
    public String getUUID();

    public String getWSDLContent();

    public void setWSDLContent(String content);

    /**
	 * Fetches a given http service endpoint URL and fetches its content which
	 * is then stored.
	 * @param sURL
	 */
    public void extractWSDLContent(String sURL) throws FileNotFoundException, IOException;

    public boolean isOperationRegistered(String sOpName);

    /**
	 * Allows the user to use free search-tags and values for tagging service templates.
	 * These tags should be searchable via the web interface. 
	 * When duplicate tag names: the old one is removed
	 * @param sTagName: null not allowed
	 * @param sTagValue: null not allowed
	 */
    public void addTag(ServiceTag tag);

    public void removeTag(String sTagName);

    public void removeTags();

    /**
	 * @param sTagName
	 * @return may return null if no item for TagName was found
	 */
    public ServiceTag getTag(String sTagName);

    /**
	 * A List of all ServiceTags for a service template
	 * @param serviceUUID
	 * @return
	 */
    public List<ServiceTag> getAllTags();

    /**
	 * Records the serviceTemplates deployment date i.e. when the first
	 * service operation was registered
	 * @param timeInMillis
	 */
    public void setDeploymentDate(long timeInMillis);

    /**
	 * Returns the templates deployment date
	 * i.e. when the first service operation was registered
	 * @return
	 */
    public Calendar getDeploymentDate();

    /**
	 * @author Andrew Lindley, ARC
	 *
	 */
    public interface ServiceOperation {

        public String getName();

        public void setName(String sName);

        public String getDescription();

        public void setDescription(String sDescr);

        public String getXMLRequestTemplate();

        public void setXMLRequestTemplate(String template);

        public String getXPathToOutput();

        public void setXPathToOutput(String xpath);

        /**
		 * Returns the restriction of maximum Input Elements 
		 * @return
		 */
        public int getMaxSupportedInputFiles();

        public void setMaxSupportedInputFiles(int i);

        /**
		 * Returns the restriction of minimum required Input Elements 
		 * @return
		 */
        public int getMinRequiredInputFiles();

        public void setMinRequiredInputFiles(int i);

        public final String SERVICE_OPERATION_TYPE_MIGRATION = "PA";

        public final String SERVICE_OPERATION_TYPE_CHARACTERISATION = "PC";

        public final String SERVICE_OPERATION_TYPE_EVALUATION = "EV";

        /**
		 * A service operation can be classified by a certain type 
		 * e.g. PA or PC
		 * @return
		 */
        public String getServiceOperationType();

        public void setServiceOperationType(String sType);

        public final String OUTPUT_OBJECT_TYPE_FILE = "File";

        public final String OUTPUT_OBJECT_TYPE_String = "String";

        /**
		 * Returns the output object's type. e.g. File or String
		 * @param type
		 */
        public void setOutputObjectType(String type);

        public String getOutputObjectType();

        /**
		 * Determins if the input type (e.g. file) is handed over by value (e.g. base64) or by reference (URI)
		 * @param b
		 */
        public void setInputTypeIsCallByValue(boolean b);

        public void setInputTypeIsCallByReference(boolean b);

        public boolean isInputTypeCallByValue();

        /**
		 * If is callByValue this method can be used to determine the file-output
		 * type of the service e.g. "doc"
		 * @param s
		 */
        public void setOutputFileType(String s);

        public String getOutputFileType();
    }
}
