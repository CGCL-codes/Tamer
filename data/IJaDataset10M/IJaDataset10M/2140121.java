package org.dcm4che2.iod.value;

/**
 * @author Antonio Magni <dcm4ceph@antoniomagni.org>
 * 
 */
public class PresentationIntentType {

    public static String PRESENTATION = "FOR PRESENTATION";

    public static String PROCESSING = "FOR PROCESSING";

    public static boolean isValid(String ss) {
        return ss.equals(PROCESSING) || ss.equals(PROCESSING);
    }
}
