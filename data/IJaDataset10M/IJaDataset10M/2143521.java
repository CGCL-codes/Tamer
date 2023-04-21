package com.astrium.faceo.server.service.programming.sps2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.PropertyResourceBundle;
import net.opengis.www.eosps._2_0.GetSensorAvailabilityDocument;
import net.opengis.www.eosps._2_0.GetSensorAvailabilityResponseDocument;
import net.opengis.www.eosps._2_0.GetSensorAvailabilityType;
import net.opengis.www.eosps._2_0.GetSensorAvailabilityType.RequestPeriod;
import net.opengis.www.gml._3_2.TimePeriodType;
import net.opengis.www.gml._3_2.TimePositionType;
import net.opengis.www.sps._2_0.DescribeResultAccessDocument;
import net.opengis.www.sps._2_0.DescribeResultAccessResponseDocument;
import net.opengis.www.sps._2_0.DescribeResultAccessType;
import net.opengis.www.sps._2_0.DescribeResultAccessType.Target;
import net.opengis.www.sps._2_0.DescribeTaskingDocument;
import net.opengis.www.sps._2_0.DescribeTaskingResponseDocument;
import net.opengis.www.sps._2_0.DescribeTaskingType;
import net.opengis.www.swes._2_0.DescribeSensorDocument;
import net.opengis.www.swes._2_0.DescribeSensorResponseDocument;
import net.opengis.www.swes._2_0.DescribeSensorType;
import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;
import org.vast.ows.sps.SPSUtils;
import com.astrium.faceo.client.bean.programming.sps2.describeResultAccess.DescribeResultAccessRequestBean;
import com.astrium.faceo.client.bean.programming.sps2.describeResultAccess.DescribeResultAccessResponseBean;
import com.astrium.faceo.client.bean.programming.sps2.describeSensor.DescribeSensorRequestBean;
import com.astrium.faceo.client.bean.programming.sps2.describeSensor.DescribeSensorResponseBean;
import com.astrium.faceo.client.bean.programming.sps2.describeTasking.DescribeTaskingRequestBean;
import com.astrium.faceo.client.bean.programming.sps2.describeTasking.DescribeTaskingResponseBean;
import com.astrium.faceo.client.bean.programming.sps2.getCapabilities.GetCapabilitiesRequestBean;
import com.astrium.faceo.client.bean.programming.sps2.getCapabilities.GetCapabilitiesResponseBean;
import com.astrium.faceo.client.bean.programming.sps2.getSensorAvailability.GetSensorAvailabilityRequestBean;
import com.astrium.faceo.client.bean.programming.sps2.getSensorAvailability.GetSensorAvailabilityResponseBean;
import com.astrium.faceo.client.common.programming.sps2.Sps2GeneralConstants;
import com.astrium.faceo.server.common.exception.sps2.FacadeSps2Exception;
import com.astrium.faceo.server.service.programming.sps2.parsing.ParsingDescribeResultAccess;
import com.astrium.faceo.server.service.programming.sps2.parsing.ParsingDescribeSensor;
import com.astrium.faceo.server.service.programming.sps2.parsing.ParsingDescribeTasking;
import com.astrium.faceo.server.service.programming.sps2.parsing.ParsingGetSensorAvailability;
import com.astrium.faceo.server.service.programming.sps2.parsing.ParsingUtils;

/**
 * <B>SITE FACEO</B> <BR>
 * 
 * <P>
 * La classe FacadeSensorRequests reprend les services utilis&eacute;s pendant le 
 * processus de lecture des r&eacute;ponses aux op&eacute;tions SPS 2.0
 * </P>
 * </P>
 * 
 * @author GR
 * @version 1.0, le 12/05/2010
 */
public class FacadeSensorRequests {

    /** Le logger de cette classe */
    private static Logger log = Logger.getLogger(FacadeSensorRequests.class);

    private static String ORIGINE = "FacadeSensorRequests";

    protected SPSUtils utils = new SPSUtils();

    /**
	 * constructor
	 */
    public FacadeSensorRequests() {
    }

    /**
	 * To get the DescribeTasking parameters for one sensor
	 * 
	 * @param _parameters (DescribeTaskingRequestBean) : 'DescribeTasking' parameters
	 * @param _serverURL (String) 	: server URL
	 * 
	 * @return DescribeTaskingResponseBean : 'DescribeTasking' response
	 * 
	 * @throws FacadeSps2Exception
	 */
    public DescribeTaskingResponseBean getSensorDescribeTasking(DescribeTaskingRequestBean _parameters, String _serverURL) throws FacadeSps2Exception {
        DescribeTaskingResponseBean response = new DescribeTaskingResponseBean();
        try {
            String endPointSOAP = "http://dione.deimos-space.com/HMA-FO/services/HMA-SPS";
            endPointSOAP = FacadeUtils.getEndPoint(_parameters.getSensorUrn(), _serverURL);
            com.deimos_space.www.hma_sps.HMASPSStub stub = new com.deimos_space.www.hma_sps.HMASPSStub(endPointSOAP);
            net.opengis.www.sps._2_0.DescribeTaskingDocument describeTaskingDocument = DescribeTaskingDocument.Factory.newInstance();
            DescribeTaskingType describeTaskingType = describeTaskingDocument.addNewDescribeTasking();
            describeTaskingType.setService(Sps2GeneralConstants.SPS2_SERVICE);
            describeTaskingType.setVersion(Sps2GeneralConstants.SPS2_VERSION);
            describeTaskingType.setProcedure(_parameters.getSensorUrn());
            log.debug("\n ------------------------------------------------ ");
            log.debug("\n" + ORIGINE + " : describeTaskingDocument : " + describeTaskingDocument.toString() + "\n");
            log.debug("\n ------------------------------------------------ ");
            DescribeTaskingResponseDocument describeTaskingResponseDocument = null;
            try {
                describeTaskingResponseDocument = stub.describeTasking(describeTaskingDocument);
            } catch (AxisFault exception) {
                net.opengis.www.ows._1_1.ExceptionReportDocument exceptionReportDocument = null;
                exceptionReportDocument = net.opengis.www.ows._1_1.ExceptionReportDocument.Factory.parse(exception.getDetail().toString());
                response.getError().setCode(FacadeSps2Exception.SPS_EXCEPTION);
                response.getError().setMessage("Exception : " + exceptionReportDocument.toString());
                log.info("\n" + ORIGINE + " : AxisFault : exceptionReportDocument : " + exceptionReportDocument.toString() + "\n");
                return response;
            } catch (Exception exception) {
                response.getError().setCode(FacadeSps2Exception.SPS_EXCEPTION);
                response.getError().setMessage("Exception : " + exception.toString());
                log.info("\n" + ORIGINE + " : describeTaskingResponseDocument : Exception : " + exception.toString() + "\n");
                return response;
            }
            log.debug("\n ------------------------------------------------ ");
            log.debug("\n" + ORIGINE + " : describeTaskingResponseDocument : " + describeTaskingResponseDocument.toString() + "\n");
            log.debug("\n ------------------------------------------------ ");
            if (describeTaskingResponseDocument != null) {
                ParsingDescribeTasking parsing = new ParsingDescribeTasking();
                response = parsing.parseDescribeTaskingResponse(_parameters, describeTaskingResponseDocument);
            }
            return response;
        } catch (AxisFault axisFault) {
            response.getError().setCode(FacadeSps2Exception.SPS_AXIS_FAULT);
            response.getError().setMessage("Exception : " + axisFault.getMessage());
            log.info("\n" + ORIGINE + " : AxisFault : " + axisFault.getMessage());
            return response;
        } catch (Exception exception) {
            response.getError().setCode(FacadeSps2Exception.SPS_EXCEPTION);
            response.getError().setMessage("Exception : " + exception.getMessage());
            log.info("\n" + ORIGINE + " : Exception : " + exception.getMessage());
            return response;
        } finally {
        }
    }

    /**
	 * To get the DescribeSensor parameters for one sensor
	 * 
	 * @param _parameters (DescribeSensorRequestBean) : 'DescribeSensor' parameters
	 * @param _serverURL (String) 	: server URL
	 * 
	 * @return DescribeSensorResponseBean : 'DescribeSensor' response
	 * 
	 * @throws FacadeSps2Exception
	 */
    public DescribeSensorResponseBean getSensorDescribeSensor(DescribeSensorRequestBean _parameters, String _serverURL) throws FacadeSps2Exception {
        DescribeSensorResponseBean response = new DescribeSensorResponseBean();
        try {
            String endPointSOAP = "http://dione.deimos-space.com/HMA-FO/services/HMA-SPS";
            endPointSOAP = FacadeUtils.getEndPoint(_parameters.getSensorUrn(), _serverURL);
            com.deimos_space.www.hma_sps.HMASPSStub stub = new com.deimos_space.www.hma_sps.HMASPSStub(endPointSOAP);
            DescribeSensorDocument describeSensorDocument = DescribeSensorDocument.Factory.newInstance();
            DescribeSensorType describeSensorType = describeSensorDocument.addNewDescribeSensor();
            describeSensorType.setService(Sps2GeneralConstants.SPS2_SERVICE);
            describeSensorType.setVersion(Sps2GeneralConstants.SPS2_VERSION);
            describeSensorType.setProcedure(_parameters.getSensorUrn());
            describeSensorType.setProcedureDescriptionFormat("http://www.opengis.net/sensorML/1.0.1");
            log.debug("\n ------------------------------------------------ ");
            log.debug("\n" + ORIGINE + " : describeSensorDocument : " + describeSensorDocument.toString() + "\n");
            log.debug("\n ------------------------------------------------ ");
            DescribeSensorResponseDocument describeSensorResponseDocument = null;
            try {
                describeSensorResponseDocument = stub.describeSensor(describeSensorDocument);
            } catch (AxisFault exception) {
                net.opengis.www.ows._1_1.ExceptionReportDocument exceptionReportDocument = null;
                exceptionReportDocument = net.opengis.www.ows._1_1.ExceptionReportDocument.Factory.parse(exception.getDetail().toString());
                response.getError().setCode(FacadeSps2Exception.SPS_EXCEPTION);
                response.getError().setMessage("Exception : " + exceptionReportDocument.toString());
                log.info("\n" + ORIGINE + " : AxisFault : exceptionReportDocument : " + exceptionReportDocument.toString() + "\n");
                return response;
            } catch (Exception exception) {
                response.getError().setCode(FacadeSps2Exception.SPS_EXCEPTION);
                response.getError().setMessage("Exception : " + exception.toString());
                log.info("\n" + ORIGINE + " : describeSensorResponseDocument : Exception : " + exception.toString() + "\n");
                return response;
            }
            log.debug("\n ------------------------------------------------ ");
            log.debug("\n" + ORIGINE + " : describeSensorResponseDocument : " + describeSensorResponseDocument.toString() + "\n");
            log.debug("\n ------------------------------------------------ ");
            if (describeSensorResponseDocument != null) {
                ParsingDescribeSensor parsing = new ParsingDescribeSensor();
                response = parsing.parseDescribeSensorResponse(_parameters, describeSensorResponseDocument);
            }
            return response;
        } catch (AxisFault axisFault) {
            response.getError().setCode(FacadeSps2Exception.SPS_AXIS_FAULT);
            response.getError().setMessage("Exception : " + axisFault.getMessage());
            log.info("\n" + ORIGINE + " : AxisFault : " + axisFault.getMessage());
            return response;
        } catch (Exception exception) {
            response.getError().setCode(FacadeSps2Exception.SPS_EXCEPTION);
            response.getError().setMessage("Exception : " + exception.getMessage());
            log.info("\n" + ORIGINE + " : Exception : " + exception.getMessage());
            return response;
        } finally {
        }
    }

    /**
	 * To get the GetCapabilities parameters for one sensor
	 * 
	 * @param _parameters (GetCapabilitiesRequestBean) : 'GetCapabilities' parameters
	 * @param _serverURL (String) 	: server URL
	 * 
	 * @return GetCapabilitiesResponseBean : 'GetCapabilities' response
	 * 
	 * @throws FacadeSps2Exception
	 */
    public GetCapabilitiesResponseBean getSensorGetCapabilities(GetCapabilitiesRequestBean _parameters, String _serverURL) throws FacadeSps2Exception {
        GetCapabilitiesResponseBean response = new GetCapabilitiesResponseBean();
        try {
            String serviceUrl = "http://dione.deimos-space.com/HMA-FO/GetCapabilitiesServlet?service=SPS&request=GetCapabilities";
            try {
                PropertyResourceBundle prb = (PropertyResourceBundle) PropertyResourceBundle.getBundle(Sps2GeneralConstants.HMAFO_PROPERTIES);
                serviceUrl = prb.getString("sps2CapabilitiesUrl").trim();
                log.debug("\n" + ORIGINE + " : >>> Reading file '" + Sps2GeneralConstants.HMAFO_PROPERTIES + "' OK !!!");
            } catch (Exception e) {
                log.info("\n" + ORIGINE + " : >>> Reading file '" + Sps2GeneralConstants.HMAFO_PROPERTIES + "' KO !!!");
            }
            log.info("\n ------------------------------------------------ ");
            log.info("\n" + ORIGINE + " : SPS 2 GetCapabilities Service URL : " + serviceUrl);
            log.info("\n ------------------------------------------------ ");
            if (!_parameters.getSensorUrn().equalsIgnoreCase("")) {
                String url = ParsingUtils.getGetCapabilitiesUrl(_parameters.getSensorUrn(), _serverURL);
                if (!url.equalsIgnoreCase("")) {
                    serviceUrl = url;
                }
            }
            HttpURLConnection httpConnection = null;
            InputStream httpInputStream = null;
            BufferedReader entree = null;
            try {
                URL httpUrl = new URL(serviceUrl);
                if (httpUrl != null) {
                    httpConnection = (HttpURLConnection) httpUrl.openConnection();
                    if (httpConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        log.info("\n" + ORIGINE + "httpConnection.getResponseMessage() : " + httpConnection.getResponseMessage());
                    }
                    httpInputStream = httpConnection.getInputStream();
                    entree = new BufferedReader(new InputStreamReader(httpInputStream));
                    String line = null;
                    String result = "";
                    while ((line = entree.readLine()) != null) {
                        result += line;
                    }
                    String htmlResponse = "<h3>" + _parameters.getSensorUrn() + "<br>" + Sps2GeneralConstants.SPS_GET_CAPABILITIES + "</h3><br><br>" + result.replace("<", "&lang;").replace(">", "&rang;<br>");
                    response.setResponse(htmlResponse);
                }
            } catch (MalformedURLException malformedURLException) {
                log.info("\n" + ORIGINE + " : MalformedURLException : " + malformedURLException.getMessage());
                response.getError().setMessage(malformedURLException.getMessage());
                response.getError().setCode(FacadeSps2Exception.SPS_MALFORMED_EXCEPTION);
                throw new FacadeSps2Exception(response.getError().getMessage(), response.getError().getCode(), ORIGINE);
            } catch (SocketTimeoutException socketTimeoutException) {
                log.info("\n" + ORIGINE + " : SocketTimeoutException : " + socketTimeoutException.getMessage());
                response.getError().setMessage(socketTimeoutException.getMessage());
                response.getError().setCode(FacadeSps2Exception.SPS_SOCKET_TIMEOUT_EXCEPTION);
                throw new FacadeSps2Exception(response.getError().getMessage(), response.getError().getCode(), ORIGINE);
            } catch (IOException iOException) {
                log.info("\n" + ORIGINE + " : IOException : " + iOException.getMessage());
                response.getError().setMessage(iOException.getMessage());
                response.getError().setCode(FacadeSps2Exception.SPS_IO_EXCEPTION);
                throw new FacadeSps2Exception(response.getError().getMessage(), response.getError().getCode(), ORIGINE);
            } catch (Exception exception) {
                log.info("\n" + ORIGINE + " : Exception : " + exception.getMessage());
                if (response.getError().getCode() == 0) {
                    response.getError().setCode(FacadeSps2Exception.SPS_EXCEPTION);
                    response.getError().setMessage(exception.getMessage());
                }
                throw new FacadeSps2Exception(response.getError().getMessage(), response.getError().getCode(), ORIGINE);
            } finally {
                log.info("\n" + ORIGINE + " : httpURLConnection.disconnect()");
                if (httpInputStream != null) {
                    httpInputStream.close();
                }
                if (entree != null) {
                    entree.close();
                }
                if (httpConnection != null) {
                    httpConnection.disconnect();
                }
            }
            return response;
        } catch (MalformedURLException malformedURLException) {
            log.info("\n" + ORIGINE + " : MalformedURLException : " + malformedURLException.getMessage());
            response.getError().setMessage(malformedURLException.getMessage());
            response.getError().setCode(FacadeSps2Exception.SPS_MALFORMED_EXCEPTION);
            throw new FacadeSps2Exception(response.getError().getMessage(), response.getError().getCode(), ORIGINE);
        } catch (AxisFault axisFault) {
            response.getError().setCode(FacadeSps2Exception.SPS_AXIS_FAULT);
            response.getError().setMessage(axisFault.getMessage());
            log.info("\n" + ORIGINE + " : AxisFault : " + axisFault.getMessage());
            throw new FacadeSps2Exception(response.getError().getMessage(), response.getError().getCode(), ORIGINE);
        } catch (Exception exception) {
            log.info("\n" + ORIGINE + " : Exception : " + exception.getMessage());
            if (response.getError().getCode() == 0) {
                response.getError().setCode(FacadeSps2Exception.SPS_EXCEPTION);
                response.getError().setMessage(exception.getMessage());
            }
            throw new FacadeSps2Exception(response.getError().getMessage(), response.getError().getCode(), ORIGINE);
        } finally {
        }
    }

    /**
	 * To get the DescribeResultAccess parameters for one sensor
	 * 
	 * @param _parameters (DescribeResultAccessRequestBean) : 'DescribeResultAccess' parameters
	 * @param _serverURL (String) 	: server URL
	 * 
	 * @return DescribeResultAccessResponseBean : 'DescribeResultAccess' response
	 * 
	 * @throws FacadeSps2Exception
	 */
    public DescribeResultAccessResponseBean getSensorDescribeResultAccess(DescribeResultAccessRequestBean _parameters, String _serverURL) throws FacadeSps2Exception {
        DescribeResultAccessResponseBean response = new DescribeResultAccessResponseBean();
        try {
            String endPointSOAP = "http://dione.deimos-space.com/HMA-FO/services/HMA-SPS";
            endPointSOAP = FacadeUtils.getEndPoint(_parameters.getSensorUrn(), _serverURL);
            log.info("\n ------------------------------------------------ ");
            log.info("\n" + ORIGINE + " : describeResultAccessDocument : endPointSOAP : " + endPointSOAP + "\n");
            log.info("\n" + ORIGINE + " : describeResultAccessDocument : _parameters.getSensorUrn() : " + _parameters.getSensorUrn() + "\n");
            log.info("\n" + ORIGINE + " : describeResultAccessDocument : _serverURL : " + _serverURL + "\n");
            log.info("\n ------------------------------------------------ ");
            com.deimos_space.www.hma_sps.HMASPSStub stub = new com.deimos_space.www.hma_sps.HMASPSStub(endPointSOAP);
            net.opengis.www.sps._2_0.DescribeResultAccessDocument describeResultAccessDocument = DescribeResultAccessDocument.Factory.newInstance();
            DescribeResultAccessType describeResultAccessType = describeResultAccessDocument.addNewDescribeResultAccess();
            describeResultAccessType.setService(Sps2GeneralConstants.SPS2_SERVICE);
            describeResultAccessType.setVersion(Sps2GeneralConstants.SPS2_VERSION);
            Target target = describeResultAccessType.addNewTarget();
            if (_parameters.getType() == Sps2GeneralConstants.DESCRIBE_ACCESS_SENSOR) {
                target.setProcedure(_parameters.getSensorUrn());
            } else {
                target.setTask(_parameters.getTaskId());
            }
            log.debug("\n ------------------------------------------------ ");
            log.debug("\n" + ORIGINE + " : describeResultAccessDocument : " + describeResultAccessDocument.toString() + "\n");
            log.debug("\n ------------------------------------------------ ");
            DescribeResultAccessResponseDocument describeResultAccessResponseDocument = null;
            try {
                describeResultAccessResponseDocument = stub.describeResultAccess(describeResultAccessDocument);
            } catch (AxisFault exception) {
                net.opengis.www.ows._1_1.ExceptionReportDocument exceptionReportDocument = null;
                exceptionReportDocument = net.opengis.www.ows._1_1.ExceptionReportDocument.Factory.parse(exception.getDetail().toString());
                response.getError().setCode(FacadeSps2Exception.SPS_AXIS_FAULT);
                response.getError().setMessage("AxisFault : " + exceptionReportDocument.toString());
                log.info("\n" + ORIGINE + " : AxisFault : exceptionReportDocument : " + exceptionReportDocument.toString() + "\n");
                throw new FacadeSps2Exception(response.getError().getMessage(), response.getError().getCode(), ORIGINE);
            } catch (Exception exception) {
                response.getError().setCode(FacadeSps2Exception.SPS_EXCEPTION);
                response.getError().setMessage(exception.toString());
                log.info("\n" + ORIGINE + " : describeResultAccessResponseDocument : Exception : " + exception.toString() + "\n");
                throw new FacadeSps2Exception(response.getError().getMessage(), response.getError().getCode(), ORIGINE);
            }
            log.debug("\n ------------------------------------------------ ");
            log.debug("\n" + ORIGINE + " : describeResultAccessResponseDocument : " + describeResultAccessResponseDocument.toString() + "\n");
            log.debug("\n ------------------------------------------------ ");
            if (describeResultAccessResponseDocument != null) {
                ParsingDescribeResultAccess parsing = new ParsingDescribeResultAccess();
                response = parsing.parseDescribeResultAccessResponse(_parameters, describeResultAccessResponseDocument);
            }
            return response;
        } catch (AxisFault axisFault) {
            response.getError().setCode(FacadeSps2Exception.SPS_AXIS_FAULT);
            response.getError().setMessage("AxisFault : " + axisFault.getMessage());
            log.info("\n" + ORIGINE + " : AxisFault : " + axisFault.getMessage());
            throw new FacadeSps2Exception(response.getError().getMessage(), response.getError().getCode(), ORIGINE);
        } catch (Exception exception) {
            response.getError().setCode(FacadeSps2Exception.SPS_EXCEPTION);
            response.getError().setMessage("Exception : " + exception.getMessage());
            log.info("\n" + ORIGINE + " : Exception : " + exception.getMessage());
            throw new FacadeSps2Exception(response.getError().getMessage(), response.getError().getCode(), ORIGINE);
        } finally {
        }
    }

    /**
	 * To get the GetSensorAvailability parameters for one sensor
	 * 
	 * @param _parameters (GetSensorAvailabilityRequestBean) : 'GetSensorAvailability' parameters
	 * @param _serverURL (String) 	: server URL
	 * 
	 * @return GetSensorAvailabilityResponseBean : 'GetSensorAvailability' response
	 * 
	 * @throws FacadeSps2Exception
	 */
    public GetSensorAvailabilityResponseBean getSensorAvailability(GetSensorAvailabilityRequestBean _parameters, String _serverURL) throws FacadeSps2Exception {
        GetSensorAvailabilityResponseBean response = new GetSensorAvailabilityResponseBean();
        try {
            String endPointSOAP = "http://dione.deimos-space.com/HMA-FO/services/HMA-SPS";
            endPointSOAP = FacadeUtils.getEndPoint(_parameters.getSensorUrn(), _serverURL);
            com.deimos_space.www.hma_sps.HMASPSStub stub = new com.deimos_space.www.hma_sps.HMASPSStub(endPointSOAP);
            GetSensorAvailabilityDocument availabilityDocument = GetSensorAvailabilityDocument.Factory.newInstance();
            GetSensorAvailabilityType getSensorAvailabilityType = availabilityDocument.addNewGetSensorAvailability();
            getSensorAvailabilityType.setService(Sps2GeneralConstants.EOSPS_SERVICE);
            getSensorAvailabilityType.setVersion(Sps2GeneralConstants.SPS2_VERSION);
            getSensorAvailabilityType.setSensor(_parameters.getSensorUrn());
            RequestPeriod requestPeriod = getSensorAvailabilityType.addNewRequestPeriod();
            TimePeriodType timePeriod = requestPeriod.addNewTimePeriod();
            timePeriod.setId("PERIOD");
            TimePositionType beginPosition = timePeriod.addNewBeginPosition();
            beginPosition.setStringValue(_parameters.getRequestedPeriod().getStrStartDate());
            TimePositionType endPosition = timePeriod.addNewEndPosition();
            endPosition.setStringValue(_parameters.getRequestedPeriod().getStrEndDate());
            getSensorAvailabilityType.setRequestPeriod(requestPeriod);
            log.debug("\n ------------------------------------------------ ");
            log.debug("\n" + ORIGINE + " : availabilityDocument : " + availabilityDocument.toString() + "\n");
            log.debug("\n ------------------------------------------------ ");
            GetSensorAvailabilityResponseDocument sensorAvailabilityResponseDocument = null;
            try {
                sensorAvailabilityResponseDocument = stub.getSensorAvailability(availabilityDocument);
            } catch (AxisFault exception) {
                net.opengis.www.ows._1_1.ExceptionReportDocument exceptionReportDocument = null;
                exceptionReportDocument = net.opengis.www.ows._1_1.ExceptionReportDocument.Factory.parse(exception.getDetail().toString());
                response.getError().setCode(FacadeSps2Exception.SPS_EXCEPTION);
                response.getError().setMessage("Exception : " + exceptionReportDocument.toString());
                log.info("\n" + ORIGINE + " : AxisFault : exceptionReportDocument : " + exceptionReportDocument.toString() + "\n");
                return response;
            } catch (Exception exception) {
                response.getError().setCode(FacadeSps2Exception.SPS_EXCEPTION);
                response.getError().setMessage("Exception : " + exception.toString());
                log.info("\n" + ORIGINE + " : sensorAvailabilityResponseDocument : Exception : " + exception.toString() + "\n");
                return response;
            }
            log.debug("\n ------------------------------------------------ ");
            log.debug("\n" + ORIGINE + " : sensorAvailabilityResponseDocument : " + sensorAvailabilityResponseDocument.toString() + "\n");
            log.debug("\n ------------------------------------------------ ");
            if (sensorAvailabilityResponseDocument != null) {
                ParsingGetSensorAvailability parsing = new ParsingGetSensorAvailability();
                response = parsing.parseGetSensorAvailabilityResponse(_parameters, sensorAvailabilityResponseDocument);
            }
            return response;
        } catch (AxisFault axisFault) {
            response.getError().setCode(FacadeSps2Exception.SPS_AXIS_FAULT);
            response.getError().setMessage("Exception : " + axisFault.getMessage());
            log.info("\n" + ORIGINE + " : AxisFault : " + axisFault.getMessage());
            return response;
        } catch (Exception exception) {
            response.getError().setCode(FacadeSps2Exception.SPS_EXCEPTION);
            response.getError().setMessage("Exception : " + exception.getMessage());
            log.info("\n" + ORIGINE + " : Exception : " + exception.getMessage());
            return response;
        } finally {
        }
    }
}
