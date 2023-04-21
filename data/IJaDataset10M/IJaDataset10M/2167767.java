package joshua.prefix_tree;

import java.util.Collections;
import java.util.List;
import joshua.corpus.MatchedHierarchicalPhrases;
import joshua.corpus.suffix_array.HierarchicalPhrases;
import joshua.corpus.suffix_array.Suffixes;
import joshua.corpus.vocab.SymbolTable;
import joshua.decoder.ff.tm.Rule;

/**
 * Root node of a prefix tree.
 *
 * @author Lane Schwartz
 */
public class RootNode extends Node {

    private final PrefixTree tree;

    private final MatchedHierarchicalPhrases matchedPhrases;

    RootNode(PrefixTree tree, int incomingArcValue) {
        super(tree.parallelCorpus, 1);
        this.tree = tree;
        SymbolTable vocab = tree.vocab;
        this.matchedPhrases = HierarchicalPhrases.emptyList(vocab);
        Suffixes suffixArray = tree.suffixArray;
        if (suffixArray != null) {
            setBounds(0, suffixArray.size() - 1);
        }
    }

    /**
	 * Gets an empty list of rules.
	 * 
	 * @return an empty list of rules
	 */
    protected List<Rule> getResults() {
        return Collections.emptyList();
    }

    /**
	 * Gets an empty list of matched hierarchical phrases.
	 * <p>
	 * The list of hierarchical phrases 
     * for the X node that comes off of ROOT 
     * is defined to be an empty list.
     * <p>
     * One could alternatively consider 
     * every phrase in the corpus to match here,
     * but we don't.
     * 
     * @return an empty list of matched hierarchical phrases
	 */
    protected MatchedHierarchicalPhrases getMatchedPhrases() {
        return matchedPhrases;
    }

    public String toString() {
        return toString(tree.vocab, PrefixTree.ROOT_NODE_ID);
    }

    public Node addChild(int child) {
        if (child == SymbolTable.X) {
            if (children.containsKey(child)) {
                throw new ChildNodeAlreadyExistsException(this, child);
            } else {
                XNode node = new XNode(this);
                children.put(child, node);
                return node;
            }
        } else {
            return super.addChild(child);
        }
    }

    String toTreeString(String tabs, SymbolTable vocab) {
        return super.toTreeString(tabs, vocab, PrefixTree.ROOT_NODE_ID);
    }
}
