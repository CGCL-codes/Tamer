package de.erdesignerng.model.utils;

import de.erdesignerng.ERDesignerBundle;
import de.mogwai.common.i18n.ResourceHelper;

public class Info {

    private static final ResourceHelper HELPER = ResourceHelper.getResourceHelper(ERDesignerBundle.BUNDLE_NAME);

    private final String what;

    protected Info(String aKey, String aWhat) {
        what = HELPER.getFormattedText(aKey, aWhat);
    }

    @Override
    public String toString() {
        return what;
    }
}
