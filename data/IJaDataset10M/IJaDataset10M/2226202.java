package joshua.sarray;

import joshua.corpus.SymbolTable;
import joshua.decoder.ff.tm.Grammar;
import joshua.decoder.ff.tm.Rule;
import joshua.util.lexprob.LexicalProbabilities;
import joshua.util.sentence.alignment.Alignments;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a prefix tree with suffix links, for use in extracting 
 * hierarchical phrase-based statistical translation rules.
 * 
 * @author Lane Schwartz
 * @version $LastChangedDate:2008-11-13 13:13:31 -0600 (Thu, 13 Nov 2008) $
 */
public class PrefixTree {

    /** Logger for this class. */
    private static final Logger logger = Logger.getLogger(PrefixTree.class.getName());

    /** Integer representation of the nonterminal X. All nonterminals are guaranteed to be represented by negative integers. */
    public static final int X = -1;

    /** Operating system-specific end of line character(s). */
    static final byte[] newline = System.getProperty("line.separator").getBytes();

    /** Root node of this tree. */
    final Node root;

    /** Responsible for performing sampling and creating translation rules. */
    final RuleExtractor ruleExtractor;

    /** Max span in the source corpus of any extracted hierarchical phrase */
    final int maxPhraseSpan;

    /** Maximum number of terminals plus nonterminals allowed in any extracted hierarchical phrase. */
    final int maxPhraseLength;

    /** Maximum number of nonterminals allowed in any extracted hierarchical phrase. */
    final int maxNonterminals;

    /** Minimum span in the source corpus of any nonterminal in an extracted hierarchical phrase. */
    final int minNonterminalSpan;

    /** Represents a very high cost, corresponding to a very unlikely probability. */
    static final float VERY_UNLIKELY = -1.0f * (float) Math.log(1.0e-9);

    /** 
	 * Indicates whether rules with an initial source-side nonterminal 
	 * should be extracted from phrases at the start of a sentence, 
	 * even though such rules do not have supporting corporal evidence.
	 * <p>
	 * This is included for compatibility with Adam Lopez's Hiero rule extractor,
	 * in which this setting is set to <code>true</code>.
	 * <p>
	 * The default value is <code>false</code>.
	 */
    static boolean SENTENCE_INITIAL_X = false;

    /** 
	 * Indicates whether rules with a final source-side nonterminal 
	 * should be extracted from phrases at the end of a sentence, 
	 * even though such rules do not have supporting corporal evidence.
	 * <p>
	 * This is included for compatibility with Adam Lopez's Hiero rule extractor,
	 * in which this setting is set to <code>true</code>.
	 * <p>
	 * The default value is <code>false</code>.
	 */
    static boolean SENTENCE_FINAL_X = false;

    static boolean EDGE_X_MAY_VIOLATE_PHRASE_SPAN = false;

    /** Unique integer identifier for the root node. */
    static final int ROOT_NODE_ID = -999;

    /** 
	 * Unique integer identifier for the special ⊥ node that represents the suffix of the root node.
	 * @see Lopez (2008), footnote 9 on p73
	 */
    static final int BOT_NODE_ID = -2000;

    /**
	 * Gets a special map that maps any integer key to the root node.
	 *  
	 * @param root Root node, which this map will always return as a value. 
	 * @return Special map that maps any integer key to the root node.
	 * @see Lopez (2008), footnote 9 on p73
	 */
    static Map<Integer, Node> botMap(final Node root) {
        return new Map<Integer, Node>() {

            public void clear() {
                throw new UnsupportedOperationException();
            }

            public boolean containsKey(Object key) {
                return true;
            }

            public boolean containsValue(Object value) {
                return value == root;
            }

            public Set<java.util.Map.Entry<Integer, Node>> entrySet() {
                throw new UnsupportedOperationException();
            }

            public Node get(Object key) {
                return root;
            }

            public boolean isEmpty() {
                return false;
            }

            public Set<Integer> keySet() {
                throw new UnsupportedOperationException();
            }

            public Node put(Integer key, Node value) {
                throw new UnsupportedOperationException();
            }

            public void putAll(Map<? extends Integer, ? extends Node> t) {
                throw new UnsupportedOperationException();
            }

            public Node remove(Object key) {
                throw new UnsupportedOperationException();
            }

            public int size() {
                throw new UnsupportedOperationException();
            }

            public Collection<Node> values() {
                return Collections.singleton(root);
            }
        };
    }

    /** Suffix array representing the source language corpus. */
    final Suffixes suffixArray;

    /** Corpus array representing the target language corpus. */
    final Corpus targetCorpus;

    /** Represents alignments between words in the source corpus and the target corpus. */
    final Alignments alignments;

    /** Lexical translation probabilities. */
    final LexicalProbabilities lexProbs;

    /** Symbol table */
    final SymbolTable vocab;

    /** Empty pattern */
    final Pattern epsilon;

    /** 
	 * Node representing phrases that start with the nonterminal X. 
	 * This node's parent is the root node of the tree. 
	 */
    private final Node xnode;

    /**
	 * Constructs a new prefix tree with suffix links
	 * using the GENERATE_PREFIX_TREE algorithm 
	 * from Lopez (2008) PhD Thesis, Algorithm 2, p 76. 
	 * 
	 * @param suffixArray
	 * @param sentence
	 * @param maxPhraseSpan
	 * @param maxPhraseLength
	 * @param maxNonterminals
	 * @param minNonterminalSpan Minimum number of source language tokens 
	 *                           a nonterminal is allowed to encompass.
	 */
    public PrefixTree(Suffixes suffixArray, Corpus targetCorpus, Alignments alignments, SymbolTable vocab, LexicalProbabilities lexProbs, RuleExtractor ruleExtractor, int maxPhraseSpan, int maxPhraseLength, int maxNonterminals, int minNonterminalSpan) {
        if (logger.isLoggable(Level.FINE)) logger.fine("\n\n\nConstructing new PrefixTree\n\n");
        this.suffixArray = suffixArray;
        this.targetCorpus = targetCorpus;
        this.alignments = alignments;
        this.lexProbs = lexProbs;
        this.ruleExtractor = ruleExtractor;
        this.maxPhraseSpan = maxPhraseSpan;
        this.maxPhraseLength = maxPhraseLength;
        this.maxNonterminals = maxNonterminals;
        this.minNonterminalSpan = minNonterminalSpan;
        this.vocab = vocab;
        Node bot = new Node(this, BOT_NODE_ID);
        bot.sourceHierarchicalPhrases = HierarchicalPhrases.emptyList(vocab);
        this.root = new Node(this, ROOT_NODE_ID);
        bot.children = botMap(root);
        this.root.linkToSuffix(bot);
        if (suffixArray != null) {
            int[] bounds = { 0, suffixArray.size() - 1 };
            root.setBounds(bounds);
        }
        root.sourceHierarchicalPhrases = HierarchicalPhrases.emptyList(vocab);
        epsilon = new Pattern(vocab);
        if (maxNonterminals > 0) {
            xnode = root.addChild(X);
            {
                xnode.sourceHierarchicalPhrases = HierarchicalPhrases.emptyList(vocab);
                if (suffixArray != null) xnode.sourcePattern = new Pattern(suffixArray.getVocabulary(), X);
                if (suffixArray != null) {
                    int[] bounds = { 0, suffixArray.size() - 1 };
                    xnode.setBounds(bounds);
                }
            }
            Node suffixLink = root.calculateSuffixLink(X);
            if (logger.isLoggable(Level.FINEST)) {
                String oldSuffixLink = (xnode.suffixLink == null) ? "null" : "id" + xnode.suffixLink.objectID;
                String newSuffixLink = (suffixLink == null) ? "null" : "id" + suffixLink.objectID;
                logger.finest("Changing suffix link from " + oldSuffixLink + " to " + newSuffixLink + " for node " + xnode.toShortString() + " with token " + X);
            }
            xnode.linkToSuffix(suffixLink);
        } else {
            this.xnode = null;
        }
        if (logger.isLoggable(Level.FINEST)) logger.finest("CURRENT TREE:  " + root);
    }

    /**
	 * Constructs a new prefix tree with suffix links
	 * using the GENERATE_PREFIX_TREE algorithm 
	 * from Lopez (2008) PhD Thesis, Algorithm 2, p 76. 
	 * <p>
	 * This constructor does not take a suffix array parameter.
	 * Instead any prefix tree constructed by this constructor
	 * will assume that all possible phrases of this sentence
	 * are valid phrases.
	 * <p>
	 * This constructor is meant to be used primarily for testing purposes.
	 * 
	 * @param sentence
	 * @param maxPhraseSpan
	 * @param maxPhraseLength
	 * @param maxNonterminals
	 */
    PrefixTree(SymbolTable vocab, int maxPhraseSpan, int maxPhraseLength, int maxNonterminals) {
        this(null, null, null, vocab, null, null, maxPhraseSpan, maxPhraseLength, maxNonterminals, 2);
    }

    /**
	 * Modify this prefix tree by adding phrases for this sentence.
	 * 
	 * @param sentence
	 */
    public void add(int[] sentence) {
        int START_OF_SENTENCE = 0;
        int END_OF_SENTENCE = sentence.length - 1;
        Queue<Tuple> queue = new LinkedList<Tuple>();
        if (logger.isLoggable(Level.FINE)) logger.fine("Last sentence index == I == " + END_OF_SENTENCE);
        for (int i = START_OF_SENTENCE; i <= END_OF_SENTENCE; i++) {
            if (logger.isLoggable(Level.FINEST)) logger.finest("Adding tuple (ε," + i + "," + i + "," + root.toShortString() + ")");
            queue.add(new Tuple(epsilon, i, i, root));
        }
        if (this.maxNonterminals > 0) {
            Pattern xpattern = new Pattern(vocab, X);
            int start = START_OF_SENTENCE;
            if (!SENTENCE_INITIAL_X) start += 1;
            for (int i = start; i <= END_OF_SENTENCE; i++) {
                if (logger.isLoggable(Level.FINEST)) logger.finest("Adding tuple (X," + (i - 1) + "," + i + "," + xnode.toShortString() + ")");
                if (EDGE_X_MAY_VIOLATE_PHRASE_SPAN) {
                    queue.add(new Tuple(xpattern, i, i, xnode));
                } else {
                    queue.add(new Tuple(xpattern, i - 1, i, xnode));
                }
            }
        }
        while (!queue.isEmpty()) {
            if (logger.isLoggable(Level.FINER)) {
                logger.finer("\n");
                if (logger.isLoggable(Level.FINEST)) logger.finest("CURRENT TREE:      " + root);
            }
            Tuple tuple = queue.remove();
            int i = tuple.spanStart;
            int j = tuple.spanEnd;
            Node prefixNode = tuple.prefixNode;
            Pattern prefixPattern = tuple.pattern;
            if (logger.isLoggable(Level.FINER)) logger.finer("Have tuple (" + prefixPattern + "," + i + "," + j + "," + prefixNode.toShortString() + ")");
            if (j <= END_OF_SENTENCE) {
                if (prefixNode.hasChild(sentence[j])) {
                    if (logger.isLoggable(Level.FINER)) logger.finer("EXISTING node for \"" + sentence[j] + "\" from " + prefixNode.toShortString() + " to node " + prefixNode.getChild(sentence[j]).toShortString() + " with pattern " + prefixPattern);
                    Node child = prefixNode.getChild(sentence[j]);
                    if (child.active == Node.INACTIVE) {
                        continue;
                    } else {
                        if (logger.isLoggable(Level.FINER)) {
                            logger.finer("Calling EXTEND_QUEUE(" + i + "," + j + "," + prefixPattern + "," + prefixNode.toShortString());
                            if (logger.isLoggable(Level.FINEST)) logger.finest("TREE BEFOR EXTEND: " + root);
                        }
                        extendQueue(queue, i, j, sentence, new Pattern(prefixPattern, sentence[j]), child);
                        if (logger.isLoggable(Level.FINEST)) logger.finest("TREE AFTER EXTEND: " + root);
                    }
                } else {
                    if (logger.isLoggable(Level.FINER)) logger.finer("Adding new node to node " + prefixNode.toShortString());
                    Node newNode = prefixNode.addChild(sentence[j]);
                    if (logger.isLoggable(Level.FINER)) {
                        String word = (suffixArray == null) ? "" + sentence[j] : suffixArray.getVocabulary().getWord(sentence[j]);
                        logger.finer("Created new node " + newNode.toShortString() + " for \"" + word + "\" and \n  added it to " + prefixNode.toShortString());
                    }
                    Node suffixNode = prefixNode.calculateSuffixLink(sentence[j]);
                    if (logger.isLoggable(Level.FINEST)) {
                        String oldSuffixLink = (newNode.suffixLink == null) ? "null" : "id" + newNode.suffixLink.objectID;
                        String newSuffixLink = (suffixNode == null) ? "null" : "id" + suffixNode.objectID;
                        logger.finest("Changing suffix link from " + oldSuffixLink + " to " + newSuffixLink + " for node " + newNode.toShortString() + " (prefix node " + prefixNode.toShortString() + " ) with token " + sentence[j]);
                    }
                    newNode.linkToSuffix(suffixNode);
                    if (suffixNode.active == Node.INACTIVE) {
                        newNode.active = Node.INACTIVE;
                    } else {
                        Pattern extendedPattern = new Pattern(prefixPattern, sentence[j]);
                        MatchedHierarchicalPhrases result = null;
                        if (suffixArray != null) {
                            result = query(extendedPattern, newNode, prefixNode, suffixNode);
                        }
                        if (result != null && result.isEmpty()) {
                            newNode.active = Node.INACTIVE;
                        } else {
                            newNode.active = Node.ACTIVE;
                            extendQueue(queue, i, j, sentence, extendedPattern, newNode);
                        }
                    }
                }
            }
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.finer("\n");
            if (logger.isLoggable(Level.FINEST)) logger.finest("FINAL TREE:  " + root);
        }
    }

    /**
	 * Implements the root QUERY algorithm (Algorithm 4) of Adam Lopez's (2008) doctoral thesis.
	 * 
	 * @param pattern
	 * @param node
	 * @param prefixNode
	 * @param suffixNode
	 * @return
	 */
    public MatchedHierarchicalPhrases query(Pattern pattern, Node node, Node prefixNode, Node suffixNode) {
        if (logger.isLoggable(Level.FINE)) logger.fine("PrefixTree.query( " + pattern + ",\n\t   new node " + node + ",\n\tprefix node " + prefixNode + ",\n\tsuffix node " + suffixNode + ")");
        MatchedHierarchicalPhrases result;
        int arity = pattern.arity();
        if (arity == 0) {
            int[] bounds = suffixArray.findPhrase(pattern, 0, pattern.size(), prefixNode.lowBoundIndex, prefixNode.highBoundIndex);
            if (bounds == null) {
                result = HierarchicalPhrases.emptyList(vocab);
            } else {
                node.setBounds(bounds);
                int[] startingPositions = suffixArray.getAllPositions(bounds);
                result = suffixArray.createHierarchicalPhrases(startingPositions, pattern, vocab);
            }
        } else {
            result = suffixArray.getMatchingPhrases(pattern);
            if (result == null) {
                if (arity == 1 && prefixNode.sourcePattern.words[0] < 0 && prefixNode.sourcePattern.words[prefixNode.sourcePattern.words.length - 1] < 0) {
                    result = suffixNode.sourceHierarchicalPhrases.copyWithInitialX();
                } else {
                    if (logger.isLoggable(Level.FINEST)) logger.finest("Calling queryIntersect(" + pattern + " M_a_alpha.pattern==" + prefixNode.sourcePattern + ", M_alpha_b.pattern==" + suffixNode.sourcePattern + ")");
                    result = HierarchicalPhrases.queryIntersect(pattern, prefixNode.sourceHierarchicalPhrases, suffixNode.sourceHierarchicalPhrases, minNonterminalSpan, maxPhraseSpan);
                }
                suffixArray.setMatchingPhrases(pattern, result);
            }
        }
        node.storeResults(result, pattern);
        return result;
    }

    static boolean BUGGY = false;

    /**
	 * Implements Function EXTEND_QUEUE from Lopez (2008) PhD Thesis, Algorithm 2, p 76
	 * 
	 * @param queue Queue of tuples
	 * @param i Start index of the pattern in the source input sentence (inclusive, 1-based).
	 * @param j End index of the pattern in the source input sentence (inclusive, 1-based).
	 * @param sentence
	 * @param pattern Pattern corresponding to the prefix node. In Lopez's terminology, this pattern is alpha f_j.
	 * @param node Node in the prefix tree to which a new node (corresponding to the pattern) will eventually be attached.
	 */
    private void extendQueue(Queue<Tuple> queue, int i, int j, int[] sentence, Pattern pattern, Node node) {
        int J = j;
        if (!SENTENCE_FINAL_X) J += 1;
        int endOfPhraseSpan = (j + 1) - i + 1;
        if (EDGE_X_MAY_VIOLATE_PHRASE_SPAN) endOfPhraseSpan -= 1;
        if (pattern.size() < maxPhraseLength && endOfPhraseSpan <= maxPhraseSpan && J < sentence.length) {
            if (logger.isLoggable(Level.FINEST)) logger.finest("\nextendQueue: Adding tuple (" + pattern + "," + i + "," + (j + 1) + "," + node + ")");
            queue.add(new Tuple(pattern, i, j + 1, node));
            if (pattern.arity() < maxNonterminals) {
                Node xNode;
                if (!node.children.containsKey(X)) {
                    xNode = node.addChild(X);
                    if (logger.isLoggable(Level.FINEST)) logger.finest("Adding node for \"" + X + "\" from " + node + " to new node " + xNode + " with alphaPattern " + pattern + "  (in extendQueue)");
                    Node suffixLink = node.calculateSuffixLink(X);
                    if (logger.isLoggable(Level.FINEST)) {
                        String oldSuffixLink = (xNode.suffixLink == null) ? "null" : "id" + xNode.suffixLink.objectID;
                        String newSuffixLink = (suffixLink == null) ? "null" : "id" + suffixLink.objectID;
                        logger.finest("Changing suffix link from " + oldSuffixLink + " to " + newSuffixLink + " for node " + xNode + " (prefix node " + node + " ) with token " + X);
                    }
                    xNode.linkToSuffix(suffixLink);
                } else {
                    xNode = node.children.get(X);
                    if (logger.isLoggable(Level.FINEST)) logger.finest("X Node is already " + xNode + " for prefixNode " + node);
                }
                xNode.active = Node.ACTIVE;
                {
                    SymbolTable vocab = (suffixArray == null) ? null : suffixArray.getVocabulary();
                    Pattern xpattern = new Pattern(vocab, pattern.words, X);
                    MatchedHierarchicalPhrases phrasesWithFinalX = node.sourceHierarchicalPhrases.copyWithFinalX();
                    xNode.storeResults(phrasesWithFinalX, xpattern);
                }
                if (logger.isLoggable(Level.FINEST)) logger.finest("Alpha pattern is " + pattern);
                if (pattern.words.length + 2 <= maxPhraseLength) {
                    int I = sentence.length;
                    if (!SENTENCE_FINAL_X) I -= 1;
                    int min = (I < i + maxPhraseSpan) ? I : i + maxPhraseSpan - 1;
                    Pattern patternX = new Pattern(pattern, X);
                    for (int k = j + 2; k <= min; k++) {
                        if (logger.isLoggable(Level.FINEST)) logger.finest("extendQueue: Adding tuple (" + patternX + "," + i + "," + k + "," + xNode + " ) in EXTEND_QUEUE ****************************************");
                        queue.add(new Tuple(patternX, i, k, xNode));
                    }
                } else if (logger.isLoggable(Level.FINEST)) {
                    logger.finest("Not extending " + pattern + "+X ");
                }
            }
        }
    }

    /**
	 * Gets the root node of this tree.
	 * 
	 * @return the root node of this tree
	 */
    public Grammar getRoot() {
        return root;
    }

    public List<Rule> getAllRules() {
        return root.getAllRules();
    }

    public String toString() {
        return root.toTreeString("", vocab);
    }

    public int size() {
        return root.size();
    }

    /**
	 * Default constructor - for testing purposes only.
	 * <p>
	 * The unit tests for Node require a dummy PrefixTree.
	 */
    private PrefixTree() {
        this(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
	 * Gets an invalid, dummy prefix tree.
	 * <p>
	 * For testing purposes only.
	 * 
	 */
    private PrefixTree(int maxPhraseSpan, int maxPhraseLength, int maxNonterminals, int minNonterminalSpan) {
        root = null;
        suffixArray = null;
        targetCorpus = null;
        alignments = null;
        lexProbs = null;
        xnode = null;
        ruleExtractor = null;
        this.epsilon = null;
        this.vocab = null;
        this.maxPhraseSpan = maxPhraseSpan;
        this.maxPhraseLength = maxPhraseLength;
        this.maxNonterminals = maxNonterminals;
        this.minNonterminalSpan = minNonterminalSpan;
    }

    /**
	 * Gets an invalid, dummy prefix tree.
	 * <p>
	 * For testing purposes only.
	 * 
	 * @return an invalid, dummy prefix tree
	 */
    static PrefixTree getDummyPrefixTree() {
        return new PrefixTree();
    }

    /**
	 * Gets an invalid, dummy prefix tree.
	 * <p>
	 * For testing purposes only.
	 * 
	 * @return an invalid, dummy prefix tree
	 */
    static PrefixTree getDummyPrefixTree(int maxPhraseSpan, int maxPhraseLength, int maxNonterminals, int minNonterminalSpan) {
        return new PrefixTree(maxPhraseSpan, maxPhraseLength, maxNonterminals, minNonterminalSpan);
    }
}
