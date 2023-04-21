package org.fao.fenix.metadataeditor.domain.struct;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author etj
 */
public class LabelList {

    private List<MLLabel> labels = new ArrayList<MLLabel>();

    public void addLabel(MLLabel label) {
        labels.add(label);
    }

    public MLLabel addLabel(String lang, String text) {
        MLLabel ret = new MLLabel(lang, text);
        labels.add(ret);
        return ret;
    }

    public String getLabel(String lang) {
        for (MLLabel mLLabel : labels) {
            if (mLLabel.getLanguage().equalsIgnoreCase(lang)) return mLLabel.getLabel();
        }
        return null;
    }

    public boolean isEmpty() {
        return labels.isEmpty();
    }
}
