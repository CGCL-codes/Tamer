package org.exmaralda.common.corpusbuild;

import org.exmaralda.partitureditor.jexmaralda.*;
import java.net.*;

/**
 *
 * @author thomas
 */
public class CheckAnnotationMismatches extends AbstractBasicTranscriptionChecker {

    /** Creates a new instance of WordCount */
    public CheckAnnotationMismatches() {
    }

    public void processTranscription(BasicTranscription bt, String currentFilename) throws URISyntaxException, org.xml.sax.SAXException {
        for (int pos = 0; pos < bt.getBody().getNumberOfTiers(); pos++) {
            Tier t = bt.getBody().getTierAt(pos);
            String[] mm = t.getAnnotationMismatches(bt);
            if (mm == null) continue;
            for (String m : mm) {
                String text = "Annotation mismatch";
                addError(currentFilename, t.getID(), m, text);
            }
        }
    }
}
