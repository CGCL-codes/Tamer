package bibtex.rdf.labeling;

import java.util.HashMap;
import java.util.Map;
import bibtex.model.AbstractEntry;
import bibtex.util.BibtexException;

/**
 * 
 */
public class Labeler {

    private static Map<String, LabelPatternParser> labelers = new HashMap<String, LabelPatternParser>();

    public static String getLabel(AbstractEntry entry, String pattern) {
        try {
            LabelPatternParser l = labelers.get(pattern);
            if (l == null) {
                l = new LabelPatternParser(pattern);
                labelers.put(pattern, l);
            }
            return l.getLabel(entry);
        } catch (Exception e) {
            throw new BibtexException("Error during label generation", e);
        }
    }
}
