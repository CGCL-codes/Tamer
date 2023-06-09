package net.seagis.sos.webservice.soap;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import net.seagis.catalog.NoSuchTableException;
import net.seagis.coverage.web.Service;
import net.seagis.coverage.web.ServiceVersion;
import net.seagis.coverage.web.WebServiceException;
import net.seagis.observation.ObservationCollectionEntry;
import net.seagis.sos.Capabilities;
import net.seagis.sos.DescribeSensor;
import net.seagis.sos.GetCapabilities;
import net.seagis.sos.GetObservation;
import net.seagis.sos.GetResult;
import net.seagis.sos.GetResultResponse;
import net.seagis.sos.InsertObservation;
import net.seagis.sos.InsertObservationResponse;
import net.seagis.sos.RegisterSensor;
import net.seagis.sos.RegisterSensorResponse;
import net.seagis.sos.webservice.SOSworker;

/**
 *
 * @author Guilhem Legal
 */
@WebService(name = "SOService")
@SOAPBinding(parameterStyle = ParameterStyle.BARE)
public class SOService {

    /**
     * use for debugging purpose
     */
    Logger logger = Logger.getLogger("fr.geomatys.sos");

    /**
     * A map containing the Capabilities Object already load from file.
     */
    private Map<String, Object> capabilities = new HashMap<String, Object>();

    /**
     * a service worker
     */
    private SOSworker worker;

    /**
     * The user directory where to store the configuration file on Unix platforms.
     */
    private static final String UNIX_DIRECTORY = ".sicade";

    /**
     * The user directory where to store the configuration file on Windows platforms.
     */
    private static final String WINDOWS_DIRECTORY = "Application Data\\Sicade";

    /**
     * A JAXB unmarshaller used to create java object from XML file.
     */
    private Unmarshaller unmarshaller;

    /**
     * Initialize the database connection.
     */
    public SOService() throws SQLException, IOException, NoSuchTableException, JAXBException {
        worker = new SOSworker();
        JAXBContext jbcontext = JAXBContext.newInstance("net.seagis.sos:net.seagis.observation");
        unmarshaller = jbcontext.createUnmarshaller();
        worker.setServiceURL("http://localhost:8080/SOServer/SOService");
        worker.setVersion(new ServiceVersion(Service.OWS, "1.0.0"));
    }

    /**
     * Web service operation describing the service and its capabilities.
     * 
     * @param requestCapabilities A document specifying the section you would obtain like :
     *      ServiceIdentification, ServiceProvider, Contents, operationMetadata.
     */
    @WebMethod(action = "getCapabilities")
    public Capabilities getCapabilities(@WebParam(name = "GetCapabilities") GetCapabilities requestCapabilities) throws SOServiceException {
        try {
            logger.info("received SOAP getCapabilities request");
            worker.setStaticCapabilities((Capabilities) getCapabilitiesObject());
            return worker.getCapabilities(requestCapabilities);
        } catch (WebServiceException ex) {
            throw new SOServiceException(ex.getMessage(), ex.getExceptionCode().name(), ex.getVersion());
        } catch (JAXBException ex) {
            throw new SOServiceException(ex.getMessage(), ex.getErrorCode(), null);
        }
    }

    /**
     * Web service operation whitch return an sml description of the specified sensor.
     * 
     * @param requestDescSensor A document specifying the id of the sensor that we want the description.
     */
    @WebMethod(action = "describeSensor")
    public String describeSensor(@WebParam(name = "DescribeSensor") DescribeSensor requestDescSensor) throws SOServiceException {
        try {
            logger.info("received SOAP DescribeSensor request");
            return worker.describeSensor(requestDescSensor);
        } catch (WebServiceException ex) {
            throw new SOServiceException(ex.getMessage(), ex.getExceptionCode().name(), ex.getVersion());
        }
    }

    /**
     * Web service operation whitch respond a collection of observation satisfying 
     * the restriction specified in the query.
     * 
     * @param requestObservation a document specifying the parameter of the request.
     */
    @WebMethod(action = "getObservation")
    public ObservationCollectionEntry getObservation(@WebParam(name = "GetObservation") GetObservation requestObservation) throws SOServiceException {
        try {
            logger.info("received SOAP getObservation request");
            return worker.getObservation(requestObservation);
        } catch (WebServiceException ex) {
            throw new SOServiceException(ex.getMessage(), ex.getExceptionCode().name(), ex.getVersion());
        }
    }

    /**
     * Web service operation
     */
    @WebMethod(action = "getResult")
    public GetResultResponse getResult(@WebParam(name = "GetResult") GetResult requestResult) throws SOServiceException {
        try {
            logger.info("received SOAP getResult request");
            return worker.getResult(requestResult);
        } catch (WebServiceException ex) {
            throw new SOServiceException(ex.getMessage(), ex.getExceptionCode().name(), ex.getVersion());
        }
    }

    /**
     * Web service operation whitch register a Sensor in the SensorML database, 
     * and initialize its observation by adding an observation template in the O&M database.
     *
     * @param requestRegSensor A request containing a SensorML File describing a Sensor,
     *                         and an observation template for this sensor.
     */
    @WebMethod(action = "registerSensor")
    public RegisterSensorResponse registerSensor(@WebParam(name = "RegisterSensor") RegisterSensor requestRegSensor) throws SOServiceException {
        try {
            logger.info("received SOAP registerSensor request");
            return worker.registerSensor(requestRegSensor);
        } catch (WebServiceException ex) {
            throw new SOServiceException(ex.getMessage(), ex.getExceptionCode().name(), ex.getVersion());
        }
    }

    /**
     * Web service operation whitch insert a new Observation for the specified sensor
     * in the O&M database.
     * 
     * @param requestInsObs an InsertObservation request containing an O&M object and a Sensor id.
     */
    @WebMethod(action = "InsertObservation")
    public InsertObservationResponse insertObservation(@WebParam(name = "InsertObservation") InsertObservation requestInsObs) throws SOServiceException {
        try {
            logger.info("received SOAP insertObservation request");
            return worker.insertObservation(requestInsObs);
        } catch (WebServiceException ex) {
            throw new SOServiceException(ex.getMessage(), ex.getExceptionCode().name(), ex.getVersion());
        }
    }

    /**
     * Returns the file where to read the capabilities document for each service.
     * If no such file is found, then this method returns {@code null}.
     *
     * @param  version the version of the service.
     * @return The capabilities Object, or {@code null} if none.
     */
    public Object getCapabilitiesObject() throws JAXBException {
        String fileName = "SOSCapabilities1.0.0.xml";
        if (fileName == null) {
            return null;
        } else {
            Object response = capabilities.get(fileName);
            if (response == null) {
                String home;
                String env = System.getenv("CATALINA_HOME");
                File path = new File(env + "/sos_configuration/");
                if (!path.isDirectory()) {
                    home = System.getProperty("user.home");
                    if (System.getProperty("os.name", "").startsWith("Windows")) {
                        path = new File(home, WINDOWS_DIRECTORY);
                    } else {
                        path = new File(home, UNIX_DIRECTORY);
                    }
                }
                File f = new File(path, fileName);
                logger.info(f.toString());
                response = unmarshaller.unmarshal(f);
                capabilities.put(fileName, response);
            }
            return response;
        }
    }
}
