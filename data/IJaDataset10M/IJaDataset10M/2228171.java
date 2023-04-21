package org.dyno.visual.swing.lnfs.meta;

public class MetaJComboBoxValue extends MetaJComponentValue {

    private static final long serialVersionUID = 1L;

    public MetaJComboBoxValue() {
        put("maximumRowCount", 8);
        put("model", null);
        put("visible", true);
        put("verifyInputWhenFocusTarget", true);
        put("opaque", true);
        put("enabled", true);
        put("focusable", true);
        put("lightWeightPopupEnabled", true);
        put("alignmentX", 0.5f);
        put("alignmentY", 0.5f);
        put("requestFocusEnabled", true);
        put("border", SYSTEM_VALUE);
    }
}
