package tei.cr.filters;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.xml.sax.SAXException;
import tei.cr.component.graph.IndexedGraph;
import tei.cr.component.matrix.AdjacencyMatrix;
import tei.cr.component.matrix.CooccurrencyMatrix;
import tei.cr.component.matrix.MatrixBuilderException;
import tei.cr.component.matrix.clustering.Hyperlex;
import tei.cr.component.matrix.clustering.HyperlexException;
import tei.cr.component.matrix.variable.Variable;
import tei.cr.filters.ProvidingGraph;
import tei.cr.filters.ProvidingCooccurrencyMatrix;
import tei.cr.pipeline.AbstractSilentBase;
import tei.cr.pipeline.FilterByNames;
import tei.cr.pipeline.WrongArgsException;
import tei.cr.querydoc.FilterArguments;
import tei.cr.teiDocument.TeiDocument;
import tei.cr.teiDocument.vocabulary.TeiElement;
import tei.cr.utils.xml.SAXCallbackWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.ContentHandler;

/**
 * @author Sylvain Loiseau
 */
public final class HyperlexClustering extends AbstractSilentBase implements ProvidingGraph {

    private Level DEFAULT_LOGGING_LEVEL = Level.INFO;

    /** The filter uprise in the pipeline providing a graph */
    private ProvidingCooccurrencyMatrix handler = null;

    /** The "root hub" according to the hyperlex algorithm */
    private Variable[] hub;

    /** the matrix created by hyperlex */
    private AdjacencyMatrix resultingMatrix;

    /** Hyperlex parameter : the root, to be deleted in the graph */
    private String pole;

    /** Hyperlex parameter : the minimum frequency for a variable to be kept. */
    private int variableFreqThreshold;

    /** Hyperlex parameter : the minimum number of cooccurrence between to variables. Beneath this value, the cooccurrence is deleted in the graph. */
    private int cooccurrenceNbrThreshold;

    /** Hyperlex parameter : the minimum number of occurrence in an observation. Beneath this value, the observation is deleted. */
    private int observationSumThreshold;

    /** Hyperlex parameter : maximum accepted inverted association frequency. Under this value, the cooccurrency is deleted. */
    private double maxAssociationFrequencyThreshold;

    /** Hyperlex parameter : test 1 for hub */
    private int requestedNumberOfAdjacentForHub;

    /** Hyperlex parameter : test 2 for hub */
    private double requestedAverageWeightWithSixFirstAdjacentsForHub;

    /** URI frow writing log information of the Hyperlex algorithm. */
    private Level loggingLevel = DEFAULT_LOGGING_LEVEL;

    /** URI frow writing the matrix provided by Hyperlex. */
    private String matrixURI;

    /** URI frow writing the root hubs list provided by Hyperlex. */
    private String hubURI;

    private static String ROOT;

    private static String TEXT;

    private static String FRONT;

    private static String BODY;

    private static final String CR = "\n";

    private Logger log = Logger.getLogger(getClass().getName());

    public void setArguments(FilterArguments fA, FilterByNames nH, TeiDocument doc) throws WrongArgsException {
        String handlerName = fA.getText(FilterArguments.HYPERLEX_HANDLER);
        if (handlerName == null) {
            throw new WrongArgsException("Argument" + FilterArguments.HYPERLEX_HANDLER + " not found");
        }
        ProvidingCooccurrencyMatrix h = (ProvidingCooccurrencyMatrix) nH.get(handlerName);
        if (h == null) {
            throw new WrongArgsException("No filter of name \"" + handlerName + "\"found in the pipeline");
        }
        setProvidingGraphFilter(h);
        String pole;
        int variableFreqThreshold;
        int cooccurrenceNbrThreshold;
        int observationSumThreshold;
        double maxAssociationFrequencyThreshold;
        int requestedNumberOfAdjacentForHub;
        double requestedAverageWeightWithSixFirstAdjacentsForHub;
        pole = fA.getText(FilterArguments.HYPERLEX_PARAM_ROOT);
        try {
            variableFreqThreshold = Integer.parseInt(fA.getText(FilterArguments.HYPERLEX_PARAM_VARIABLE_FREQ));
            cooccurrenceNbrThreshold = Integer.parseInt(fA.getText(FilterArguments.HYPERLEX_PARAM_OCCURRENCE_NBR));
            observationSumThreshold = Integer.parseInt(fA.getText(FilterArguments.HYPERLEX_PARAM_OBSERVATION_SUM));
            maxAssociationFrequencyThreshold = Double.parseDouble(fA.getText(FilterArguments.HYPERLEX_PARAM_MAX_INVERTED_ASSOCIATION));
            requestedNumberOfAdjacentForHub = Integer.parseInt(fA.getText(FilterArguments.HYPERLEX_PARAM_REQUESTED_NUMBER_OF_ADJACENT));
            requestedAverageWeightWithSixFirstAdjacentsForHub = Double.parseDouble(fA.getText(FilterArguments.HYPERLEX_PARAM_REQUESTED_AVERAGE_WEIGHT));
        } catch (NumberFormatException nFE) {
            throw new WrongArgsException("Fail to extract a numeric value: " + nFE.getMessage(), nFE);
        } catch (NullPointerException nFE) {
            throw new WrongArgsException("Fail to extract a numeric value: " + nFE.getMessage(), nFE);
        }
        setPole(pole);
        setVariableFreqThreshold(variableFreqThreshold);
        setCooccurrenceNbrThreshold(cooccurrenceNbrThreshold);
        setObservationSumThreshold(observationSumThreshold);
        setMaxAssociationFrequencyThreshold(maxAssociationFrequencyThreshold);
        setRequestedNumberOfAdjacentForHub(requestedNumberOfAdjacentForHub);
        setRequestedAverageWeightWithSixFirstAdjacentsForHub(requestedAverageWeightWithSixFirstAdjacentsForHub);
        String loggingLevel;
        String matrixURI;
        String hubURI;
        loggingLevel = fA.getText("log/@level");
        if ((loggingLevel != null) && (!loggingLevel.equals(""))) {
            setLoggingLevel(loggingLevel);
        }
        matrixURI = fA.getText(FilterArguments.HYPERLEX_WRITE_MATRIX_URI);
        hubURI = fA.getText(FilterArguments.HYPERLEX_WRITE_HUBS_URI);
        setMatrixURI(matrixURI);
        setHubURI(hubURI);
        ROOT = doc.getTeiElementLocalName(TeiElement.TEI);
        TEXT = doc.getTeiElementLocalName(TeiElement.TEXT);
        FRONT = doc.getTeiElementLocalName(TeiElement.FRONT);
        BODY = doc.getTeiElementLocalName(TeiElement.BODY);
    }

    public void setLoggingLevel(String loggingLevel) throws WrongArgsException {
        try {
            this.loggingLevel = Level.parse(loggingLevel);
        } catch (IllegalArgumentException iaE) {
            String msg = "Illegal value as log level in the Query doc: legal values are ALL, CONFIG, FINE, " + "FINER, FINEST, INFO, OFF, SEVERE or WARNING. Found: " + loggingLevel;
            throw new WrongArgsException(msg, iaE);
        }
    }

    public void setMatrixURI(String matrixURI) {
        this.matrixURI = matrixURI;
    }

    public void setHubURI(String hubURI) {
        this.hubURI = hubURI;
    }

    public void setProvidingGraphFilter(ProvidingCooccurrencyMatrix f) throws WrongArgsException {
        if (f == null) {
            throw new WrongArgsException("The providingGraphFilter should not be null.");
        }
        this.handler = f;
    }

    public void setPole(String pole) throws WrongArgsException {
        if ((pole == null) || (pole.equals(""))) {
            log.warning("No pole provided.");
        }
        this.pole = pole;
    }

    public void setMaxAssociationFrequencyThreshold(double maxAssociationFrequencyThreshold) {
        this.maxAssociationFrequencyThreshold = maxAssociationFrequencyThreshold;
    }

    public void setVariableFreqThreshold(int variableFreqThreshold) throws WrongArgsException {
        if (variableFreqThreshold <= 0) {
            throw new WrongArgsException("The variable sum threshold may not be less than or equal to zero");
        }
        this.variableFreqThreshold = variableFreqThreshold;
    }

    public void setCooccurrenceNbrThreshold(int cooccurrenceNbrThreshold) throws WrongArgsException {
        if (cooccurrenceNbrThreshold <= 0) {
            throw new WrongArgsException("The cooccurrence sum threshold may not be less than or equal to zero");
        }
        this.cooccurrenceNbrThreshold = cooccurrenceNbrThreshold;
    }

    public void setObservationSumThreshold(int observationSumThreshold) throws WrongArgsException {
        this.observationSumThreshold = observationSumThreshold;
    }

    public void setRequestedNumberOfAdjacentForHub(int requestedNumberOfAdjacentForHub) {
        this.requestedNumberOfAdjacentForHub = requestedNumberOfAdjacentForHub;
    }

    public void setRequestedAverageWeightWithSixFirstAdjacentsForHub(double requestedAverageWeightWithSixFirstAdjacentsForHub) {
        this.requestedAverageWeightWithSixFirstAdjacentsForHub = requestedAverageWeightWithSixFirstAdjacentsForHub;
    }

    public void endDocument() throws SAXException {
        clustering();
    }

    public IndexedGraph getGraph() {
        throw new UnsupportedOperationException("Use the getMatrix() method insteed.");
    }

    public AdjacencyMatrix getMatrix() {
        return resultingMatrix;
    }

    private void clustering() throws SAXException, IllegalArgumentException {
        ContentHandler target = super.getContentHandler();
        target.startDocument();
        SAXCallbackWriter sCW = new SAXCallbackWriter(target);
        sCW.startElement(ROOT);
        sCW.characters(CR);
        sCW.startElement(TEXT);
        sCW.characters(CR);
        sCW.startElement(FRONT);
        sCW.characters(CR);
        CooccurrencyMatrix m = null;
        try {
            m = handler.getCooccurrencyMatrix();
        } catch (MatrixBuilderException e) {
            FilterException fE = new FilterException("Unable to create the matrix: " + e.getMessage());
            fE.initCause(e);
            throw fE;
        }
        Hyperlex hyperlexImpl = null;
        try {
            hyperlexImpl = new Hyperlex(((CooccurrencyMatrix) m), pole, variableFreqThreshold, cooccurrenceNbrThreshold, observationSumThreshold, maxAssociationFrequencyThreshold, requestedNumberOfAdjacentForHub, requestedAverageWeightWithSixFirstAdjacentsForHub, (ContentHandler) this);
        } catch (HyperlexException e) {
            throw new FilterException("Unable to initiate the clustering.", e);
        }
        hyperlexImpl.setLogLevel(loggingLevel);
        try {
            hyperlexImpl.createCluster();
        } catch (HyperlexException e) {
            throw new FilterException("Unable to create the clusters:" + e.getMessage(), e);
        }
        sCW.endElement(FRONT);
        sCW.characters(CR);
        sCW.startElement(BODY);
        Variable[] hub = hyperlexImpl.getHub();
        resultingMatrix = hyperlexImpl.getAdjacencyMatrix();
        if (matrixURI != null) {
            try {
                resultingMatrix.saveWithHeader(matrixURI);
            } catch (IOException io) {
                throw new FilterException("Error while saving matrix", io);
            }
        }
        if (hubURI != null) {
            try {
                Writer w = (Writer) new BufferedWriter(new OutputStreamWriter(new FileOutputStream(hubURI), "ISO-8859-1"));
                for (int y = 0; y < hub.length; y++) {
                    w.write(hub[y].toString() + "\n");
                }
                w.close();
            } catch (IOException ioe) {
                throw new FilterException("Error while saving hubs list", ioe);
            }
        }
        sCW.endElement(BODY);
        sCW.characters(CR);
        sCW.endElement(TEXT);
        sCW.characters(CR);
        sCW.endElement(ROOT);
        sCW.characters(CR);
        target.endDocument();
    }
}
