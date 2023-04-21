package org.monet.fms.control.actions;

import java.net.URLEncoder;
import java.util.HashMap;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.monet.fms.control.constants.Actions;
import org.monet.fms.control.constants.Parameter;
import org.monet.fms.core.constants.ErrorCode;
import org.monet.kernel.components.ComponentDocuments;
import org.monet.kernel.components.ComponentPersistence;
import org.monet.kernel.components.ComponentSecurity;
import org.monet.kernel.exceptions.DataException;
import org.monet.kernel.exceptions.NodeAccessException;
import org.monet.kernel.exceptions.SystemException;
import org.monet.kernel.library.LibraryFile;
import org.monet.kernel.model.Node;

public class ActionDownloadDocument extends Action {

    private ComponentPersistence oComponentPersistence;

    private ComponentSecurity oComponentSecurity;

    private ComponentDocuments oComponentDocuments;

    public ActionDownloadDocument() {
        super();
        this.oComponentPersistence = ComponentPersistence.getInstance();
        this.oComponentSecurity = ComponentSecurity.getInstance();
        this.oComponentDocuments = ComponentDocuments.getInstance();
    }

    @Override
    public String execute() {
        String idNode = this.oRequest.getParameter(Parameter.ID_NODE);
        Node oNode;
        HttpClient oHttpClient = new HttpClient();
        GetMethod oMethod;
        Integer iStatus;
        Boolean bDownload;
        byte[] oOutput;
        if (!this.oComponentAccountsManager.isLogged()) {
            return ErrorCode.USER_NOT_LOGGED;
        }
        if (idNode == null) {
            throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.DOWNLOAD_DOCUMENT);
        }
        oNode = this.oComponentPersistence.loadNode(idNode);
        if (!this.oComponentSecurity.canWrite(oNode, this.getAccount())) {
            throw new NodeAccessException(org.monet.kernel.constants.ErrorCode.READ_NODE_PERMISSIONS, idNode);
        }
        bDownload = this.oComponentDocuments.getSupportedFeatures().get(ComponentDocuments.Feature.DOWNLOAD);
        if ((bDownload == null) || (!bDownload)) {
            throw new SystemException(ErrorCode.DOWNLOAD_NOT_SUPPORTED, idNode);
        }
        try {
            HashMap<String, String> hmParameters = new HashMap<String, String>();
            String fileId = this.oRequest.getParameter(Parameter.FILENAME);
            hmParameters.put(Parameter.ID, URLEncoder.encode(fileId, "UTF-8"));
            oMethod = new GetMethod(this.oComponentDocuments.getDownloadUrl(hmParameters));
            iStatus = oHttpClient.executeMethod(oMethod);
            if (iStatus == HttpStatus.SC_NOT_FOUND) throw new SystemException(ErrorCode.DOWNLOAD_NODE_FILE, idNode, null);
            oOutput = oMethod.getResponseBody();
            this.oResponse.setContentLength(oOutput.length);
            this.oResponse.setContentType(oMethod.getResponseHeader("Content-Type").getValue());
            this.oResponse.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(LibraryFile.getFilename(fileId), "UTF-8"));
            this.oResponse.getOutputStream().write(oOutput);
            this.oResponse.getOutputStream().flush();
        } catch (Exception oException) {
            throw new SystemException(ErrorCode.DOWNLOAD_NODE_FILE, idNode, oException);
        }
        return null;
    }
}
