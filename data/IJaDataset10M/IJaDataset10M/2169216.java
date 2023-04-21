package jp.co.omronsoft.openwnn;

/**
 * The filter class for candidates.
 * This class is used for filtering candidates by {link WnnEngine}.
 * 
 * @author Copyright (C) 2009 OMRON SOFTWARE CO., LTD.  All Rights Reserved.
 *
 */
public class CandidateFilter {

    /** Filtering pattern (No filter) */
    public static final int FILTER_NONE = 0x0;

    /** Filtering pattern (Non ASCII) */
    public static final int FILTER_NON_ASCII = 0x2;

    /** Current filter type */
    public int filter = 0;

    /**
     * Checking whether a specified word is filtered.
     * 
     * @param word      A word
     * @return          {@code true} if the word is allowed; {@code false} if the word is denied.
     */
    public boolean isAllowed(WnnWord word) {
        if (filter == 0) {
            return true;
        }
        if ((filter & FILTER_NON_ASCII) != 0) {
            String str = word.candidate;
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) < 0x20 || 0x7E < str.charAt(i)) {
                    return false;
                }
            }
        }
        return true;
    }
}
