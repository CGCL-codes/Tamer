package org.tolven.ws.document;

import java.util.Date;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.WebServiceContext;
import org.tolven.core.AccountDAOLocal;
import org.tolven.core.entity.AccountUser;
import org.tolven.doc.DocumentLocal;
import org.tolven.doc.ProcessLocal;

/**
* Provide document services.
* 
* @author Joseph Isaac
*/
@WebService(name = "Document", serviceName = "DocumentService", targetNamespace = "http://tolven.org/document")
public class DocumentServiceImpl {

    @Resource
    private WebServiceContext wsCtx;

    @EJB
    private AccountDAOLocal accountBean;

    @EJB
    private DocumentLocal documentBean;

    @EJB
    private ProcessLocal processLocal;

    public DocumentServiceImpl() {
    }

    @WebMethod(action = "urn:queueMessage")
    @WebResult
    @RequestWrapper(localName = "queueMessage", targetNamespace = "http://tolven.org/document", className = "org.tolven.ws.document.jaxws.QueueMessageRequest")
    @ResponseWrapper(localName = "queueMessageResponse", className = "org.tolven.ws.document.jaxws.QueueMessageResponse")
    public String queueMessage(@WebParam(name = "payload", targetNamespace = "http://tolven.org/document") byte[] payload, @WebParam(name = "xmlns", targetNamespace = "http://tolven.org/document") String xmlns, @WebParam(name = "accountId", targetNamespace = "http://tolven.org/document") long accountId) {
        String principalName = wsCtx.getUserPrincipal().getName();
        AccountUser accountUser = accountBean.findAccountUser(principalName, accountId);
        if (accountUser == null) {
            throw new RuntimeException(principalName + " not authorized for Account: " + accountId);
        }
        return documentBean.queueWSMessage(payload, xmlns, accountId, accountUser.getUser().getId());
    }

    @WebMethod(action = "urn:test")
    @WebResult(targetNamespace = "http://tolven.org/document")
    @RequestWrapper(localName = "test", targetNamespace = "http://tolven.org/document", className = "org.tolven.ws.document.jaxws.TestRequest")
    @ResponseWrapper(localName = "testResponse", targetNamespace = "http://tolven.org/document", className = "org.tolven.ws.document.jaxws.TestResponse")
    public String test() {
        return documentBean.testWS();
    }

    @WebMethod(action = "urn:processDocument")
    @WebResult
    @RequestWrapper(localName = "processDocument", targetNamespace = "http://tolven.org/document", className = "org.tolven.ws.document.jaxws.ProcessDocumentRequest")
    @ResponseWrapper(localName = "processDocumentResponse", className = "org.tolven.ws.document.jaxws.ProcessDocumentResponse")
    public long processDocument(@WebParam(name = "payload", targetNamespace = "http://tolven.org/document") byte[] payload, @WebParam(name = "xmlns", targetNamespace = "http://tolven.org/document") String xmlns, @WebParam(name = "accountId", targetNamespace = "http://tolven.org/document") long accountId) {
        String principalName = wsCtx.getUserPrincipal().getName();
        AccountUser accountUser = accountBean.findAccountUser(principalName, accountId);
        if (accountUser == null) {
            throw new RuntimeException(principalName + " not authorized for Account: " + accountId);
        }
        return processLocal.processMessage(payload, "text/xml", xmlns, accountId, accountUser.getUser().getId(), new Date());
    }
}
