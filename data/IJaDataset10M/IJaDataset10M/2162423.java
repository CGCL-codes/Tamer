package it.webscience.uima.annotators.storage;

import it.webscience.uima.Singleton;
import it.webscience.uima.CasErrorManager;
import it.webscience.uima.annotations.OcStorageEventAnnotation;
import it.webscience.uima.annotations.eventData.AttachmentAnnotation;
import it.webscience.uima.ocAlfrescoStorage.OcAlfrescoFactory;
import it.webscience.uima.ocStorageAbstract.OcStorageAdapterAgent;
import it.webscience.uima.ocStorageAbstract.OcStorageExitCodes;
import it.webscience.uima.ocStorageAbstract.OcStorageResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.log4j.Logger;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

/**
 * Annotator che si occupa dello storage dell'evento.
 */
public class OcStorageAnnotator extends JCasAnnotator_ImplBase {

    /**interfaccia al factory. */
    private OcStorageFactory ocStorageFactory;

    /** agent per lo storage. */
    private OcStorageAdapterAgent storageAdapterAgent;

    /** logger. */
    private Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * @see AnalysisComponent#initialize(UimaContext)
     * @param aContext context
     * @throws ResourceInitializationException exception
     */
    public final void initialize(final UimaContext aContext) throws ResourceInitializationException {
        if (aContext != null) {
            super.initialize(aContext);
        }
        createOcStorageFactory();
        setStorageAdapterAgent();
    }

    /** imposta il default adapter agent. */
    private void setStorageAdapterAgent() {
        storageAdapterAgent = ocStorageFactory.createStorageAdapterAgent();
    }

    /**
     * @see JCasAnnotator_ImplBase#process(JCas)
     * @param cas cas
     * @throws AnalysisEngineProcessException exception
     */
    public final void process(final JCas cas) throws AnalysisEngineProcessException {
        logger.debug("Start process");
        int ok = OcStorageExitCodes.EXIT_OK;
        try {
            OcStorageResponse ocStoreResponse = storageAdapterAgent.storeEventData(cas);
            if (ocStoreResponse != null && ocStoreResponse.getExitCode() == ok) {
                processCasProperties(cas, ocStoreResponse);
            } else {
                logger.fatal("exit code " + ocStoreResponse.getExitCode());
                logger.fatal("error message: " + ocStoreResponse.getErrorMessage());
                throw new AnalysisEngineProcessException();
            }
        } catch (Exception e) {
            logger.fatal("CAS non elaborata correttamente: ");
            logger.fatal("Salvataggio della cas " + cas.toString() + " su file");
            StringWriter sWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(sWriter));
            logger.fatal(sWriter.getBuffer().toString());
            CasErrorManager cem = new CasErrorManager();
            cem.storeCasInFile(cas);
            throw new AnalysisEngineProcessException();
        }
        logger.debug("End process");
    }

    /** Eleborazione della cas post-chiamata all'agent.
     * @param cas CAS da elaborare
     * @param ocStorageResponse risposta dallo storage
     */
    private void processCasProperties(final JCas cas, final OcStorageResponse ocStorageResponse) {
        String xml = cas.getDocumentText();
        Hashtable<String, String> urlMap = ocStorageResponse.getUrlMap();
        String alfrescoUrl = Singleton.getInstance().getProperty("alfresco-public-url");
        OcStorageEventAnnotation ann = new OcStorageEventAnnotation(cas);
        ann.setBegin(0);
        ann.setEnd(xml.length() - 1);
        ann.setEventNodeRefId(ocStorageResponse.getEventNodeRefId());
        ann.setEventUrl(alfrescoUrl + ocStorageResponse.getEventUrl());
        ann.addToIndexes();
        AnnotationIndex<Annotation> attAnn = cas.getAnnotationIndex(AttachmentAnnotation.type);
        FSIterator<Annotation> itAttachment = attAnn.iterator();
        while (itAttachment.hasNext()) {
            AttachmentAnnotation current = (AttachmentAnnotation) itAttachment.next();
            String url = alfrescoUrl + urlMap.get(current.getId());
            current.setUrlAttachment(url);
        }
    }

    /** costruisce il factory legato a proprieta' esterne. */
    private void createOcStorageFactory() {
        String agent = Singleton.getInstance().getProperty("storage-agent");
        if (agent.equals("Alfresco")) {
            ocStorageFactory = new OcAlfrescoFactory();
        }
    }
}
