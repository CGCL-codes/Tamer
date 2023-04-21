package org.adl.datamodels.cmi;

import java.io.Serializable;
import org.adl.datamodels.Element;
import org.adl.util.debug.DebugIndicator;

public class CMIResponse extends CMICategory implements Serializable {

    public CMIResponse() {
        super(true);
        pattern = new Element("", "checkFeedback", "NULL", true, false, false);
    }

    public Element getPattern() {
        return pattern;
    }

    public boolean isInitialized() {
        boolean flag = false;
        if (pattern.isInitialized()) flag = true;
        return flag;
    }

    public void setPattern(String s) {
        pattern.setValue(s);
    }

    public void performSet(CMIRequest cmirequest, DMErrorManager dmerrormanager) {
        String s = cmirequest.getNextToken();
        if (cmirequest.hasMoreTokensToProcess()) {
            if (DebugIndicator.ON) {
                System.out.println("Error - Data Model Element not implemented");
                System.out.println("Element being processed: " + s + "is not a valid element of the CMI Response\n" + "Data Model Category");
            }
            dmerrormanager.recNotImplementedError(cmirequest);
        } else if (!cmirequest.isAKeywordRequest()) {
            String s1 = cmirequest.getValue();
            doSet(this, s, s1, dmerrormanager);
        } else {
            dmerrormanager.recKeyWordError(s);
        }
    }

    public Element pattern;
}
