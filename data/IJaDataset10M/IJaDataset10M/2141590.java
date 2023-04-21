package SearchDB;

import Analysis.*;
import org.apache.lucene.analysis.*;
import Stemmer.Stemmer;
import java.util.*;

/**
 *
 * @author Belay
 */
public class TextFragmenter {

    Stemmer stemmer;

    String userquery;

    /** Creates a new instance of TextFragmenter */
    public TextFragmenter(String query) {
        stemmer = new Stemmer();
        userquery = "";
        StringTokenizer tokens = new StringTokenizer(query, " ");
        while (tokens.hasMoreTokens()) userquery += stemmer.stem(tokens.nextToken()) + " ";
    }

    public String highlightedBestFragmentedText(String content) {
        String fragment = "";
        String[] temp = new String[5];
        String word, stemmed_word;
        String content_copy = new String(content);
        StringTokenizer tokens = new StringTokenizer(content_copy, " ");
        int i = 0;
        while (tokens.hasMoreTokens()) {
            word = tokens.nextToken();
            stemmed_word = stemmer.stem(word);
            if (userquery.indexOf(stemmed_word) == -1) {
                switch(i) {
                    case 5:
                        for (int j = 0; j < 4; j++) temp[j] = temp[j + 1];
                        temp[4] = word;
                        break;
                    default:
                        temp[i++] = word;
                }
            } else {
                fragment = fragment + "...";
                for (int j = 0; j < i; j++) fragment = fragment + " " + temp[j];
                fragment = fragment + "<i><b>" + word + "</b></i>";
                for (int j = 0; j < 5; j++) {
                    if (tokens.hasMoreTokens()) {
                        word = tokens.nextToken();
                        stemmed_word = stemmer.stem(word);
                        if (userquery.indexOf(stemmed_word) != -1) {
                            fragment = fragment + "  " + "<i><b>" + word + "</b></i>";
                        } else {
                            fragment = fragment + " " + word;
                        }
                    }
                }
                i = 0;
            }
            if (fragment.length() > 150) break;
        }
        return fragment + "...";
    }
}
