package com.astrium.faceo.server.rpc.programming.sps2;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.astrium.faceo.client.bean.programming.sps2.getCapabilities.GetCapabilitiesRequestBean;
import com.astrium.faceo.client.bean.programming.sps2.getCapabilities.GetCapabilitiesResponseBean;
import com.astrium.faceo.client.common.programming.sps2.Sps2GeneralConstants;
import com.astrium.faceo.client.rpc.programming.sps2.GetCapabilitiesService;
import com.astrium.faceo.server.common.exception.sps2.FacadeSps2Exception;
import com.astrium.faceo.server.service.programming.sps2.FacadeSensorRequests;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Server side implementation of the GetCapabilitiesService interface.
 * 
 * The mandatory GetCapabilities operation allows clients 
 * to retrieve service metadata from a server. 
 * The response to a GetCapabilities request shall be service metadata 
 * about the server, including specific information about the sensors provided by the service, 
 * supported data encodings and – if supported by the binding employed by the service – 
 * also metadata about the supported notification functionality.
 * 
 * @author ASTRIUM
 *
 */
public class GetCapabilitiesServiceImpl extends RemoteServiceServlet implements GetCapabilitiesService {

    /**
	 * 
	 */
    private static final long serialVersionUID = 351948864744811382L;

    /** Le logger de cette classe */
    private static Logger log = Logger.getLogger(GetCapabilitiesServiceImpl.class);

    String INFOS_CLASSE = "GetCapabilitiesServiceImpl";

    /**
	 *  traitement de la requete HTTP GET
	 *  
	 * @param _request (HttpServletRequest)	: the request
	 * @param _response (HttpServletResponse)	: the response
	 *  
	 * @throws ServletException
	 * @throws IOException
	 */
    public void doGet(HttpServletRequest _request, HttpServletResponse _response) throws ServletException, IOException {
    }

    /**
	 * The annotation indicates that the returned List will only 
	 * contain objects of  type <com.astrium.faceo.bean.client.bean.programming.sps2.getCapabilities.GetCapabilitiesResponseBean>
	 * 
	 * This method returns the response of a 'DescribeTasking' resquets for oe sensor
	 * 
	 * 
	 * @param _operation (String)						: operation of the FACEO service ('GetCapabilities')
	 * @param _parameters (GetCapabilitiesRequestBean)	: 'GetCapabilities' parameters for one sensor
	 * @param _webAccessMode (boolean)					: true for using web services, false else (for using kml files)
	 * 
	 * @return GetCapabilitiesRequestBean : 'GetCapabilities' response in HTML format
	 * 
	 * @gwt.typeArgs <com.astrium.faceo.bean.client.bean.programming.sps2.getCapabilities.GetCapabilitiesResponseBean>
	 */
    public GetCapabilitiesResponseBean getGetCapabilitiesResult(String _operation, GetCapabilitiesRequestBean _parameters, boolean _webAccessMode) {
        GetCapabilitiesResponseBean getCapabilitiesResponse = new GetCapabilitiesResponseBean();
        String messageErreur = null;
        int code_erreur = 0;
        log.debug("\n" + INFOS_CLASSE + " : operation : " + _operation);
        if (_operation == null) {
            messageErreur = "undefined operation";
            code_erreur = 531;
        } else {
            if (!_operation.equalsIgnoreCase(Sps2GeneralConstants.SPS_GET_CAPABILITIES)) {
                messageErreur = "bad operation";
                code_erreur = 534;
            }
            if (_parameters.getSensorUrn().equalsIgnoreCase("")) {
                messageErreur = "undefined sensor Id";
                code_erreur = 536;
            }
        }
        if (messageErreur != null) {
            log.debug("\n" + INFOS_CLASSE + " : error : " + messageErreur);
            getCapabilitiesResponse.getError().setMessage("error : " + messageErreur);
            getCapabilitiesResponse.getError().setCode(code_erreur);
            return getCapabilitiesResponse;
        }
        try {
            HttpServletRequest request = this.getThreadLocalRequest();
            String requestURL = request.getRequestURL().toString();
            int pos = requestURL.lastIndexOf(Sps2GeneralConstants.GET_CAPABILITIES_CONTROLLER);
            String serverURL = requestURL;
            if (pos > 0) {
                serverURL = requestURL.substring(0, pos);
            }
            FacadeSensorRequests facadeSensorsRequests = new FacadeSensorRequests();
            getCapabilitiesResponse = facadeSensorsRequests.getSensorGetCapabilities(_parameters, serverURL);
            return getCapabilitiesResponse;
        } catch (FacadeSps2Exception exception) {
            log.info("\n" + INFOS_CLASSE + " : getGetCapabilitiesResult : FacadeSps2Exception : " + exception.getMessage());
            getCapabilitiesResponse.getError().setMessage(exception.getMessage());
            getCapabilitiesResponse.getError().setCode(exception.getCode());
            return getCapabilitiesResponse;
        } catch (Exception exception) {
            log.info("\n" + INFOS_CLASSE + " : getGetCapabilitiesResult : Exception : " + exception.getMessage());
            if (getCapabilitiesResponse.getError().getCode() == 0) {
                getCapabilitiesResponse.getError().setMessage(exception.getMessage());
                getCapabilitiesResponse.getError().setCode(FacadeSps2Exception.SPS_EXCEPTION);
            }
            return getCapabilitiesResponse;
        }
    }
}
