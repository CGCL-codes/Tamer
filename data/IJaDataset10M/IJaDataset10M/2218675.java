package scam.webdav.util.views;

import java.io.*;
import java.util.*;
import scam.*;
import scam.webdav.share.DCModelView;
import scam.share.Resource;

/**
 * Represents the Full Metadata Set available for a file resource.
 *
 * @author J�ran Stark
 * @author Jan Danils
 * @version $Revision: 1.1.1.1 $
 */
public class FullFileView extends DCModelViewAbs {

    private static int[] cLabelIndicies = { DCModelView.I_TITLE, DCModelView.I_DESCRIPTION, DCModelView.I_KEYWORDS, DCModelView.I_CREATORS, DCModelView.I_CONTRIBUTOR, DCModelView.I_PUBLISHER, DCModelView.I_LANGUAGE, DCModelView.I_RIGHTS, DCModelView.I_TYPE, DCModelView.I_SIZE, DCModelView.I_CREATED, DCModelView.I_ISSUED, DCModelView.I_MODIFIED };

    protected static Hashtable hPreds = new Hashtable();

    protected static Hashtable hIndex = new Hashtable();

    /** Metametadata array */
    protected static String[][] labels = new String[cLabelIndicies.length][4];

    public FullFileView(Resource resource) {
        super(resource);
    }

    protected Hashtable getIndicies() {
        return hIndex;
    }

    protected Hashtable getPredicates() {
        return hPreds;
    }

    protected String[][] getLabels() {
        return labels;
    }

    static {
        for (int i = 0; i < cLabelIndicies.length; i++) {
            String[] curr = LABELS[cLabelIndicies[i]];
            labels[i] = curr;
            hPreds.put(curr[LABEL], PREDICATES[cLabelIndicies[i]]);
            hIndex.put(curr[LABEL], new Integer(i));
        }
    }
}
