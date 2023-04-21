package edu.ucla.mbi.xml.MIF.elements.comparators;

import edu.ucla.mbi.xml.MIF.elements.controlledVocabularies.ControlledVocabularyTerm;
import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: Apr 6, 2006
 * Time: 6:41:33 PM
 */
public class ControlledVocabularyTermComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        ControlledVocabularyTerm cv1 = (ControlledVocabularyTerm) o1, cv2 = (ControlledVocabularyTerm) o2;
        if (cv1 == cv2) return 0;
        if (cv1 == null) return -1;
        if (cv2 == null) return 1;
        return cv1.getTermId().compareTo(cv2.getTermId());
    }
}
