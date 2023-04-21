package org.jasen.core.linguistics;

import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import java.io.IOException;
import java.util.Arrays;
import org.jasen.core.token.SimpleWordTokenizer;

/**
 * Employes a lexical tree approach to word recognition.
 * <p>
 * Based on a sample corpus, the analyser builds a tree of characters such that each characters in a word is a node in the tree.
 * </p>
 * <p>
 * When a word with a similar character sequence is found, the path to the next character is strengthened
 * </p>
 * @author Jason Polites
 */
public class LexicalTreeAnalyzer {

    private String[] tokens;

    private Char2ObjectOpenHashMap forwardTree;

    private Char2ObjectOpenHashMap reverseTree;

    private static final String ENGLISH_DICTIONARY_PATH = "org/jasen/core/linguistics/dictionaries/english.dic";

    /**
	 * Creates and initialized the analyzer
	 */
    public void initialize() throws IOException {
        SimpleWordTokenizer t = new SimpleWordTokenizer(this.getClass().getClassLoader().getResourceAsStream(ENGLISH_DICTIONARY_PATH));
        t.tokenize();
        tokens = t.getTokens();
        Arrays.sort(tokens);
        buildTrees();
    }

    /**
	 * Computes the probability that the given sequence of characters is an English word.
	 * <p>
	 * This works on the premise that most English words exhibit a similar set of character sequence patterns 
	 * in both their prefix, body and suffix.
	 * </p>
	 * The value of the word is determined by analysis if the characters in the word
	 * against the values in both the forward and backward lexical trees.
	 * <BR><BR>
	 * The maximium possible value a word can have is 1 (100%), thus for each character in the word
	 * which is correctly positioned in accordance with the rules in the tree, the computed value is
	 * increased by 1/W where 'W' is the length of the word; such that if a word perfectly matches a
	 * branch of the tree a result of 1/W x W (or 1) will be returned.
	 * <BR><BR>
	 * Where a word fails to match a forward branch perfectly, two things are done:
	 * <OL>
	 * 	<LI>For each remaining character in the token, the current total is reduced by the same percentile fraction as used to calculate the total.</LI>
	 * 	<LI>The token is given a "second chance" by repeating the initial calculation process with the reverse tree.</LI>
	 * </OL>
	 * @param word The word to be tested
	 * @return A value between 0.0 and 1.0 indicating the probability that the String is an English word.
	 */
    public double computeWordValue(String word) {
        word = word.toLowerCase();
        double result;
        if (Arrays.binarySearch(tokens, word) > -1) {
            result = 1.0d;
        } else {
            double percentile = (1.0d / (double) word.length());
            result = computeWordValue(forwardTree, word, true, 0, percentile, 0.0d);
            if (result < 0.0d) {
                result = 0.0d;
            }
        }
        return result;
    }

    /**
	 * The value of the word is determined by analysis if the characters in the word
	 * against the values in both the forward and backward lexical trees
	 * <BR><BR>
	 * The maximium possible value a word can have is 1 (100%), thus for each character in the word
	 * which is correctly positioned in accordance with the rules in the tree, the computed value is
	 * increased by 1/W where 'W' is the length of the word; such that if a word perfectly matches a
	 * branch of the tree a result of 1/W x W (or 1) will be returned
	 * <BR><BR>
	 * Where a word fails to match a forward branch perfectly, two things are done:
	 * <OL>
	 * 	<LI>For each remaining character in the token, the current total is reduced by the same percentile fraction as used to calculate the total</LI>
	 * 	<LI>The token is given a "second chance" by repeating the initial calculation process with the reverse tree</LI>
	 * </OL>
	 * @param node
	 * @param word
	 * @param index
	 * @return
	 */
    private double computeWordValue(Char2ObjectOpenHashMap node, String word, boolean forward, int index, double percentile, double total) {
        char chr = word.charAt(index);
        Char2ObjectOpenHashMap next = (Char2ObjectOpenHashMap) node.get(chr);
        if (next != null) {
            total += percentile;
            if (forward) {
                index++;
                if (index < word.length()) {
                    total = computeWordValue(next, word, forward, index, percentile, total);
                }
            } else {
                index--;
                if (index >= 0) {
                    total = computeWordValue(next, word, forward, index, percentile, total);
                }
            }
        } else if (index < word.length() - 1 && forward) {
            for (int i = index; i < word.length(); i++) {
                total -= percentile;
            }
            total = computeWordValue(reverseTree, word, false, (word.length() - 1), percentile, total);
        }
        return total;
    }

    /**
	 * Loops through the list of tokens and builds a character tree
	 *
	 */
    private void buildTrees() {
        forwardTree = new Char2ObjectOpenHashMap();
        reverseTree = new Char2ObjectOpenHashMap();
        String token = null;
        char chr;
        Char2ObjectOpenHashMap currentNode = null;
        Char2ObjectOpenHashMap targetNode = null;
        for (int i = 0; i < tokens.length; i++) {
            token = tokens[i];
            if (token != null) {
                currentNode = forwardTree;
                for (int j = 0; j < token.length(); j++) {
                    chr = token.charAt(j);
                    targetNode = (Char2ObjectOpenHashMap) currentNode.get(chr);
                    if (targetNode == null) {
                        targetNode = new Char2ObjectOpenHashMap(1);
                        currentNode.put(chr, targetNode);
                    }
                    currentNode = targetNode;
                }
                currentNode = reverseTree;
                for (int j = token.length() - 1; j >= 0; j--) {
                    chr = token.charAt(j);
                    targetNode = (Char2ObjectOpenHashMap) currentNode.get(chr);
                    if (targetNode == null) {
                        targetNode = new Char2ObjectOpenHashMap(1);
                        currentNode.put(chr, targetNode);
                    }
                    currentNode = targetNode;
                }
            }
        }
    }
}
