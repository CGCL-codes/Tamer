package de.spieleck.app.cngram;

import java.util.Iterator;
import java.util.Comparator;

/**
 * A device to keep a bunch of ngram statistics.
 * @author frank nestel
 * @author $Author: nestefan $
 * @version $Revision: 2 $ $Date: 2006-03-27 16:00:21 -0500 (Mon, 27 Mar 2006) $ $Author: nestefan $
 */
public interface NGramProfile {

    public static final String NGRAM_PROFILE_EXTENSION = "ngp";

    public static final String NORMALIZATION_STR = "ngram_count";

    public static final String FINISHREAD_STR = "#END";

    public static final NGram[] NO_NGRAM = new NGram[0];

    public static final Comparator CHAR_SEQ_COMPARATOR = new Comparator() {

        public int compare(Object o1, Object o2) {
            CharSequence c1 = (CharSequence) o1;
            CharSequence c2 = (CharSequence) o2;
            for (int i = 0; i < c1.length() && i < c2.length(); i++) {
                int d = c1.charAt(i) - c2.charAt(i);
                if (d != 0) return d;
            }
            return c2.length() - c1.length();
        }
    };

    /**
   * Return sorted ngrams
   * 
   * @return sorted ngrams
   */
    public Iterator getSorted();

    /**
   * @return Returns the number of ngrams.
   */
    public int getCount();

    /**
   * @return Returns the name.
   */
    public String getName();

    /**
   * Get the normalization of all NGrams contained.
   */
    public int getNormalization();

    /**
   * @return NGram corresponding to seq, null if not found.
   */
    public NGram get(CharSequence seq);
}
