package de.tud.inf.st.rubadoc.rating;

import de.tud.inf.st.rubadoc.model.DocumentationOccurrence;

public class CICommentLengthEvaluator implements Evaluator {

    private int POINT = 1;

    private DocumentationOccurrence max = null;

    private int maxlength = 0;

    public void evaluate(DocumentationOccurrence occ) {
        if (occ.getDocumentationEntry().getElemComment(occ.getCoveringQueryVar()).length() > maxlength) {
            max = occ;
            maxlength = max.getDocumentationEntry().getElemComment(max.getCoveringQueryVar()).length();
        }
    }

    public void makeFinal() {
        if (max != null) max.curPoints += POINT;
    }
}
