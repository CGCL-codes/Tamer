package edu.uwm.nlp.jude.internal;

import java.util.ArrayList;
import edu.uwm.nlp.jude.exception.NoReferenceSectionException;

/**
 * @author qing
 *
 * Jun 15, 2009
 */
public class ReferenceAnalyser {

    private String fullText;

    private int fullTextEnd = -1;

    public ReferenceAnalyser(String fullText) {
        this.fullText = fullText;
    }

    public String findFullRef() throws NoReferenceSectionException {
        int curRefIdx = fullText.lastIndexOf("References");
        int curRefIdx2 = fullText.lastIndexOf("REFERENCES");
        int validIdx = -1;
        if (curRefIdx < 0 && curRefIdx2 < 0) throw new NoReferenceSectionException("No reference section."); else {
            validIdx = Math.max(curRefIdx, curRefIdx2);
            fullTextEnd = validIdx;
        }
        if (validIdx != -1) {
            int nIdx = fullText.indexOf('\n', validIdx);
            return fullText.substring(nIdx);
        } else return null;
    }

    public String findPureFullText() {
        int abs1 = fullText.indexOf("Abstract");
        int abs2 = fullText.indexOf("ABSTRACT");
        if (abs1 >= 0 && abs1 < fullTextEnd) return fullText.substring(abs1, fullTextEnd); else if (abs2 >= 0 && abs2 < fullTextEnd) return fullText.substring(abs2, fullTextEnd); else return fullText.substring(0, fullTextEnd);
    }

    public ArrayList<ReferenceEntity> analyzeReference() {
        String fullRef;
        ArrayList<ReferenceEntity> res = null;
        ArrayList<ReferenceEntity> rEntList = null;
        try {
            fullRef = this.findFullRef();
            ReferenceListAnalyser listAnalyser;
            if (fullRef != null) {
                listAnalyser = new ReferenceListAnalyser(fullRef);
                rEntList = listAnalyser.forkFullRef();
                if (rEntList != null) {
                    FullText full = new FullText(this.findPureFullText(), listAnalyser.getPtnCode(), rEntList);
                    res = full.doSpot(true);
                }
                return res;
            }
        } catch (NoReferenceSectionException e) {
            return res;
        }
        return res;
    }
}
