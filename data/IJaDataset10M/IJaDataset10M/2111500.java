package cs224n.assignments;

import cs224n.io.BioIETreebankReader;
import cs224n.io.GENIATreebankReader;
import cs224n.io.PennTreebankReader;
import cs224n.ling.Tree;
import cs224n.ling.Trees;
import cs224n.parser.EnglishPennTreebankParseEvaluator;
import cs224n.util.*;
import cs224n.classify.ProbabilisticClassifier;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;

/**
 * Harness for PCFG Parser project.
 *
 * @author Dan Klein
 */
public class PCFGParserTester {

    private static final String ROOT_STR = "ROOT";

    /**
   * Parsers are required to map sentences to trees.  How a parser is
   * constructed and trained is not specified.
   */
    public static interface Parser {

        public void train(List<Tree<String>> trainTrees);

        public Tree<String> getBestParse(List<String> sentence);
    }

    /**
   * The PCFG Parser you will implement.
   */
    public static class PCFGParser implements Parser {

        private Grammar grammar;

        private Lexicon lexicon;

        private List<String> tagList;

        private List<String> currSentence;

        public void train(List<Tree<String>> trainTrees) {
            TreeAnnotations.verticalMarkovizeAll(trainTrees);
            lexicon = new Lexicon(TreeAnnotations.annotateAllTrees(trainTrees));
            grammar = new Grammar(TreeAnnotations.annotateAllTrees(trainTrees));
            tagList = new ArrayList<String>(lexicon.getAllTags());
        }

        public Tree<String> getBestParse(List<String> sentence) {
            currSentence = sentence;
            int length = sentence.size() + 1;
            Map<String, Double>[][] logScoreTable = new HashMap[length][length];
            Map<String, Pair<String, String>>[][] history = new HashMap[length][length];
            Map<String, Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>>[][] childLocs = new HashMap[length][length];
            for (int pos = 0; pos < length - 1; pos++) {
                logScoreTable[pos][pos + 1] = new HashMap<String, Double>();
                history[pos][pos + 1] = new HashMap<String, Pair<String, String>>();
                childLocs[pos][pos + 1] = new HashMap<String, Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>>();
                for (String tag : tagList) {
                    if (lexicon.scoreTagging(sentence.get(pos), tag) <= 0.0) {
                        continue;
                    }
                    logScoreTable[pos][pos + 1].put(tag, Math.log(lexicon.scoreTagging(sentence.get(pos), tag)));
                    Pair<String, String> histPair = new Pair<String, String>(sentence.get(pos), null);
                    history[pos][pos + 1].put(tag, histPair);
                    childLocs[pos][pos + 1].put(tag, null);
                }
                boolean modified;
                String toAdd = "";
                double matching = 0.0;
                do {
                    modified = false;
                    for (String tag : logScoreTable[pos][pos + 1].keySet()) {
                        List<UnaryRule> unaryCandidates = grammar.getUnaryRulesByChild(tag);
                        for (UnaryRule candidate : unaryCandidates) {
                            double candidateProb = Math.log(candidate.getScore()) + logScoreTable[pos][pos + 1].get(tag);
                            if ((!logScoreTable[pos][pos + 1].containsKey(candidate.getParent())) || candidateProb > logScoreTable[pos][pos + 1].get(candidate.getParent())) {
                                toAdd = candidate.getParent();
                                matching = candidateProb;
                                Pair<String, String> newPair = new Pair<String, String>(candidate.getChild(), null);
                                history[pos][pos + 1].put(candidate.getParent(), newPair);
                                Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> toPut = new Pair(new Pair<Integer, Integer>(pos, pos + 1), null);
                                childLocs[pos][pos + 1].put(candidate.getParent(), toPut);
                                modified = true;
                                break;
                            }
                        }
                        if (modified) {
                            break;
                        }
                    }
                    if (modified) {
                        logScoreTable[pos][pos + 1].put(toAdd, matching);
                    }
                } while (modified);
            }
            for (int span = 2; span < length; span++) {
                for (int begin = 0; begin < length - span; begin++) {
                    int end = begin + span;
                    logScoreTable[begin][end] = new HashMap<String, Double>();
                    history[begin][end] = new HashMap<String, Pair<String, String>>();
                    childLocs[begin][end] = new HashMap<String, Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>>();
                    for (int split = begin + 1; split < end; split++) {
                        for (String tag : logScoreTable[begin][split].keySet()) {
                            List<BinaryRule> binRules = grammar.getBinaryRulesByLeftChild(tag);
                            for (BinaryRule bRule : binRules) {
                                if (!logScoreTable[split][end].containsKey(bRule.getRightChild())) {
                                    continue;
                                }
                                double rightProb = logScoreTable[split][end].get(bRule.getRightChild());
                                double leftProb = logScoreTable[begin][split].get(tag);
                                double newProb = leftProb + rightProb + Math.log(bRule.getScore());
                                if ((!logScoreTable[begin][end].containsKey(bRule.getParent())) || newProb > logScoreTable[begin][end].get(bRule.getParent())) {
                                    logScoreTable[begin][end].put(bRule.getParent(), newProb);
                                    Pair<String, String> strPair = new Pair<String, String>(bRule.getLeftChild(), bRule.getRightChild());
                                    history[begin][end].put(bRule.getParent(), strPair);
                                    Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> locPair = new Pair(new Pair<Integer, Integer>(begin, split), new Pair<Integer, Integer>(split, end));
                                    childLocs[begin][end].put(bRule.getParent(), locPair);
                                }
                            }
                        }
                        boolean modified;
                        String toAdd = "";
                        double matching = 0.0;
                        do {
                            modified = false;
                            for (String tag : logScoreTable[begin][end].keySet()) {
                                List<UnaryRule> unaryCandidates = grammar.getUnaryRulesByChild(tag);
                                for (UnaryRule candidate : unaryCandidates) {
                                    double candidateProb = Math.log(candidate.getScore()) + logScoreTable[begin][end].get(tag);
                                    if ((!logScoreTable[begin][end].containsKey(candidate.getParent())) || candidateProb > logScoreTable[begin][end].get(candidate.getParent())) {
                                        toAdd = candidate.getParent();
                                        matching = candidateProb;
                                        Pair<String, String> strPair = new Pair<String, String>(candidate.getChild(), null);
                                        history[begin][end].put(candidate.getParent(), strPair);
                                        Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> locPair = new Pair(new Pair<Integer, Integer>(begin, end), null);
                                        childLocs[begin][end].put(candidate.getParent(), locPair);
                                        modified = true;
                                        break;
                                    }
                                }
                                if (modified) {
                                    break;
                                }
                            }
                            if (modified) {
                                logScoreTable[begin][end].put(toAdd, matching);
                            }
                        } while (modified);
                    }
                }
            }
            return TreeAnnotations.unAnnotateTree(this.buildTree(logScoreTable, history, childLocs));
        }

        private Tree<String> buildTree(Map<String, Double>[][] logScoreTable, Map<String, Pair<String, String>>[][] history, Map<String, Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>>[][] childLocs) {
            int length = logScoreTable.length;
            Tree<String> root = recBuildTree(history, childLocs, ROOT_STR, new Pair<Integer, Integer>(0, length - 1));
            return root;
        }

        private Tree<String> recBuildTree(Map<String, Pair<String, String>>[][] history, Map<String, Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>>[][] childLocs, String currTag, Pair<Integer, Integer> currPos) {
            Tree<String> nodeToRet = new Tree<String>(currTag);
            List<Tree<String>> children = new ArrayList<Tree<String>>();
            Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> childLocPair = childLocs[currPos.getFirst()][currPos.getSecond()].get(currTag);
            Pair<String, String> histPair = history[currPos.getFirst()][currPos.getSecond()].get(currTag);
            if (childLocPair == null) {
                if (histPair != null) {
                    Tree<String> leaf = new Tree<String>(histPair.getFirst());
                    children.add(leaf);
                } else {
                    System.out.println("Null hist tag: " + currTag);
                    System.out.println("Null hist pos: " + currPos);
                    System.out.println("Existing keys: " + history[currPos.getFirst()][currPos.getSecond()].keySet());
                    Tree<String> leaf = new Tree<String>(currSentence.get(currPos.getFirst()));
                    children.add(leaf);
                }
            } else if (histPair.getSecond() == null) {
                children.add(recBuildTree(history, childLocs, histPair.getFirst(), currPos));
            } else {
                children.add(recBuildTree(history, childLocs, histPair.getFirst(), childLocPair.getFirst()));
                children.add(recBuildTree(history, childLocs, histPair.getSecond(), childLocPair.getSecond()));
            }
            nodeToRet.setChildren(children);
            return nodeToRet;
        }
    }

    /**
   * Baseline parser (though not a baseline I've ever seen before).  Tags the
   * sentence using the baseline tagging method, then either retrieves a known
   * parse of that tag sequence, or builds a right-branching parse for unknown
   * tag sequences.
   */
    public static class BaselineParser implements Parser {

        CounterMap<List<String>, Tree<String>> knownParses;

        CounterMap<Integer, String> spanToCategories;

        Lexicon lexicon;

        public void train(List<Tree<String>> trainTrees) {
            lexicon = new Lexicon(trainTrees);
            knownParses = new CounterMap<List<String>, Tree<String>>();
            spanToCategories = new CounterMap<Integer, String>();
            for (Tree<String> trainTree : trainTrees) {
                List<String> tags = trainTree.getPreTerminalYield();
                knownParses.incrementCount(tags, trainTree, 1.0);
                tallySpans(trainTree, 0);
            }
        }

        public Tree<String> getBestParse(List<String> sentence) {
            List<String> tags = getBaselineTagging(sentence);
            if (knownParses.keySet().contains(tags)) {
                return getBestKnownParse(tags, sentence);
            }
            return buildRightBranchParse(sentence, tags);
        }

        private Tree<String> buildRightBranchParse(List<String> words, List<String> tags) {
            int currentPosition = words.size() - 1;
            Tree<String> rightBranchTree = buildTagTree(words, tags, currentPosition);
            while (currentPosition > 0) {
                currentPosition--;
                rightBranchTree = merge(buildTagTree(words, tags, currentPosition), rightBranchTree);
            }
            rightBranchTree = addRoot(rightBranchTree);
            return rightBranchTree;
        }

        private Tree<String> merge(Tree<String> leftTree, Tree<String> rightTree) {
            int span = leftTree.getYield().size() + rightTree.getYield().size();
            String mostFrequentLabel = spanToCategories.getCounter(span).argMax();
            List<Tree<String>> children = new ArrayList<Tree<String>>();
            children.add(leftTree);
            children.add(rightTree);
            return new Tree<String>(mostFrequentLabel, children);
        }

        private Tree<String> addRoot(Tree<String> tree) {
            return new Tree<String>(ROOT_STR, Collections.singletonList(tree));
        }

        private Tree<String> buildTagTree(List<String> words, List<String> tags, int currentPosition) {
            Tree<String> leafTree = new Tree<String>(words.get(currentPosition));
            Tree<String> tagTree = new Tree<String>(tags.get(currentPosition), Collections.singletonList(leafTree));
            return tagTree;
        }

        private Tree<String> getBestKnownParse(List<String> tags, List<String> sentence) {
            Tree<String> parse = knownParses.getCounter(tags).argMax().deepCopy();
            parse.setWords(sentence);
            return parse;
        }

        private List<String> getBaselineTagging(List<String> sentence) {
            List<String> tags = new ArrayList<String>();
            for (String word : sentence) {
                String tag = getBestTag(word);
                tags.add(tag);
            }
            return tags;
        }

        private String getBestTag(String word) {
            double bestScore = Double.NEGATIVE_INFINITY;
            String bestTag = null;
            for (String tag : lexicon.getAllTags()) {
                double score = lexicon.scoreTagging(word, tag);
                if (bestTag == null || score > bestScore) {
                    bestScore = score;
                    bestTag = tag;
                }
            }
            return bestTag;
        }

        private int tallySpans(Tree<String> tree, int start) {
            if (tree.isLeaf() || tree.isPreTerminal()) return 1;
            int end = start;
            for (Tree<String> child : tree.getChildren()) {
                int childSpan = tallySpans(child, end);
                end += childSpan;
            }
            String category = tree.getLabel();
            if (!category.equals(ROOT_STR)) spanToCategories.incrementCount(end - start, category, 1.0);
            return end - start;
        }
    }

    /**
   * Class which contains code for annotating and binarizing trees for
   * the parser's use, and debinarizing and unannotating them for
   * scoring.
   */
    public static class TreeAnnotations {

        public static List<Tree<String>> annotateAllTrees(List<Tree<String>> unAnnotatedTrees) {
            List<Tree<String>> toRet = new ArrayList<Tree<String>>();
            for (Tree<String> tree : unAnnotatedTrees) {
                toRet.add(annotateTree(tree));
            }
            return toRet;
        }

        public static Tree<String> annotateTree(Tree<String> unAnnotatedTree) {
            return binarizeTree(unAnnotatedTree);
        }

        public static void verticalMarkovizeAll(List<Tree<String>> trees) {
            for (Tree<String> tree : trees) {
                verticalMarkovize(tree);
            }
        }

        private static void verticalMarkovize(Tree<String> tree) {
            for (Tree<String> subTree : tree.getChildren()) {
                recMarkovize(subTree, tree.getLabel());
            }
        }

        private static void recMarkovize(Tree<String> tree, String parLabel) {
            if (!tree.isLeaf()) {
                for (Tree<String> subTree : tree.getChildren()) {
                    recMarkovize(subTree, tree.getLabel());
                }
                tree.setLabel(tree.getLabel() + "-^" + parLabel);
            }
        }

        private static Tree<String> binarizeTree(Tree<String> tree) {
            String label = tree.getLabel();
            if (tree.isLeaf()) return new Tree<String>(label);
            if (tree.getChildren().size() == 1) {
                return new Tree<String>(label, Collections.singletonList(binarizeTree(tree.getChildren().get(0))));
            }
            String intermediateLabel = "@" + label + "->";
            Tree<String> intermediateTree = binarizeTreeHelper(tree, 0, intermediateLabel);
            return new Tree<String>(label, intermediateTree.getChildren());
        }

        private static Tree<String> binarizeTreeHelper(Tree<String> tree, int numChildrenGenerated, String intermediateLabel) {
            Tree<String> leftTree = tree.getChildren().get(numChildrenGenerated);
            List<Tree<String>> children = new ArrayList<Tree<String>>();
            children.add(binarizeTree(leftTree));
            if (numChildrenGenerated < tree.getChildren().size() - 1) {
                Tree<String> rightTree = binarizeTreeHelper(tree, numChildrenGenerated + 1, intermediateLabel + "_" + leftTree.getLabel());
                children.add(rightTree);
            }
            return new Tree<String>(intermediateLabel, children);
        }

        public static Tree<String> unAnnotateTree(Tree<String> annotatedTree) {
            Tree<String> debinarizedTree = Trees.spliceNodes(annotatedTree, new Filter<String>() {

                public boolean accept(String s) {
                    return s.startsWith("@");
                }
            });
            Tree<String> unAnnotatedTree = (new Trees.FunctionNodeStripper()).transformTree(debinarizedTree);
            recDeMarcovize(unAnnotatedTree);
            return unAnnotatedTree;
        }
    }

    public static void recDeMarcovize(Tree<String> tree) {
        if (tree.isLeaf()) {
            return;
        }
        int index = tree.getLabel().indexOf("-^");
        if (index != -1) {
            tree.setLabel(tree.getLabel().substring(0, index));
        }
        for (Tree<String> subTree : tree.getChildren()) {
            recDeMarcovize(subTree);
        }
    }

    /**
   * Simple default implementation of a lexicon, which scores word,
   * tag pairs with a smoothed estimate of P(tag|word)/P(tag).
   */
    public static class Lexicon {

        CounterMap<String, String> wordToTagCounters = new CounterMap<String, String>();

        double totalTokens = 0.0;

        double totalWordTypes = 0.0;

        Counter<String> tagCounter = new Counter<String>();

        Counter<String> wordCounter = new Counter<String>();

        Counter<String> typeTagCounter = new Counter<String>();

        public Set<String> getAllTags() {
            return tagCounter.keySet();
        }

        public boolean isKnown(String word) {
            return wordCounter.keySet().contains(word);
        }

        public double scoreTagging(String word, String tag) {
            double p_tag = tagCounter.getCount(tag) / totalTokens;
            double c_word = wordCounter.getCount(word);
            double c_tag_and_word = wordToTagCounters.getCount(word, tag);
            if (c_word < 10) {
                c_word += 1.0;
                c_tag_and_word += typeTagCounter.getCount(tag) / totalWordTypes;
            }
            double p_word = (1.0 + c_word) / (totalTokens + totalWordTypes);
            double p_tag_given_word = c_tag_and_word / c_word;
            return p_tag_given_word / p_tag * p_word;
        }

        public Lexicon(List<Tree<String>> trainTrees) {
            for (Tree<String> trainTree : trainTrees) {
                List<String> words = trainTree.getYield();
                List<String> tags = trainTree.getPreTerminalYield();
                for (int position = 0; position < words.size(); position++) {
                    String word = words.get(position);
                    String tag = tags.get(position);
                    tallyTagging(word, tag);
                }
            }
        }

        private void tallyTagging(String word, String tag) {
            if (!isKnown(word)) {
                totalWordTypes += 1.0;
                typeTagCounter.incrementCount(tag, 1.0);
            }
            totalTokens += 1.0;
            tagCounter.incrementCount(tag, 1.0);
            wordCounter.incrementCount(word, 1.0);
            wordToTagCounters.incrementCount(word, tag, 1.0);
        }
    }

    /**
   * Simple implementation of a PCFG grammar, offering the ability to
   * look up rules by their child symbols.  Rule probability estimates
   * are just relative frequency estimates off of training trees.
   */
    public static class Grammar {

        Map<String, List<BinaryRule>> binaryRulesByLeftChild = new HashMap<String, List<BinaryRule>>();

        Map<String, List<BinaryRule>> binaryRulesByRightChild = new HashMap<String, List<BinaryRule>>();

        Map<String, List<UnaryRule>> unaryRulesByChild = new HashMap<String, List<UnaryRule>>();

        public List<BinaryRule> getBinaryRulesByLeftChild(String leftChild) {
            return CollectionUtils.getValueList(binaryRulesByLeftChild, leftChild);
        }

        public List<BinaryRule> getBinaryRulesByRightChild(String rightChild) {
            return CollectionUtils.getValueList(binaryRulesByRightChild, rightChild);
        }

        public List<UnaryRule> getUnaryRulesByChild(String child) {
            return CollectionUtils.getValueList(unaryRulesByChild, child);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            List<String> ruleStrings = new ArrayList<String>();
            for (String leftChild : binaryRulesByLeftChild.keySet()) {
                for (BinaryRule binaryRule : getBinaryRulesByLeftChild(leftChild)) {
                    ruleStrings.add(binaryRule.toString());
                }
            }
            for (String child : unaryRulesByChild.keySet()) {
                for (UnaryRule unaryRule : getUnaryRulesByChild(child)) {
                    ruleStrings.add(unaryRule.toString());
                }
            }
            for (String ruleString : CollectionUtils.sort(ruleStrings)) {
                sb.append(ruleString);
                sb.append("\n");
            }
            return sb.toString();
        }

        private void addBinary(BinaryRule binaryRule) {
            CollectionUtils.addToValueList(binaryRulesByLeftChild, binaryRule.getLeftChild(), binaryRule);
            CollectionUtils.addToValueList(binaryRulesByRightChild, binaryRule.getRightChild(), binaryRule);
        }

        private void addUnary(UnaryRule unaryRule) {
            CollectionUtils.addToValueList(unaryRulesByChild, unaryRule.getChild(), unaryRule);
        }

        public Grammar(List<Tree<String>> trainTrees) {
            Counter<UnaryRule> unaryRuleCounter = new Counter<UnaryRule>();
            Counter<BinaryRule> binaryRuleCounter = new Counter<BinaryRule>();
            Counter<String> symbolCounter = new Counter<String>();
            for (Tree<String> trainTree : trainTrees) {
                tallyTree(trainTree, symbolCounter, unaryRuleCounter, binaryRuleCounter);
            }
            for (UnaryRule unaryRule : unaryRuleCounter.keySet()) {
                double unaryProbability = unaryRuleCounter.getCount(unaryRule) / symbolCounter.getCount(unaryRule.getParent());
                unaryRule.setScore(unaryProbability);
                addUnary(unaryRule);
            }
            for (BinaryRule binaryRule : binaryRuleCounter.keySet()) {
                double binaryProbability = binaryRuleCounter.getCount(binaryRule) / symbolCounter.getCount(binaryRule.getParent());
                binaryRule.setScore(binaryProbability);
                addBinary(binaryRule);
            }
        }

        private void tallyTree(Tree<String> tree, Counter<String> symbolCounter, Counter<UnaryRule> unaryRuleCounter, Counter<BinaryRule> binaryRuleCounter) {
            if (tree.isLeaf()) return;
            if (tree.isPreTerminal()) return;
            if (tree.getChildren().size() == 1) {
                UnaryRule unaryRule = makeUnaryRule(tree);
                symbolCounter.incrementCount(tree.getLabel(), 1.0);
                unaryRuleCounter.incrementCount(unaryRule, 1.0);
            }
            if (tree.getChildren().size() == 2) {
                BinaryRule binaryRule = makeBinaryRule(tree);
                symbolCounter.incrementCount(tree.getLabel(), 1.0);
                binaryRuleCounter.incrementCount(binaryRule, 1.0);
            }
            if (tree.getChildren().size() < 1 || tree.getChildren().size() > 2) {
                throw new RuntimeException("Attempted to construct a Grammar with an illegal tree: " + tree);
            }
            for (Tree<String> child : tree.getChildren()) {
                tallyTree(child, symbolCounter, unaryRuleCounter, binaryRuleCounter);
            }
        }

        private UnaryRule makeUnaryRule(Tree<String> tree) {
            return new UnaryRule(tree.getLabel(), tree.getChildren().get(0).getLabel());
        }

        private BinaryRule makeBinaryRule(Tree<String> tree) {
            return new BinaryRule(tree.getLabel(), tree.getChildren().get(0).getLabel(), tree.getChildren().get(1).getLabel());
        }
    }

    /**
   * Forces rules through a common interface, which allows for polymorphism...
   */
    public static interface Rule {

        public String getParent();

        public double getScore();

        public void setScore(double score);
    }

    public static class BinaryRule implements Rule {

        String parent;

        String leftChild;

        String rightChild;

        double score;

        public String getParent() {
            return parent;
        }

        public String getLeftChild() {
            return leftChild;
        }

        public String getRightChild() {
            return rightChild;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BinaryRule)) return false;
            final BinaryRule binaryRule = (BinaryRule) o;
            if (leftChild != null ? !leftChild.equals(binaryRule.leftChild) : binaryRule.leftChild != null) return false;
            if (parent != null ? !parent.equals(binaryRule.parent) : binaryRule.parent != null) return false;
            if (rightChild != null ? !rightChild.equals(binaryRule.rightChild) : binaryRule.rightChild != null) return false;
            return true;
        }

        public int hashCode() {
            int result;
            result = (parent != null ? parent.hashCode() : 0);
            result = 29 * result + (leftChild != null ? leftChild.hashCode() : 0);
            result = 29 * result + (rightChild != null ? rightChild.hashCode() : 0);
            return result;
        }

        public String toString() {
            return parent + " -> " + leftChild + " " + rightChild + " %% " + score;
        }

        public BinaryRule(String parent, String leftChild, String rightChild) {
            this.parent = parent;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }
    }

    /** A unary grammar rule with score representing its probability. */
    public static class UnaryRule implements Rule {

        String parent;

        String child;

        double score;

        public String getParent() {
            return parent;
        }

        public String getChild() {
            return child;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof UnaryRule)) return false;
            final UnaryRule unaryRule = (UnaryRule) o;
            if (child != null ? !child.equals(unaryRule.child) : unaryRule.child != null) return false;
            if (parent != null ? !parent.equals(unaryRule.parent) : unaryRule.parent != null) return false;
            return true;
        }

        public int hashCode() {
            int result;
            result = (parent != null ? parent.hashCode() : 0);
            result = 29 * result + (child != null ? child.hashCode() : 0);
            return result;
        }

        public String toString() {
            return parent + " -> " + child + " %% " + score;
        }

        public UnaryRule(String parent, String child) {
            this.parent = parent;
            this.child = child;
        }
    }

    private static int MAX_LENGTH = 20;

    private static void testParser(Parser parser, List<Tree<String>> testTrees) {
        testParser(parser, testTrees, null);
    }

    private static void testParser(Parser parser, List<Tree<String>> testTrees, ProbabilisticClassifier nerClassifier) {
        EnglishPennTreebankParseEvaluator.LabeledConstituentEval<String> eval = new EnglishPennTreebankParseEvaluator.LabeledConstituentEval<String>(Collections.singleton(ROOT_STR), new HashSet<String>(Arrays.asList(new String[] { "''", "``", ".", ":", "," })));
        for (Tree<String> testTree : testTrees) {
            List<String> testSentence = testTree.getYield();
            if (nerClassifier != null) {
                System.err.println(testTree);
                System.err.println("test sentence: " + testSentence);
                List<Pair<String, String>> chunkedSentence = MaximumEntropyClassifierTester.labelAndChunkSequence(nerClassifier, testSentence);
                testSentence = new ArrayList<String>();
                for (Pair<String, String> chunkAndLabel : chunkedSentence) {
                    testSentence.add(chunkAndLabel.getFirst());
                }
                System.err.println("chunked sentence: " + testSentence);
            }
            if (testSentence.size() > MAX_LENGTH) continue;
            Tree<String> guessedTree = parser.getBestParse(testSentence);
            System.out.println("Guess:\n" + Trees.PennTreeRenderer.render(guessedTree));
            System.out.println("Gold:\n" + Trees.PennTreeRenderer.render(testTree));
            eval.evaluate(guessedTree, testTree);
        }
        eval.display(true);
    }

    private static List<Tree<String>> readTrees(String basePath, int low, int high) {
        Collection<Tree<String>> trees = PennTreebankReader.readTrees(basePath, low, high);
        Trees.TreeTransformer<String> treeTransformer = new Trees.StandardTreeNormalizer();
        List<Tree<String>> normalizedTreeList = new ArrayList<Tree<String>>();
        for (Tree<String> tree : trees) {
            Tree<String> normalizedTree = treeTransformer.transformTree(tree);
            normalizedTreeList.add(normalizedTree);
        }
        return normalizedTreeList;
    }

    private static List<Tree<String>> readTrees(String basePath) {
        Collection<Tree<String>> trees = PennTreebankReader.readTrees(basePath);
        Trees.TreeTransformer<String> treeTransformer = new Trees.StandardTreeNormalizer();
        List<Tree<String>> normalizedTreeList = new ArrayList<Tree<String>>();
        for (Tree<String> tree : trees) {
            Tree<String> normalizedTree = treeTransformer.transformTree(tree);
            normalizedTreeList.add(normalizedTree);
        }
        return normalizedTreeList;
    }

    private static List<Tree<String>> readGENIATrees(String basePath, int low, int high) {
        Collection<Tree<String>> trees = GENIATreebankReader.readTrees(basePath, low, high);
        Trees.TreeTransformer<String> treeTransformer = new Trees.StandardTreeNormalizer();
        List<Tree<String>> normalizedTreeList = new ArrayList<Tree<String>>();
        for (Tree<String> tree : trees) {
            Tree<String> normalizedTree = treeTransformer.transformTree(tree);
            normalizedTreeList.add(normalizedTree);
        }
        return normalizedTreeList;
    }

    private static List<Tree<String>> readGENIATrees(String basePath) {
        Collection<Tree<String>> trees = GENIATreebankReader.readTrees(basePath);
        Trees.TreeTransformer<String> treeTransformer = new Trees.StandardTreeNormalizer();
        List<Tree<String>> normalizedTreeList = new ArrayList<Tree<String>>();
        for (Tree<String> tree : trees) {
            System.out.println("Tree: " + tree);
            Tree<String> normalizedTree = treeTransformer.transformTree(tree);
            normalizedTreeList.add(normalizedTree);
        }
        return normalizedTreeList;
    }

    private static List<Tree<String>> readBioIETrees(String basePath, int low, int high) {
        Collection<Tree<String>> trees = BioIETreebankReader.readTrees(basePath, low, high);
        Trees.TreeTransformer<String> treeTransformer = new Trees.StandardTreeNormalizer();
        List<Tree<String>> normalizedTreeList = new ArrayList<Tree<String>>();
        for (Tree<String> tree : trees) {
            Tree<String> normalizedTree = treeTransformer.transformTree(tree);
            normalizedTreeList.add(normalizedTree);
        }
        return normalizedTreeList;
    }

    private static List<Tree<String>> readBioIETrees(String basePath) {
        Collection<Tree<String>> trees = BioIETreebankReader.readTrees(basePath);
        Trees.TreeTransformer<String> treeTransformer = new Trees.StandardTreeNormalizer();
        List<Tree<String>> normalizedTreeList = new ArrayList<Tree<String>>();
        for (Tree<String> tree : trees) {
            System.out.println("Tree: " + tree);
            Tree<String> normalizedTree = treeTransformer.transformTree(tree);
            normalizedTreeList.add(normalizedTree);
        }
        return normalizedTreeList;
    }

    public static void main(String[] args) {
        Map<String, String> options = new HashMap<String, String>();
        options.put("-path", "/afs/ir/class/cs224n/pa3/data/parser/");
        options.put("-data", "ptb");
        options.put("-parser", "cs224n.assignments.PCFGParserTester$PCFGParser");
        options.put("-maxLength", "20");
        options.putAll(CommandLineUtils.simpleCommandLineParser(args));
        System.out.println("PCFGParserTester options:");
        for (Map.Entry<String, String> entry : options.entrySet()) {
            System.out.printf("  %-12s: %s%n", entry.getKey(), entry.getValue());
        }
        System.out.println();
        MAX_LENGTH = Integer.parseInt(options.get("-maxLength"));
        Parser parser;
        try {
            Class parserClass = Class.forName(options.get("-parser"));
            parser = (Parser) parserClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Using parser: " + parser);
        String basePath = options.get("-path");
        String preBasePath = basePath;
        String dataSet = options.get("-data");
        if (!basePath.endsWith("/")) basePath += "/";
        System.out.println("Data will be loaded from: " + basePath + "\n");
        List<Tree<String>> trainTrees = new ArrayList<Tree<String>>(), validationTrees = new ArrayList<Tree<String>>(), testTrees = new ArrayList<Tree<String>>();
        if (dataSet.equals("miniTest")) {
            basePath += "parser/" + dataSet;
            System.out.println("Loading training trees...");
            trainTrees = readTrees(basePath, 1, 3);
            System.out.println("done.");
            System.out.println("Loading test trees...");
            testTrees = readTrees(basePath, 4, 4);
            System.out.println("done.");
        }
        if (dataSet.equals("genia") || dataSet.equals("combo")) {
            basePath += "parser/";
            System.out.println("Loading GENIA training trees... from: " + basePath + "genia");
            trainTrees.addAll(readGENIATrees(basePath + "genia", 0, 440));
            System.out.println("done.");
            System.out.println("Train trees size: " + trainTrees.size());
            System.out.println("First train tree: " + Trees.PennTreeRenderer.render(trainTrees.get(0)));
            System.out.println("Last train tree: " + Trees.PennTreeRenderer.render(trainTrees.get(trainTrees.size() - 1)));
            System.out.println("Loading GENIA test trees...");
            testTrees.addAll(readGENIATrees(basePath + "genia", 481, 500));
            System.out.println("Test trees size: " + testTrees.size());
            System.out.println("done.");
            System.out.println("First train tree: " + Trees.PennTreeRenderer.render(testTrees.get(0)));
            System.out.println("Last train tree: " + Trees.PennTreeRenderer.render(testTrees.get(testTrees.size() - 1)));
        }
        if (dataSet.equals("bioie") || dataSet.equals("combo")) {
            if (!dataSet.equals("combo")) basePath += "parser/";
            System.out.println("Loading BioIE training trees...");
            trainTrees.addAll(readBioIETrees(basePath + "bioie", 0, 580));
            System.out.println("done.");
            System.out.println("Train trees size: " + trainTrees.size());
            System.out.println("First train tree: " + Trees.PennTreeRenderer.render(trainTrees.get(0)));
            System.out.println("Last train tree: " + Trees.PennTreeRenderer.render(trainTrees.get(trainTrees.size() - 1)));
            System.out.println("Loading BioIE test trees...");
            testTrees.addAll(readBioIETrees(basePath + "bioie", 613, 645));
            System.out.println("Test trees size: " + testTrees.size());
            System.out.println("done.");
            System.out.println("First train tree: " + Trees.PennTreeRenderer.render(testTrees.get(0)));
            System.out.println("Last train tree: " + Trees.PennTreeRenderer.render(testTrees.get(testTrees.size() - 1)));
        }
        if (!dataSet.equals("miniTest") && !dataSet.equals("genia") && !dataSet.equals("bioie") && !dataSet.equals("combo")) {
            throw new RuntimeException("Bad data set: " + dataSet + ": use miniTest, genia, bioie, or combo (genia and bioie).");
        }
        if (options.containsKey("-testData")) {
            System.out.println("Loading " + options.get("-testData") + " test trees ...");
            testTrees.clear();
            testTrees = readTrees(preBasePath + "parser/" + options.get("-testData"));
            System.out.println("Test trees size: " + testTrees.size());
        }
        System.out.println("\nTraining parser...");
        parser.train(trainTrees);
        ProbabilisticClassifier nerClassifier = null;
        if (options.get("-nerTrainFile") != null) {
            System.out.println("\nTraining NER classifier...");
            try {
                nerClassifier = MaximumEntropyClassifierTester.getClassifier(options.get("-nerTrainFile") + ".train");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("\nTesting parser...");
        testParser(parser, testTrees, nerClassifier);
    }
}
