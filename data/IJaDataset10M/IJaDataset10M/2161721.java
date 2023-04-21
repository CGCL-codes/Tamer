package com.ipolyglot.service.worddiff;

/**
 * The concept of regions is implemented in this class. 
 */
class Region {

    private String correctString = null;

    private String userString = null;

    private String matchArray = null;

    Region(String correctString, String userString, String matchArray) {
        this.correctString = correctString;
        this.userString = userString;
        this.matchArray = matchArray;
    }

    /** Concatenates the specified region to the end of this region */
    Region concat(Region region) {
        String c = correctString + region.correctString;
        String u = userString + region.userString;
        String m = matchArray + region.matchArray;
        Region resultRegion = new Region(c, u, m);
        return resultRegion;
    }

    /** Returns match array */
    String getMatchArray() {
        return matchArray;
    }

    /** Returns correct string */
    String getCorrectString() {
        String str = correctString.replace("-", "");
        return str;
    }

    /** Returns correct string without removing empty chars */
    String getFullCorrectString() {
        return correctString;
    }

    /** Returns user string */
    String getUserString() {
        String str = userString.replace("-", "");
        return str;
    }

    /** Returns user string without removing empty chars */
    String getFullUserString() {
        return userString;
    }

    /** 
	 * Checks whether the region is dirty. Region is dirty if match array has 
	 * different chars.  
	 */
    boolean isDirty() {
        for (int i = 1; i < matchArray.length(); i++) {
            char p = matchArray.charAt(i - 1);
            char c = matchArray.charAt(i);
            if (p != c) {
                return true;
            }
        }
        return false;
    }

    /** Deletes all absent chars which stay beside the not-match char */
    void deleteDirtyAbsentChars() {
        StringBuilder ma = new StringBuilder(matchArray);
        StringBuilder c = new StringBuilder(correctString);
        StringBuilder u = new StringBuilder(userString);
        int p = ma.indexOf("an");
        while (p > -1) {
            ma.deleteCharAt(p);
            c.deleteCharAt(p);
            u.deleteCharAt(p);
            p = ma.indexOf("an");
        }
        p = ma.indexOf("na");
        while (p > -1) {
            p++;
            ma.deleteCharAt(p);
            c.deleteCharAt(p);
            u.deleteCharAt(p);
            p = ma.indexOf("na");
        }
        matchArray = ma.toString();
        correctString = c.toString();
        userString = u.toString();
    }
}
