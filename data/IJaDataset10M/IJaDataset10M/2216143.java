package gr.demokritos.iit.jinsect.documentModel.representations;

import java.io.IOException;
import java.io.InvalidClassException;
import gr.demokritos.iit.jinsect.documentModel.comparators.CachedDocumentComparator;
import gr.demokritos.iit.jinsect.documentModel.documentTypes.NGramDocument;
import gr.demokritos.iit.jinsect.structs.ISimilarity;

/**
 *
 * @author ggianna
 */
public class NGramGaussNormDocument extends NGramDocument {

    protected DocumentNGramGaussNormGraph Graph;

    /** Creates a new instance, using graphs with Gauss-function-normalized edge weights for neighbouring.*/
    public NGramGaussNormDocument() {
        Graph = new DocumentNGramGaussNormGraph();
        Histogram = new DocumentNGramHistogram();
    }

    public NGramGaussNormDocument(int iMinGraphSize, int iMaxGraphSize, int iGraphCorrelationWindow, int iMinHistogramSize, int iMaxHistogramSize) {
        Graph = new DocumentNGramGaussNormGraph(iMinGraphSize, iMaxGraphSize, iGraphCorrelationWindow);
        Histogram = new DocumentNGramHistogram(iMinHistogramSize, iMaxHistogramSize);
    }

    public DocumentNGramGaussNormGraph getDocumentGraph() {
        return Graph;
    }

    /** Set the graph to a {@link DocumentNGramGaussNormGraph} graph supplied. If a type of 
     *@param idgNew The graph to use.
     */
    public void setDocumentGraph(DocumentNGramGaussNormGraph idgNew) {
        Graph = idgNew;
    }

    public static void main(String[] sArgs) {
        final NGramGaussNormDocument dggTest1 = new NGramGaussNormDocument(3, 5, 30, 3, 3);
        final NGramGaussNormDocument dggTest2 = new NGramGaussNormDocument(3, 5, 30, 3, 3);
        final String s1 = "Gauss1";
        final String s2 = "Gauss2";
        Thread t1 = new Thread(new Runnable() {

            public void run() {
                System.err.println("Complex Loading " + s1);
                dggTest1.loadDataStringFromFile(s1);
                System.err.println("Cache (1) success:" + dggTest1.getDocumentGraph().eclLocator.getSuccessRatio());
            }
        });
        t1.start();
        Thread t2 = new Thread(new Runnable() {

            public void run() {
                System.err.println("Complex Loading " + s2);
                dggTest2.loadDataStringFromFile(s2);
                System.err.println("Cache (2) success:" + dggTest2.getDocumentGraph().eclLocator.getSuccessRatio());
            }
        });
        t2.start();
        while (t1.isAlive() || t2.isAlive()) Thread.yield();
        System.err.println("Loading complete. Starting comparison...");
        t1 = new Thread(new Runnable() {

            public void run() {
                CachedDocumentComparator std = new CachedDocumentComparator(1.0);
                ISimilarity sSimil;
                try {
                    sSimil = std.getSimilarityBetween(dggTest1, dggTest2);
                    System.err.println("Overall similarity (Gauss)" + sSimil.getOverallSimilarity());
                } catch (InvalidClassException ex) {
                    ex.printStackTrace();
                }
            }
        });
        t2 = new Thread(new Runnable() {

            public void run() {
                CachedDocumentComparator std = new CachedDocumentComparator(1.0);
                try {
                    ISimilarity sSimil = std.getSimilarityBetween(dggTest1, dggTest1);
                    System.err.println("Overall similarity in case of complete match (Gauss)" + sSimil.getOverallSimilarity());
                } catch (InvalidClassException ex) {
                    ex.printStackTrace();
                }
            }
        });
        t1.start();
        t2.start();
        while (t1.isAlive() || t2.isAlive()) Thread.yield();
        System.err.println("\nTest complete.");
    }

    public void loadDataStringFromFile(String sFilename) {
        try {
            Histogram.loadDataStringFromFile(sFilename);
            Graph.loadDataStringFromFile(sFilename);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Histogram.setDataString("");
            Graph.setDataString("");
        }
    }

    public void setDataString(String sDataString) {
        Histogram.setDataString(sDataString);
        Graph.setDataString(sDataString);
    }

    public void clear() {
        Graph.InitGraphs();
        Histogram.setDataString("");
    }
}
