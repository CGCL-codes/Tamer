package org.apache.lucene.analysis;

import java.io.Reader;
import org.apache.lucene.analysis.de.WordlistLoader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Set;

/** Filters LetterTokenizer with LowerCaseFilter and StopFilter. */
public final class WhitespaceStopAnalyzer extends Analyzer {

    private Set stopWords;

    /** An array containing some common English words that are not usually useful
   for searching. */
    public static final String[] ENGLISH_STOP_WORDS = { "a", "an", "and", "are", "as", "at", "be", "but", "by", "for", "if", "in", "into", "is", "it", "no", "not", "of", "on", "or", "s", "such", "t", "that", "the", "their", "then", "there", "these", "they", "this", "to", "was", "will", "with" };

    /** Builds an analyzer which removes words in ENGLISH_STOP_WORDS. */
    public WhitespaceStopAnalyzer() {
        stopWords = StopFilter.makeStopSet(ENGLISH_STOP_WORDS);
    }

    /** Builds an analyzer with the stop words from the given set.
  */
    public WhitespaceStopAnalyzer(Set stopWords) {
        this.stopWords = stopWords;
    }

    /** Builds an analyzer which removes words in the provided array. */
    public WhitespaceStopAnalyzer(String[] stopWords) {
        this.stopWords = StopFilter.makeStopSet(stopWords);
    }

    /** Builds an analyzer with the stop words from the given file.
  * @see WordlistLoader#getWordSet(File)
  */
    public WhitespaceStopAnalyzer(File stopwordsFile) throws IOException {
        stopWords = WordlistLoader.getWordSet(stopwordsFile);
    }

    /** Filters LowerCaseTokenizer with StopFilter. */
    public TokenStream tokenStream(String fieldName, Reader reader) {
        return new StopFilter(new WhitespaceTokenizer(reader), stopWords);
    }
}
