package it.webscience.uima.annotators.solr;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import it.webscience.uima.annotations.OcStorageEventAnnotation;
import it.webscience.uima.annotations.UserAnnotation;
import it.webscience.uima.annotations.action.ActionReferenceAnnotation;
import it.webscience.uima.annotations.action.ActionTypeAnnotation;
import it.webscience.uima.annotations.action.EventIdAnnotation;
import it.webscience.uima.annotations.action.SystemAnnotation;
import it.webscience.uima.annotations.eventData.AttachmentAnnotation;
import it.webscience.uima.annotations.eventData.BodyAnnotation;
import it.webscience.uima.annotations.eventData.DateCreationDateAnnotation;
import it.webscience.uima.annotations.eventData.PropertyAnnotation;
import it.webscience.uima.annotations.eventData.TitleAnnotation;
import it.webscience.uima.annotations.eventData.UserAuthorAnnotation;
import it.webscience.uima.annotations.eventData.UserReceiverBccAnnotation;
import it.webscience.uima.annotations.eventData.UserReceiverCcAnnotation;
import it.webscience.uima.annotations.eventData.UserReceiverToAnnotation;
import it.webscience.uima.ocControl.ocStorageUtility.OcAlfrescoServerUtility;
import it.webscience.uima.ocSolrAdapter.solrAdapterAgent.DefaultSolrAdapterAgent;
import org.apache.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import edu.emory.mathcs.backport.java.util.LinkedList;

/**
 * Annotator che si occupa del'indicizzazione su Solr.
 */
public class OcSolrAnnotator extends JCasAnnotator_ImplBase {

    /** logger. */
    private Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * attachments - lista contenente gli attachment associati all'evento che
     * devono essere indicizzati da solr.
     */
    private List<AttachmentAnnotation> attachments;

    /**
     * solrAdapterAgent - istanza della classe DefaultSolrAdapterAgent per
     * l'indicizzazione dell'evento.
     */
    private DefaultSolrAdapterAgent solrAdapterAgent = new DefaultSolrAdapterAgent();

    private OcSolrProperties eventProperties = new OcSolrProperties();

    /**
     * @see AnalysisComponent#initialize(UimaContext)
     * @param aContext
     *            context
     * @throws ResourceInitializationException
     *             exception
     */
    public final void initialize(final UimaContext aContext) throws ResourceInitializationException {
        if (aContext != null) {
            super.initialize(aContext);
        }
    }

    /**
     * @see JCasAnnotator_ImplBase#process(JCas)
     * @param cas
     *            - CAS da elaborare
     * @throws AnalysisEngineProcessException
     *             exception
     */
    public final void process(final JCas cas) throws AnalysisEngineProcessException {
        if (logger.isDebugEnabled()) {
            logger.debug("Solr annotator - start");
        }
        processEvent(cas);
        try {
            processAttachments(cas);
        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException: " + e.getMessage());
        }
        OcAlfrescoServerUtility.deleteDir(Thread.currentThread().getId());
        if (logger.isDebugEnabled()) {
            logger.debug("Solr annotator - end");
        }
    }

    /**
     * @param cas
     *            - CAS da elaborare
     */
    private void processEvent(final JCas cas) {
        if (logger.isDebugEnabled()) {
            logger.debug("Solr processEvent - start");
        }
        String eventId = getEventIdAnnotation(cas);
        if (eventId != null) {
            eventProperties.setProperty(SolrIndexName.SOLR_EVENT_ID, eventId);
            eventProperties.setProperty(SolrIndexName.SOLR_ID, eventId);
        }
        String actionType = getActionType(cas);
        if (actionType != null) {
            eventProperties.setProperty(SolrIndexName.SOLR_ACTION_TYPE, actionType);
        }
        String actionReference = getActionReference(cas);
        if (actionReference != null) {
            eventProperties.setProperty(SolrIndexName.SOLR_ACTION_REF, actionReference);
        }
        String systemType = getSystemAnnotation(cas)[0];
        if (systemType != null) {
            eventProperties.setProperty(SolrIndexName.SOLR_SYSTEM_TYPE, systemType);
        }
        String systemId = getSystemAnnotation(cas)[1];
        if (systemId != null) {
            eventProperties.setProperty(SolrIndexName.SOLR_SYSTEM_ID, systemId);
        }
        Date creationDate = getCreationDate(cas);
        if (creationDate != null) {
            eventProperties.setProperty(SolrIndexName.SOLR_EVENT_CREATION_DATE, creationDate);
        }
        String title = getTitleAnnotation(cas);
        if (title != null) {
            eventProperties.setProperty(SolrIndexName.SOLR_EVENT_TITLE, title);
        }
        HashMap<String, Object> author = getUserAuthorAnnotation(cas);
        if (!author.isEmpty()) {
            Iterator<String> it = author.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                String value = (String) author.get(key);
                eventProperties.setProperty(key, value);
            }
        }
        getBodyAnnotation(cas);
        getPropertyAnnotation(cas);
        getDetailsAnnotation(cas);
        List<String> attachmentsId = getAttachmentAnnotation(cas);
        if (!attachmentsId.isEmpty()) {
            eventProperties.setProperty(SolrIndexName.SOLR_ATTACHMENT_ID, attachmentsId);
        }
        String alfrescoEventUrl = getOcStorageEventAnnotation(cas);
        if (alfrescoEventUrl != null) {
            eventProperties.setProperty(SolrIndexName.SOLR_EVENT_STORAGE_URL, alfrescoEventUrl);
        }
        solrAdapterAgent.indexEvent();
        if (logger.isDebugEnabled()) {
            logger.debug("Solr processEvent - end");
        }
    }

    private String getOcStorageEventAnnotation(JCas cas) {
        if (logger.isDebugEnabled()) {
            logger.debug("Solr getOcStorageEventAnnotation - start");
        }
        Type annotationType = cas.getTypeSystem().getType(OcStorageEventAnnotation.class.getCanonicalName());
        FSIterator<Annotation> iter = cas.getAnnotationIndex(annotationType).iterator();
        String alfrescoEventUrl = null;
        if (iter.isValid()) {
            FeatureStructure fs = iter.get();
            OcStorageEventAnnotation ann = (OcStorageEventAnnotation) fs;
            alfrescoEventUrl = ann.getEventUrl();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Solr getOcStorageEventAnnotation - end");
        }
        return alfrescoEventUrl;
    }

    private void getPropertyAnnotation(JCas cas) {
        if (logger.isDebugEnabled()) {
            logger.debug("Solr getPropertyAnnotation - start");
        }
        Type annotationType = cas.getTypeSystem().getType(PropertyAnnotation.class.getCanonicalName());
        FSIterator<Annotation> iter = cas.getAnnotationIndex(annotationType).iterator();
        String propertyKey = null;
        String propertyValue = null;
        while (iter.hasNext() && iter.isValid()) {
            FeatureStructure fs = iter.next();
            PropertyAnnotation ann = (PropertyAnnotation) fs;
            propertyKey = ann.getKey();
            propertyValue = ann.getValue();
            if (propertyKey != null && propertyValue != null) {
                eventProperties.setProperty(SolrIndexName.SOLR_PROPERTY + propertyKey, propertyValue);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Solr getPropertyAnnotation - end");
        }
    }

    private void getDetailsAnnotation(JCas cas) {
        if (logger.isDebugEnabled()) {
            logger.debug("Solr getDetailsAnnotation - start");
        }
        Type annotationType = cas.getTypeSystem().getType(UserAnnotation.class.getCanonicalName());
        FSIterator<Annotation> iter = cas.getAnnotationIndex(annotationType).iterator();
        List<String> UserReceiverBcc = new LinkedList();
        List<String> UserReceiverTo = new LinkedList();
        List<String> UserReceiverCc = new LinkedList();
        while (iter.hasNext() && iter.isValid()) {
            FeatureStructure fs = iter.next();
            UserAnnotation ann = (UserAnnotation) fs;
            String email = ann.getEmail();
            if (email != null) {
                if (ann instanceof UserReceiverBccAnnotation) {
                    UserReceiverBcc.add(email);
                }
                if (ann instanceof UserReceiverCcAnnotation) {
                    UserReceiverCc.add(email);
                }
                if (ann instanceof UserReceiverToAnnotation) {
                    UserReceiverTo.add(email);
                }
            }
        }
        if (!UserReceiverBcc.isEmpty()) {
            eventProperties.setProperty(SolrIndexName.SOLR_EVENT_DETAILS_RECEIVERBCC, UserReceiverBcc);
        }
        if (!UserReceiverTo.isEmpty()) {
            eventProperties.setProperty(SolrIndexName.SOLR_EVENT_DETAILS_RECEIVERTO, UserReceiverTo);
        }
        if (!UserReceiverCc.isEmpty()) {
            eventProperties.setProperty(SolrIndexName.SOLR_EVENT_DETAILS_RECEIVERCC, UserReceiverCc);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Solr getDetailsAnnotation - end");
        }
    }

    private String getTitleAnnotation(JCas cas) {
        if (logger.isDebugEnabled()) {
            logger.debug("Solr getTitleAnnotation - start");
        }
        Type annotationType = cas.getTypeSystem().getType(TitleAnnotation.class.getCanonicalName());
        FSIterator<Annotation> iter = cas.getAnnotationIndex(annotationType).iterator();
        String title = null;
        if (iter.isValid()) {
            FeatureStructure fs = iter.get();
            TitleAnnotation ann = (TitleAnnotation) fs;
            title = ann.getValue();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Solr getTitleAnnotation - end");
        }
        return title;
    }

    private Date getCreationDate(JCas cas) {
        if (logger.isDebugEnabled()) {
            logger.debug("Solr getCreationDate - start");
        }
        Type annotationType = cas.getTypeSystem().getType(DateCreationDateAnnotation.class.getCanonicalName());
        FSIterator<Annotation> iter = cas.getAnnotationIndex(annotationType).iterator();
        Date creationdate = null;
        if (iter.isValid()) {
            FeatureStructure fs = iter.get();
            DateCreationDateAnnotation ann = (DateCreationDateAnnotation) fs;
            creationdate = new Date(ann.getTimeInMillis());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Solr getCreationDate - end");
        }
        return creationdate;
    }

    /**
     * @param cas CAS da elaborare. al quale sono associati gli attachment.
     * @throws UnsupportedEncodingException problema con l'encoding dell'url
     */
    private void processAttachments(final JCas cas) throws UnsupportedEncodingException {
        if (logger.isDebugEnabled()) {
            logger.debug("Solr processAttachments - start");
        }
        AnnotationIndex<Annotation> attachmentAnnotation = cas.getAnnotationIndex(AttachmentAnnotation.type);
        AnnotationIndex<Annotation> eventAnnotation = cas.getAnnotationIndex(EventIdAnnotation.type);
        int i = eventAnnotation.size();
        int y = attachmentAnnotation.size();
        logger.debug("cas event annotation size " + i);
        logger.debug("cas attachment annotation size " + y);
        FSIterator<Annotation> itAttachment = attachmentAnnotation.iterator();
        int z = 0;
        logger.debug("z : " + z);
        while (itAttachment.hasNext()) {
            z = z + 1;
            logger.debug("z + : " + z);
            AttachmentAnnotation current = (AttachmentAnnotation) itAttachment.next();
            String idAttachment = current.getId();
            logger.debug("Solr processAttachments " + idAttachment);
            int index = current.getUrlAttachment().lastIndexOf("/");
            String encodedUrl = current.getUrlAttachment().substring(0, index + 1) + URLEncoder.encode(current.getUrlAttachment().substring(index + 1), "UTF-8");
            OcAlfrescoServerUtility alfrescoUtil = new OcAlfrescoServerUtility();
            File file = alfrescoUtil.getEventAttacchment(encodedUrl);
            logger.debug("Solr processAttachments " + file.getName());
            Iterator<AttachmentAnnotation> it = attachments.iterator();
            while (it.hasNext()) {
                AttachmentAnnotation ann = it.next();
                String attachmentId = ann.getId();
                logger.debug("Solr processAttachments while sulle annotations " + attachmentId);
                if (idAttachment.equals(attachmentId)) {
                    String attachmentName = ann.getAttachmentName();
                    String attachmentType = ann.getAttachmentType();
                    String attachmentHashcode = ann.getHashcode();
                    OcSolrProperties attachmentProperties = new OcSolrProperties();
                    attachmentProperties.setProperty(SolrIndexName.SOLR_ID, attachmentHashcode);
                    attachmentProperties.setProperty(SolrIndexName.SOLR_ATTACHMENT_NAME, attachmentName);
                    attachmentProperties.setProperty(SolrIndexName.SOLR_ATTACHMENT_TYPE, attachmentType);
                    attachmentProperties.setProperty(SolrIndexName.SOLR_ATTACHMENT_URL, encodedUrl);
                    logger.debug("Chiamata ad indexAttachment start");
                    solrAdapterAgent.indexAttachment(file);
                    logger.debug("Chiamata ad indexAttachment end");
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Solr processAttachments - end");
        }
    }

    /**
     * @param cas
     *            - CAS da elaborare
     * @return
     */
    private void getBodyAnnotation(final JCas cas) {
        if (logger.isDebugEnabled()) {
            logger.debug("Solr getBodyAnnotation - start");
        }
        Type annotationType = cas.getTypeSystem().getType(BodyAnnotation.class.getCanonicalName());
        FSIterator<Annotation> iter = cas.getAnnotationIndex(annotationType).iterator();
        if (iter.isValid()) {
            FeatureStructure fs = iter.get();
            BodyAnnotation ann = (BodyAnnotation) fs;
            eventProperties.setProperty(SolrIndexName.SOLR_EVENT_BODY, ann.getValue());
            eventProperties.setProperty(SolrIndexName.SOLR_EVENT_BODY_TYPE, ann.getBodyType());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Solr getBodyAnnotation - end");
        }
    }

    /**
     * @param cas
     *            - Cas da elaborare
     * @return authorEmail - indirizzo email associato all'utente che ha
     *         generato l'evento.
     */
    private HashMap<String, Object> getUserAuthorAnnotation(final JCas cas) {
        if (logger.isDebugEnabled()) {
            logger.debug("Solr getUserAuthorAnnotation - start");
        }
        Type annotationType = cas.getTypeSystem().getType(UserAuthorAnnotation.class.getCanonicalName());
        FSIterator<Annotation> iter = cas.getAnnotationIndex(annotationType).iterator();
        HashMap<String, Object> author = new HashMap<String, Object>();
        if (iter.isValid()) {
            FeatureStructure fs = iter.get();
            UserAuthorAnnotation ann = (UserAuthorAnnotation) fs;
            String authorEmail = ann.getEmail();
            if (authorEmail != null) {
                author.put(SolrIndexName.SOLR_AUTHOR_EMAIL, authorEmail);
            }
            String authorName = ann.getName();
            if (authorName != null) {
                author.put(SolrIndexName.SOLR_AUTHOR_NAME, authorName);
            }
            String authorUsername = ann.getUsername();
            if (authorUsername != null) {
                author.put(SolrIndexName.SOLR_AUTHOR_USERNAME, authorUsername);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Solr getUserAuthorAnnotation - end");
        }
        return author;
    }

    /**
     * @param cas
     *            - Cas da elaborare
     * @return attachmentsHashcode - lista contenente le hashcode associate agli
     *         attachment dell'evento che � processato.
     */
    private List<String> getAttachmentAnnotation(final JCas cas) {
        if (logger.isDebugEnabled()) {
            logger.debug("Solr getAttachmentAnnotation - start");
        }
        attachments = new LinkedList();
        Type annotationType = cas.getTypeSystem().getType(AttachmentAnnotation.class.getCanonicalName());
        FSIterator<Annotation> iter = cas.getAnnotationIndex(annotationType).iterator();
        List<String> attachmentsHashcode = new LinkedList();
        while (iter.hasNext() && iter.isValid()) {
            FeatureStructure fs = iter.next();
            AttachmentAnnotation ann = (AttachmentAnnotation) fs;
            String attachmentHashcode = ann.getHashcode();
            attachmentsHashcode.add(attachmentHashcode);
            attachments.add(ann);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Solr getAttachmentAnnotation - end");
        }
        return attachmentsHashcode;
    }

    /**
     * @param cas
     *            - Cas da elaborare
     * @return actionReference - oggetto di tipo string identificativo
     *         dell'actionReference associata all'evento.
     */
    private String getActionReference(final JCas cas) {
        if (logger.isDebugEnabled()) {
            logger.debug("Solr getActionReference - start");
        }
        Type annotationType = cas.getTypeSystem().getType(ActionReferenceAnnotation.class.getCanonicalName());
        FSIterator<Annotation> iter = cas.getAnnotationIndex(annotationType).iterator();
        String actionReference = null;
        if (iter.isValid()) {
            FeatureStructure fs = iter.get();
            ActionReferenceAnnotation ann = (ActionReferenceAnnotation) fs;
            actionReference = ann.getValue();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Solr getActionReference - end");
        }
        return actionReference;
    }

    /**
     * @param cas
     *            - Cas da elaborare
     * @return actionType - riferimento al tipo di azione (communication,
     *         process ecc.)
     */
    private String getActionType(final JCas cas) {
        if (logger.isDebugEnabled()) {
            logger.debug("Solr getActionType - start");
        }
        Type annotationType = cas.getTypeSystem().getType(ActionTypeAnnotation.class.getCanonicalName());
        FSIterator<Annotation> iter = cas.getAnnotationIndex(annotationType).iterator();
        String actionType = null;
        if (iter.isValid()) {
            FeatureStructure fs = iter.get();
            ActionTypeAnnotation ann = (ActionTypeAnnotation) fs;
            actionType = ann.getValue();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Solr getActionType - end");
        }
        return actionType;
    }

    /**
     * @param cas
     *            - Cas da elaborare
     * @return eventId - identificativo univoco dell'evento
     */
    private String getEventIdAnnotation(final JCas cas) {
        if (logger.isDebugEnabled()) {
            logger.debug("Solr getEventIdAnnotation - start");
        }
        Type annotationType = cas.getTypeSystem().getType(EventIdAnnotation.class.getCanonicalName());
        FSIterator<Annotation> iter = cas.getAnnotationIndex(annotationType).iterator();
        String eventId = null;
        if (iter.isValid()) {
            FeatureStructure fs = iter.get();
            EventIdAnnotation ann = (EventIdAnnotation) fs;
            eventId = ann.getValue();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Solr getEventIdAnnotation - end");
        }
        return eventId;
    }

    /**
     * @param cas
     *            - Cas da elaborare
     * @return system - un oggetto di tipo String[] contenente le propriet� del
     *         sistema verticale interessato dall'evento
     */
    private String[] getSystemAnnotation(final JCas cas) {
        if (logger.isDebugEnabled()) {
            logger.debug("Solr getSystemAnnotation - start");
        }
        Type annotationType = cas.getTypeSystem().getType(SystemAnnotation.class.getCanonicalName());
        FSIterator<Annotation> iter = cas.getAnnotationIndex(annotationType).iterator();
        String[] system = new String[2];
        String systemType = null;
        String systemId = null;
        if (iter.isValid()) {
            FeatureStructure fs = iter.get();
            SystemAnnotation ann = (SystemAnnotation) fs;
            systemType = ann.getSystemType();
            system[0] = systemType;
            systemId = ann.getSystemId();
            system[1] = systemId;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Solr getSystemAnnotation - end");
        }
        return system;
    }
}
