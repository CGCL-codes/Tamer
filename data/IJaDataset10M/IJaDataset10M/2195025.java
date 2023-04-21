package com.yubuild.coreman.web.action;

import java.util.HashSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import com.yubuild.coreman.business.DocumentManager;
import com.yubuild.coreman.business.DocumentSetManager;
import com.yubuild.coreman.business.LookupManager;
import com.yubuild.coreman.data.DocumentSet;
import com.yubuild.coreman.data.User;

public class DocumentSetAddToController implements Controller {

    private final Log log;

    private DocumentSetManager documentSetManager;

    private DocumentManager documentManager;

    private LookupManager lookupManager;

    public DocumentSetAddToController() {
        log = LogFactory.getLog(com.yubuild.coreman.web.action.DocumentSetAddToController.class);
    }

    public DocumentSetManager getDocumentSetManager() {
        return documentSetManager;
    }

    public void setDocumentSetManager(DocumentSetManager documentSetManager) {
        this.documentSetManager = documentSetManager;
    }

    public DocumentManager getDocumentManager() {
        return documentManager;
    }

    public void setDocumentManager(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    public LookupManager getLookupManager() {
        return lookupManager;
    }

    public void setLookupManager(LookupManager lookupManager) {
        this.lookupManager = lookupManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) log.debug(getClass().getName() + ".onSubmit started...");
        String id = request.getParameter("documentSetId");
        DocumentSet documentSet = null;
        Long lid = new Long(id);
        documentSet = getDocumentSetManager().getDocumentSet(lid);
        User user = (User) request.getSession().getAttribute("currentUserForm");
        String documentIds[] = request.getParameterValues("documentIds");
        if (documentIds != null) {
            HashSet hashSet = new HashSet();
            int size = documentIds.length;
            com.yubuild.coreman.data.Document doc = null;
            for (int i = 0; i < size; i++) {
                if (log.isDebugEnabled()) log.debug("set=" + documentSet.getName() + ", id koji se dodajte u set:" + documentIds[i]);
                doc = documentManager.getDocument(new Long(documentIds[i]));
                hashSet.add(doc);
            }
            documentSet.getDocuments().addAll(hashSet);
        }
        if (log.isDebugEnabled()) log.debug("before save");
        getDocumentSetManager().saveDocumentSet(documentSet, user.getId());
        if (log.isDebugEnabled()) log.debug("after save");
        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");
        response.getWriter().write("Documents addes successfuly ");
        return null;
    }
}
