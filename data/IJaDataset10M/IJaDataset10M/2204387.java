package edu.pitt.dbmi.odie.gapp.gwt.server.navigator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import com.allen_sauer.gwt.log.client.Log;
import edu.pitt.dbmi.odie.gapp.gwt.client.dao.ODIE_DaoAnalysis;
import edu.pitt.dbmi.odie.gapp.gwt.client.dao.ODIE_DaoAnnotation;
import edu.pitt.dbmi.odie.gapp.gwt.client.dao.ODIE_DaoDocument;
import edu.pitt.dbmi.odie.gapp.gwt.model.ODIE_Analysis;
import edu.pitt.dbmi.odie.gapp.gwt.model.ODIE_Annotation;
import edu.pitt.dbmi.odie.gapp.gwt.model.ODIE_Cls;
import edu.pitt.dbmi.odie.gapp.gwt.model.ODIE_Document;
import edu.pitt.dbmi.odie.gapp.gwt.model.ODIE_PMF;
import edu.pitt.dbmi.odie.gapp.gwt.model.ODIE_User;
import edu.pitt.dbmi.odie.gapp.gwt.server.user.ODIE_ServerSideLoginInfo;

public class ODIE_SelectionCacher {

    private static final String CONST_SELECTION_CACHER = "odieSelectionCacher";

    private PersistenceManager persistenceManager = null;

    private ODIE_User currentUser;

    private ODIE_Analysis currentAnalysis;

    private ODIE_Document currentDocument;

    private ODIE_Annotation currentAnnotation;

    private ODIE_Cls currentCls;

    private ODIE_ServerSideLoginInfo loginInfo;

    private static final Comparator<ODIE_Annotation> overlapComparator = new Comparator<ODIE_Annotation>() {

        public int compare(ODIE_Annotation o1, ODIE_Annotation o2) {
            int ret = (int) (o1.getSPos() - o2.getSPos());
            if ((o1.getSPos() <= o2.getSPos() && o1.getEPos() >= o2.getSPos()) || (o1.getEPos() >= o2.getSPos() && o1.getEPos() <= o2.getEPos())) {
                ret = 0;
            }
            return ret;
        }
    };

    public static synchronized ODIE_SelectionCacher fetchOrCreateOdieSelectionCacher(ServletConfig config) {
        ODIE_SelectionCacher odieSelectionCacher = fetchOdieSelectionCacher(config);
        if (odieSelectionCacher == null) {
            odieSelectionCacher = new ODIE_SelectionCacher();
            cacheOdieSelectionCacher(config, odieSelectionCacher);
        } else {
            Log.debug("fetched existing selection cacher");
        }
        return odieSelectionCacher;
    }

    public static synchronized void releaseOdieSelectionCacher(ServletConfig config) {
        try {
            config.getServletContext().removeAttribute(CONST_SELECTION_CACHER);
            Log.debug("released odieSelectionCacher");
        } catch (NullPointerException x) {
            Log.warn("Failed with null servletContext.");
        }
    }

    private static void cacheOdieSelectionCacher(ServletConfig config, ODIE_SelectionCacher odieSelectionCacher) {
        try {
            config.getServletContext().setAttribute(CONST_SELECTION_CACHER, odieSelectionCacher);
            Log.debug("cached new odieSelectionCacher");
        } catch (NullPointerException x) {
            Log.warn("Failed with null servletContext.");
        }
    }

    public static ODIE_SelectionCacher fetchOdieSelectionCacher(ServletConfig config) {
        ODIE_SelectionCacher odieSelectionCacher = null;
        try {
            ServletContext context = config.getServletContext();
            odieSelectionCacher = (ODIE_SelectionCacher) context.getAttribute(CONST_SELECTION_CACHER);
        } catch (NullPointerException x) {
            x.printStackTrace();
        }
        return odieSelectionCacher;
    }

    private ODIE_SelectionCacher() {
        this.persistenceManager = ODIE_PMF.get().getPersistenceManager();
    }

    public void finalize() {
        try {
            if (this.persistenceManager != null && !this.persistenceManager.isClosed()) {
                this.persistenceManager.close();
            }
            super.finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public ODIE_User fetchOrCreateUser(String userEmail, String nickname) {
        if (this.currentUser == null || !this.currentUser.getEmailAddress().equals(userEmail)) {
            this.currentUser = fetchUser(userEmail);
            if (this.currentUser == null) {
                this.currentUser = new ODIE_User();
                this.currentUser.setEmailAddress(userEmail);
                this.currentUser.setNickname(nickname);
                persistenceManager.makePersistent(this.currentUser);
                this.currentUser = fetchUser(userEmail);
            }
            selectFirstAnalysis();
        }
        return this.currentUser;
    }

    @SuppressWarnings("unchecked")
    public ODIE_User fetchUser(String userEmail) {
        ODIE_User result = null;
        try {
            Query query = persistenceManager.newQuery(ODIE_User.class, "emailAddress == emailAddressParam");
            query.declareParameters("String" + " emailAddressParam");
            List<ODIE_User> objs = (List<ODIE_User>) query.execute(userEmail);
            if (!objs.isEmpty()) {
                for (ODIE_User obj : objs) {
                    result = obj;
                    break;
                }
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public String fetchAnalyses() {
        StringBuffer sb = new StringBuffer();
        sb.append("<analyses>");
        try {
            if (this.currentUser != null) {
                Query query = persistenceManager.newQuery(ODIE_Analysis.class, "userId == userIdParam");
                query.declareParameters("String" + " userIdParam");
                List<ODIE_Analysis> objs = (List<ODIE_Analysis>) query.execute(this.currentUser.getOdieId());
                if (!objs.isEmpty()) {
                    for (ODIE_Analysis obj : objs) {
                        sb.append(obj.toXml());
                    }
                }
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        sb.append("</analyses>");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public String fetchDocuments() {
        StringBuffer sb = new StringBuffer();
        sb.append("<documents>");
        if (this.currentAnalysis != null) {
            try {
                Query query = persistenceManager.newQuery(ODIE_Document.class, "odieAnalysisId == odieAnalysisIdParam");
                query.declareParameters("String" + " odieAnalysisIdParam");
                List<ODIE_Document> objs = (List<ODIE_Document>) query.execute(this.currentAnalysis.getOdieId());
                if (!objs.isEmpty()) {
                    for (ODIE_Document obj : objs) {
                        sb.append(obj.toXml());
                    }
                }
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
        sb.append("</documents>");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public ODIE_Document fetchDocumentWithStatus(ODIE_Analysis odieAnalysis, String documentStatus) {
        ODIE_Document odieDocument = null;
        try {
            Query query = persistenceManager.newQuery(ODIE_Document.class, "odieAnalysisId == odieAnalysisIdParam && documentStatus == documentStatusParam");
            query.declareParameters("String" + " odieAnalysisIdParam," + "String" + " documentStatusParam");
            List<ODIE_Document> objs = (List<ODIE_Document>) query.execute(odieAnalysis.getOdieId(), documentStatus);
            if (!objs.isEmpty()) {
                odieDocument = objs.get(0);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return odieDocument;
    }

    @SuppressWarnings("unchecked")
    public List<ODIE_Document> fetchDocumentsForAnalysis(String odieAnalysisId) {
        List<ODIE_Document> result = new ArrayList<ODIE_Document>();
        try {
            Query query = persistenceManager.newQuery(ODIE_Document.class, "odieAnalysisId == odieAnalysisIdParam");
            query.declareParameters("String" + " odieAnalysisIdParam");
            result = (List<ODIE_Document>) query.execute(odieAnalysisId);
        } catch (Exception x) {
            result = new ArrayList<ODIE_Document>();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public ODIE_Analysis fetchSelectedAnalysis() {
        ODIE_Analysis result = null;
        try {
            Query query = persistenceManager.newQuery(ODIE_Analysis.class, "isSelected == isSelectedParam");
            query.declareParameters("boolean isSelectedParam");
            List<ODIE_Analysis> resultObjs = (List<ODIE_Analysis>) query.execute(true);
            if (resultObjs != null && resultObjs.size() > 0) {
                result = this.currentAnalysis = resultObjs.get(0);
            }
        } catch (Exception x) {
            result = null;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public ODIE_Document fetchSelectedDocumentForAnalysis(String odieAnalysisId) {
        ODIE_Document result = null;
        try {
            Query query = persistenceManager.newQuery(ODIE_Document.class, "odieAnalysisId == odieAnalysisIdParam && isSelected == isSelectedParam");
            query.declareParameters("String odieAnalysisIdParam, boolean isSelectedParam");
            List<ODIE_Document> resultObjs = (List<ODIE_Document>) query.execute(odieAnalysisId, true);
            if (resultObjs != null && resultObjs.size() > 0) {
                result = this.currentDocument = resultObjs.get(0);
            }
        } catch (Exception x) {
            result = null;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<ODIE_Annotation> fetchAnnotationsForAnalysis(String odieAnalysisId) {
        List<ODIE_Annotation> result = new ArrayList<ODIE_Annotation>();
        try {
            Query query = persistenceManager.newQuery(ODIE_Annotation.class, "odieAnalysisId == odieAnalysisIdParam");
            query.declareParameters("String" + " odieAnalysisIdParam");
            result = (List<ODIE_Annotation>) query.execute(odieAnalysisId);
        } catch (Exception x) {
            result = new ArrayList<ODIE_Annotation>();
        }
        return result;
    }

    public String fetchClses() {
        StringBuffer sb = new StringBuffer();
        if (this.currentAnalysis == null) {
            fetchAnalysisById("5ad35ef3-b2ff-11de-83f2-2529ca3d792e");
        }
        if (this.currentAnalysis != null) {
            String odieAnalysisId = this.currentAnalysis.getOdieId();
            List<ODIE_Cls> clsList = fetchClsesForAnalysis(odieAnalysisId);
            sb.append("<clses>");
            for (ODIE_Cls obj : clsList) {
                sb.append(obj.toXml());
            }
            sb.append("</clses>");
        }
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public List<ODIE_Cls> fetchClsesForAnalysis(String odieAnalysisId) {
        List<ODIE_Cls> result = new ArrayList<ODIE_Cls>();
        try {
            Log.debug("fetachClsesForAnalysis " + odieAnalysisId);
            Query query = persistenceManager.newQuery(ODIE_Cls.class, "odieAnalysisId == odieAnalysisIdParam");
            query.declareParameters("String" + " odieAnalysisIdParam");
            result = (List<ODIE_Cls>) query.execute(odieAnalysisId);
        } catch (Exception x) {
            x.printStackTrace();
            result = new ArrayList<ODIE_Cls>();
        }
        Log.debug("Returning a result of " + result.size() + " records ");
        return result;
    }

    @SuppressWarnings("unchecked")
    public ODIE_Cls fetchClsById(String odieId) {
        ODIE_Cls result = null;
        try {
            Query query = persistenceManager.newQuery(ODIE_Cls.class, "odieId == odieIdParam");
            query.declareParameters("String" + " odieIdParam");
            List<ODIE_Cls> results = (List<ODIE_Cls>) query.execute(odieId);
            result = results.get(0);
        } catch (Exception x) {
            x.printStackTrace();
            result = null;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public void updateCls(String odieId, String color) {
        ODIE_Cls result = null;
        try {
            Query query = persistenceManager.newQuery(ODIE_Cls.class, "odieId == odieIdParam");
            query.declareParameters("String" + " odieIdParam");
            List<ODIE_Cls> results = (List<ODIE_Cls>) query.execute(odieId);
            result = results.get(0);
            persistenceManager.currentTransaction().begin();
            result.setColorAsString(color);
            persistenceManager.currentTransaction().commit();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public String fetchAnnotations() {
        String result = "<annotations></annotations>";
        if (this.currentDocument != null) {
            result = fetchAnnotationsXmlForDocument(this.currentDocument.getOdieId());
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<ODIE_Annotation> fetchAnnotationsForDocument(String odieDocumentId) {
        List<ODIE_Annotation> result = new ArrayList<ODIE_Annotation>();
        try {
            persistenceManager = ODIE_PMF.get().getPersistenceManager();
            Query query = persistenceManager.newQuery(ODIE_Annotation.class, "odieDocumentId == odieDocumentIdParam");
            query.declareParameters("String" + " odieDocumentIdParam");
            List<ODIE_Annotation> objs = (List<ODIE_Annotation>) query.execute(odieDocumentId);
            if (!objs.isEmpty()) {
                TreeSet<ODIE_Annotation> nonOverlappingAnnotations = new TreeSet<ODIE_Annotation>(overlapComparator);
                nonOverlappingAnnotations.addAll(objs);
                result.addAll(nonOverlappingAnnotations);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public String fetchAnnotationsXmlForDocument(String odieDocumentId) {
        StringBuffer sb = new StringBuffer();
        sb.append("<annotations>");
        try {
            persistenceManager = ODIE_PMF.get().getPersistenceManager();
            Query query = persistenceManager.newQuery(ODIE_Annotation.class, "odieDocumentId == odieDocumentIdParam");
            query.declareParameters("String" + " odieDocumentIdParam");
            List<ODIE_Annotation> objs = (List<ODIE_Annotation>) query.execute(odieDocumentId);
            if (!objs.isEmpty()) {
                TreeSet<ODIE_Annotation> nonOverlappingAnnotations = new TreeSet<ODIE_Annotation>(overlapComparator);
                nonOverlappingAnnotations.addAll(objs);
                for (ODIE_Annotation obj : nonOverlappingAnnotations) {
                    sb.append(obj.toXml());
                }
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        sb.append("</annotations>");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public void selectFirstAnalysis() {
        try {
            String queryString = "select from " + ODIE_Analysis.class.getName();
            Query query = persistenceManager.newQuery(queryString);
            List<ODIE_Analysis> results = (List<ODIE_Analysis>) query.execute();
            if (!results.isEmpty()) {
                this.currentAnalysis = results.get(0);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public ODIE_DaoAnalysis fetchAnalysisDao() {
        ODIE_DaoAnalysis daoAnalysis = null;
        ODIE_Analysis analysis = fetchSelectedAnalysis();
        if (analysis != null) {
            daoAnalysis = analysis.convertToClientSideRecord();
            List<ODIE_Document> persistedDocuments = fetchDocumentsForAnalysis(analysis.getOdieId());
            if (persistedDocuments != null && persistedDocuments.size() > 0) {
                final ODIE_DaoDocument[] daoDocuments = new ODIE_DaoDocument[persistedDocuments.size()];
                int documentIdx = 0;
                for (ODIE_Document document : persistedDocuments) {
                    daoDocuments[documentIdx] = document.convertToClientSideRecord();
                    if (document.isSelected()) {
                        List<ODIE_Annotation> persistedAnnotations = fetchAnnotationsForDocument(document.getOdieId());
                        if (persistedAnnotations != null && persistedAnnotations.size() > 0) {
                            final ODIE_DaoAnnotation[] daoAnnotations = new ODIE_DaoAnnotation[persistedAnnotations.size()];
                            int annotationIdx = 0;
                            for (ODIE_Annotation annotation : persistedAnnotations) {
                                daoAnnotations[annotationIdx] = annotation.convertToClientSideRecord();
                                String odieClsId = annotation.getOdieClsId();
                                ODIE_Cls cls = fetchClsById(odieClsId);
                                daoAnnotations[annotationIdx].setCls(cls.convertToClientSideRecord());
                                annotationIdx++;
                            }
                            daoDocuments[documentIdx].setAnnotations(daoAnnotations);
                        }
                    }
                    documentIdx++;
                }
                daoAnalysis.setDocuments(daoDocuments);
            }
        }
        return daoAnalysis;
    }

    public String fetchAnalysisXml() {
        ODIE_Analysis analysis = fetchSelectedAnalysis();
        StringBuffer sb = new StringBuffer();
        sb.append(analysis.toXml().replaceAll("</analysis>$", ""));
        for (ODIE_Document document : fetchDocumentsForAnalysis(analysis.getOdieId())) {
            if (!document.isSelected()) {
                sb.append(document.toXml());
            } else {
                sb.append(document.toXml().replaceAll("</document>$", ""));
                sb.append(fetchAnnotationsForDocument(document.getOdieId()));
                sb.append("</document>");
            }
        }
        sb.append("</analysis>");
        return sb.toString();
    }

    public String fetchAnalysisXml(String id) {
        ODIE_Analysis analysis = fetchAnalysisById(id);
        StringBuffer sb = new StringBuffer();
        sb.append(analysis.toXml().replaceAll("</analysis>$", ""));
        for (ODIE_Document document : fetchDocumentsForAnalysis(analysis.getOdieId())) {
            if (!document.isSelected()) {
                sb.append(document.toXml());
            } else {
                sb.append(document.toXml().replaceAll("</document>$", ""));
                sb.append(fetchAnnotationsForDocument(document.getOdieId()));
                sb.append("</document>");
            }
        }
        sb.append("<analysis/>");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public ODIE_Analysis fetchAnalysisById(String id) {
        ODIE_Analysis odieAnalysis = null;
        try {
            Query query = persistenceManager.newQuery(ODIE_Analysis.class, "odieId == idParam");
            query.declareParameters("String idParam");
            List<ODIE_Analysis> results = (List<ODIE_Analysis>) query.execute(id);
            if (results == null || results.size() == 0) {
                Log.warn("fetchAnalysisById: Failed to load analysis with odieId ==> " + id);
            } else {
                Log.debug("fetchAnalysisById: Loaded analysis ==> " + id);
                odieAnalysis = results.get(0);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return odieAnalysis;
    }

    @SuppressWarnings("unchecked")
    public void deSelectAnalysis() {
        try {
            persistenceManager = ODIE_PMF.get().getPersistenceManager();
            Query query = persistenceManager.newQuery(ODIE_Analysis.class, "isSelected == isSelectedParam");
            query.declareParameters("boolean isSelectedParam");
            List<ODIE_Analysis> results = (List<ODIE_Analysis>) query.execute(true);
            if (results != null && results.size() > 0) {
                this.currentAnalysis = results.get(0);
                persistenceManager.currentTransaction().begin();
                this.currentAnalysis.setSelected(false);
                persistenceManager.currentTransaction().commit();
                this.currentAnalysis = null;
                this.currentDocument = null;
                this.currentAnnotation = null;
                this.currentCls = null;
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void deSelectDocumentForAnalysis(String odieAnalysisId) {
        try {
            persistenceManager = ODIE_PMF.get().getPersistenceManager();
            Query query = persistenceManager.newQuery(ODIE_Document.class, "odieAnalysisId == odieAnalysisIdParam && isSelected == isSelectedParam");
            query.declareParameters("String odieAnalysisIdParam, boolean isSelectedParam");
            List<ODIE_Document> results = (List<ODIE_Document>) query.execute(odieAnalysisId, true);
            if (results != null && results.size() > 0) {
                this.currentDocument = results.get(0);
                persistenceManager.currentTransaction().begin();
                this.currentDocument.setSelected(false);
                persistenceManager.currentTransaction().commit();
                this.currentDocument = null;
                this.currentAnnotation = null;
                this.currentCls = null;
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void deSelectAnnotationForDocument(String odieAnalysisId, String odieDocumentId) {
        try {
            persistenceManager = ODIE_PMF.get().getPersistenceManager();
            Query query = persistenceManager.newQuery(ODIE_Document.class, "odieAnalysisId == odieAnalysisIdParam && odieDocumentId == odieDocumentIdParam && isSelected == isSelectedParam");
            query.declareParameters("String odieAnalysisIdParam, String odieDocumentIdParam, boolean isSelectedParam");
            List<ODIE_Annotation> results = (List<ODIE_Annotation>) query.execute(odieAnalysisId, odieDocumentId, true);
            if (results != null && results.size() > 0) {
                this.currentAnnotation = results.get(0);
                persistenceManager.currentTransaction().begin();
                this.currentAnnotation.setSelected(false);
                persistenceManager.currentTransaction().commit();
                this.currentAnnotation = null;
                this.currentCls = null;
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void selectAnalysisById(String id) {
        try {
            Query query = persistenceManager.newQuery(ODIE_Analysis.class, "odieId == idParam");
            query.declareParameters("String idParam");
            List<ODIE_Analysis> results = (List<ODIE_Analysis>) query.execute(id);
            if (results == null || results.size() == 0) {
                Log.warn("Failed to load analysis with odieId ==> " + id);
            } else {
                Log.debug("Loaded analysis ==> " + id);
                this.currentAnalysis = results.get(0);
                persistenceManager.currentTransaction().begin();
                this.currentAnalysis.setSelected(true);
                persistenceManager.currentTransaction().commit();
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void selectDocumentById(String id) {
        try {
            persistenceManager = ODIE_PMF.get().getPersistenceManager();
            Query query = persistenceManager.newQuery(ODIE_Document.class, "odieId == idParam");
            query.declareParameters("String idParam");
            List<ODIE_Document> results = (List<ODIE_Document>) query.execute(id);
            if (results != null && results.size() > 0) {
                this.currentDocument = results.get(0);
                persistenceManager.currentTransaction().begin();
                this.currentDocument.setSelected(true);
                persistenceManager.currentTransaction().commit();
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void selectAnnotationById(String id) {
        try {
            persistenceManager = ODIE_PMF.get().getPersistenceManager();
            Query query = persistenceManager.newQuery(ODIE_Annotation.class, "odieId == idParam");
            query.declareParameters("String idParam");
            List<ODIE_Annotation> results = (List<ODIE_Annotation>) query.execute(id);
            if (results != null && results.size() > 0) {
                this.currentAnnotation = results.get(0);
                selectClsById(this.currentAnnotation.getOdieClsId());
                persistenceManager.currentTransaction().begin();
                this.currentAnnotation.setSelected(true);
                persistenceManager.currentTransaction().commit();
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void selectClsById(String odieId) {
        try {
            persistenceManager = ODIE_PMF.get().getPersistenceManager();
            Query query = persistenceManager.newQuery(ODIE_Cls.class, "odieId == odieIdParam");
            query.declareParameters("String odieIdParam");
            List<ODIE_Cls> results = (List<ODIE_Cls>) query.execute(odieId);
            this.currentCls = results.get(0);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public void clearSelection() {
        setCurrentAnalysis(null);
        setCurrentDocument(null);
        setCurrentAnnotation(null);
        setCurrentCls(null);
    }

    public PersistenceManager getPersistenceManager() {
        return persistenceManager;
    }

    public ODIE_User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(ODIE_User currentUser) {
        this.currentUser = currentUser;
    }

    public ODIE_Analysis getCurrentAnalysis() {
        return currentAnalysis;
    }

    public void setCurrentAnalysis(ODIE_Analysis currentAnalysis) {
        this.currentAnalysis = currentAnalysis;
    }

    public ODIE_Document getCurrentDocument() {
        return currentDocument;
    }

    public void setCurrentDocument(ODIE_Document currentDocument) {
        this.currentDocument = currentDocument;
    }

    public ODIE_Annotation getCurrentAnnotation() {
        return currentAnnotation;
    }

    public void setCurrentAnnotation(ODIE_Annotation currentAnnotation) {
        this.currentAnnotation = currentAnnotation;
    }

    public ODIE_Cls getCurrentCls() {
        return currentCls;
    }

    public void setCurrentCls(ODIE_Cls currentCls) {
        this.currentCls = currentCls;
    }

    public ODIE_ServerSideLoginInfo getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(ODIE_ServerSideLoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }
}
