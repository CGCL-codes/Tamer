package opennlp.ccg.ngrams;

import opennlp.ccg.lexicon.Word;
import java.util.*;

/**
 * Interface for objects that filter unhappy n-grams.
 *
 * @author      Michael White
 * @version     $Revision: 1.3 $, $Date: 2005/10/20 18:49:42 $
 */
public interface NgramFilter {

    /** Returns whether to filter out the given list of words. */
    public boolean filterOut(List<Word> words);
}
