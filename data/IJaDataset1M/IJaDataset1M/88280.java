package org.ddth.txbb.util;

public class DefaultUnicodeNormalizer implements UnicodeNormalizer {

    /**
	 * Auto-generated serial version UID.
	 */
    private static final long serialVersionUID = -8161212491090013002L;

    public String normalize(String text) {
        if (text == null) return "";
        return text.replaceAll("\\W+", "_").replaceAll("^_+", "").replaceAll("_+$", "").replaceAll("_+", "_");
    }
}
