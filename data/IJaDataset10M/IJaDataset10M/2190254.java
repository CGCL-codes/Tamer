package joshua.sarray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import joshua.corpus.SymbolTable;
import joshua.decoder.ff.tm.AbstractGrammar;
import joshua.decoder.ff.tm.BasicRuleCollection;
import joshua.decoder.ff.tm.Grammar;
import joshua.decoder.ff.tm.Rule;
import joshua.decoder.ff.tm.RuleCollection;
import joshua.decoder.ff.tm.Trie;

public class Node extends AbstractGrammar implements Comparable<Node>, Grammar, Trie {

    private static final Logger logger = Logger.getLogger(Node.class.getName());

    static final boolean ACTIVE = true;

    static final boolean INACTIVE = false;

    final int incomingArcValue;

    final int objectID;

    int lowBoundIndex;

    int highBoundIndex;

    boolean active;

    Node suffixLink;

    /** Maps from integer representations of words to nodes. */
    Map<Integer, Node> children;

    /** Source side hierarchical phrases for this node. */
    MatchedHierarchicalPhrases sourceHierarchicalPhrases;

    /** Representation of the source side tokens corresponding to the hierarchical phrases for this node. */
    Pattern sourcePattern;

    /** Translation rules for this node. */
    List<Rule> results;

    final PrefixTree tree;

    Node(PrefixTree tree, int incomingArcValue) {
        this(tree, true, incomingArcValue);
    }

    Node(PrefixTree tree, boolean active, int incomingArcValue) {
        this.tree = tree;
        this.active = active;
        this.suffixLink = null;
        this.children = new HashMap<Integer, Node>();
        this.incomingArcValue = incomingArcValue;
        this.objectID = nodeIDCounter++;
        this.sourceHierarchicalPhrases = HierarchicalPhrases.emptyList(tree.vocab);
        this.results = Collections.emptyList();
    }

    Node calculateSuffixLink(int endOfPattern) {
        Node suffixLink = this.suffixLink.getChild(endOfPattern);
        if (suffixLink == null) throw new RuntimeException("No child " + endOfPattern + " for node " + this.suffixLink + " (Parent was " + this + ")");
        return suffixLink;
    }

    public RuleCollection getRules() {
        int[] empty = {};
        final int[] sourceSide = (sourcePattern == null) ? empty : sourcePattern.words;
        final int arity = (sourcePattern == null) ? 0 : sourcePattern.arity;
        return new BasicRuleCollection(arity, sourceSide, results);
    }

    /**
	 * Gets rules for this node and the children of this node.
	 * 
	 * @return rules for this node and the children of this node.
	 */
    public List<Rule> getAllRules() {
        List<Rule> result = new ArrayList<Rule>((results == null) ? Collections.<Rule>emptyList() : results);
        for (Node child : children.values()) {
            result.addAll(child.getAllRules());
        }
        return result;
    }

    public boolean hasExtensions() {
        if (children.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean hasRules() {
        if (sourceHierarchicalPhrases.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public Trie matchOne(int symbol) {
        if (children.containsKey(symbol)) {
            return children.get(symbol);
        } else {
            return null;
        }
    }

    public Trie matchPrefix(List<Integer> symbols) {
        Node node = this;
        for (Integer symbol : symbols) {
            if (node.children.containsKey(symbol)) {
                node = node.children.get(symbol);
            } else {
                return null;
            }
        }
        return node;
    }

    public boolean hasChild(int child) {
        return children.containsKey(child);
    }

    public Node getChild(int child) {
        return children.get(child);
    }

    public Node addChild(int child) {
        if (children.containsKey(child)) throw new RuntimeException("Child " + child + " already exists in node " + this);
        Node node = new Node(tree, child);
        children.put(child, node);
        return node;
    }

    public void linkToSuffix(Node suffix) {
        this.suffixLink = suffix;
    }

    public void setBounds(int[] bounds) {
        lowBoundIndex = bounds[0];
        highBoundIndex = bounds[1];
    }

    /**
	 * Stores in this node
	 * a list of source language hierarchical phrases, 
	 * the associated source language pattern,
	 * and the list of associated translation rules.
	 * <p>
	 * This method is responsible for creating and storing
	 * translation rules from the provided list 
	 * of source language hierarchical phrases.
	 * 
	 * @param hierarchicalPhrases Source language hierarchical phrases.
	 * @param sourceTokens Source language pattern that should correspond to the hierarchical phrases.
	 */
    public void storeResults(MatchedHierarchicalPhrases hierarchicalPhrases, Pattern sourcePattern) {
        if (logger.isLoggable(Level.FINER)) {
            logger.finer("Storing " + hierarchicalPhrases.size() + " source phrases at node " + objectID + ":");
        }
        this.sourcePattern = sourcePattern;
        this.results = new ArrayList<Rule>(hierarchicalPhrases.size());
        this.sourceHierarchicalPhrases = hierarchicalPhrases;
        if (tree.ruleExtractor != null) {
            this.results = tree.ruleExtractor.extractRules(sourcePattern, hierarchicalPhrases);
        }
    }

    public int size() {
        int size = 1;
        for (Node child : children.values()) {
            size += child.size();
        }
        return size;
    }

    public String toString() {
        SymbolTable vocab = tree.vocab;
        StringBuilder s = new StringBuilder();
        s.append("[id");
        s.append(objectID);
        s.append(' ');
        if (incomingArcValue == PrefixTree.X) {
            s.append('X');
        } else if (incomingArcValue == PrefixTree.ROOT_NODE_ID) {
            s.append("ROOT");
        } else if (vocab != null) {
            s.append(vocab.getWord(incomingArcValue));
        } else {
            s.append('v');
            s.append(incomingArcValue);
        }
        s.append(" (");
        if (suffixLink != null) s.append(suffixLink.objectID); else s.append("null");
        s.append(')');
        s.append(' ');
        List<Node> kids = new ArrayList<Node>(children.values());
        Collections.sort(kids);
        for (Node kid : kids) {
            s.append(kid.toString());
            s.append(' ');
        }
        if (!active) s.append('*');
        s.append(']');
        return s.toString();
    }

    String toShortString() {
        SymbolTable vocab = tree.vocab;
        StringBuilder s = new StringBuilder();
        s.append("[id");
        s.append(objectID);
        s.append(' ');
        if (incomingArcValue == PrefixTree.X) {
            s.append('X');
        } else if (incomingArcValue == PrefixTree.ROOT_NODE_ID) {
            s.append("ROOT");
        } else if (vocab != null) {
            s.append(vocab.getWord(incomingArcValue));
        } else {
            s.append('v');
            s.append(incomingArcValue);
        }
        s.append(" (");
        if (suffixLink != null) s.append(suffixLink.objectID); else s.append("null");
        s.append(')');
        s.append(' ');
        s.append('{');
        s.append(children.size());
        s.append(" children}");
        if (!active) s.append('*');
        s.append(']');
        return s.toString();
    }

    String toTreeString(String tabs, SymbolTable vocab) {
        StringBuilder s = new StringBuilder();
        s.append(tabs);
        s.append("[id");
        s.append(objectID);
        s.append(' ');
        if (incomingArcValue == PrefixTree.X) {
            s.append('X');
        } else if (incomingArcValue == PrefixTree.ROOT_NODE_ID) {
            s.append("ROOT");
        } else if (vocab != null) {
            s.append(vocab.getWord(incomingArcValue));
        } else {
            s.append('v');
            s.append(incomingArcValue);
        }
        s.append(" (");
        if (suffixLink != null) s.append(suffixLink.objectID); else s.append("null");
        s.append(')');
        if (children.size() > 0) {
            s.append(" \n\n");
            List<Node> kids = new ArrayList<Node>(children.values());
            Collections.sort(kids);
            for (Node entry : kids) {
                s.append(entry.toTreeString(tabs + "\t", vocab));
                s.append("\n\n");
            }
            s.append(tabs);
        } else {
            s.append(' ');
        }
        if (!active) s.append('*');
        s.append(']');
        return s.toString();
    }

    public int compareTo(Node o) {
        Integer i = objectID;
        Integer j = o.objectID;
        return i.compareTo(j);
    }

    public Trie getTrieRoot() {
        return this;
    }

    public boolean hasRuleForSpan(int startIndex, int endIndex, int pathLength) {
        if (tree.maxPhraseSpan == -1) {
            return (startIndex == 0);
        } else {
            return (endIndex - startIndex <= tree.maxPhraseSpan);
        }
    }

    public Collection<Node> getExtensions() {
        return this.children.values();
    }

    static int nodeIDCounter = 0;

    static void resetNodeCounter() {
        nodeIDCounter = 0;
    }

    public int getNumRules() {
        int numRules = (results == null) ? 0 : results.size();
        if (children != null) {
            for (Node child : children.values()) {
                numRules += child.getNumRules();
            }
        }
        return numRules;
    }

    public Rule constructOOVRule(int num_feats, int sourceWord, boolean have_lm_model) {
        return null;
    }

    public int getOOVRuleID() {
        return 0;
    }
}
