package joshua.sarray;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import joshua.util.Cache;
import joshua.util.ReverseOrder;
import joshua.util.io.BinaryIn;
import joshua.util.sentence.Phrase;
import joshua.util.sentence.Vocabulary;

/**
 * Represents the most frequent phrases in a corpus.
 * 
 * @author Chris Callison-Burch
 * @author Lane Schwartz
 */
public class FrequentPhrases {

    /** Logger for this class. */
    private static final Logger logger = Logger.getLogger(FrequentPhrases.class.getName());

    /** Suffix array in which frequent phrases are located. */
    private final Suffixes suffixes;

    /** 
	 * Stores the number of times a phrase occurred in the corpus.
	 * <p>
	 * The iteration order of this map should 
	 * start with the most frequent phrase and end
	 * with the least frequent phrase stored in the map.
	 * <p>
	 * The key set for this map should be identical to
	 * the key set in the <code>ranks</code> map. 
	 */
    private final LinkedHashMap<Phrase, Integer> frequentPhrases;

    /** Maximum number of phrases of which this object is aware. */
    private final short maxPhrases;

    /**
	 * Constructs data regarding the frequencies 
	 * of the <em>n</em> most frequent phrases found in the corpus
	 * backed by the provided suffix array.
	 * 
	 * @param suffixes Suffix array corresponding to a corpus.
	 * @param minFrequency The minimum frequency required to 
	 *                     for a phrase to be considered frequent.
	 * @param maxPhrases The number of phrases to consider.
	 * @param maxPhraseLength Maximum phrase length to consider.
	 */
    public FrequentPhrases(Suffixes suffixes, int minFrequency, short maxPhrases, int maxPhraseLength) {
        this.maxPhrases = maxPhrases;
        this.suffixes = suffixes;
        this.frequentPhrases = getMostFrequentPhrases(suffixes, minFrequency, maxPhrases, maxPhraseLength);
    }

    /**
	 * This method performs a one-pass computation of the
	 * collocation of two frequent subphrases. It is used for
	 * the precalculation of the translations of hierarchical
	 * phrases which are problematic to calculate on the fly.
	 * This procedure is described in "Hierarchical Phrase-Based
	 * Translation with Suffix Arrays" by Adam Lopez.
	 *
	 * @param phrases         the phrases which are to be checked
	 *                        for their collocation
	 * @param maxPhraseLength the maximum length of any phrase
	 *                        in the phrases
	 * @param windowSize      the maximum allowable space between
	 *                        phrases for them to still be
	 *                        considered collocated
	 */
    public FrequentMatches getCollocations(int maxPhraseLength, int windowSize, short minNonterminalSpan) {
        logger.fine("Calculating number of frequent collocations");
        int totalCollocations = countCollocations(maxPhraseLength, windowSize);
        logger.fine("Total collocations: " + totalCollocations);
        FrequentMatches collocations = new FrequentMatches(getRanks(frequentPhrases), maxPhrases, totalCollocations, minNonterminalSpan);
        LinkedList<Phrase> phrasesInWindow = new LinkedList<Phrase>();
        LinkedList<Integer> positions = new LinkedList<Integer>();
        int sentenceNumber = 1;
        int endOfSentence = suffixes.getSentencePosition(sentenceNumber);
        if (logger.isLoggable(Level.FINER)) logger.finer("END OF SENT: " + sentenceNumber + " at position " + endOfSentence);
        Corpus corpus = suffixes.getCorpus();
        for (int currentPosition = 0, endOfCorpus = suffixes.size(); currentPosition < endOfCorpus; currentPosition++) {
            for (int i = 1, endOfPhrase = currentPosition + i; i < maxPhraseLength && endOfPhrase <= endOfSentence && endOfPhrase <= endOfCorpus; i++, endOfPhrase = currentPosition + i) {
                Phrase phrase = new ContiguousPhrase(currentPosition, endOfPhrase, corpus);
                if (logger.isLoggable(Level.FINEST)) logger.finest("Found phrase (" + currentPosition + "," + endOfPhrase + ") " + phrase);
                if (frequentPhrases.containsKey(phrase)) {
                    if (logger.isLoggable(Level.FINER)) logger.finer("\"" + phrase + "\" found at currentPosition " + currentPosition);
                    phrasesInWindow.add(phrase);
                    positions.add(currentPosition);
                }
            }
            if (currentPosition == endOfSentence) {
                if (logger.isLoggable(Level.FINEST)) {
                    logger.finest("REACHED END OF SENT: " + currentPosition);
                    logger.finest("PHRASES:   " + phrasesInWindow);
                    logger.finest("POSITIONS: " + positions);
                }
                for (int i = 0, n = phrasesInWindow.size(); i < n; i++) {
                    Phrase phrase1 = phrasesInWindow.remove();
                    int position1 = positions.remove();
                    Iterator<Phrase> phraseIterator = phrasesInWindow.iterator();
                    Iterator<Integer> positionIterator = positions.iterator();
                    for (int j = i + 1; j < n; j++) {
                        Phrase phrase2 = phraseIterator.next();
                        int position2 = positionIterator.next();
                        if (logger.isLoggable(Level.FINEST)) logger.finest("CASE1: " + phrase1 + "\t" + phrase2 + "\t" + position1 + "\t" + position2);
                        collocations.add(phrase1, phrase2, position1, position2);
                    }
                }
                phrasesInWindow.clear();
                positions.clear();
                sentenceNumber++;
                endOfSentence = suffixes.getSentencePosition(sentenceNumber) - 1;
                if (logger.isLoggable(Level.FINER)) logger.finer("END OF SENT: " + sentenceNumber + " at position " + endOfSentence);
            }
            if (phrasesInWindow.size() > 0) {
                int position1 = positions.peek();
                while ((position1 + windowSize < currentPosition) && phrasesInWindow.size() > 0) {
                    if (logger.isLoggable(Level.FINEST)) logger.finest("OUTSIDE OF WINDOW: " + position1 + " " + currentPosition + " " + windowSize);
                    Phrase phrase1 = phrasesInWindow.remove();
                    positions.remove();
                    Iterator<Phrase> phraseIterator = phrasesInWindow.iterator();
                    Iterator<Integer> positionIterator = positions.iterator();
                    for (int j = 0, n = phrasesInWindow.size(); j < n; j++) {
                        Phrase phrase2 = phraseIterator.next();
                        int position2 = positionIterator.next();
                        collocations.add(phrase1, phrase2, position1, position2);
                        if (logger.isLoggable(Level.FINEST)) logger.finest("CASE2: " + phrase1 + "\t" + phrase2 + "\t" + position1 + "\t" + position2);
                    }
                    if (phrasesInWindow.size() > 0) {
                        position1 = positions.peek();
                    } else {
                        position1 = currentPosition;
                    }
                }
            }
        }
        if (logger.isLoggable(Level.FINE)) logger.fine("Sorting collocations");
        collocations.histogramSort();
        return collocations;
    }

    /**
	 * This method performs a one-pass computation of the
	 * collocation of two frequent subphrases. It is used for
	 * the precalculation of the translations of hierarchical
	 * phrases which are problematic to calculate on the fly.
	 * This procedure is described in "Hierarchical Phrase-Based
	 * Translation with Suffix Arrays" by Adam Lopez.
	 *
	 * @param phrases         the phrases which are to be checked
	 *                        for their collocation
	 * @param maxPhraseLength the maximum length of any phrase
	 *                        in the phrases
	 * @param windowSize      the maximum allowable space between
	 *                        phrases for them to still be
	 *                        considered collocated
	 */
    protected int countCollocations(int maxPhraseLength, int windowSize) {
        int count = 0;
        LinkedList<Phrase> phrasesInWindow = new LinkedList<Phrase>();
        LinkedList<Integer> positions = new LinkedList<Integer>();
        int sentenceNumber = 1;
        int endOfSentence = suffixes.getSentencePosition(sentenceNumber);
        if (logger.isLoggable(Level.FINEST)) logger.finest("END OF SENT: " + endOfSentence);
        Corpus corpus = suffixes.getCorpus();
        for (int currentPosition = 0, endOfCorpus = suffixes.size(); currentPosition < endOfCorpus; currentPosition++) {
            for (int i = 1, endOfPhrase = currentPosition + i; i < maxPhraseLength && endOfPhrase <= endOfSentence && endOfPhrase <= endOfCorpus; i++, endOfPhrase = currentPosition + i) {
                Phrase phrase = new ContiguousPhrase(currentPosition, endOfPhrase, corpus);
                if (logger.isLoggable(Level.FINEST)) logger.finest("Found phrase (" + currentPosition + "," + endOfPhrase + ") " + phrase);
                if (frequentPhrases.containsKey(phrase)) {
                    if (logger.isLoggable(Level.FINER)) logger.finer("\"" + phrase + "\" found at currentPosition " + currentPosition);
                    phrasesInWindow.add(phrase);
                    positions.add(currentPosition);
                }
            }
            if (currentPosition == endOfSentence) {
                if (logger.isLoggable(Level.FINEST)) {
                    logger.finest("REACHED END OF SENT: " + currentPosition);
                    logger.finest("PHRASES:   " + phrasesInWindow);
                    logger.finest("POSITIONS: " + positions);
                }
                for (int i = 0, n = phrasesInWindow.size(); i < n; i++) {
                    Phrase phrase1 = phrasesInWindow.removeFirst();
                    int position1 = positions.removeFirst();
                    Iterator<Phrase> phraseIterator = phrasesInWindow.iterator();
                    Iterator<Integer> positionIterator = positions.iterator();
                    for (int j = i + 1; j < n; j++) {
                        Phrase phrase2 = phraseIterator.next();
                        int position2 = positionIterator.next();
                        if (logger.isLoggable(Level.FINEST)) logger.finest("CASE1: " + phrase1 + "\t" + phrase2 + "\t" + position1 + "\t" + position2);
                        count++;
                    }
                }
                phrasesInWindow.clear();
                positions.clear();
                sentenceNumber++;
                endOfSentence = suffixes.getSentencePosition(sentenceNumber) - 1;
                if (logger.isLoggable(Level.FINER)) logger.finer("END OF SENT: " + sentenceNumber + " at position " + endOfSentence);
            }
            if (phrasesInWindow.size() > 0) {
                int position1 = positions.get(0);
                while ((position1 + windowSize < currentPosition) && phrasesInWindow.size() > 0) {
                    if (logger.isLoggable(Level.FINEST)) logger.finest("OUTSIDE OF WINDOW: " + position1 + " " + currentPosition + " " + windowSize);
                    Phrase phrase1 = phrasesInWindow.removeFirst();
                    positions.removeFirst();
                    Iterator<Phrase> phraseIterator = phrasesInWindow.iterator();
                    Iterator<Integer> positionIterator = positions.iterator();
                    for (int j = 0, n = phrasesInWindow.size(); j < n; j++) {
                        Phrase phrase2 = phraseIterator.next();
                        int position2 = positionIterator.next();
                        count++;
                        if (logger.isLoggable(Level.FINEST)) logger.finest("CASE2: " + phrase1 + "\t" + phrase2 + "\t" + position1 + "\t" + position2);
                    }
                    if (phrasesInWindow.size() > 0) {
                        position1 = positions.getFirst();
                    } else {
                        position1 = currentPosition;
                    }
                }
            }
        }
        return count;
    }

    /**
	 * Calculates the frequency ranks of the provided phrases.
	 * <p>
	 * The iteration order of the <code>frequentPhrases</code> parameter
	 * is be used by this method to determine the rank of each phrase.
	 * Specifically, the first phrase returned by the map's iterator is 
	 * taken to be the most frequent phrase; the last phrase returned by the
	 * map's iterator is taken to be the least frequent phrase.
	 * 
	 * @param frequentPhrases Map from phrase to 
	 *                        frequency of that phrase in a corpus.
	 * @return the frequency ranks of the provided phrases
	 */
    protected LinkedHashMap<Phrase, Short> getRanks(LinkedHashMap<Phrase, Integer> frequentPhrases) {
        logger.fine("Calculating ranks of frequent phrases");
        LinkedHashMap<Phrase, Short> ranks = new LinkedHashMap<Phrase, Short>(frequentPhrases.size());
        short i = 0;
        for (Phrase phrase : frequentPhrases.keySet()) {
            ranks.put(phrase, i++);
        }
        logger.fine("Done calculating ranks");
        return ranks;
    }

    /**
	 * Calculates the most frequent phrases in the corpus.
	 * <p>
	 * Allows a threshold to be set for the minimum frequency to remember, 
	 * as well as the maximum number of phrases.
	 *
	 * @param suffixes a suffix array for the corpus
	 * @param minFrequency the minimum frequency required to
	 *                     retain phrases
	 * @param maxPhrases   the maximum number of phrases to return
	 * @param maxPhraseLength the maximum phrase length to consider
	 * 
	 * @return A map from phrase to the number of times that phrase occurred in the corpus.
	 *         The iteration order of the map will start with the most frequent phrase,
	 *         and end with the least frequent calculated phrase.
	 */
    protected LinkedHashMap<Phrase, Integer> getMostFrequentPhrases(Suffixes suffixes, int minFrequency, int maxPhrases, int maxPhraseLength) {
        LinkedList<Phrase> phrases = new LinkedList<Phrase>();
        LinkedList<Integer> frequencies = new LinkedList<Integer>();
        phrases.clear();
        frequencies.clear();
        Comparator<Integer> comparator = new ReverseOrder<Integer>();
        int[] longestCommonPrefixes = calculateLongestCommonPrefixes(suffixes);
        Stack<Integer> startIndices = new Stack<Integer>();
        Stack<Integer> shortestInteriorLCPIndices = new Stack<Integer>();
        startIndices.push(0);
        shortestInteriorLCPIndices.push(0);
        for (int j = 0, size = suffixes.size(); j < size; j++) {
            recordPhraseFrequencies(suffixes, longestCommonPrefixes, j, j, 0, phrases, frequencies, minFrequency, maxPhrases, maxPhraseLength, comparator);
            while (longestCommonPrefixes[j + 1] < longestCommonPrefixes[shortestInteriorLCPIndices.peek()]) {
                recordPhraseFrequencies(suffixes, longestCommonPrefixes, startIndices.pop(), j, shortestInteriorLCPIndices.pop(), phrases, frequencies, minFrequency, maxPhrases, maxPhraseLength, comparator);
            }
            startIndices.push(shortestInteriorLCPIndices.peek());
            shortestInteriorLCPIndices.push(j + 1);
            if (phrases.size() > maxPhrases) {
                int frequency = frequencies.get(maxPhrases);
                int cutPoint = maxPhrases;
                if (phrases.size() >= maxPhrases && frequency == frequencies.get(maxPhrases)) {
                    cutPoint = Collections.binarySearch(frequencies, frequency, comparator);
                    if (cutPoint < 0) {
                        cutPoint = -1 * cutPoint;
                        cutPoint--;
                    }
                    while (cutPoint > 0 && frequencies.get(cutPoint - 1) == frequency) {
                        cutPoint--;
                    }
                }
                for (int i = phrases.size() - 1; i >= cutPoint; i--) {
                    phrases.removeLast();
                    frequencies.removeLast();
                }
            }
        }
        LinkedHashMap<Phrase, Integer> results = new LinkedHashMap<Phrase, Integer>();
        for (int i = phrases.size(); i > 0; i--) {
            Phrase phrase = phrases.removeFirst();
            Integer frequency = frequencies.removeFirst();
            results.put(phrase, frequency);
        }
        return results;
    }

    /**
	 * Constructs an auxiliary array that stores longest common
	 * prefixes. The length of the array is the corpus size+1.
	 * Each elements lcp[i] indicates the length of the common
	 * prefix between two positions s[i-1] and s[i] in the
	 * suffix array.
	 */
    protected static int[] calculateLongestCommonPrefixes(Suffixes suffixes) {
        int length = suffixes.size();
        Corpus corpus = suffixes.getCorpus();
        int[] longestCommonPrefixes = new int[length + 1];
        for (int i = 1; i < length; i++) {
            int commonPrefixSize = 0;
            int corpusIndex = suffixes.getCorpusIndex(i);
            int prevCorpusIndex = suffixes.getCorpusIndex(i - 1);
            while (corpusIndex + commonPrefixSize < length && prevCorpusIndex + commonPrefixSize < length && (corpus.getWordID(corpusIndex + commonPrefixSize) == corpus.getWordID(prevCorpusIndex + commonPrefixSize) && commonPrefixSize <= Suffixes.MAX_COMPARISON_LENGTH)) {
                commonPrefixSize++;
            }
            longestCommonPrefixes[i] = commonPrefixSize;
        }
        longestCommonPrefixes[0] = 0;
        longestCommonPrefixes[length] = 0;
        return longestCommonPrefixes;
    }

    /**
	 * This method extracts phrases which reach the specified
	 * minimum frequency. It uses the equivalency classes for
	 * substrings in the interval i-j in the suffix array, as
	 * defined in section 2.3 of the the Yamamoto and Church
	 * CL article. This is a helper function for the
	 * getMostFrequentPhrases method.
	 */
    protected static void recordPhraseFrequencies(Suffixes suffixes, int[] longestCommonPrefixes, int i, int j, int k, List<Phrase> phrases, List<Integer> frequencies, int minFrequency, int maxPhrases, int maxPhraseLength, Comparator<Integer> comparator) {
        Corpus corpus = suffixes.getCorpus();
        int longestBoundingLCP = (longestCommonPrefixes[i] > longestCommonPrefixes[j + 1]) ? longestCommonPrefixes[i] : longestCommonPrefixes[j + 1];
        int shortestInteriorLCP = longestCommonPrefixes[k];
        if (shortestInteriorLCP == 0) {
            shortestInteriorLCP = suffixes.size() - suffixes.getCorpusIndex(i);
        }
        int frequency = 0;
        if (i == j) {
            frequency = 1;
        } else if (longestBoundingLCP < shortestInteriorLCP) {
            frequency = j - i + 1;
        }
        if (frequency >= minFrequency) {
            int position = Collections.binarySearch(frequencies, frequency, comparator);
            if (position < 0) {
                position = -1 * position;
                position--;
            }
            if (position < maxPhrases) {
                int startIndex = suffixes.getCorpusIndex(i);
                int sentenceNumber = suffixes.getSentenceIndex(startIndex);
                int endOfSentence = suffixes.getSentencePosition(sentenceNumber + 1);
                int distanceToEndOfSentence = endOfSentence - startIndex;
                int maxLength = Math.min(shortestInteriorLCP - 1, distanceToEndOfSentence);
                maxLength = Math.min(maxLength, maxPhraseLength);
                for (int length = longestBoundingLCP; length <= maxLength; length++) {
                    int endIndex = startIndex + length + 1;
                    Phrase phrase = new ContiguousPhrase(startIndex, endIndex, corpus);
                    phrases.add(position, phrase);
                    frequencies.add(position, frequency);
                    position++;
                }
            }
        }
    }

    public String toString() {
        String format = null;
        StringBuilder s = new StringBuilder();
        for (Map.Entry<Phrase, Integer> entry : frequentPhrases.entrySet()) {
            Phrase phrase = entry.getKey();
            Integer frequency = entry.getValue();
            if (format == null) {
                int length = frequency.toString().length();
                format = "%1$" + length + "d";
            }
            s.append(String.format(format, frequency));
            s.append('\t');
            s.append(phrase.toString());
            s.append('\n');
        }
        return s.toString();
    }

    /**
	 * Private helper method for performing fast intersection.
	 * 
	 * @param <E>
	 * @param sortedData
	 * @param sortedQueries
	 * @param result
	 */
    private static <E extends Comparable<E>> void fastIntersect(List<E> sortedData, List<E> sortedQueries, SortedSet<E> result) {
        int medianQueryIndex = sortedQueries.size() / 2;
        E medianQuery = sortedQueries.get(medianQueryIndex);
        int index = Collections.binarySearch(sortedData, medianQuery);
        if (index >= 0) {
            result.add(medianQuery);
        } else {
            index = (-1 * index) + 1;
        }
        if (index - 1 >= 0 && medianQueryIndex - 1 >= 0) {
            fastIntersect(sortedData.subList(0, index), sortedQueries.subList(0, medianQueryIndex), result);
        }
        if (index + 1 < sortedData.size() && medianQueryIndex + 1 < sortedQueries.size()) {
            fastIntersect(sortedData.subList(index + 1, sortedData.size()), sortedQueries.subList(medianQueryIndex + 1, sortedQueries.size()), result);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Vocabulary symbolTable;
        Corpus corpusArray;
        Suffixes suffixArray;
        FrequentPhrases frequentPhrases;
        if (args.length == 1) {
            String corpusFileName = args[0];
            logger.info("Constructing vocabulary from file " + corpusFileName);
            symbolTable = new Vocabulary();
            int[] lengths = Vocabulary.initializeVocabulary(corpusFileName, symbolTable, true);
            logger.info("Constructing corpus array from file " + corpusFileName);
            corpusArray = SuffixArrayFactory.createCorpusArray(corpusFileName, symbolTable, lengths[0], lengths[1]);
            logger.info("Constructing suffix array from file " + corpusFileName);
            suffixArray = new SuffixArray(corpusArray, Cache.DEFAULT_CAPACITY);
        } else if (args.length == 3) {
            String binarySourceVocabFileName = args[0];
            String binaryCorpusFileName = args[1];
            String binarySuffixArrayFileName = args[2];
            if (logger.isLoggable(Level.INFO)) logger.info("Constructing source language vocabulary from binary file " + binarySourceVocabFileName);
            ObjectInput in = BinaryIn.vocabulary(binarySourceVocabFileName);
            symbolTable = new Vocabulary();
            symbolTable.readExternal(in);
            logger.info("Constructing corpus array from file " + binaryCorpusFileName);
            if (logger.isLoggable(Level.INFO)) logger.info("Constructing memory mapped source language corpus array.");
            corpusArray = new MemoryMappedCorpusArray(symbolTable, binaryCorpusFileName);
            logger.info("Constructing suffix array from file " + binarySuffixArrayFileName);
            suffixArray = new MemoryMappedSuffixArray(binarySuffixArrayFileName, corpusArray, Cache.DEFAULT_CAPACITY);
        } else {
            System.err.println("Usage: java " + SuffixArray.class.getName() + " source.vocab source.corpus source.suffixes");
            System.exit(0);
            symbolTable = null;
            corpusArray = null;
            suffixArray = null;
        }
        int minFrequency = 0;
        short maxPhrases = 100;
        int maxPhraseLength = 10;
        int windowSize = 10;
        short minNonterminalSpan = 2;
        logger.info("Calculating " + maxPhrases + " most frequent phrases");
        frequentPhrases = new FrequentPhrases(suffixArray, minFrequency, maxPhrases, maxPhraseLength);
        logger.info("Frequent phrases: \n" + frequentPhrases.toString());
        logger.info("Calculating collocations for most frequent phrases");
        FrequentMatches matches = frequentPhrases.getCollocations(maxPhraseLength, windowSize, minNonterminalSpan);
        logger.info("Printing collocations for most frequent phrases");
        logger.info("Total collocations: " + matches.counter);
    }
}
