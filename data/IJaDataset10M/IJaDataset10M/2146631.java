package uk.ac.ncl.neresc.dynasoar.codestore;

import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.message.SOAPBody;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.message.MessageElement;
import org.apache.axis.utils.XMLUtils;
import org.apache.axis.client.Service;
import org.apache.axis.client.Call;
import org.apache.log4j.Logger;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;
import uk.ac.ncl.neresc.dynasoar.constants.DynasoarConstants;
import uk.ac.ncl.neresc.dynasoar.messages.*;
import uk.ac.ncl.neresc.dynasoar.messages.types.Status;
import uk.ac.ncl.neresc.dynasoar.utils.DOMUtils;
import uk.ac.ncl.neresc.dynasoar.utils.FileUtils;
import uk.ac.ncl.neresc.dynasoar.fault.RegistrationException;
import uk.ac.ncl.neresc.dynasoar.fault.MessageException;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Properties;
import java.net.InetAddress;

/**
 * Created by IntelliJ IDEA.
 * User: Arijit Mukherjee
 * Date: 08-Mar-2006
 * Time: 13:52:50
 * To change this template use File | Settings | File Templates.
 */
public class CodeStoreServiceImpl {

    private static Logger mLog = Logger.getLogger(CodeStoreServiceImpl.class.getName());

    String tempDir = System.getProperty("java.io.tmpdir");

    private final String CODE_STORE = tempDir + File.separator + "codelist.txt";

    private static String SELF_URI = null;

    private static String WSP_URI = null;

    public void processRequest(SOAPEnvelope req, SOAPEnvelope resp) throws SOAPException {
        String input = null;
        try {
            if (SELF_URI == null) {
                setSelfURL();
            }
            SOAPBody reqB = (SOAPBody) req.getBody();
            Iterator itr = reqB.getChildElements();
            if (itr.hasNext()) {
                while (itr.hasNext()) {
                    SOAPBodyElement el1 = (SOAPBodyElement) itr.next();
                    Iterator itr2 = el1.getChildElements(req.createName(DynasoarConstants.CS_REQUEST_NAME));
                    if (!itr2.hasNext()) {
                        mLog.debug("Unable to process request, unknown message");
                        throw new SOAPException("Unable to process request, unknown message");
                    } else {
                        SOAPElement el2 = (SOAPElement) itr2.next();
                        Document doc = ((MessageElement) el2).getAsDocument();
                        input = XMLUtils.DocumentToString(doc);
                        mLog.debug("Unmarshalling request document");
                        CodeStoreRequest request = (CodeStoreRequest) CodeStoreRequest.unmarshal(new StringReader(input));
                        if (request.getAddServiceRequest() != null) {
                            AddServiceRequest addSrv = request.getAddServiceRequest();
                            addService(resp, addSrv);
                        } else if (request.getAddVMServiceRequest() != null) {
                            AddVMServiceRequest vmReq = request.getAddVMServiceRequest();
                            addVMServices(resp, vmReq);
                        } else if (request.getDetailsRequest() != null) {
                        } else if (request.getValidateRequest() != null) {
                            ValidateRequest vldt = request.getValidateRequest();
                            String[] nameList = vldt.getServiceName();
                            validateService(resp, nameList);
                        }
                    }
                }
            } else {
                mLog.debug("Unable to process request, unknown message");
                throw new SOAPException("Unable to process request, unknown message");
            }
        } catch (Exception ex) {
            mLog.error("Cause: " + ex.getCause() + ", Message: " + ex.getMessage());
            throw new SOAPException(ex.getMessage(), ex.getCause());
        }
    }

    public void addService(SOAPEnvelope resp, AddServiceRequest input) throws RegistrationException {
        String srvName = input.getServiceID();
        String srvURI = input.getServiceURI();
        mLog.debug("Printing from Code Store...");
        mLog.debug("Service Name: " + srvName);
        mLog.debug("Service URI: " + srvURI);
        try {
            FileUtils.addNameValuePair(CODE_STORE, srvName, srvURI);
            registerCodeWithWSP(srvName, srvURI);
            returnAddResponse(resp, true);
        } catch (Exception ex) {
            mLog.error("Cause: " + ex.getCause() + ", Message: " + ex.getMessage());
            throw new RegistrationException(ex.getMessage(), ex.getCause());
        }
    }

    private void returnAddResponse(SOAPEnvelope resp, boolean success) throws MessageException {
        try {
            CodeStoreResponse csResp = new CodeStoreResponse();
            AddServiceResponse addResp = new AddServiceResponse();
            if (success) {
                mLog.debug("returning success");
                addResp.setStatus(Status.SUCCESS);
            } else {
                mLog.debug("returning failure");
                addResp.setStatus(Status.FAILURE);
            }
            csResp.setAddServiceResponse(addResp);
            StringWriter writer = new StringWriter();
            Marshaller.marshal(csResp, writer);
            String writerOutput = writer.toString();
            marshallAndReturn(resp, writerOutput);
        } catch (Exception ex) {
            mLog.error("Cause: " + ex.getCause() + ", Message: " + ex.getMessage());
            throw new MessageException(ex.getMessage(), ex.getCause());
        }
    }

    public void addVMServices(SOAPEnvelope resp, AddVMServiceRequest input) throws RegistrationException {
        try {
            String vmName = input.getVmName();
            mLog.debug("Registering the services contained in " + vmName + " with the WSP registry");
            ServiceProviderRequest spReq = new ServiceProviderRequest();
            RegisterVMServices regVM = new RegisterVMServices();
            regVM.setVmName(input.getVmName());
            regVM.setVmPort(input.getVmPort());
            regVM.setVMWrappedServicesList(input.getVMWrappedServicesList());
            regVM.setRepositoryURI(SELF_URI);
            spReq.setRegisterVMServices(regVM);
            StringWriter writer = new StringWriter();
            Marshaller.marshal(spReq, writer);
            String writerOutput = writer.toString();
            String s1 = writerOutput.substring(DynasoarConstants.XML_HEADER.length() + 1);
            String newString = s1.replaceAll(DynasoarConstants.SPREQ_TO_REPLACE, DynasoarConstants.SP_REQUEST_NAME);
            Document outputDocument = XMLUtils.newDocument(new ByteArrayInputStream(newString.getBytes()));
            SOAPEnvelope envelope = new SOAPEnvelope();
            SOAPBody bdy = (SOAPBody) envelope.getBody();
            SOAPBodyElement csReq = (SOAPBodyElement) bdy.addBodyElement(envelope.createName(DynasoarConstants.DYNASOAR_SERVICE_REQUEST));
            SOAPElement elem1 = DOMUtils.convertDOMToSOAPElement(envelope, outputDocument.getDocumentElement());
            csReq.addChildElement(elem1);
            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(WSP_URI));
            call.setTimeout(Integer.MAX_VALUE);
            SOAPEnvelope csResp = call.invoke(envelope);
            SOAPBody respB = (SOAPBody) csResp.getBody();
            Iterator itr = respB.getChildElements();
            if (itr.hasNext()) {
                while (itr.hasNext()) {
                    SOAPBodyElement el1 = (SOAPBodyElement) itr.next();
                    Iterator itr2 = el1.getChildElements(csResp.createName(DynasoarConstants.SP_RESPONSE_NAME));
                    if (!itr2.hasNext()) {
                        mLog.debug("Unable to process request, unknown message");
                    } else {
                        SOAPElement el2 = (SOAPElement) itr2.next();
                        Document doc = ((MessageElement) el2).getAsDocument();
                        String inputString = XMLUtils.DocumentToString(doc);
                        System.out.println("Input request:\n" + inputString);
                        mLog.debug("Unmarshalling request document");
                        ServiceProviderResponse hpR = (ServiceProviderResponse) ServiceProviderResponse.unmarshal(new StringReader(inputString));
                        if (hpR.getSpResponse() != null) {
                            SpResponse aR = hpR.getSpResponse();
                            if (aR.getRegistrationState().equals(Status.SUCCESS.toString())) {
                                mLog.debug("Registered uploaded code");
                                String[] fileAccessURI = input.getFileAccessURI();
                                String outputString = "";
                                for (int i = 0; i < fileAccessURI.length; i++) {
                                    outputString = outputString + fileAccessURI[i];
                                    if (i < fileAccessURI.length - 1) {
                                        outputString = outputString + ";";
                                    }
                                }
                                FileUtils.addNameValuePair(CODE_STORE, input.getVmName(), outputString);
                                returnAddResponse(resp, true);
                            } else {
                                mLog.debug("Service code registration with WSP failed");
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            mLog.error("Exception: " + ex.getMessage() + ", cause: " + ex.getCause());
            throw new RegistrationException(ex.getMessage(), ex.getCause());
        }
    }

    private void validateService(SOAPEnvelope resp, String[] list) {
        mLog.debug("validating one or more services");
        boolean status = false;
        if (list != null) {
            int num = list.length;
            mLog.debug("number of services to validate: " + num);
            ValidatedService vsd = new ValidatedService();
            for (int i = 0; i < num; i++) {
                String serviceName = list[i];
                mLog.debug("Validating \"" + serviceName + "\"");
                try {
                    String location = FileUtils.getValueForNameAsStream(serviceName, CODE_STORE);
                    if (location == null) {
                    }
                    mLog.debug("\"" + serviceName + "\"" + " found at: " + location);
                    ValidatedServiceItem item = new ValidatedServiceItem();
                    item.setServiceName(serviceName);
                    item.setServiceLocation(location);
                    vsd.addValidatedServiceItem(item);
                    status = true;
                } catch (Exception ex) {
                    mLog.error("Can't validate " + serviceName);
                    mLog.error("Cause: " + ex.getCause() + ", Message: " + ex.getMessage());
                    status = false;
                }
            }
            returnValidateResponse(resp, vsd, status);
        }
    }

    private void returnValidateResponse(SOAPEnvelope resp, ValidatedService list, boolean success) {
        try {
            CodeStoreResponse csResp = new CodeStoreResponse();
            ValidationResponse vldResp = new ValidationResponse();
            if (success) {
                mLog.debug("returning success");
                vldResp.setStatus(Status.SUCCESS);
                vldResp.setValidatedService(list);
            } else {
                mLog.debug("returning failure");
                vldResp.setStatus(Status.FAILURE);
            }
            csResp.setValidationResponse(vldResp);
            StringWriter writer = new StringWriter();
            Marshaller.marshal(csResp, writer);
            String writerOutput = writer.toString();
            marshallAndReturn(resp, writerOutput);
        } catch (Exception ex) {
            mLog.error("Exception: " + ex.getMessage() + ", cause: " + ex.getCause());
        }
    }

    private void marshallAndReturn(SOAPEnvelope resp, String message) throws MessageException {
        try {
            SOAPBody resB = (SOAPBody) resp.getBody();
            String s1 = message.substring(DynasoarConstants.XML_HEADER.length() + 1);
            String newString = s1.replaceAll(DynasoarConstants.CSRESP_TO_REPLACE, DynasoarConstants.CS_RESPONSE_NAME);
            Document outputDocument = XMLUtils.newDocument(new ByteArrayInputStream(newString.getBytes()));
            SOAPBodyElement rB = (SOAPBodyElement) resB.addBodyElement(resp.createName(DynasoarConstants.DYNASOAR_SERVICE_RESPONSE));
            SOAPElement elem1 = DOMUtils.convertDOMToSOAPElement(resp, outputDocument.getDocumentElement());
            rB.addChildElement(elem1);
        } catch (Exception ex) {
            mLog.error("Exception: " + ex.getMessage() + ", cause: " + ex.getCause());
            throw new MessageException(ex.getMessage(), ex.getCause());
        }
    }

    private void setSelfURL() throws RegistrationException {
        try {
            InputStream inpStream = null;
            Properties csProp = new Properties();
            inpStream = this.getClass().getResourceAsStream("/DynasoarUploader.properties");
            mLog.debug("Loading ServiceRepository data...");
            csProp.load(inpStream);
            if (inpStream != null) {
                String self = csProp.getProperty("codestore.URL");
                String hostName = InetAddress.getLocalHost().getCanonicalHostName();
                SELF_URI = self.replaceAll("localhost", hostName);
                mLog.debug("Self URI: " + SELF_URI);
                WSP_URI = csProp.getProperty("wsp.uri");
                mLog.debug("WSP URI: " + WSP_URI);
            }
        } catch (Exception ex) {
            mLog.error("Exception: " + ex.getMessage() + ", cause: " + ex.getCause());
            throw new RegistrationException(ex.getMessage(), ex.getCause());
        }
    }

    private void registerCodeWithWSP(String srvName, String srvURI) throws RegistrationException {
        try {
            mLog.debug("Registering the code with the WSP registry");
            ServiceProviderRequest spReq = new ServiceProviderRequest();
            RegisterCode regC = new RegisterCode();
            regC.setServiceName(srvName);
            regC.setRepositoryURI(SELF_URI);
            spReq.setRegisterCode(regC);
            StringWriter writer = new StringWriter();
            Marshaller.marshal(spReq, writer);
            String writerOutput = writer.toString();
            String s1 = writerOutput.substring(DynasoarConstants.XML_HEADER.length() + 1);
            String newString = s1.replaceAll(DynasoarConstants.SPREQ_TO_REPLACE, DynasoarConstants.SP_REQUEST_NAME);
            Document outputDocument = XMLUtils.newDocument(new ByteArrayInputStream(newString.getBytes()));
            SOAPEnvelope envelope = new SOAPEnvelope();
            SOAPBody bdy = (SOAPBody) envelope.getBody();
            SOAPBodyElement csReq = (SOAPBodyElement) bdy.addBodyElement(envelope.createName(DynasoarConstants.DYNASOAR_SERVICE_REQUEST));
            SOAPElement elem1 = DOMUtils.convertDOMToSOAPElement(envelope, outputDocument.getDocumentElement());
            csReq.addChildElement(elem1);
            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(WSP_URI));
            call.setTimeout(Integer.MAX_VALUE);
            SOAPEnvelope csResp = call.invoke(envelope);
            SOAPBody respB = (SOAPBody) csResp.getBody();
            Iterator itr = respB.getChildElements();
            if (itr.hasNext()) {
                while (itr.hasNext()) {
                    SOAPBodyElement el1 = (SOAPBodyElement) itr.next();
                    Iterator itr2 = el1.getChildElements(csResp.createName(DynasoarConstants.SP_RESPONSE_NAME));
                    if (!itr2.hasNext()) {
                        mLog.debug("Unable to process request, unknown message");
                    } else {
                        SOAPElement el2 = (SOAPElement) itr2.next();
                        Document doc = ((MessageElement) el2).getAsDocument();
                        String input = XMLUtils.DocumentToString(doc);
                        mLog.debug("Unmarshalling request document");
                        ServiceProviderResponse hpR = (ServiceProviderResponse) ServiceProviderResponse.unmarshal(new StringReader(input));
                        if (hpR.getSpResponse() != null) {
                            SpResponse aR = hpR.getSpResponse();
                            if (aR.getRegistrationState().equals(Status.SUCCESS.toString())) {
                                mLog.debug("Registered uploaded code");
                            } else {
                                mLog.debug("Service code registration with WSP failed");
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            mLog.error("Exception: " + ex.getMessage() + ", cause: " + ex.getCause());
            throw new RegistrationException(ex.getMessage(), ex.getCause());
        }
    }
}
