package org.personalsmartspace.pss_sm_session.impl;

public final class SessionManagerUtility {

    /**
     * Replace a specified string place holder with an actual value. This method
     * can be replaced with the String replace method if using Java 1.5
     * 
     * @param target
     * @param placeHolder
     * @param replaceWith
     * @return String modified string
     */
    public static String replaceKeyPlaceHolder(String target, String placeHolder, String replaceWith) {
        String modified = null;
        modified = target.replaceFirst(placeHolder, replaceWith);
        return modified;
    }
}
