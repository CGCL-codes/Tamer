package ch.ethz.mxquery.model;

import ch.ethz.mxquery.exceptions.MXQueryException;

public interface DataflowAnalysis {

    public static final int MATERIALIZATION_SUPPORT = 1;

    public static final int FT_SUPPORT = 2;

    public static final int UPDATE_SUPPORT = 4;

    public static final int SCORING_SUPPORT = 8;

    public static final int NODEID_IDENTITY = 16;

    public static final int NODEID_ORDER = 32;

    public static final int NODEID_STRUCTURE = 64;

    public static final int ORDER_ORDERED = 128;

    public static final int ORDER_UNORDERED = 256;

    public static final int ITEM_ACCESS = 512;

    /**
     * Indicate, check and enable the given set of required setting 
     * @param requiredOptions a conjunction of required settings
     * @return the resulting XDMIterator
     * @throws MXQueryException if inconsistent or incompatible options are requested, e.g. for both fulltext and updating
     */
    XDMIterator require(int requiredOptions) throws MXQueryException;
}
