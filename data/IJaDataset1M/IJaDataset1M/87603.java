package org.n52.sos.decode.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.gml.AbstractGeometryType;
import net.opengis.gml.AbstractRingPropertyType;
import net.opengis.gml.AbstractRingType;
import net.opengis.gml.DirectPositionListType;
import net.opengis.gml.DirectPositionType;
import net.opengis.gml.EnvelopeType;
import net.opengis.gml.LineStringType;
import net.opengis.gml.LinearRingType;
import net.opengis.gml.PointType;
import net.opengis.gml.PolygonType;
import net.opengis.gml.TimeInstantType;
import net.opengis.gml.TimePositionType;
import net.opengis.gml.impl.LineStringTypeImpl;
import net.opengis.gml.impl.PointTypeImpl;
import net.opengis.gml.impl.PolygonTypeImpl;
import net.opengis.ogc.BBOXType;
import net.opengis.ogc.BinaryComparisonOpType;
import net.opengis.ogc.BinarySpatialOpType;
import net.opengis.ogc.ComparisonOpsType;
import net.opengis.ogc.ExpressionType;
import net.opengis.ogc.LiteralType;
import net.opengis.ogc.LowerBoundaryType;
import net.opengis.ogc.PropertyIsBetweenType;
import net.opengis.ogc.PropertyIsLikeType;
import net.opengis.ogc.PropertyIsNullDocument;
import net.opengis.ogc.PropertyIsNullType;
import net.opengis.ogc.PropertyNameType;
import net.opengis.ogc.SpatialOpsType;
import net.opengis.ogc.UpperBoundaryType;
import net.opengis.om.x10.MeasurementDocument;
import net.opengis.om.x10.MeasurementType;
import net.opengis.om.x10.ObservationType;
import net.opengis.ows.x11.GetCapabilitiesType;
import net.opengis.sensorML.x101.SystemDocument;
import net.opengis.sos.x10.DescribeFeatureTypeDocument;
import net.opengis.sos.x10.DescribeSensorDocument;
import net.opengis.sos.x10.GetCapabilitiesDocument;
import net.opengis.sos.x10.GetFeatureOfInterestDocument;
import net.opengis.sos.x10.GetFeatureOfInterestTimeDocument;
import net.opengis.sos.x10.GetObservationByIdDocument;
import net.opengis.sos.x10.GetObservationDocument;
import net.opengis.sos.x10.GetResultDocument;
import net.opengis.sos.x10.InsertObservationDocument;
import net.opengis.sos.x10.RegisterSensorDocument;
import net.opengis.sos.x10.DescribeSensorDocument.DescribeSensor;
import net.opengis.sos.x10.GetFeatureOfInterestDocument.GetFeatureOfInterest;
import net.opengis.sos.x10.GetFeatureOfInterestDocument.GetFeatureOfInterest.Location;
import net.opengis.sos.x10.GetObservationByIdDocument.GetObservationById;
import net.opengis.sos.x10.GetObservationDocument.GetObservation;
import net.opengis.sos.x10.GetObservationDocument.GetObservation.EventTime;
import net.opengis.sos.x10.GetObservationDocument.GetObservation.FeatureOfInterest;
import net.opengis.sos.x10.GetObservationDocument.GetObservation.Result;
import net.opengis.sos.x10.GetResultDocument.GetResult;
import net.opengis.sos.x10.InsertObservationDocument.InsertObservation;
import net.opengis.sos.x10.ObservationTemplateDocument.ObservationTemplate;
import net.opengis.sos.x10.RegisterSensorDocument.RegisterSensor;
import net.opengis.sos.x10.RegisterSensorDocument.RegisterSensor.SensorDescription;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.n52.sos.CapabilitiesCache;
import org.n52.sos.SosConfigurator;
import org.n52.sos.SosConstants;
import org.n52.sos.SosConstants.GetObservationParams;
import org.n52.sos.decode.GMLDecoder;
import org.n52.sos.decode.IHttpPostRequestDecoder;
import org.n52.sos.decode.OMDecoder;
import org.n52.sos.decode.SensorMLDecoder;
import org.n52.sos.ogc.filter.ComparisonFilter;
import org.n52.sos.ogc.filter.FilterConstants;
import org.n52.sos.ogc.filter.SpatialFilter;
import org.n52.sos.ogc.filter.TemporalFilter;
import org.n52.sos.ogc.filter.FilterConstants.ComparisonOperator;
import org.n52.sos.ogc.filter.FilterConstants.SpatialOperator;
import org.n52.sos.ogc.gml.GMLConstants;
import org.n52.sos.ogc.gml.SosDuration;
import org.n52.sos.ogc.gml.time.ISosTime;
import org.n52.sos.ogc.gml.time.TimeInstant;
import org.n52.sos.ogc.gml.time.TimePeriod;
import org.n52.sos.ogc.om.AbstractSosObservation;
import org.n52.sos.ogc.om.OMConstants;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.ows.OwsExceptionReport.ExceptionCode;
import org.n52.sos.ogc.ows.OwsExceptionReport.ExceptionLevel;
import org.n52.sos.ogc.sensorML.SensorSystem;
import org.n52.sos.request.AbstractSosRequest;
import org.n52.sos.request.SosDescribeSensorRequest;
import org.n52.sos.request.SosGetCapabilitiesRequest;
import org.n52.sos.request.SosGetFeatureOfInterestRequest;
import org.n52.sos.request.SosGetObservationByIdRequest;
import org.n52.sos.request.SosGetObservationRequest;
import org.n52.sos.request.SosGetResultRequest;
import org.n52.sos.request.SosInsertObservationRequest;
import org.n52.sos.request.SosRegisterSensorRequest;
import org.w3c.dom.Node;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * class offers parsing method to create a SOSOperationRequest, which encapsulates the request parameters,
 * from a GetOperationDocument (XmlBeans generated Java class representing the request) The different
 * SosOperationRequest classes are useful, because XmlBeans generates no useful documentation and handling of
 * substitution groups is not simple. So it may be easier for foreign developers to implement the DAO
 * implementation classes for other data sources then PGSQL databases.
 * 
 * @author Christoph Stasch
 * 
 */
public class HttpPostRequestDecoderStandard implements IHttpPostRequestDecoder {

    /**
     * logger, used for logging while initializing the constants from config file
     */
    private static Logger log = Logger.getLogger(HttpPostRequestDecoderStandard.class);

    /**
     * method receives request and returns internal SOS representation of request
     * 
     * @param docString
     *        string, which contains the request document
     * @return Returns internal SOS representation of request
     * @throws OwsExceptionReport
     *         if parsing of request fails
     */
    public AbstractSosRequest receiveRequest(String docString) throws OwsExceptionReport {
        AbstractSosRequest response = null;
        XmlObject doc;
        try {
            doc = XmlObject.Factory.parse(docString);
        } catch (XmlException xmle) {
            OwsExceptionReport se = new OwsExceptionReport();
            log.error("Error while parsing xml request!", xmle);
            se.addCodedException(OwsExceptionReport.ExceptionCode.InvalidRequest, "RequestOperator.doOperation()", "An xml error occured when parsing the request: XML Exception: " + xmle.getMessage());
            throw se;
        }
        log.info("REQUESTTYPE:" + doc.getClass());
        if (doc instanceof GetCapabilitiesDocument) {
            GetCapabilitiesDocument capsDoc = (GetCapabilitiesDocument) doc;
            response = parseGetCapabilitiesRequest(capsDoc);
        } else if (doc instanceof GetObservationDocument) {
            GetObservationDocument obsDoc = (GetObservationDocument) doc;
            response = parseGetObservationRequest(obsDoc);
        } else if (doc instanceof GetObservationByIdDocument) {
            GetObservationByIdDocument obsDoc = (GetObservationByIdDocument) doc;
            response = parseGetObsByIdRequest(obsDoc);
        } else if (doc instanceof DescribeSensorDocument) {
            DescribeSensorDocument obsDoc = (DescribeSensorDocument) doc;
            response = parseDescribeSensorRequest(obsDoc);
        } else if (doc instanceof GetResultDocument) {
            GetResultDocument obsDoc = (GetResultDocument) doc;
            response = parseGetResultRequest(obsDoc);
        } else if (doc instanceof GetFeatureOfInterestDocument) {
            GetFeatureOfInterestDocument obsDoc = (GetFeatureOfInterestDocument) doc;
            response = parseGetFoiRequest(obsDoc);
        } else if (doc instanceof InsertObservationDocument) {
            InsertObservationDocument insertObsDoc = (InsertObservationDocument) doc;
            response = parseInsertObsRequest(insertObsDoc);
        } else if (doc instanceof DescribeFeatureTypeDocument) {
            OwsExceptionReport se = new OwsExceptionReport();
            se.addCodedException(OwsExceptionReport.ExceptionCode.OperationNotSupported, SosConstants.Operations.describeFeatureOfInterest.name(), "The requested Operation " + SosConstants.Operations.describeFeatureOfInterest.name() + "is not supported by this SOS.");
            throw se;
        } else if (doc instanceof GetFeatureOfInterestTimeDocument) {
            OwsExceptionReport se = new OwsExceptionReport();
            se.addCodedException(OwsExceptionReport.ExceptionCode.OperationNotSupported, SosConstants.Operations.getFeatureOfInterestTime.name(), "The requested Operation " + SosConstants.Operations.getFeatureOfInterestTime.name() + "is not supported by this SOS.");
            throw se;
        } else if (doc instanceof RegisterSensorDocument) {
            RegisterSensorDocument regSensDoc = (RegisterSensorDocument) doc;
            response = parseRegisterSensorDocument(regSensDoc);
        }
        return response;
    }

    /**
     * parses the passes XmlBeans document and creates a SOS InsertObservation request
     * 
     * @param xb_insertObsDoc
     *        XmlBeans document of InsertObservation request
     * @return Returns SOS representation of InsertObservation request
     * @throws OwsExceptionReport
     *         if parsing of request or parameter in Observation, which should be inserted, is incorrect
     */
    public AbstractSosRequest parseInsertObsRequest(InsertObservationDocument xb_insertObsDoc) throws OwsExceptionReport {
        InsertObservation xb_insertObs = xb_insertObsDoc.getInsertObservation();
        String assignedSensorID = xb_insertObs.getAssignedSensorId();
        ObservationType xb_obsType = xb_insertObs.getObservation();
        XmlCursor cursor = xb_insertObs.newCursor();
        QName obsName = new QName(OMConstants.NS_OM, OMConstants.EN_MEASUREMENT);
        MeasurementDocument doc = null;
        if (cursor.toChild(obsName)) {
            try {
                doc = MeasurementDocument.Factory.parse(cursor.getDomNode());
            } catch (XmlException e) {
                e.printStackTrace();
            }
        } else {
            String message = "Currently only om:Measurement is supported for inserting observations into the 52N SOS!";
            log.error(message);
        }
        Collection<AbstractSosObservation> obsCol = null;
        if (doc != null) {
            obsCol = OMDecoder.parseMeasurement(doc.getMeasurement());
        }
        return new SosInsertObservationRequest(assignedSensorID, obsCol);
    }

    /**
     * parses the passes XmlBeans document and creates a SOS describeSensor request
     * 
     * @param xb_descSensDoc
     *        XmlBeans document representing the describeSensor request
     * @return Returns SOS describeSensor request
     * @throws OwsExceptionReport
     *         if validation of the request failed
     */
    public AbstractSosRequest parseDescribeSensorRequest(DescribeSensorDocument xb_descSensDoc) throws OwsExceptionReport {
        validateDocument(xb_descSensDoc);
        SosDescribeSensorRequest descSensorRequest = new SosDescribeSensorRequest();
        DescribeSensor xb_descSensor = xb_descSensDoc.getDescribeSensor();
        descSensorRequest.setService(xb_descSensor.getService());
        descSensorRequest.setVersion(xb_descSensor.getVersion());
        descSensorRequest.setProcedures(xb_descSensor.getProcedure());
        if (xb_descSensor.getOutputFormat() != null && !xb_descSensor.getOutputFormat().equals("")) {
            descSensorRequest.setOutputFormat(xb_descSensor.getOutputFormat());
        } else {
            descSensorRequest.setOutputFormat(SosConstants.PARAMETER_NOT_SET);
        }
        return descSensorRequest;
    }

    /**
     * parses a RegisterSensorDocument and returns a SosRegisterSensorRequest
     * 
     * @param xb_regSensDoc
     *        the XMLBeans document of the RegisterSensor request
     * @return Returns SosRegisterSensorRequest
     * @throws OwsExceptionReport
     *         if request is incorrect or not valid
     */
    public SosRegisterSensorRequest parseRegisterSensorDocument(RegisterSensorDocument xb_regSensDoc) throws OwsExceptionReport {
        SosRegisterSensorRequest request = null;
        RegisterSensor xb_regSens = xb_regSensDoc.getRegisterSensor();
        SensorDescription xb_sensDesc = xb_regSens.getSensorDescription();
        ObservationTemplate xb_obsTemplate = xb_regSens.getObservationTemplate();
        try {
            MeasurementDocument.Factory.parse(xb_obsTemplate.toString());
        } catch (XmlException xmle) {
            OwsExceptionReport se = new OwsExceptionReport();
            se.addCodedException(ExceptionCode.InvalidParameterValue, null, "52�North SOS currently only allows measurements to be inserted!! Parsing of Measurement template failed because: " + xmle.getLocalizedMessage());
            throw se;
        }
        SystemDocument xb_system = null;
        try {
            xb_system = SystemDocument.Factory.parse(xb_sensDesc.toString());
        } catch (XmlException xmle) {
            OwsExceptionReport se = new OwsExceptionReport();
            se.addCodedException(ExceptionCode.InvalidParameterValue, null, "52�North SOS currently only allows sml:Systems to be registered!! Parsing of sml:System failed because: " + xmle.getLocalizedMessage());
            throw se;
        }
        SensorSystem sensorSystem = SensorMLDecoder.parseSystem(xb_system.getSystem());
        String systemDescription = xb_sensDesc.toString();
        request = new SosRegisterSensorRequest(sensorSystem, sensorSystem.getOutputs(), systemDescription);
        return request;
    }

    /**
     * parses the passes XmlBeans document and creates a SOS getFeatureOfInterest request
     * 
     * @param xb_getFoiDoc
     *        XmlBeans document representing the getFeatureOfInterest request
     * @return Returns SOS getFeatureOfInterest request
     * @throws OwsExceptionReport
     *         if validation of the request failed
     */
    public AbstractSosRequest parseGetFoiRequest(GetFeatureOfInterestDocument xb_getFoiDoc) throws OwsExceptionReport {
        validateDocument(xb_getFoiDoc);
        SosGetFeatureOfInterestRequest getFoiRequest = new SosGetFeatureOfInterestRequest();
        GetFeatureOfInterest xb_getFoi = xb_getFoiDoc.getGetFeatureOfInterest();
        getFoiRequest.setVersion(xb_getFoi.getVersion());
        getFoiRequest.setService(xb_getFoi.getService());
        if (xb_getFoi.getFeatureOfInterestIdArray() != null && xb_getFoi.getFeatureOfInterestIdArray().length != 0) {
            getFoiRequest.setFeatureIDs(xb_getFoi.getFeatureOfInterestIdArray());
        }
        if (xb_getFoi.getLocation() != null) {
            getFoiRequest.setLocation(parseLocation(xb_getFoi.getLocation()));
        }
        if (xb_getFoi.getEventTimeArray() != null && xb_getFoi.getEventTimeArray().length != 0) {
            getFoiRequest.setEventTimes(parseEventTime((EventTime[]) xb_getFoi.getEventTimeArray()));
        }
        return getFoiRequest;
    }

    /**
     * parses the getResult XmlBeans representation and creates a SOSGetResultRequest
     * 
     * @param xb_getResultDoc
     *        XmlBeans representation of the getResultRequest
     * @return Returns SOS representation of the getResult request
     * @throws OwsExceptionReport
     *         if validation of the getResult request failed
     */
    public AbstractSosRequest parseGetResultRequest(GetResultDocument xb_getResultDoc) throws OwsExceptionReport {
        SosGetResultRequest request = new SosGetResultRequest();
        GetResult xb_getRes = xb_getResultDoc.getGetResult();
        request.setService(xb_getRes.getService());
        request.setVersion(xb_getRes.getVersion());
        request.setObservationTemplateId(xb_getRes.getObservationTemplateId());
        if (xb_getRes.getEventTimeArray() != null && xb_getRes.getEventTimeArray().length != 0) {
            EventTime[] resultEventTimes = (EventTime[]) xb_getRes.getEventTimeArray();
            request.setEventTimes(parseEventTime(resultEventTimes));
        }
        return request;
    }

    /**
     * parses the XmlBean representing the getObservation request and creates a SoSGetObservation request
     * 
     * @param xb_obsDoc
     *        XmlBean created from the incoming request stream
     * @return Returns SosGetObservationRequest representing the request
     * @throws OwsExceptionReport
     *         If parsing the XmlBean failed
     */
    public AbstractSosRequest parseGetObservationRequest(GetObservationDocument xb_obsDoc) throws OwsExceptionReport {
        SosGetObservationRequest request = new SosGetObservationRequest();
        validateDocument(xb_obsDoc);
        GetObservation xb_getObs = xb_obsDoc.getGetObservation();
        if (xb_getObs.getResponseMode() != null) {
            String responseMode = xb_getObs.getResponseMode().toString();
            checkResponseMode(responseMode);
            request.setResponseMode(responseMode);
        }
        request.setService(xb_getObs.getService());
        request.setVersion(xb_getObs.getVersion());
        String srsName = xb_getObs.getSrsName();
        if (srsName == null || srsName.equals("")) {
        } else {
            request.setSrsName(srsName);
        }
        request.setOffering(xb_getObs.getOffering());
        request.setEventTime(parseEventTime(xb_getObs.getEventTimeArray()));
        if (xb_getObs.getProcedureArray() != null) {
            request.setProcedure(xb_getObs.getProcedureArray());
        }
        request.setObservedProperty(xb_getObs.getObservedPropertyArray());
        if (xb_getObs.getFeatureOfInterest() != null) {
            request.setFeatureOfInterest(parseFeatureOfInterest(xb_getObs.getFeatureOfInterest()));
        }
        if (xb_getObs.getResult() != null) {
            request.setResult(parseResult(xb_getObs.getResult()));
        }
        request.setResponseFormat(xb_getObs.getResponseFormat());
        if (xb_getObs.getResultModel() != null) {
            request.setResultModel(xb_getObs.getResultModel());
        }
        return request;
    }

    /**
     * parses the XmlBean representing the getCapabilities request and creates a SosGetCapabilities request
     * 
     * @param xb_capsDoc
     *        XmlBean created from the incoming request stream
     * @return Returns SosGetCapabilitiesRequest representing the request
     * @throws OwsExceptionReport
     *         If parsing the XmlBean failed
     */
    public AbstractSosRequest parseGetCapabilitiesRequest(GetCapabilitiesDocument xb_capsDoc) throws OwsExceptionReport {
        SosGetCapabilitiesRequest request = new SosGetCapabilitiesRequest();
        validateDocument(xb_capsDoc);
        GetCapabilitiesType xb_getCaps = xb_capsDoc.getGetCapabilities();
        request.setService(xb_capsDoc.getGetCapabilities().getService());
        if (xb_getCaps.getUpdateSequence() != null) {
            request.setUpdateSequence(xb_getCaps.getUpdateSequence());
        }
        if (xb_getCaps.getAcceptFormats() != null && xb_getCaps.getAcceptFormats().sizeOfOutputFormatArray() != 0) {
            request.setAcceptFormats(xb_getCaps.getAcceptFormats().getOutputFormatArray());
        }
        if (xb_getCaps.getAcceptVersions() != null && xb_getCaps.getAcceptVersions().sizeOfVersionArray() != 0) {
            request.setAcceptVersions(xb_getCaps.getAcceptVersions().getVersionArray());
        }
        if (xb_getCaps.getSections() != null && xb_getCaps.getSections().getSectionArray().length != 0) {
            request.setSections(xb_getCaps.getSections().getSectionArray());
        }
        return request;
    }

    /**
     * method parses the GetObservationById document and creates a SosGetObservationByIdRequest object from
     * the passed request
     * 
     * @param xb_getObsByIdDoc
     *        XmlBeans representation of GetObservationById request
     * @return Returns SosGetObservationByIdRequest object representing the request
     * @throws OwsExceptionReport
     */
    public AbstractSosRequest parseGetObsByIdRequest(GetObservationByIdDocument xb_getObsByIdDoc) {
        SosGetObservationByIdRequest request = new SosGetObservationByIdRequest();
        GetObservationById xb_getObsById = xb_getObsByIdDoc.getGetObservationById();
        request.setService(xb_getObsById.getService());
        request.setVersion(xb_getObsById.getVersion());
        request.setObservationID(xb_getObsById.getObservationId());
        request.setSrsName(xb_getObsById.getSrsName());
        request.setResponseFormat(xb_getObsById.getResponseFormat());
        request.setResultModel(xb_getObsById.getResultModel());
        if (xb_getObsById.getResponseMode() != null) {
            request.setResponseMode(xb_getObsById.getResponseMode().toString());
        }
        return request;
    }

    /**
     * parses the eventTime of the requests and returns an array representing the temporal filters
     * 
     * @param xb_eventTimes
     *        array of XmlObjects representing the eventTime element in the getObservation request
     * @return Returns array representing the temporal filters
     * @throws OwsExceptionReport
     *         if parsing of the element failed
     */
    private TemporalFilter[] parseEventTime(EventTime[] xb_eventTimes) throws OwsExceptionReport {
        if (xb_eventTimes == null || xb_eventTimes.length == 0) {
            return null;
        }
        TemporalFilter[] result = new TemporalFilter[xb_eventTimes.length];
        String opNodeName;
        XmlCursor timeCursor;
        boolean isTimeInstant;
        boolean isTimePeriod;
        QName timeInstantName = new QName(OMConstants.NS_GML, OMConstants.EN_TIME_INSTANT);
        QName timePeriodName = new QName(OMConstants.NS_GML, OMConstants.EN_TIME_PERIOD);
        ISosTime sosTime;
        for (int i = 0; i < xb_eventTimes.length; i++) {
            timeCursor = xb_eventTimes[i].newCursor();
            timeCursor.toFirstChild();
            opNodeName = timeCursor.getDomNode().getNodeName().replace("ogc:", "");
            try {
                isTimeInstant = timeCursor.toChild(timeInstantName);
                if (isTimeInstant) {
                    sosTime = parseTimeInstantNode(timeCursor.getDomNode());
                    result[i] = new TemporalFilter(opNodeName, sosTime);
                }
                isTimePeriod = timeCursor.toChild(timePeriodName);
                if (isTimePeriod) {
                    sosTime = parseTimePeriod(timeCursor.getDomNode());
                    result[i] = new TemporalFilter(opNodeName, sosTime);
                }
            } catch (Exception e) {
                OwsExceptionReport se = new OwsExceptionReport();
                log.error(se.getMessage() + e.getMessage());
                se.addCodedException(OwsExceptionReport.ExceptionCode.InvalidParameterValue, "EventTime", "Error while parsing eventTime of GetObservation request: " + e.getMessage());
                throw se;
            }
        }
        return result;
    }

    /**
     * parses the featureOfInterest element of the GetObservation request
     * 
     * @param xb_foi
     *        XmlBean representing the feature of interest parameter of the request
     * @return Returns SpatialFilter created from the passed foi request parameter
     * @throws OwsExceptionReport
     *         if creation of the SpatialFilter failed
     */
    private SpatialFilter parseLocation(Location xb_location) throws OwsExceptionReport {
        if (xb_location == null) {
            return null;
        }
        SpatialFilter result = new SpatialFilter();
        SpatialOperator operator = null;
        Geometry geometry = null;
        SpatialOpsType xb_abstractSpatialOps = xb_location.getSpatialOps();
        if (xb_abstractSpatialOps instanceof BBOXType) {
            operator = FilterConstants.SpatialOperator.BBOX;
            BBOXType xb_bbox = (BBOXType) (xb_abstractSpatialOps);
            geometry = getGeometry4BBOX(xb_bbox);
        } else {
            String operatorName = xb_abstractSpatialOps.getDomNode().getNodeName().replace("ogc:", "");
            if (operatorName.equalsIgnoreCase(SpatialOperator.Contains.name())) {
                operator = FilterConstants.SpatialOperator.Contains;
                geometry = parseBinarySpatialOpTypeGeom((BinarySpatialOpType) xb_abstractSpatialOps);
            } else if (operatorName.equalsIgnoreCase(SpatialOperator.Overlaps.name())) {
                operator = FilterConstants.SpatialOperator.Overlaps;
                geometry = parseBinarySpatialOpTypeGeom((BinarySpatialOpType) xb_abstractSpatialOps);
            } else if (operatorName.equalsIgnoreCase(SpatialOperator.Intersects.name())) {
                operator = FilterConstants.SpatialOperator.Intersects;
                geometry = parseBinarySpatialOpTypeGeom((BinarySpatialOpType) xb_abstractSpatialOps);
            } else {
                OwsExceptionReport se = new OwsExceptionReport();
                se.addCodedException(ExceptionCode.InvalidParameterValue, null, "SpatialOps operator '" + operatorName + "' not supported");
                throw se;
            }
        }
        result.setOperator(operator);
        result.setGeometry(geometry);
        return result;
    }

    /**
     * parses the featureOfInterest element of the GetObservation request
     * 
     * @param xb_foi
     *        XmlBean representing the feature of interest parameter of the request
     * @return Returns SpatialFilter created from the passed foi request parameter
     * @throws OwsExceptionReport
     *         if creation of the SpatialFilter failed
     */
    private SpatialFilter parseFeatureOfInterest(FeatureOfInterest xb_foi) throws OwsExceptionReport {
        SpatialFilter result = new SpatialFilter();
        CapabilitiesCache cache = CapabilitiesCache.getInstance();
        List<String> fois_cache = cache.getFois();
        String[] fois_request = xb_foi.getObjectIDArray();
        StringBuffer invalidFois = new StringBuffer();
        if (xb_foi == null) {
            return null;
        } else if (xb_foi.getObjectIDArray() != null && xb_foi.getObjectIDArray().length != 0) {
            for (int i = 0; i < fois_request.length; i++) if (!fois_cache.contains(fois_request[i])) {
                invalidFois.append("'" + fois_request[i] + "' ");
            }
            if (invalidFois.length() > 0) {
                OwsExceptionReport se = new OwsExceptionReport();
                log.error("The FeatureOfInterest ID(s) " + invalidFois.toString() + " in the request is/are invalid!");
                se.addCodedException(OwsExceptionReport.ExceptionCode.InvalidParameterValue, "FeatureOfInterest", "The FeatureOfInterest ID(s) " + invalidFois.toString() + " in the request is/are invalid!");
                throw se;
            }
            result.setFoiIDs(xb_foi.getObjectIDArray());
            return result;
        } else if (xb_foi.getSpatialOps() != null) {
            SpatialOperator operator = null;
            Geometry geometry = null;
            SpatialOpsType xb_abstractSpatialOps = xb_foi.getSpatialOps();
            if (xb_abstractSpatialOps instanceof BBOXType) {
                operator = FilterConstants.SpatialOperator.BBOX;
                BBOXType xb_bbox = (BBOXType) (xb_abstractSpatialOps);
                geometry = getGeometry4BBOX(xb_bbox);
            } else {
                String operatorName = xb_abstractSpatialOps.getDomNode().getLocalName();
                if (operatorName.equalsIgnoreCase(SpatialOperator.Contains.name())) {
                    operator = FilterConstants.SpatialOperator.Contains;
                    geometry = parseBinarySpatialOpTypeGeom((BinarySpatialOpType) xb_abstractSpatialOps);
                } else if (operatorName.equalsIgnoreCase(SpatialOperator.Overlaps.name())) {
                    operator = FilterConstants.SpatialOperator.Overlaps;
                    geometry = parseBinarySpatialOpTypeGeom((BinarySpatialOpType) xb_abstractSpatialOps);
                } else if (operatorName.equalsIgnoreCase(SpatialOperator.Intersects.name())) {
                    operator = FilterConstants.SpatialOperator.Intersects;
                    geometry = parseBinarySpatialOpTypeGeom((BinarySpatialOpType) xb_abstractSpatialOps);
                } else {
                    OwsExceptionReport se = new OwsExceptionReport();
                    se.addCodedException(ExceptionCode.InvalidParameterValue, null, "SpatialOps operator '" + operatorName + "' not supported");
                    throw se;
                }
            }
            result.setOperator(operator);
            result.setGeometry(geometry);
            return result;
        } else {
            throw new IllegalArgumentException("Incorrect encoding of 'featureOfInterest' element");
        }
    }

    /**
     * parses the geometry or envelope element of the spatial filter and returns a WKT-representation of this
     * geometry
     * 
     * @param xb_binSpatialOpType
     *        XmlBean representing either the envelope or the geometry
     * @return Returns Well-Known-Text (WKT) representation of the geometry or envelope
     * @throws OwsExceptionReport
     *         if parsing failed
     */
    private Geometry parseBinarySpatialOpTypeGeom(BinarySpatialOpType xb_binSpatialOpType) throws OwsExceptionReport {
        Geometry geometry = null;
        EnvelopeType xb_envelope = xb_binSpatialOpType.getEnvelope();
        AbstractGeometryType xb_geometry = xb_binSpatialOpType.getGeometry();
        if (xb_envelope != null) {
            geometry = getWKT4Envelope(xb_envelope);
        } else if (xb_geometry != null) {
            geometry = getGeometry4XmlGeometry(xb_geometry);
        }
        return geometry;
    }

    /**
     * parses the BBOX element of the featureOfInterest element contained in the GetObservation request and
     * returns a String representing the BOX in Well-Known-Text format
     * 
     * @param xb_bbox
     *        XmlBean representing the BBOX-element in the request
     * @return Returns WKT-String representing the BBOX as Multipoint with two elements
     * @throws OwsExceptionReport
     *         if parsing the BBOX element failed
     */
    private Geometry getGeometry4BBOX(BBOXType xb_bbox) throws OwsExceptionReport {
        Geometry result = null;
        EnvelopeType xb_envelope = xb_bbox.getEnvelope();
        int srid = parseSrsName(xb_envelope.getSrsName());
        DirectPositionType xb_lowerCorner = xb_envelope.getLowerCorner();
        DirectPositionType xb_upperCorner = xb_envelope.getUpperCorner();
        String lower = xb_lowerCorner.getStringValue();
        String upper = xb_upperCorner.getStringValue();
        String geomWKT = null;
        if (!SosConfigurator.getInstance().isEastingFirst()) {
            lower = switchCoordinatesInString(lower);
            upper = switchCoordinatesInString(upper);
        }
        geomWKT = "MULTIPOINT(" + lower + ", " + upper + ")";
        result = createGeometryFromWKT(geomWKT);
        result.setSRID(srid);
        return result;
    }

    /**
     * parses the XmlBeans AbstractGeometryType and returns a String representing the geometry in
     * Well-Known-Text (WKT) format
     * 
     * @param xb_geometry
     *        XmlBean represnting the geometry
     * @return Returns geometry as WKT-String
     * @throws OwsExceptionReport
     *         if parsing the geometry failed
     */
    private Geometry getGeometry4XmlGeometry(AbstractGeometryType xb_geometry) throws OwsExceptionReport {
        Geometry geometry = null;
        String geomWKT = null;
        int srid = Integer.MIN_VALUE;
        if (xb_geometry instanceof PolygonTypeImpl) {
            PolygonType xb_polygon = (PolygonType) xb_geometry;
            geometry = getGeometry4Polygon(xb_polygon);
            return geometry;
        } else if (xb_geometry instanceof PointTypeImpl) {
            PointType xb_point = (PointType) xb_geometry;
            srid = parseSrsName(xb_point.getSrsName());
            if (xb_point.getPos() != null) {
                DirectPositionType xb_position = xb_point.getPos();
                String directPosition = xb_position.getStringValue();
                if (!SosConfigurator.getInstance().isEastingFirst()) {
                    directPosition = switchCoordinatesInString(directPosition);
                }
                geomWKT = "POINT(" + directPosition + ")";
            } else {
                OwsExceptionReport se = new OwsExceptionReport();
                se.addCodedException(OwsExceptionReport.ExceptionCode.InvalidParameterValue, GetObservationParams.featureOfInterest.toString(), "For geometry type 'gml:Point' only element " + "'gml:position' is allowed " + "in the feature of interest parameter!");
                throw se;
            }
        } else if (xb_geometry instanceof LineStringTypeImpl) {
            LineStringType xb_lineString = (LineStringType) xb_geometry;
            srid = parseSrsName(xb_lineString.getSrsName());
            DirectPositionType[] xb_positions = xb_lineString.getPosArray();
            StringBuffer positions = new StringBuffer();
            for (DirectPositionType xb_position : xb_positions) {
                String positionString = xb_position.getStringValue();
                if (!SosConfigurator.getInstance().isEastingFirst()) {
                    positionString = switchCoordinatesInString(positionString);
                }
                positions.append(" " + positionString + ",");
            }
            positions.deleteCharAt(positions.length() - 1);
            geomWKT = "LINESTRING(" + positions.toString() + ")";
        }
        geometry = createGeometryFromWKT(geomWKT);
        geometry.setSRID(srid);
        return geometry;
    }

    /**
     * parses the passed XmlBeans EnvelopeType and returns a MultiPoint with lowerLeft and upperRight of this
     * BBOX in WKT-format
     * 
     * @param xb_envelope
     *        XmlBean representing the envelope
     * @return Returns MultiPoint in WKT-format, which contains the lowerLeft and upperRight of the BBOX
     * @throws OwsExceptionReport
     */
    public Geometry getWKT4Envelope(EnvelopeType xb_envelope) throws OwsExceptionReport {
        int srid = parseSrsName(xb_envelope.getSrsName());
        DirectPositionType xb_lowerCorner = xb_envelope.getLowerCorner();
        DirectPositionType xb_upperCorner = xb_envelope.getUpperCorner();
        String lower = xb_lowerCorner.getStringValue();
        String upper = xb_upperCorner.getStringValue();
        if (!SosConfigurator.getInstance().isEastingFirst()) {
            lower = switchCoordinatesInString(lower);
            upper = switchCoordinatesInString(upper);
        }
        String geomWKT = "MULTIPOINT(" + lower + ", " + upper + ")";
        Geometry geometry = createGeometryFromWKT(geomWKT);
        geometry.setSRID(srid);
        return geometry;
    }

    /**
     * parses the XmlBean which represents the result filter and creates a ComparisonFilter representing the
     * result Filter
     * 
     * @param xb_result
     *        XmlBean representing the result filter
     * @return Returns a ComparisonFilter which represents the result filter
     * @throws OwsExceptionReport
     *         if parsing the result filter failed
     */
    public static ComparisonFilter parseResult(Result xb_result) throws OwsExceptionReport {
        ComparisonOpsType xb_compOpsType = xb_result.getComparisonOps();
        ComparisonFilter compFilter = new ComparisonFilter();
        ComparisonOperator operator = null;
        String propertyName = null;
        String value = null;
        String valueUpper = null;
        if (xb_compOpsType instanceof PropertyIsBetweenType) {
            operator = ComparisonOperator.PropertyIsBetween;
            PropertyIsBetweenType xb_propBetween = (PropertyIsBetweenType) xb_compOpsType;
            propertyName = xb_propBetween.getExpression().newCursor().getTextValue();
            LowerBoundaryType xb_lowerBoundary = xb_propBetween.getLowerBoundary();
            value = xb_lowerBoundary.getExpression().newCursor().getTextValue();
            UpperBoundaryType xb_upperBoundary = xb_propBetween.getUpperBoundary();
            valueUpper = xb_upperBoundary.getExpression().getDomNode().getTextContent();
        } else if (xb_compOpsType instanceof PropertyIsLikeType) {
            operator = ComparisonOperator.PropertyIsLike;
            PropertyIsLikeType xb_propIsLikeType = (PropertyIsLikeType) xb_compOpsType;
            propertyName = xb_propIsLikeType.getPropertyName().getDomNode().getTextContent();
            value = xb_propIsLikeType.getLiteral().getDomNode().getTextContent();
        } else if (xb_compOpsType instanceof PropertyIsNullDocument) {
            operator = ComparisonOperator.PropertyIsNull;
            PropertyIsNullType xb_propIsNullType = (PropertyIsNullType) xb_compOpsType;
            propertyName = xb_propIsNullType.getPropertyName().getDomNode().getTextContent();
        } else {
            BinaryComparisonOpType xb_binCompOpType = (BinaryComparisonOpType) xb_compOpsType;
            String operatorName = xb_binCompOpType.getDomNode().getNodeName().replace("ogc:", "");
            if (operatorName.equalsIgnoreCase(ComparisonOperator.PropertyIsEqualTo.name())) {
                operator = ComparisonOperator.PropertyIsEqualTo;
                String[] propValue = getPropValue4ExpressionArray(xb_binCompOpType.getExpressionArray());
                propertyName = propValue[0];
                value = propValue[1];
            } else if (operatorName.equalsIgnoreCase(ComparisonOperator.PropertyIsGreaterThan.name())) {
                operator = ComparisonOperator.PropertyIsGreaterThan;
                String[] propValue = getPropValue4ExpressionArray(xb_binCompOpType.getExpressionArray());
                propertyName = propValue[0];
                value = propValue[1];
            } else if (operatorName.equalsIgnoreCase(ComparisonOperator.PropertyIsGreaterThanOrEqualTo.name())) {
                operator = ComparisonOperator.PropertyIsGreaterThanOrEqualTo;
                String[] propValue = getPropValue4ExpressionArray(xb_binCompOpType.getExpressionArray());
                propertyName = propValue[0];
                value = propValue[1];
            } else if (operatorName.equalsIgnoreCase(ComparisonOperator.PropertyIsLessThan.name())) {
                operator = ComparisonOperator.PropertyIsLessThan;
                String[] propValue = getPropValue4ExpressionArray(xb_binCompOpType.getExpressionArray());
                propertyName = propValue[0];
                value = propValue[1];
            } else if (operatorName.equalsIgnoreCase(ComparisonOperator.PropertyIsLessThanOrEqualTo.name())) {
                operator = ComparisonOperator.PropertyIsLessThanOrEqualTo;
                String[] propValue = getPropValue4ExpressionArray(xb_binCompOpType.getExpressionArray());
                propertyName = propValue[0];
                value = propValue[1];
            } else if (operatorName.equalsIgnoreCase(ComparisonOperator.PropertyIsNotEqualTo.name())) {
                operator = ComparisonOperator.PropertyIsNotEqualTo;
                String[] propValue = getPropValue4ExpressionArray(xb_binCompOpType.getExpressionArray());
                propertyName = propValue[0];
                value = propValue[1];
            } else {
            }
        }
        compFilter.setValue(value);
        compFilter.setPropertyName(propertyName);
        compFilter.setOperator(operator);
        compFilter.setValueUpper(valueUpper);
        return compFilter;
    }

    /**
     * Returns an array with propertyname and value form the passed expressionArray, which is xmlBeans
     * generated
     * 
     * @param xb_expressionArray
     *        XmlBean representing the expressionarray
     * @return Returns String[] of length=2 containing the property as first element and the value as second
     * @throws OwsExceptionReport
     *         if parsing the expression array failed
     */
    private static String[] getPropValue4ExpressionArray(ExpressionType[] xb_expressionArray) throws OwsExceptionReport {
        String returnValue = null;
        String propertyName = null;
        if (xb_expressionArray.length == 2) {
            if (xb_expressionArray[0] instanceof PropertyNameType) {
                propertyName = xb_expressionArray[0].newCursor().getTextValue();
            }
            if (xb_expressionArray[1] instanceof LiteralType) {
                returnValue = xb_expressionArray[1].newCursor().getTextValue();
            }
        } else {
            OwsExceptionReport se = new OwsExceptionReport();
            se.addCodedException(ExceptionCode.InvalidParameterValue, GetObservationParams.result.toString(), "The requested '" + GetObservationParams.result.toString() + "' filter is not supported by this service! PropertyIsEqualTo Filter can just have one Literal!");
            throw se;
        }
        String[] result = new String[] { propertyName, returnValue };
        return result;
    }

    /**
     * parses the srsName and returns an integer representing the number of the EPSG-code of the passed
     * srsName
     * 
     * @param srsName
     *        name of the spatial reference system in EPSG-format (withn urn identifier for EPSG)
     * @return Returns an integer representing the number of the EPSG-code of the passed srsName
     * @throws OwsExceptionReport
     *         if parsing the srsName failed
     */
    private int parseSrsName(String srsName) throws OwsExceptionReport {
        int srid = Integer.MIN_VALUE;
        if (!(srsName == null || srsName.equals(""))) {
            srsName = srsName.replace(SosConstants.SRS_NAME_PREFIX, "");
            try {
                srid = new Integer(srsName).intValue();
            } catch (Exception e) {
                OwsExceptionReport se = new OwsExceptionReport();
                se.addCodedException(OwsExceptionReport.ExceptionCode.InvalidParameterValue, GetObservationParams.featureOfInterest.toString(), "For geometry of the feature of interest parameter has to have a srsName attribute, which contains the Srs Name as EPSGcode following the following schema:" + SosConstants.SRS_NAME_PREFIX + "number!");
                log.error("Error while parsing srsName of featureOfInterest parameter: " + se.getMessage());
                throw se;
            }
        } else {
            OwsExceptionReport se = new OwsExceptionReport();
            se.addCodedException(OwsExceptionReport.ExceptionCode.InvalidParameterValue, GetObservationParams.featureOfInterest.toString(), "For geometry of the feature of interest parameter has to have a srsName attribute, which contains the Srs Name as EPSGcode following the following schema:" + SosConstants.SRS_NAME_PREFIX + "number!");
            log.error("Error while parsing srsName of featureOfInterest parameter: " + se.getMessage());
            throw se;
        }
        return srid;
    }

    /**
     * creates a JTS Geometry from the passed WKT string representation of the geometry
     * 
     * @param wktString
     *        string representation in Well-Known-Text format of the geometry
     * @return Returns JTS Geometry
     * @throws OwsExceptionReport
     *         if creating the JTS Geometry failed
     */
    private Geometry createGeometryFromWKT(String wktString) throws OwsExceptionReport {
        WKTReader wktReader = new WKTReader();
        Geometry geom = null;
        try {
            log.debug("FOI Geometry: " + wktString);
            geom = wktReader.read(wktString);
        } catch (ParseException pe) {
            OwsExceptionReport se = new OwsExceptionReport();
            se.addCodedException(OwsExceptionReport.ExceptionCode.InvalidParameterValue, GetObservationParams.featureOfInterest.toString(), "An error occurred, while parsing the foi parameter: " + pe.getMessage());
            log.error("Error while parsing the geometry of featureOfInterest parameter: " + se.getMessage());
            throw se;
        }
        return geom;
    }

    /**
     * parses the XmlBean which represents a Polygon and returns an JTS Geometry (which is a polygon)
     * 
     * @param xb_polygonType
     *        XmlBean which represents the Polygon
     * @return Returns JTS Geometry which represents the passe polygon
     * @throws OwsExceptionReport
     */
    private Geometry getGeometry4Polygon(PolygonType xb_polygonType) throws OwsExceptionReport {
        Geometry geom = null;
        String srsName = xb_polygonType.getSrsName();
        String exteriorCoordString = null;
        String geomWKT = null;
        String interiorCoordString = null;
        AbstractRingPropertyType xb_exterior = xb_polygonType.getExterior();
        if (xb_exterior != null) {
            AbstractRingType xb_exteriorRing = xb_exterior.getRing();
            if (xb_exteriorRing instanceof LinearRingType) {
                LinearRingType xb_linearRing = (LinearRingType) xb_exteriorRing;
                exteriorCoordString = getCoordString4LinearRing(xb_linearRing);
            } else {
                OwsExceptionReport se = new OwsExceptionReport();
                se.addCodedException(OwsExceptionReport.ExceptionCode.InvalidParameterValue, GetObservationParams.featureOfInterest.toString(), "The Polygon must contain the following elements <gml:exterior><gml:LinearRing><gml:posList>!");
                log.error("Error while parsing polygon of featureOfInterest parameter: " + se.getMessage());
                throw se;
            }
        }
        AbstractRingPropertyType[] xb_interior = xb_polygonType.getInteriorArray();
        AbstractRingPropertyType xb_interiorRing;
        if (xb_interior != null && xb_interior.length != 0) {
            interiorCoordString = "(";
            for (int i = 0; i < xb_interior.length; i++) {
                xb_interiorRing = xb_interior[i];
                if (xb_interiorRing != null && xb_interiorRing instanceof LinearRingType) {
                    interiorCoordString += getCoordString4LinearRing((LinearRingType) xb_interiorRing);
                }
            }
            interiorCoordString += ")";
        }
        geomWKT = "POLYGON(" + exteriorCoordString;
        if (interiorCoordString != null) {
            geomWKT += ",(" + interiorCoordString + "))";
        } else {
            geomWKT += ")";
        }
        geom = createGeometryFromWKT(geomWKT);
        geom.setSRID(parseSrsName(srsName));
        return geom;
    }

    /**
     * method parses the passed linearRing(generated thru XmlBEans) and returns a string containing the
     * coordinate values of the passed ring
     * 
     * @param xb_linearRing
     *        linearRing(generated thru XmlBEans)
     * @return Retruns a string containing the coordinate values of the passed ring
     * @throws OwsExceptionReport
     *         if parsing the linear Ring failed
     */
    private String getCoordString4LinearRing(LinearRingType xb_linearRing) throws OwsExceptionReport {
        StringBuilder result = new StringBuilder();
        DirectPositionListType xb_posList = xb_linearRing.getPosList();
        if (xb_posList != null && !(xb_posList.getStringValue().equals(""))) {
            String[] positionCoords = xb_posList.getStringValue().split(" ");
            if (positionCoords.length % 2 != 0) {
                OwsExceptionReport se = new OwsExceptionReport();
                se.addCodedException(OwsExceptionReport.ExceptionCode.InvalidParameterValue, GetObservationParams.featureOfInterest.toString(), "The Polygon must contain the following elements <gml:exterior><gml:LinearRing><gml:posList>!");
                log.error("Error while parsing polygon of featureOfInterest parameter: " + se.getMessage());
                throw se;
            }
            result.append("(");
            for (int i = 0; i < positionCoords.length; i = i + 2) {
                if (SosConfigurator.getInstance().isEastingFirst()) {
                    result.append(positionCoords[i] + " " + positionCoords[i + 1] + ",");
                } else {
                    result.append(positionCoords[i + 1] + " " + positionCoords[i] + ",");
                }
            }
            result.replace(result.length() - 1, result.length(), "");
            result.append(")");
        } else {
            OwsExceptionReport se = new OwsExceptionReport();
            se.addCodedException(OwsExceptionReport.ExceptionCode.InvalidParameterValue, GetObservationParams.featureOfInterest.toString(), "The Polygon must contain the following elements <gml:exterior><gml:LinearRing><gml:posList>!");
            log.error("Error while parsing polygon of featureOfInterest parameter: " + se.getMessage());
            throw se;
        }
        return result.toString();
    }

    /**
     * checks whether the getObservationRequest XMLDocument is valid
     * 
     * @param getObsDoc
     *        the document which should be checked
     * 
     * @throws OwsExceptionReport
     *         if the Document is not valid
     */
    private void validateDocument(XmlObject xb_doc) throws OwsExceptionReport {
        ArrayList<XmlError> validationErrors = new ArrayList<XmlError>();
        XmlOptions validationOptions = new XmlOptions();
        validationOptions.setErrorListener(validationErrors);
        boolean isValid = xb_doc.validate(validationOptions);
        if (!isValid) {
            String message = null;
            String parameterName = null;
            Iterator<XmlError> iter = validationErrors.iterator();
            while (iter.hasNext()) {
                ExceptionCode exCode = null;
                XmlError error = iter.next();
                message = error.getMessage();
                if (message != null) {
                    if (message.startsWith("The value")) {
                        exCode = ExceptionCode.InvalidParameterValue;
                        String[] messAndAttribute = message.split("attribute '");
                        if (messAndAttribute.length == 2) {
                            parameterName = messAndAttribute[1].replace("'", "");
                        }
                    } else if (message.contains("not a valid enumeration value")) {
                        exCode = ExceptionCode.InvalidParameterValue;
                        String[] messAndAttribute = message.split(" ");
                        parameterName = messAndAttribute[10];
                    } else if (message.startsWith("Expected attribute")) {
                        exCode = ExceptionCode.MissingParameterValue;
                        String[] messAndAttribute = message.split("attribute: ");
                        if (messAndAttribute.length == 2) {
                            String[] attrAndRest = messAndAttribute[1].split(" in");
                            if (attrAndRest.length == 2) {
                                parameterName = attrAndRest[0];
                            }
                        }
                    } else if (message.startsWith("Expected element")) {
                        exCode = ExceptionCode.MissingParameterValue;
                        String[] messAndElements = message.split(" '");
                        if (messAndElements.length >= 2) {
                            String elements = messAndElements[1];
                            if (elements.contains("offering")) {
                                parameterName = "offering";
                            } else if (elements.contains("observedProperty")) {
                                parameterName = "observedProperty";
                            } else if (elements.contains("responseFormat")) {
                                parameterName = "responseFormat";
                            } else if (elements.contains("procedure")) {
                                parameterName = "procedure";
                            } else if (elements.contains("featureOfInterest")) {
                                parameterName = "featureOfInterest";
                            } else {
                            }
                        }
                    } else if (message.startsWith("Element")) {
                        exCode = ExceptionCode.InvalidParameterValue;
                        String[] messAndElements = message.split(" '");
                        if (messAndElements.length >= 2) {
                            String elements = messAndElements[1];
                            if (elements.contains("offering")) {
                                parameterName = "offering";
                            } else if (elements.contains("observedProperty")) {
                                parameterName = "observedProperty";
                            } else if (elements.contains("responseFormat")) {
                                parameterName = "responseFormat";
                            } else if (elements.contains("procedure")) {
                                parameterName = "procedure";
                            } else if (elements.contains("featureOfInterest")) {
                                parameterName = "featureOfInterest";
                            } else {
                            }
                        }
                    } else {
                        OwsExceptionReport se = new OwsExceptionReport();
                        se.addCodedException(ExceptionCode.InvalidRequest, null, "[XmlBeans validation error:] " + message);
                        log.error("The request is invalid!", se);
                        throw se;
                    }
                    OwsExceptionReport se = new OwsExceptionReport();
                    se.addCodedException(exCode, parameterName, "[XmlBeans validation error:] " + message);
                    log.error("The request is invalid!", se);
                    throw se;
                }
            }
        }
    }

    /**
     * parse methode, which creates an TimePeriod object from the DOM-node of the TimePeriodType. This
     * constructor is necessary cause XMLBeans does not fully support substitution groups. So one has to do a
     * workaround with XmlCursor and the DomNodes of the elements.
     * 
     * 
     * @param timePeriod
     *        the DomNode of the timePeriod element
     * @throws XmlException
     *         if the Node could not be parsed into a XmlBean
     * @throws OwsExceptionReport
     *         if required elements of the timePeriod are missed
     * @throws ParseException
     * @throws java.text.ParseException
     */
    public TimePeriod parseTimePeriod(Node timePeriod) throws XmlException, OwsExceptionReport, java.text.ParseException {
        TimePeriod tp = new TimePeriod();
        SimpleDateFormat gmlSdf = new SimpleDateFormat(GMLConstants.GML_DATE_FORMAT);
        XmlObject timeObject = XmlObject.Factory.parse(timePeriod);
        XmlCursor cursor = timeObject.newCursor();
        QName qnameBeginPos = new QName("http://www.opengis.net/gml", "beginPosition");
        QName qnameBegin = new QName("http://www.opengis.net/gml", "begin");
        QName qnameEndPos = new QName("http://www.opengis.net/gml", "endPosition");
        QName qnameEnd = new QName("http://www.opengis.net/gml", "end");
        QName qnameDuration = new QName("http://www.opengis.net/gml", "duration");
        QName qnameIntervall = new QName("http://www.opengis.net/gml", "intervall");
        cursor.toNextToken();
        boolean isBegin = cursor.toChild(qnameBegin);
        boolean isBeginPos = cursor.toChild(qnameBeginPos);
        if (isBegin) {
            if (cursor.getTextValue().equals("")) {
                tp.setStart(null);
            } else {
                String startString = cursor.getTextValue();
                startString = startString.replace(" ", "+");
                startString = GMLDecoder.checkTimeZone(startString);
                tp.setStart(gmlSdf.parse(startString));
            }
            boolean isIndet = cursor.toFirstAttribute();
            if (isIndet) {
                tp.setStartIndet(cursor.getTextValue());
                cursor.toParent();
            }
        } else if (isBeginPos) {
            if (cursor.getTextValue().equals("")) {
                tp.setStart(null);
            } else {
                String textValue = cursor.getTextValue();
                textValue = textValue.replace(" ", "+");
                textValue = GMLDecoder.checkTimeZone(textValue);
                tp.setStart(gmlSdf.parse(textValue));
            }
            boolean isIndet = cursor.toFirstAttribute();
            if (isIndet) {
                tp.setStartIndet(cursor.getTextValue());
                cursor.toParent();
            }
        } else {
            OwsExceptionReport se = new OwsExceptionReport();
            se.addCodedException(OwsExceptionReport.ExceptionCode.InvalidParameterValue, GetObservationParams.eventTime.toString(), "The start time is missed in the timePeriod element of time parameter!");
            throw se;
        }
        cursor.toParent();
        boolean isEnd = cursor.toChild(qnameEnd);
        boolean isEndPos = cursor.toChild(qnameEndPos);
        if (isEnd) {
            if (cursor.getTextValue().equals("")) {
                tp.setEnd(null);
            } else {
                String endString = cursor.getTextValue();
                endString = endString.replace(" ", "+");
                endString = GMLDecoder.checkTimeZone(endString);
                tp.setEnd(gmlSdf.parse(endString));
            }
            boolean isIndet = cursor.toFirstAttribute();
            if (isIndet) {
                tp.setEndIndet(cursor.getTextValue());
                cursor.toParent();
            }
        } else if (isEndPos) {
            if (cursor.getTextValue().equals("") || cursor.getTextValue() == null) {
                tp.setEnd(null);
            } else {
                String endString = cursor.getTextValue();
                endString = endString.replace(" ", "+");
                endString = GMLDecoder.checkTimeZone(endString);
                tp.setEnd(gmlSdf.parse(endString));
            }
            boolean isIndet = cursor.toFirstAttribute();
            if (isIndet) {
                tp.setEndIndet(cursor.getTextValue());
                cursor.toParent();
            }
        } else {
            OwsExceptionReport se = new OwsExceptionReport();
            se.addCodedException(OwsExceptionReport.ExceptionCode.InvalidParameterValue, GetObservationParams.eventTime.toString(), "The end time is missed in the timePeriod element of time parameter!");
            throw se;
        }
        cursor.toParent();
        boolean isDuration = cursor.toChild(qnameDuration);
        if (isDuration) {
            tp.setDuration(new SosDuration(cursor.getTextValue()));
        }
        boolean isIntervall = cursor.toChild(qnameIntervall);
        if (isIntervall) {
            tp.setIntervall(cursor.getTextValue());
        }
        return tp;
    }

    /**
     * parses TimeInstant
     * 
     * @param tp
     *        XmlBean representation of TimeInstant
     * @throws java.text.ParseException
     * @throws ParseException
     *         if parsing the datestring into java.util.Date failed
     */
    public TimeInstant parseTimeInstant(TimeInstantType tp) throws java.text.ParseException {
        TimeInstant ti = new TimeInstant();
        SimpleDateFormat gmlSdf = new SimpleDateFormat(GMLConstants.GML_DATE_FORMAT);
        TimePositionType tpt = tp.getTimePosition();
        String endString = tpt.getStringValue();
        endString = GMLDecoder.checkTimeZone(endString);
        ti.setValue(gmlSdf.parse(endString));
        ti.setIndeterminateValue(tpt.getIndeterminatePosition().toString());
        return ti;
    }

    /**
     * help method, which creates an TimeInstant object from the DOM-node of the TimeInstantType. This
     * constructor is necessary cause XMLBeans does not full support substitution groups. So one has to do a
     * workaround with XmlCursor and the DomNodes of the elements.
     * 
     * @param timeInstant
     *        DOM Node of timeInstant element
     * @throws OwsExceptionReport
     *         if no timePosition element is cotained in the timeInstant element
     * @throws XmlException
     *         if parsing the DomNode to an XMLBeans XmlObject failed
     * @throws ParseException
     * @throws java.text.ParseException
     */
    public TimeInstant parseTimeInstantNode(Node timeInstant) throws OwsExceptionReport, XmlException, java.text.ParseException {
        TimeInstant ti = new TimeInstant();
        SimpleDateFormat gmlSdf = new SimpleDateFormat(GMLConstants.GML_DATE_FORMAT);
        XmlObject timeObject = XmlObject.Factory.parse(timeInstant);
        XmlCursor cursor = timeObject.newCursor();
        QName qname = new QName(GMLConstants.NS_GML, GMLConstants.EN_TIME_POSITION);
        cursor.toNextToken();
        boolean isTP = cursor.toChild(qname);
        if (isTP) {
            String positionString = cursor.getTextValue();
            if (positionString != null && !positionString.equals("")) {
                positionString = GMLDecoder.checkTimeZone(positionString);
                ti.setValue(gmlSdf.parse(positionString));
            }
            boolean hasAttr = cursor.toFirstAttribute();
            if (hasAttr) {
                ti.setIndeterminateValue(cursor.getTextValue());
            }
        } else {
            OwsExceptionReport se = new OwsExceptionReport(ExceptionLevel.DetailedExceptions);
            se.addCodedException(ExceptionCode.MissingParameterValue, "gml:timePosition", "No timePosition element is contained in the gml:timeInstant element");
            throw se;
        }
        return ti;
    }

    /**
     * switches the order of coordinates contained in a string, e.g. from String '3.5 4.4' to '4.4 3.5'
     * 
     * NOTE: ACTUALLY checks, whether dimension is 2D, othewise throws Exception!!
     * 
     * @param coordsString
     *        contains coordinates, which should be switched
     * @return Returns String contained coordinates in switched order
     * @throws OwsExceptionReport
     *         if dimension of coordinates is not 2
     */
    private String switchCoordinatesInString(String coordsString) throws OwsExceptionReport {
        String switchedCoordString = null;
        String[] coordsArray = coordsString.split(" ");
        if (coordsArray.length != 2) {
            OwsExceptionReport se = new OwsExceptionReport();
            se.addCodedException(OwsExceptionReport.ExceptionCode.InvalidParameterValue, GetObservationParams.featureOfInterest.toString(), "Only 2D coordinates are supported in the 52�North SOS at the moment!");
            throw se;
        }
        switchedCoordString = coordsArray[1] + " " + coordsArray[0];
        return switchedCoordString;
    }

    /**
     * help method to check the response mode parameter of GetObservation request. If the value is incorrect,
     * an exception report will be returned!
     * 
     * @param responseMode
     *        String containing the value of the response mode parameter
     * @throws OwsExceptionReport
     *         if the parameter value is incorrect
     */
    private void checkResponseMode(String responseMode) throws OwsExceptionReport {
        if (responseMode != null) {
            if (!responseMode.equals(SosConstants.RESPONSE_MODE_INLINE) && !responseMode.equals(SosConstants.RESPONSE_RESULT_TEMPLATE)) {
                OwsExceptionReport se = new OwsExceptionReport();
                se.addCodedException(OwsExceptionReport.ExceptionCode.InvalidParameterValue, GetObservationParams.responseMode.toString(), "The value of the parameter '" + GetObservationParams.responseMode.toString() + "'" + "must be '" + SosConstants.RESPONSE_MODE_INLINE + " or " + SosConstants.RESPONSE_RESULT_TEMPLATE + "'. Delivered value was: " + responseMode);
                log.error("The responseMode parameter of GetObservation request is incorrect!", se);
                throw se;
            }
        }
    }
}
