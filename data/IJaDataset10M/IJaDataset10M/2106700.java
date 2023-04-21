package edu.asu.vogon.embryo.internal;

import java.util.ArrayList;
import java.util.List;
import edu.asu.vogon.model.Annotation;
import edu.asu.vogon.model.GeneralTextTerm;
import edu.asu.vogon.model.SpecificTextTerm;
import edu.asu.vogon.model.Text;

public class SelectedWordHelper {

    public List<Annotation> getAnnotationsAtPosition(Text text, int position) {
        List<Annotation> annotations = text.getAnnotations();
        List<Annotation> annotationsAtPos = new ArrayList<Annotation>();
        for (Annotation an : annotations) {
            GeneralTextTerm term = an.getFirstTextTerm();
            if (term instanceof SpecificTextTerm) {
                int start = ((SpecificTextTerm) term).getStartIndex();
                int end = ((SpecificTextTerm) term).getEndIndex();
                if (position >= start && position <= end) annotationsAtPos.add(an);
            }
        }
        return annotationsAtPos;
    }

    public List<SpecificTextTerm> getTermsAtPosition(Text text, int position) {
        List<GeneralTextTerm> terms = text.getResults();
        List<SpecificTextTerm> termsAtPos = new ArrayList<SpecificTextTerm>();
        for (GeneralTextTerm term : terms) {
            if (term instanceof SpecificTextTerm) {
                int start = ((SpecificTextTerm) term).getStartIndex();
                int end = ((SpecificTextTerm) term).getEndIndex();
                if (position >= start && position <= end) termsAtPos.add((SpecificTextTerm) term);
            }
        }
        return termsAtPos;
    }
}
