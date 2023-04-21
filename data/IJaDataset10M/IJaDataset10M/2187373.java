package org.sensorweb.service.scs;

import org.sensorweb.model.swe.observation.*;
import org.sensorweb.model.scs.*;
import org.sensorweb.service.ServiceException;
import org.sensorweb.model.sensorml.Sensor;
import org.sensorweb.model.sensorml.StationaryPlatform;
import org.jdom.Element;
import java.util.concurrent.atomic.*;
import net.opengis.om.x10.ObservationCollectionDocument;
import net.opengis.sos.x10.GetObservationDocument;
import net.opengis.sos.x10.GetObservationDocument.GetObservation;

/**
 * @author Xingchen Chu & Tom Kobialka
 * @version 0.1
 *
 * <code>SensorCollecitonService</code> has a set of methods to collect 
 * observation and measurement information from a sensor or a collection
 * of sensors
 */
public interface SensorCollectionService {

    /**
	 * <code> getObservation </code> collect the observation information
	 * from a single sensor, platform or database
	 * 
	 * @param requestData
	 * @return
	 */
    ObservationCollectionDocument getObservation(GetObservationDocument.GetObservation requestData, AtomicInteger atomInt) throws ServiceException;

    /**
	 * <code> describeSensor </code> gets the sensor information that is
	 * described by sensorML 
	 * 
	 * @param describeType
	 * @return
	 */
    Sensor[] describeSensor(DescribeType describeType) throws ServiceException;

    /**
	 * <code> describePlatform </code> gets the sensor platform information
	 * that is described by sensorML
	 * 
	 * @param describeType
	 * @return
	 */
    StationaryPlatform[] describePlatform(DescribeType describeType) throws ServiceException;

    public org.w3c.dom.Element getServiceInformation(org.w3c.dom.Element id) throws ServiceException;
}
