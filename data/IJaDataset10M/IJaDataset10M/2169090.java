package com.idna.gav.dao.impl;

import java.io.UnsupportedEncodingException;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import com.idna.gav.dao.VoltDao;
import com.idna.gav.exceptions.GavApplicationException;
import com.idna.gav.exceptions.VoltServiceException;
import com.idna.gav.remote.volt.VoltRequestExecutor;

/**
 * Encapsulates the logic for performing searches on VOLT
 * 
 */
public class VoltDaoImpl implements VoltDao {

    private VoltRequestExecutor voltConnector;

    private String authenticationUsername;

    private String maxListings;

    private String timeout = "10000";

    private final Logger logger = Logger.getLogger(VoltDaoImpl.class);

    /**
	 * Performs search on VOLT using the data contained in request document supplied as a parameter.
	 *  
	 * This method also takes responsibility of adding some required fields to the request document. 
	 * Currently these are MaxListings, MaxTimeout and Authentication/Username.
	 * 
	 * @see com.idna.gav.service.volt.impl.VoltServiceImpl
	 * 
	 * @param  request  a dom4j Document containing the search criteria
	 * @param searchFeatureId
	 * @param loginId 
	 * @return String
	 * @throws UnsupportedEncodingException 
	 * @throws VoltServiceException 
	 */
    public String getVoltResponse(Document request, String searchFeatureId, String loginId) throws UnsupportedEncodingException, GavApplicationException, VoltServiceException {
        this.enhanceRequestDocument(request);
        String requestString = null;
        if (request != null) {
            requestString = request.asXML();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!  REQUEST  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            logger.debug("xml BEFORE sending to VOLT: " + requestString);
        }
        String voltResponse = voltConnector.executeVOLTRequest(requestString, searchFeatureId, loginId);
        if (logger.isDebugEnabled()) {
            logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!  RESPONSE  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            logger.debug("xml AFTER sending to VOLT: " + voltResponse);
        }
        return voltResponse;
    }

    private void enhanceRequestDocument(Document request) {
        Element search = null;
        if (request != null) {
            search = (Element) request.selectSingleNode("//Search");
        } else {
            return;
        }
        search.addAttribute("MaxListings", maxListings);
        search.addAttribute("MaxTimeout", timeout);
        search.addElement("Authentication").addElement("Username").addText(authenticationUsername);
        return;
    }

    public void setVoltConnector(VoltRequestExecutor voltConnector) {
        this.voltConnector = voltConnector;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public void setAuthenticationUsername(String authenticationUsername) {
        this.authenticationUsername = authenticationUsername;
    }

    public void setMaxListings(String maxListings) {
        this.maxListings = maxListings;
    }
}
