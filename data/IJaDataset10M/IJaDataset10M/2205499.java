package edu.ucla.sspace.wordsi.psd;

import edu.ucla.sspace.wordsi.DependencyContextExtractor;
import edu.ucla.sspace.wordsi.DependencyContextGenerator;
import edu.ucla.sspace.wordsi.Wordsi;
import edu.ucla.sspace.dependency.DependencyExtractor;
import edu.ucla.sspace.dependency.DependencyPath;
import edu.ucla.sspace.dependency.DependencyTreeNode;
import edu.ucla.sspace.dependency.SimpleDependencyTreeNode;
import edu.ucla.sspace.vector.SparseDoubleVector;
import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.util.Map;

/**
 * A pseudo word based {@link DependencyContextExtractor}.  Given a mapping from
 * raw tokens to pseudo words, this extractor will automatically change the text
 * for any dependency node that has a valid pseudo word mapping.  The pseudo
 * word will serve as the primary key for assignments and the original token
 * will serve as the secondary key.
 *
 * @author Keith Stevens
 */
public class PseudoWordDependencyContextExtractor extends DependencyContextExtractor {

    /**
     * The mapping used between tokens and their pseudoword replacement.
     */
    private Map<String, String> pseudoWordMap;

    /**
     * Creates a new {@link PseudoWordDependencyContextExtractor}.
     *
     * @param extractor The {@link DependencyExtractor} that parses the document
     *        and returns a valid dependency tree
     * @param basisMapping A mapping from dependency paths to feature indices
     * @param weighter A weighting function for dependency paths
     * @param acceptor An accepting function that validates dependency paths
     *        which may serve as features
     * @param pseudoWordMap A mapping from raw tokens to pseudo words
     */
    public PseudoWordDependencyContextExtractor(DependencyExtractor extractor, DependencyContextGenerator generator, Map<String, String> pseudoWordMap) {
        super(extractor, generator, true);
        this.pseudoWordMap = pseudoWordMap;
    }

    /**
     * {@inheritDoc}
     */
    public void processDocument(BufferedReader document, Wordsi wordsi) {
        try {
            String contextHeader = handleContextHeader(document);
            String[] contextTokens = contextHeader.split("\\s+");
            int focusIndex = Integer.parseInt(contextTokens[3]);
            DependencyTreeNode[] nodes = extractor.readNextTree(document);
            if (nodes.length == 0) return;
            DependencyTreeNode focusNode = nodes[focusIndex];
            String focusWord = getPrimaryKey(focusNode);
            String secondarykey = pseudoWordMap.get(focusWord);
            if (secondarykey == null) return;
            if (!acceptWord(focusNode, contextTokens[1], wordsi)) return;
            SparseDoubleVector focusMeaning = generator.generateContext(nodes, focusIndex);
            wordsi.handleContextVector(secondarykey, focusWord, focusMeaning);
            document.close();
        } catch (IOException ioe) {
            throw new IOError(ioe);
        }
    }

    /**
     * Returns true if {@code focusWord} is a known pseudo word.
     */
    protected boolean acceptWord(DependencyTreeNode focusNode, String contextHeader, Wordsi wordsi) {
        return pseudoWordMap.containsKey(focusNode.word()) && focusNode.word().equals(contextHeader);
    }
}
