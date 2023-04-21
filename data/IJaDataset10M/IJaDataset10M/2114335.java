package uk.org.biotext.graphspider.tools;

import edu.stanford.nlp.ling.WordLemmaTag;
import edu.stanford.nlp.trees.TreeGraphNode;

/**
 * The Class Lemmatizer.
 */
public class Lemmatizer {

    /**
     * From node.
     * 
     * @param node
     *            the node
     * 
     * @return the string
     */
    public static String fromNode(TreeGraphNode node) {
        WordLemmaTag wlt = new WordLemmaTag(node.label().value(), node.parent().label().value());
        return wlt.lemma();
    }
}
