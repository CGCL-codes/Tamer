package org.fao.fenix.web.server;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.activation.DataHandler;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.llom.OMAttributeImpl;
import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.wsdl.WSDLConstants;
import org.apache.log4j.Logger;
import org.fao.fenix.web.client.CommunicationResource;
import org.fao.fenix.web.client.services.CommunicationService;
import org.fao.fenix.web.utils.UrlFinder;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class CommunicationServiceImpl extends RemoteServiceServlet implements CommunicationService {

    private static EndpointReference targetEPR;

    public static String NAMESPACE;

    UrlFinder urlFinder;

    public CommunicationServiceImpl() {
        targetEPR = new EndpointReference("http://localhost:8080/fenix-services/services/CommunicationModuleService");
        NAMESPACE = "http://webservice.communication.fenix.fao.org/xsd";
    }

    private void setEpr(String target) {
        targetEPR = new EndpointReference(target);
        System.out.println("TARGET HAS BEEN SET TO " + targetEPR.toString());
    }

    public String ping(String target) {
        setEpr(target);
        try {
            OMElement pingPayload = CommunicationServicePayload.getPingPayload();
            Options options = new Options();
            options.setTo(targetEPR);
            options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
            options.setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
            ServiceClient sender = new ServiceClient();
            sender.setOptions(options);
            OMElement result = sender.sendReceive(pingPayload);
            String response = result.getFirstElement().getText();
            return "true";
        } catch (AxisFault e) {
            return "false";
        }
    }

    public Integer getCommunicationResourcesListLength(String peer) {
        return new Integer(getCommunicationResources(peer).size());
    }

    public List getCommunicationResources(String peer, int fromIndex, int toIndex) {
        List fullList = getCommunicationResources(peer);
        ArrayList partialList = new ArrayList();
        for (int i = fromIndex; i < toIndex; i++) {
            partialList.add(fullList.get(i));
        }
        return partialList;
    }

    public List getCommunicationResources(String peer) {
        ArrayList list = new ArrayList();
        try {
            OMElement communicationResourcesPayload = CommunicationServicePayload.getCommunicationResourcesPayload();
            Options options = new Options();
            EndpointReference target = new EndpointReference(peer);
            options.setTo(target);
            options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
            options.setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
            ServiceClient sender = new ServiceClient();
            sender.setOptions(options);
            OMElement root = sender.sendReceive(communicationResourcesPayload);
            Iterator children = root.getChildren();
            while (children.hasNext()) {
                OMElement node = (OMElement) children.next();
                CommunicationResource rsrc = new CommunicationResource();
                Iterator ats = node.getAllAttributes();
                rsrc.setId(Long.valueOf(((OMAttributeImpl) ats.next()).getAttributeValue()));
                rsrc.setLocalId(((OMAttributeImpl) ats.next()).getAttributeValue());
                rsrc.setTitle(((OMAttributeImpl) ats.next()).getAttributeValue());
                rsrc.setType(((OMAttributeImpl) ats.next()).getAttributeValue());
                rsrc.setDigest(((OMAttributeImpl) ats.next()).getAttributeValue());
                rsrc.setHost(((OMAttributeImpl) ats.next()).getAttributeValue());
                rsrc.setHostLabel(((OMAttributeImpl) ats.next()).getAttributeValue());
                rsrc.setOGroup(((OMAttributeImpl) ats.next()).getAttributeValue());
                rsrc.setPrivilege(((OMAttributeImpl) ats.next()).getAttributeValue());
                list.add(rsrc);
            }
        } catch (AxisFault e) {
            System.out.println("********** CommunicationServiceImpl " + e.getMessage());
        }
        return list;
    }

    public List getActivePeerList(String fenixServicesUrl) {
        List peers = getPeerList(fenixServicesUrl);
        ArrayList activePeerList = new ArrayList();
        for (int i = 0; i < peers.size(); i++) {
            CommunicationResource rsrc = (CommunicationResource) peers.get(i);
            if (Boolean.parseBoolean(ping(rsrc.getHost()))) activePeerList.add(rsrc);
        }
        return activePeerList;
    }

    public List getActiveGroupPeerList(String fenixServicesUrl, String group) {
        List peers = getPeerList(fenixServicesUrl);
        ArrayList activePeerList = new ArrayList();
        for (int i = 0; i < peers.size(); i++) {
            CommunicationResource rsrc = (CommunicationResource) peers.get(i);
            if (rsrc.getOGroup().equals(group)) if (Boolean.parseBoolean(ping(rsrc.getHost()))) activePeerList.add(rsrc);
        }
        return activePeerList;
    }

    public List getPeerList(String fenixServicesUrl) {
        ArrayList list = new ArrayList();
        setEpr(fenixServicesUrl);
        try {
            OMElement peerListPayload = CommunicationServicePayload.getPeerListPayload();
            Options options = new Options();
            options.setTo(targetEPR);
            options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
            options.setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
            ServiceClient sender = new ServiceClient();
            sender.setOptions(options);
            OMElement root = sender.sendReceive(peerListPayload);
            Iterator children = root.getChildren();
            while (children.hasNext()) {
                OMElement node = (OMElement) children.next();
                CommunicationResource rsrc = new CommunicationResource();
                Iterator ats = node.getAllAttributes();
                rsrc.setHost(((OMAttributeImpl) ats.next()).getAttributeValue());
                rsrc.setHostLabel(((OMAttributeImpl) ats.next()).getAttributeValue());
                list.add(rsrc);
            }
        } catch (AxisFault e) {
            e.printStackTrace();
        }
        return list;
    }

    public String saveCommunicationResource(String fenixServicesUrl, CommunicationResource rsrc) {
        setEpr(fenixServicesUrl);
        try {
            OMElement saveCommunicationResource = CommunicationServicePayload.getSaveCommunicationResourcePayload(rsrc);
            Options options = new Options();
            options.setTo(targetEPR);
            options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
            options.setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
            ServiceClient sender = new ServiceClient();
            sender.setOptions(options);
            OMElement result = sender.sendReceive(saveCommunicationResource);
            return result.getFirstElement().getText();
        } catch (AxisFault e) {
            System.out.println("********** AXIS FAULT @ saveCommunicationResource(resource)");
            System.out.println("********** AXIS FAULT " + e.getMessage());
        }
        return "Share resource failed. Please try again.";
    }

    public String shaDigest(String fenixServicesUrl, CommunicationResource rsrc) {
        setEpr(fenixServicesUrl);
        try {
            OMElement saveCommunicationResource = CommunicationServicePayload.getShaDigestPayload(rsrc);
            Options options = new Options();
            options.setTo(targetEPR);
            options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
            options.setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
            ServiceClient sender = new ServiceClient();
            sender.setOptions(options);
            OMElement result = sender.sendReceive(saveCommunicationResource);
            return result.getFirstElement().getText();
        } catch (AxisFault e) {
            System.out.println("********** AXIS FAULT @ shaDigest(resource)");
            System.out.println("********** AXIS FAULT " + e.getMessage());
        }
        return "Share resource failed. Please try again.";
    }

    public String pushInformation(String fenixServicesUrl, CommunicationResource rsrc) {
        List peers = getActivePeerList(fenixServicesUrl);
        System.out.println("PUSHING INFO TO " + peers.size() + " ACTIVE PEERS");
        for (int i = 0; i < peers.size(); i++) {
            String target = ((CommunicationResource) peers.get(i)).getHost();
            System.out.println("TARGET " + i + ": " + target);
            try {
                OMElement pushInfo = CommunicationServicePayload.getPushInfoPayload(rsrc);
                Options options = new Options();
                options.setTo(new EndpointReference(target));
                options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
                options.setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
                ServiceClient sender = new ServiceClient();
                sender.setOptions(options);
                OMElement result = sender.sendReceive(pushInfo);
                return result.getFirstElement().getText();
            } catch (AxisFault e) {
                e.printStackTrace();
            }
        }
        return "Share resource failed. Please try again.";
    }

    public void pushBulkInformation(String fenixServicesUrl, List peers, List resources) {
        for (int i = 0; i < peers.size(); i++) {
            String target = ((CommunicationResource) peers.get(i)).getHost();
            for (int j = 0; j < resources.size(); j++) {
                CommunicationResource rsrc = (CommunicationResource) resources.get(j);
                try {
                    OMElement pushInfo = CommunicationServicePayload.getPushInfoPayload(rsrc);
                    Options options = new Options();
                    options.setTo(new EndpointReference(target));
                    options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
                    options.setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
                    ServiceClient sender = new ServiceClient();
                    sender.setOptions(options);
                    OMElement result = sender.sendReceive(pushInfo);
                } catch (AxisFault e) {
                    e.printStackTrace();
                }
            }
        }
        Logger.getRootLogger().warn("Push Bulk Informatino Finished.");
    }

    public String findServicesUrl() {
        return urlFinder.fenixServicesUrl;
    }

    public void downloadResource(String fenixServicesUrl, String id) {
        System.out.println("GWT - SYSTEM ENTERED DOWNLOAD RESOURCE");
        setEpr(fenixServicesUrl);
        try {
            Options options = new Options();
            options.setTo(targetEPR);
            options.setAction("urn:downloadResourceServer");
            options.setSoapVersionURI(SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            options.setProperty(Constants.Configuration.CACHE_ATTACHMENTS, Constants.VALUE_TRUE);
            options.setProperty(Constants.Configuration.ATTACHMENT_TEMP_DIR, "temp_dir");
            options.setProperty(Constants.Configuration.FILE_SIZE_THRESHOLD, "4000");
            options.setTimeOutInMilliSeconds(10000);
            ServiceClient sender = new ServiceClient();
            sender.setOptions(options);
            OperationClient mepClient = sender.createClient(ServiceClient.ANON_OUT_IN_OP);
            MessageContext mc = new MessageContext();
            SOAPEnvelope env = createEnvelope(id);
            mc.setEnvelope(env);
            mepClient.addMessageContext(mc);
            mepClient.execute(true);
            MessageContext response = mepClient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            SOAPBody body = response.getEnvelope().getBody();
            OMElement element = body.getFirstChildWithName(new QName(NAMESPACE, "downloadResourceServerResponse"));
            if (element != null) {
                try {
                    processResponse(response, element);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                throw new Exception("Malformed response.");
            }
        } catch (AxisFault e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("GWT - SYSTEM DOWNLOADED RESOURCE");
    }

    private void processResponse(MessageContext response, OMElement element) throws Exception {
        OMElement resourceElement = element.getFirstChildWithName(new QName(NAMESPACE, "resource"));
        String resourceElementID = resourceElement.getAttributeValue(new QName("href"));
        resourceElementID = resourceElementID.substring(4);
        DataHandler dataHandler = response.getAttachment(resourceElementID);
        if (dataHandler != null) {
            File resourceFile = new File(System.getProperty("java.io.tmpdir") + File.separator + element.getFirstChildWithName(new QName(NAMESPACE, "localId")).getText() + ".zip");
            FileOutputStream outputStream = new FileOutputStream(resourceFile);
            dataHandler.writeTo(outputStream);
            outputStream.flush();
        } else {
            throw new Exception("Cannot find the data handler.");
        }
    }

    private static SOAPEnvelope createEnvelope(String destinationFile) {
        SOAPFactory fac = OMAbstractFactory.getSOAP11Factory();
        SOAPEnvelope env = fac.getDefaultEnvelope();
        OMNamespace omNs = fac.createOMNamespace(NAMESPACE, "swa");
        OMElement statsElement = fac.createOMElement("downloadResourceServer", omNs);
        OMElement nameEle = fac.createOMElement("projectName", omNs);
        nameEle.setText(destinationFile);
        statsElement.addChild(nameEle);
        env.getBody().addChild(statsElement);
        return env;
    }

    public void importDataset(String localResourceId) {
        try {
            OMElement pushInfo = CommunicationServicePayload.getImportDatasetPayload(localResourceId);
            Options options = new Options();
            options.setTo(new EndpointReference(findServicesUrl()));
            options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
            options.setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
            ServiceClient sender = new ServiceClient();
            sender.setOptions(options);
            OMElement result = sender.sendReceive(pushInfo);
            System.out.println("IMPORT DATASET RESPONSE: " + result.getFirstElement().getText());
        } catch (AxisFault e) {
            System.out.println("AXIS FAULT - GWT IMPORT DATASET: " + e.getMessage());
        }
    }

    public void setUrlFinder(UrlFinder urlFinder) {
        this.urlFinder = urlFinder;
    }
}
