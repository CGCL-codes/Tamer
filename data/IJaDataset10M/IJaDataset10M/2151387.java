package net.sourceforge.gateway.sstp.faces.beans;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import javax.xml.rpc.ServiceException;
import net.sourceforge.gateway.sstp.constants.Constants;
import net.sourceforge.gateway.sstp.databases.SSTPMessageManager;
import net.sourceforge.gateway.sstp.databases.tables.AckDTO;
import net.sourceforge.gateway.sstp.registration.SSTRegistrationsServiceSoapProxy;
import net.sourceforge.gateway.sstp.registration.TRegAckResults;
import net.sourceforge.gateway.sstp.registration.TRegReqResults;
import net.sourceforge.gateway.sstp.registration.TRegRetrieveResults;
import net.sourceforge.gateway.sstp.registration.TRegStatResults;
import net.sourceforge.gateway.sstp.services.LastAckResponseAcknowledgement;
import net.sourceforge.gateway.sstp.services.SSTPServiceLocator;
import net.sourceforge.gateway.sstp.services.SSTPServiceSoap;
import net.sourceforge.gateway.sstp.services.SSTPServiceSoapStub;
import net.sourceforge.gateway.sstp.utilities.SSTPUtils;
import net.sourceforge.gateway.sstp.utilities.Utils;
import net.sourceforge.gateway.sstp.utilities.XMLUtils;
import org.apache.axis.message.MessageElement;
import org.apache.commons.codec.binary.Base64;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.log4j.Logger;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0.Password;
import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0.SecurityHeader;
import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0.UsernameToken;
import org.streamlinedsalestax.efile.EFileService;
import org.streamlinedsalestax.efile.EFileServiceSoap;
import org.streamlinedsalestax.efile.Send.Transmission;
import org.streamlinedsalestax.efile.SendResponse.Receipt;
import org.w3c.dom.Node;

/**
 * Manage the current user's locale setting.
 */
public class SSTRegistrationBean {

    /**
     * Default Constructor.
     */
    public SSTRegistrationBean() {
        securityHeader = new SecurityHeader();
        try {
            wsloc = new SSTPServiceLocator();
            service = wsloc.getSSTPServiceSoap((new URL(Constants.LOCAL_WEB_SERVICES_PORT)));
            sstRegistrationsServiceSoapProxy = new SSTRegistrationsServiceSoapProxy();
        } catch (Exception e) {
            LOG.error("Setup Failed", e);
        }
    }

    /**
     * Constants.TEST_SREG_WEB_SERVICES_PORT or
     * Constants.PRD_SREG_WEB_SERVICES_PORT
     */
    private String registrationEndpoint = null;

    /**
     * The username
     */
    private String registrationUserID = null;

    /**
     * The password
     */
    private String registrationPassword = null;

    /**
     * Type of Request
     */
    private String registrationRequestType = null;

    /**
     * Return code
     */
    private String registrationRequestReturnCode = null;

    /**
     * Message
     */
    private String registrationRequestReturnMessage = null;

    /**
     * HTML tranformation of message
     */
    private String registrationRequestReturnMessageHTML = null;

    /**
     * Status of the Job
     */
    private String registrationJobStatus = null;

    /**
     * The Job Identifier
     */
    private String registrationJobID = null;

    /**
     * Ack Results
     */
    private String registrationAckResults = null;

    /**
     * Confirmation Number
     */
    private String registrationAckConfirmationNo = null;

    /**
     * The username of the gateway user.
     */
    private String gatewayUserID = null;

    /**
     * The password of the gateway user.
     */
    private String gatewayPassword = null;

    /**
     * The Results
     */
    private String gatewayRegistrationResults = null;

    /**
     * The ack file
     */
    private String gatewayAckFile = null;

    /**
     * Ack file zipped?
     */
    private String ackFileZipped = null;

    /**
     * Ack file base64?
     */
    private String ackFileBase64 = null;

    /**
     * SOAP Service Locator.
     */
    private SSTPServiceLocator wsloc = null;

    /**
     * The SSTPSOAP Service.
     */
    private SSTPServiceSoap service = null;

    /**
     * The Security Header for the SOAP Message.
     */
    private SecurityHeader securityHeader = null;

    /**
     * Version UID for Serialization.
     */
    private static final long serialVersionUID = 1;

    /**
     * SSTRegistrationsServiceSoapProxy
     */
    private SSTRegistrationsServiceSoapProxy sstRegistrationsServiceSoapProxy = null;

    /**
     * Logging facility.
     */
    protected static final Logger LOG = Logger.getLogger("net.sourceforge.gateway");

    /**
     * @return the ackFileBase64
     */
    public String getAckFileBase64() {
        return ackFileBase64;
    }

    /**
     * @param ackFileBase64
     *        the ackFileBase64 to set
     */
    public void setAckFileBase64(String ackFileBase64) {
        this.ackFileBase64 = ackFileBase64;
    }

    /**
     * @return the ackFileZipped
     */
    public String getAckFileZipped() {
        return ackFileZipped;
    }

    /**
     * @param ackFileZipped
     *        the ackFileZipped to set
     */
    public void setAckFileZipped(String ackFileZipped) {
        this.ackFileZipped = ackFileZipped;
    }

    /**
     * @return the gatewayAckFile
     */
    public String getGatewayAckFile() {
        return gatewayAckFile;
    }

    /**
     * @param gatewayAckFile
     *        the gatewayAckFile to set
     */
    public void setGatewayAckFile(String gatewayAckFile) {
        this.gatewayAckFile = gatewayAckFile;
    }

    /**
     * @return the gatewayPassword
     */
    public String getGatewayPassword() {
        return gatewayPassword;
    }

    /**
     * @param gatewayPassword
     *        the gatewayPassword to set
     */
    public void setGatewayPassword(String gatewayPassword) {
        this.gatewayPassword = gatewayPassword;
    }

    /**
     * @return the gatewayRegistrationResults
     */
    public String getGatewayRegistrationResults() {
        return gatewayRegistrationResults;
    }

    /**
     * @param gatewayRegistrationResults
     *        the gatewayRegistrationResults to set
     */
    public void setGatewayRegistrationResults(String gatewayRegistrationResults) {
        this.gatewayRegistrationResults = gatewayRegistrationResults;
    }

    /**
     * @return the gatewayUserID
     */
    public String getGatewayUserID() {
        return gatewayUserID;
    }

    /**
     * @param gatewayUserID
     *        the gatewayUserID to set
     */
    public void setGatewayUserID(String gatewayUserID) {
        this.gatewayUserID = gatewayUserID;
    }

    /**
     * @return the registrationAckResults
     */
    public String getRegistrationAckResults() {
        return registrationAckResults;
    }

    /**
     * @param registrationAckResults
     *        the registrationAckResults to set
     */
    public void setRegistrationAckResults(String registrationAckResults) {
        this.registrationAckResults = registrationAckResults;
    }

    /**
     * @return the registrationEndpoint
     */
    public String getRegistrationEndpoint() {
        return registrationEndpoint;
    }

    /**
     * @param registrationEndpoint
     *        the registrationEndpoint to set
     */
    public void setRegistrationEndpoint(String registrationEndpoint) {
        this.registrationEndpoint = registrationEndpoint;
    }

    /**
     * @return the registrationJobID
     */
    public String getRegistrationJobID() {
        return registrationJobID;
    }

    /**
     * @param registrationJobID
     *        the registrationJobID to set
     */
    public void setRegistrationJobID(String registrationJobID) {
        this.registrationJobID = registrationJobID;
    }

    /**
     * @return the registrationJobStatus
     */
    public String getRegistrationJobStatus() {
        return registrationJobStatus;
    }

    /**
     * @param registrationJobStatus
     *        the registrationJobStatus to set
     */
    public void setRegistrationJobStatus(String registrationJobStatus) {
        this.registrationJobStatus = registrationJobStatus;
    }

    /**
     * @return the registrationPassword
     */
    public String getRegistrationPassword() {
        return registrationPassword;
    }

    /**
     * @param registrationPassword
     *        the registrationPassword to set
     */
    public void setRegistrationPassword(String registrationPassword) {
        this.registrationPassword = registrationPassword;
    }

    /**
     * @return the registrationRequestReturnCode
     */
    public String getRegistrationRequestReturnCode() {
        return registrationRequestReturnCode;
    }

    /**
     * @param registrationRequestReturnCode
     *        the registrationRequestReturnCode to set
     */
    public void setRegistrationRequestReturnCode(String registrationRequestReturnCode) {
        this.registrationRequestReturnCode = registrationRequestReturnCode;
    }

    /**
     * @return the registrationRequestReturnMessage
     */
    public String getRegistrationRequestReturnMessage() {
        return registrationRequestReturnMessage;
    }

    /**
     * @param registrationRequestReturnMessage
     *        the registrationRequestReturnMessage to set
     */
    public void setRegistrationRequestReturnMessage(String registrationRequestReturnMessage) {
        this.registrationRequestReturnMessage = registrationRequestReturnMessage;
    }

    /**
     * @return the registrationRequestType
     */
    public String getRegistrationRequestType() {
        return registrationRequestType;
    }

    /**
     * @param registrationRequestType
     *        the registrationRequestType to set
     */
    public void setRegistrationRequestType(String registrationRequestType) {
        this.registrationRequestType = registrationRequestType;
    }

    /**
     * @return the registrationUserID
     */
    public String getRegistrationUserID() {
        return registrationUserID;
    }

    /**
     * @param registrationUserID
     *        the registrationUserID to set
     */
    public void setRegistrationUserID(String registrationUserID) {
        this.registrationUserID = registrationUserID;
    }

    /**
     * @return the registrationAckConfirmationNo
     */
    public String getRegistrationAckConfirmationNo() {
        return registrationAckConfirmationNo;
    }

    /**
     * @param registrationAckConfirmationNo
     *        the registrationAckConfirmationNo to set
     */
    public void setRegistrationAckConfirmationNo(String registrationAckConfirmationNo) {
        this.registrationAckConfirmationNo = registrationAckConfirmationNo;
    }

    /**
     * @return the registrationRequestReturnMessageHTML
     */
    public String getRegistrationRequestReturnMessageHTML() {
        return registrationRequestReturnMessageHTML;
    }

    /**
     * @param registrationRequestReturnMessageHTML
     *        the registrationRequestReturnMessageHTML to set
     */
    public void setRegistrationRequestReturnMessageHTML(String registrationRequestReturnMessageHTML) {
        this.registrationRequestReturnMessageHTML = registrationRequestReturnMessageHTML;
    }

    /**
     * Set the Soap Services Endpoint for Central Registration Vendor to the
     * Test or Production web services.
     * 
     */
    private void setSregServiceEndpoint() {
        String endpoint = null;
        if (getRegistrationEndpoint().equals("T")) {
            endpoint = Constants.TEST_SREG_WEB_SERVICES_PORT;
        } else if (getRegistrationEndpoint().equals("P")) {
            endpoint = Constants.PRD_SREG_WEB_SERVICES_PORT;
        }
        sstRegistrationsServiceSoapProxy.setEndpoint(endpoint);
    }

    /**
     * Get the ack XMLString from the local SOAP service for userID
     * 
     * @param userID
     *        the SOAP user's username
     * @param passwd
     *        the SOAP user's password
     * @return String[2] of the resulting XML and Transmission ID
     * @throws java.rmi.RemoteException
     */
    private String ackFromLocalWebServices(String userID, String passwd) throws java.rmi.RemoteException {
        EFileServiceSoap client = this.getEFileServiceSoap(userID, passwd);
        String returnStrings = null;
        try {
            org.streamlinedsalestax.efile.AckResponse.Acknowledgement result = client.ack(Utils.getLoginBean().getUsername().toUpperCase());
            if (result.getAny() != null) {
                returnStrings = XMLUtils.nodeToString((Node) result.getAny());
            } else {
                returnStrings = "Ack was null";
            }
        } catch (Exception e) {
            LOG.error("Ack Failed", e);
        }
        return returnStrings;
    }

    /**
     * Get the last ack XMLString from the local SOAP service for userID
     * 
     * @param userID
     *        the SOAP user's username
     * @param passwd
     *        the SOAP user's password
     * @return String[2] of the resulting XML and Transmission ID
     * @throws java.rmi.RemoteException
     */
    private String lastAckFromLocalWebServices(String userID, String passwd) throws java.rmi.RemoteException {
        EFileServiceSoap client = this.getEFileServiceSoap(userID, passwd);
        String returnStrings = null;
        try {
            org.streamlinedsalestax.efile.LastAckResponse.Acknowledgement result = client.lastAck(Utils.getLoginBean().getUsername().toUpperCase());
            if (result.getAny() != null) {
                returnStrings = XMLUtils.nodeToString((Node) result.getAny());
            } else {
                returnStrings = "Last Ack was null";
            }
        } catch (Exception e) {
            LOG.error("Last Ack Failed", e);
        }
        return returnStrings;
    }

    /**
     * Makes a request for SST Registrations from the Central Registration
     * Vendor
     * 
     * @return "success" or "failed"
     */
    public String putRegistrationsRequest() {
        setSregServiceEndpoint();
        try {
            TRegReqResults putRegistrationsRequest = sstRegistrationsServiceSoapProxy.putRegistrationsRequest(getRegistrationUserID(), getRegistrationPassword(), Integer.parseInt(getRegistrationRequestType()));
            if (putRegistrationsRequest == null) {
                return "failed";
            } else {
                setRegistrationJobID(putRegistrationsRequest.getStrJobID());
                setRegistrationRequestReturnCode(String.valueOf(putRegistrationsRequest.getIntRsltCode()));
                setRegistrationRequestReturnMessage(putRegistrationsRequest.getStrRsltMsg());
            }
        } catch (NumberFormatException e) {
            LOG.error("Put Regs Req Failed", e);
            return "failed";
        } catch (RemoteException e) {
            LOG.error("Put Regs Req Failed", e);
            return "failed";
        }
        return "success";
    }

    /**
     * Check the Status of request for SST Registrations from the Central
     * Registration Vendor
     * 
     * @return "success" or "failed"
     */
    public String getRegistrationsJobStatus() {
        setSregServiceEndpoint();
        try {
            TRegStatResults getRegistrationsJobStatus = sstRegistrationsServiceSoapProxy.getRegistrationsJobStatus(getRegistrationUserID(), getRegistrationPassword(), getRegistrationJobID());
            if (getRegistrationsJobStatus == null) {
                return "failed";
            } else {
                setRegistrationJobStatus(String.valueOf(getRegistrationsJobStatus.getIntJobStatus()));
                setRegistrationRequestReturnCode(String.valueOf(getRegistrationsJobStatus.getIntRsltCode()));
                setRegistrationRequestReturnMessage(getRegistrationsJobStatus.getStrRsltMsg());
            }
        } catch (NumberFormatException e) {
            LOG.error("get reg job status failed", e);
            return "failed";
        } catch (RemoteException e) {
            LOG.error("get reg job status failed", e);
            e.printStackTrace();
            return "failed";
        }
        return "success";
    }

    /**
     * Get the requested for SST Registrations from the Central Registration
     * Vendor and process it in the Local Gateway.
     * 
     * @return "success" or "failed"
     */
    public String getRegistrationsJob() {
        setSregServiceEndpoint();
        try {
            TRegRetrieveResults getRegistrationsJob = sstRegistrationsServiceSoapProxy.getRegistrationsJob(getRegistrationUserID(), getRegistrationPassword(), getRegistrationJobID());
            if (getRegistrationsJob == null) {
                return "failed";
            } else {
                String registrationFile = new String(Base64.decodeBase64(getRegistrationsJob.getBinRegFile().getBytes("UTF-8")));
                LOG.warn(registrationFile);
                setRegistrationRequestReturnMessage(XMLUtils.fmt(registrationFile));
                EFileServiceSoap client = this.getEFileServiceSoap(getGatewayUserID().toUpperCase(), getGatewayPassword());
                Transmission transmission = new Transmission();
                transmission.setAny(XMLUtils.stringToDocument(registrationFile).getElementsByTagName(Constants.SSTREGISTRATIONTRANSMISSION).item(0));
                Receipt registrationResults = client.send(transmission);
                if (registrationResults == null) {
                    setGatewayRegistrationResults("Failed");
                } else {
                    setGatewayRegistrationResults(XMLUtils.nodeToString((Node) registrationResults.getAny()));
                }
            }
        } catch (NumberFormatException e) {
            LOG.error("get reg job failed", e);
            return "failed";
        } catch (RemoteException e) {
            LOG.error("get reg job failed", e);
            return "failed";
        } catch (UnsupportedEncodingException e) {
            LOG.error("get reg job failed", e);
            return "failed";
        } catch (Exception e) {
            LOG.error("get reg job failed", e);
            return "failed";
        }
        return "success";
    }

    /**
     * Get the Acknowledgements from the Local Gateway's processing of SST
     * Registrations and sent to the Central Registration Vendor.
     * 
     * @return "success" or "failed"
     */
    public String putJobAcknowledgements() {
        setSregServiceEndpoint();
        setGatewayAckFile("");
        String ackFile2Send = null;
        int intZipFile = 0;
        try {
            TRegAckResults putJobAcknowledgements = null;
            String ackFile = ackFromLocalWebServices(getGatewayUserID().toUpperCase(), getGatewayPassword());
            if (ackFile == null) {
                return "failed";
            } else {
                setGatewayAckFile(XMLUtils.fmt(ackFile));
                setRegistrationJobID(SSTPUtils.getTransmissionID(ackFile));
                if (getAckFileBase64().equals("Y")) {
                    ackFile2Send = new String(Base64.encodeBase64Chunked(ackFile.getBytes("UTF-8")));
                }
                putJobAcknowledgements = sstRegistrationsServiceSoapProxy.putJobAcknowledgements(getRegistrationUserID(), getRegistrationPassword(), getRegistrationJobID(), intZipFile, ackFile2Send);
                if (putJobAcknowledgements == null) {
                    return "failed";
                } else {
                    setRegistrationRequestReturnCode(String.valueOf(putJobAcknowledgements.getIntRsltCode()));
                    setRegistrationAckConfirmationNo(putJobAcknowledgements.getStrConfirmationNo());
                    setRegistrationRequestReturnMessage(putJobAcknowledgements.getStrRsltMsg());
                    setRegistrationRequestReturnMessageHTML(getRegistrationRequestReturnMessage());
                }
            }
        } catch (NumberFormatException e) {
            LOG.error("put job acks failed", e);
            return "failed";
        } catch (RemoteException e) {
            LOG.error("put job acks failed", e);
            return "failed";
        } catch (UnsupportedEncodingException e) {
            LOG.error("put job acks failed", e);
            return "failed";
        } catch (Exception e) {
            LOG.error("put job acks failed", e);
            return "failed";
        }
        return "success";
    }

    /**
     * Repeats the Last Acknowledgements from the Local Gateway's processing of
     * SST Registrations and sent to the Central Registration Vendor.
     * 
     * @return "success" or "failed"
     */
    public String putLastJobAcknowledgements() {
        setSregServiceEndpoint();
        setGatewayAckFile("");
        String ackFile2Send = null;
        int intZipFile = 0;
        try {
            TRegAckResults putLastJobAcknowledgements = null;
            String ackFile = lastAckFromLocalWebServices(getGatewayUserID().toUpperCase(), getGatewayPassword());
            if (ackFile == null) {
                return "failed";
            } else {
                setGatewayAckFile(XMLUtils.fmt(ackFile));
                setRegistrationJobID(SSTPUtils.getTransmissionID(ackFile));
                if (getAckFileBase64().equals("Y")) {
                    ackFile2Send = new String(Base64.encodeBase64Chunked(ackFile.getBytes("UTF-8")));
                }
                putLastJobAcknowledgements = sstRegistrationsServiceSoapProxy.putJobAcknowledgements(getRegistrationUserID(), getRegistrationPassword(), getRegistrationJobID(), intZipFile, ackFile2Send);
                if (putLastJobAcknowledgements == null) {
                    return "failed";
                } else {
                    setRegistrationRequestReturnCode(String.valueOf(putLastJobAcknowledgements.getIntRsltCode()));
                    setRegistrationAckConfirmationNo(putLastJobAcknowledgements.getStrConfirmationNo());
                    setRegistrationRequestReturnMessage(putLastJobAcknowledgements.getStrRsltMsg());
                    XMLUtils pp = new XMLUtils();
                    setRegistrationRequestReturnMessageHTML(pp.transformXML2HTML(getRegistrationRequestReturnMessage(), Constants.ACK_2_HTML_TRANSFORM_XSL));
                }
            }
        } catch (NumberFormatException e) {
            LOG.error("put last job acks failed", e);
            return "failed";
        } catch (RemoteException e) {
            LOG.error("put last job acks failed", e);
            return "failed";
        } catch (UnsupportedEncodingException e) {
            LOG.error("put last job acks failed", e);
            return "failed";
        } catch (Exception e) {
            LOG.error("put last job acks failed", e);
            return "failed";
        }
        return "success";
    }

    protected EFileServiceSoap getEFileServiceSoap(String user, String password) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL WSDL_LOCATION = null;
        if (null == cl) cl = EFileService.class.getClassLoader();
        WSDL_LOCATION = cl.getResource("EFileServiceSoap.wsdl");
        EFileService service = new EFileService(WSDL_LOCATION);
        EFileServiceSoap efile = service.getEFileServiceSoap();
        Client client = org.apache.cxf.frontend.ClientProxy.getClient(efile);
        Endpoint cxfEndpoint = client.getEndpoint();
        Map<String, Object> outProps = new HashMap<String, Object>();
        outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
        outProps.put(WSHandlerConstants.USER, user);
        outProps.put("password", password);
        outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
        outProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, PasswordCallbackHandler.class.getName());
        WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
        cxfEndpoint.getOutInterceptors().add(wssOut);
        return efile;
    }
}
