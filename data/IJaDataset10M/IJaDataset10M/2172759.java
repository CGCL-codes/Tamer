package netkit.classifiers.io;

import netkit.classifiers.Classification;
import netkit.graph.Graph;
import netkit.graph.AttributeCategorical;
import java.io.File;

/**
 * This interface defines the methods needed to read a set of true classifications from a file.
 *
 * @author Sofus A. Macskassy (sofmac@gmail.com)
 */
public interface ReadClassification {

    /**
     * Read from a given file a estimate of classifications for nodes in
     * the given graph.  Return these classifications in a Classification
     * object.
     *
     * @param graph The graph whose nodes are two be classified
     * @param nodeType  The nodeType which the classification object refers to
     * @param attribute  The categorial values to map classification-names against.
     * @param input The file from which to read classifications
     *
     * @return A Classification object for the classifications read in.
     *         Nodes whose classifications were not in the given file are
     *         classified as index '-1'
     **/
    public Classification readClassification(Graph graph, String nodeType, AttributeCategorical attribute, File input);

    /**
     * Read from a given file a estimate of classifications for nodes in
     * the given graph.  Fill these into the given Classification
     * object.
     *
     * @param graph  The graph whose nodes are two be classified
     * @param labels The classification object to fill in with the
     *               read in labels.   The calling function should
     *               lock the valuemap if no new labels are to be
     *               accepted.
     * @param input The file from which to read classifications
     **/
    public void readClassification(Graph graph, Classification labels, File input);
}
