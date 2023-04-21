package collection.Search;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class AcceptableWords {

    private StringTokenizer tokenizer;

    static char punctuationsMarks[] = { ',', '.', ';', ':', ')', '(', '!', '?', '[', ']' };

    static final String[] STOP_WORDS = new String[] { "about", "after", "all", "also", "an", "a", "and", "another", "any", "are", "as", "at", "be", "because", "been", "before", "being", "between", "both", "but", "by", "came", "can", "come", "could", "did", "do", "each", "for", "from", "get", "got", "has", "had", "he", "have", "her", "here", "him", "himself", "his", "how", "if", "in", "into", "is", "it", "it's", "like", "make", "many", "me", "might", "more", "most", "much", "must", "my", "never", "now", "of", "on", "only", "or", "other", "our", "out", "over", "said", "same", "see", "should", "since", "some", "still", "such", "take", "than", "that", "the", "their", "them", "then", "there", "these", "they", "this", "those", "through", "to", "too", "under", "up", "very", "was", "way", "we", "well", "were", "what", "where", "which", "while", "who", "with", "would", "you", "your" };

    public AcceptableWords(String text) {
        tokenizer = new StringTokenizer(deletePunctuationMarks(text), " ");
    }

    public boolean hasMoreTokens() {
        return tokenizer.hasMoreTokens();
    }

    public ArrayList<String> tokenize() {
        ArrayList<String> tokens = new ArrayList<String>();
        String result;
        while (hasMoreTokens()) {
            result = tokenizer.nextToken();
            if (!isStopWord(result)) {
                tokens.add(result);
            }
        }
        return tokens;
    }

    public boolean isStopWord(String word) {
        for (int i = 0; i < STOP_WORDS.length; i++) {
            if (word.equalsIgnoreCase(STOP_WORDS[i])) {
                return true;
            }
        }
        return false;
    }

    public static String deletePunctuationMarks(String text) {
        String result = text;
        for (int i = 0; i < punctuationsMarks.length; i++) {
            char punctuationsMark = punctuationsMarks[i];
            result = result.replace(punctuationsMark, ' ');
        }
        return result;
    }
}
