package com.vangent.hieos.DocViewer.client.model.patient;

/**
 * 
 * @author Bernie Thuman
 * 
 */
public class PatientUtil {

    private static final int EUID_COMPONENT_COUNT = 4;

    private static final int UNIVERSALID_COMPONENT_COUNT = 3;

    private static final String EUID_SPLIT_CHAR = "\\^";

    private static final String UNIVERSALID_SPLIT_CHAR = "\\&";

    /**
	 * 
	 * @param pid
	 * @return
	 */
    public static boolean validatePIDStringFormat(String pid) {
        String[] pidSplit = pid.split(EUID_SPLIT_CHAR);
        if (pidSplit.length == EUID_COMPONENT_COUNT) {
            String aa = pidSplit[3];
            String aaSplit[] = aa.split(UNIVERSALID_SPLIT_CHAR);
            if (aaSplit.length == UNIVERSALID_COMPONENT_COUNT) {
                return true;
            }
        }
        return false;
    }

    /**
	 * 
	 * @param pid
	 * @return
	 */
    public static String getIDFromPIDString(String pid) {
        String[] pidSplit = pid.split(EUID_SPLIT_CHAR);
        if (pidSplit.length == EUID_COMPONENT_COUNT) {
            return pidSplit[0];
        }
        return "UNKNOWN";
    }

    /**
	 * 
	 * @param pid
	 * @return
	 */
    public static String getAssigningAuthorityFromPIDString(String pid) {
        String[] pidSplit = pid.split(EUID_SPLIT_CHAR);
        if (pidSplit.length == EUID_COMPONENT_COUNT) {
            String aa = pidSplit[3];
            return aa;
        }
        return "UNKNOWN";
    }

    /**
	 * 
	 * @param pid
	 * @return
	 */
    public static String getUniversalIDFromPIDString(String pid) {
        String[] pidSplit = pid.split(EUID_SPLIT_CHAR);
        if (pidSplit.length == EUID_COMPONENT_COUNT) {
            String aa = pidSplit[3];
            String aaSplit[] = aa.split(UNIVERSALID_SPLIT_CHAR);
            if (aaSplit.length == UNIVERSALID_COMPONENT_COUNT) {
                return aaSplit[1];
            }
        }
        return "UNKNOWN";
    }
}
