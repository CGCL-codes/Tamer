package org.sourcejammer.client;

import org.sourcejammer.project.view.*;
import org.sourcejammer.util.ConfigurationException;
import java.io.*;
import java.util.*;
import java.net.*;
import org.w3c.dom.*;
import org.apache.soap.util.xml.*;
import org.apache.soap.*;
import org.apache.soap.encoding.*;
import org.apache.soap.encoding.soapenc.*;
import org.apache.soap.rpc.*;
import org.sourcejammer.util.SJDate;
import org.sourcejammer.util.SourceJammerConnectionException;
import org.sourcejammer.project.controller.LabelVersionMappingBean;

public class SOAPPortal {

    public static final class MCPMethodNames {

        public static final String ADD_ARCHIVE = "addArchive";

        public static final String ADD_FILE = "addFile";

        public static final String ADD_PROJECT = "addProject";

        public static final String ADD_USER = "addUser";

        public static final String CHECK_IN_FILE = "checkInFile";

        public static final String CHECK_OUT_FILE = "checkOutFile";

        public static final String CONNECT = "connect";

        public static final String DISCONNECT = "disconnect";

        public static final String GET_FILE_INFO = "getFileInfo";

        public static final String GET_FILE_LATEST_VERSION = "getFileLatestVersion";

        public static final String GET_FILE_VERSION = "getFileVersion";

        public static final String GET_PROJECT_INFO = "getProjectInfo";

        public static final String PERMANENTLY_DELETE_NODE = "permanentlyDeleteNode";

        public static final String REMOVE = "remove";

        public static final String RESTORE_REMOVED_NODE = "restoreRemovedNode";

        public static final String ROLLBACK_TO_VERSION = "rollbackToVersions";

        public static final String UNDO_CHECKOUT = "undoCheckOut";

        public static final String VIEW_REMOVED_NODES = "viewRemovedNodes";

        public static final String VIEW_VERSION_COMMENT = "viewVersionComment";

        public static final String LOG_IN = "login";

        public static final String CHANGE_PASSWORD = "changePassword";

        public static final String MAKE_LABEL = "makeLabeledVersion";

        public static final String GET_LABEL = "getLabelContentList";

        public static final String LABEL_LIST = "getLabelList";
    }

    private SOAPMappingRegistry moRegistry = null;

    private URL mUrl = null;

    public SOAPPortal() {
        moRegistry = new SOAPMappingRegistry();
        BeanSerializer oSerializer = new BeanSerializer();
        moRegistry.mapTypes(Constants.NS_URI_SOAP_ENC, new QName("urn:sj-mastercontrolprogram", "request"), SJRequest.class, oSerializer, oSerializer);
        moRegistry.mapTypes(Constants.NS_URI_SOAP_ENC, new QName("urn:sj-mastercontrolprogram", "response"), SJResponse.class, oSerializer, oSerializer);
        moRegistry.mapTypes(Constants.NS_URI_SOAP_ENC, new QName("urn:sj-mastercontrolprogram", "file"), org.sourcejammer.project.view.File.class, oSerializer, oSerializer);
        moRegistry.mapTypes(Constants.NS_URI_SOAP_ENC, new QName("urn:sj-mastercontrolprogram", "Project"), Project.class, oSerializer, oSerializer);
        moRegistry.mapTypes(Constants.NS_URI_SOAP_ENC, new QName("urn:sj-mastercontrolprogram", "nodeinfo"), NodeInfo.class, oSerializer, oSerializer);
        moRegistry.mapTypes(Constants.NS_URI_SOAP_ENC, new QName("urn:sj-mastercontrolprogram", "nodename"), NodeName.class, oSerializer, oSerializer);
        moRegistry.mapTypes(Constants.NS_URI_SOAP_ENC, new QName("urn:sj-mastercontrolprogram", "sjdate"), SJDate.class, oSerializer, oSerializer);
        moRegistry.mapTypes(Constants.NS_URI_SOAP_ENC, new QName("urn:sj-mastercontrolprogram", "labelversionmappingbean"), LabelVersionMappingBean.class, oSerializer, oSerializer);
        moRegistry.mapTypes(Constants.NS_URI_SOAP_ENC, new QName("urn:sj-mastercontrolprogram", "label"), Label.class, oSerializer, oSerializer);
    }

    public void setURL(String s) throws MalformedURLException {
        mUrl = new URL(s);
    }

    public void setURL(URL url) {
        mUrl = url;
    }

    public SJResponse sendRequest(SJRequest request, String sMethod) throws SourceJammerConnectionException {
        if (mUrl == null) {
            throw new ConfigurationException("URL property must be set before a request can be sent.");
        }
        Call call = new Call();
        call.setSOAPMappingRegistry(moRegistry);
        call.setTargetObjectURI("urn:MasterControlProgram");
        call.setMethodName(sMethod);
        call.setEncodingStyleURI(Constants.NS_URI_SOAP_ENC);
        Vector params = new Vector();
        params.addElement(new Parameter("request", SJRequest.class, request, null));
        call.setParams(params);
        Response resp;
        try {
            resp = call.invoke(mUrl, "");
        } catch (SOAPException ex) {
            throw new SourceJammerConnectionException("Problem with SOAP communication.", ex);
        }
        SJResponse response = null;
        if (!resp.generatedFault()) {
            Parameter ret = resp.getReturnValue();
            response = (SJResponse) ret.getValue();
        } else {
            Fault fault = resp.getFault();
            StringBuffer strException = new StringBuffer();
            strException.append("A fault was generated:\r\n").append("  Fault Code   = ").append(fault.getFaultCode()).append("\r\n").append("  Fault String = ").append(fault.getFaultString());
            throw new SourceJammerConnectionException(strException.toString());
        }
        return response;
    }
}
