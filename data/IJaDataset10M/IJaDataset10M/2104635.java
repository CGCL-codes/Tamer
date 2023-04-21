package org.salamy;

import java.io.IOException;
import java.util.List;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CasConsumer;
import org.apache.uima.collection.CollectionProcessingManager;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.collection.EntityProcessStatus;
import org.apache.uima.collection.StatusCallbackListener;
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.salamy.salsa.EvaluationSetFilter;

/**
 * Encapsulates the basic UIMA functionality (CollectionReader, {@link CollectionProcessingManager}
 * and {@link CasConsumer}) while forcing a serial processing with its {@link #processSerial()}
 * method.
 * 
 * @author Peter palaga
 */
public class SerialLauncher {

    private class StatusCallbackListenerImpl implements StatusCallbackListener {

        /**
     * Number of entities which have been prosessed so far.
     */
        private int entityCount = 0;

        /**
     * Writes "Aborted" to {@link System#out}.
     * 
     * @see org.apache.uima.collection.processing.StatusCallbackListener#aborted()
     */
        public void aborted() {
            System.out.println("Aborted");
        }

        /**
     * Does nothing in this implementation.
     * 
     * @see org.apache.uima.collection.base_cpm.BaseStatusCallbackListener#batchProcessComplete()
     */
        public void batchProcessComplete() {
        }

        /**
     * Sets #finished to <code>true</code>
     * 
     * @see org.apache.uima.collection.base_cpm.BaseStatusCallbackListener#collectionProcessComplete()
     */
        public void collectionProcessComplete() {
            finished = true;
        }

        /**
     * Writes the exceptions to {@link System#out} and increments #entityCount.
     * 
     * @param aCas
     *          CAS corresponding to the completed processing
     * @param aStatus
     *          EntityProcessStatus that holds the status of all the events for aEntity
     */
        public void entityProcessComplete(CAS aCas, EntityProcessStatus aStatus) {
            if (aStatus.isException()) {
                List<?> exceptions = aStatus.getExceptions();
                for (int i = 0; i < exceptions.size(); i++) {
                    ((Throwable) exceptions.get(i)).printStackTrace();
                }
                return;
            }
            entityCount++;
        }

        /**
     * Does nothing in this implementation.
     * 
     * @see org.apache.uima.collection.processing.StatusCallbackListener#initializationComplete()
     */
        public void initializationComplete() {
        }

        /**
     * Writes "Paused" to {@link System#out}.
     * 
     * @see org.apache.uima.collection.processing.StatusCallbackListener#paused()
     */
        public void paused() {
            System.out.println("Paused");
        }

        /**
     * Writes "Resumed" to {@link System#out}.
     * 
     * @see org.apache.uima.collection.processing.StatusCallbackListener#resumed()
     */
        public void resumed() {
            System.out.println("Resumed");
        }
    }

    /**
   * The Collection Processing Manager instance that coordinates the processing.
   */
    private CollectionProcessingManager collectionProcessingManager;

    /**
   * The collection reader.
   */
    private CollectionReader collectionReader;

    /**
   * The state of the application. Initially <code>false</code>; setting to <code>true</code>
   * signals the application that it should exit.
   */
    private boolean finished = false;

    /**
   * Creates a new instance using the named XML descriptors.
   * 
   * @param readerDecriptionPath
   *          a file system path or URL to an XML descriptor of an {@link CollectionReader}.
   * @param analysisEngineDecriptionPath
   *          a file system path or URL to an XML descriptor of an {@link AnalysisEngine}.
   * @param casConsumerDecriptionPath
   *          a file system path or URL to an XML descriptor of a {@link CasConsumer}.
   * @param filter
   *          filter telling which sentences should be used for training and which for evaluation.
   * @throws InvalidXMLException
   * @throws IOException
   * @throws ResourceInitializationException
   * @throws ResourceConfigurationException
   */
    public SerialLauncher(String readerDecriptionPath, String analysisEngineDecriptionPath, String casConsumerDecriptionPath, EvaluationSetFilter filter) throws InvalidXMLException, IOException, ResourceInitializationException, ResourceConfigurationException {
        super();
        ResourceSpecifier colReaderSpecifier = UIMAFramework.getXMLParser().parseCollectionReaderDescription(new XMLInputSource(readerDecriptionPath));
        collectionReader = UIMAFramework.produceCollectionReader(colReaderSpecifier);
        ResourceSpecifier aeSpecifier = UIMAFramework.getXMLParser().parseResourceSpecifier(new XMLInputSource(analysisEngineDecriptionPath));
        AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(aeSpecifier);
        ResourceSpecifier consumerSpecifier = UIMAFramework.getXMLParser().parseCasConsumerDescription(new XMLInputSource(casConsumerDecriptionPath));
        CasConsumer casConsumer = UIMAFramework.produceCasConsumer(consumerSpecifier);
        if (filter != null) {
            collectionReader.getUimaContext().getSession().put(SalamyConstants.KEY_TRAINING_SET_FILTER, filter);
            ae.getUimaContext().getSession().put(SalamyConstants.KEY_TRAINING_SET_FILTER, filter);
            casConsumer.getUimaContext().getSession().put(SalamyConstants.KEY_TRAINING_SET_FILTER, filter);
        }
        collectionProcessingManager = UIMAFramework.newCollectionProcessingManager();
        collectionProcessingManager.setAnalysisEngine(ae);
        collectionProcessingManager.addCasConsumer(casConsumer);
        collectionProcessingManager.addStatusCallbackListener(new StatusCallbackListenerImpl());
        collectionProcessingManager.setPauseOnException(false);
        collectionProcessingManager.setSerialProcessingRequired(true);
    }

    /**
   * Starts the processing and does not exit before {@link #finished} is set to <code>true</code>.
   * 
   * @throws ResourceInitializationException
   */
    public void processSerial() throws ResourceInitializationException {
        collectionProcessingManager.process(collectionReader);
        while (!finished) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
