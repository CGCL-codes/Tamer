package org.phenoscape.tto.catalog;

import java.util.Arrays;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

public abstract class CatalogReader {

    static final Pattern tabPattern = Pattern.compile("\t");

    static final Pattern dotPattern = Pattern.compile(".");

    public abstract boolean checkEntry(String[] line);

    public abstract String getBadFileName();

    protected static String ttoprefix;

    protected static Logger logger;

    public enum status {

        valid, synonym, invalid, missing
    }

    static final String synonymStatus = "synonym";

    static final String validStatus = "valid";

    protected static final int CLASSBASE = 10;

    protected static final int CLASSMULTIPLIER = 1;

    protected static final int ORDERBASE = 1000;

    protected static final int ORDERMULTIPLIER = 10;

    protected static final int FAMILYBASE = 10000;

    protected static final int FAMILYMULTIPLIER = 10;

    protected static final int GENUSBASE = 100000;

    protected static final int GENUSMULTIPLIER = 1;

    protected static final int SPECIESBASE = 1000000;

    protected static final int SPECIESMULTIPLIER = 1;

    public static final int HIGHBASE = 10000000;

    /**
     * This sets the prefix of terms returned by the subclases of this reader
     * @param prefix String specifying the id prefix
     */
    public CatalogReader(String prefix) {
        ttoprefix = prefix;
    }

    /**
     * 
     * @param headerSet
     * @param header
     * @return
     */
    public static boolean inHeaderSet(String[] headerSet, String header) {
        return (Arrays.binarySearch(headerSet, header, String.CASE_INSENSITIVE_ORDER) >= 0);
    }

    /**
     * Extracts the integer portion of a string that (?) contains a number - really just everything
     * before the decimal point.  This is used for checking ids at the genus and lineage readers CoF lineage id extraction (some lineage ids are xx.y)
     * @param s the string to extract from
     * @return the portion of the string that represents an integer (whole string if no '.')
     */
    public static String integerPortion(String s) {
        if (s.length() == 0) return s;
        final int dotpos = s.indexOf('.');
        if (dotpos == -1) return s; else {
            return s.substring(0, dotpos);
        }
    }

    /** 
     * Generates an appropriate OBOID (prefix:number) for a (taxonomic)class identifier, based on its lineage number 
     * @param s
     * @return An
     */
    public static String classIDNumber(String s, String ontBase) {
        return idNumber(s, ontBase, CLASSBASE, CLASSMULTIPLIER);
    }

    /**
     * 
     * @param s
     * @return
     */
    public static String orderIDNumber(String s, String ontBase) {
        return idNumber(s, ontBase, ORDERBASE, ORDERMULTIPLIER);
    }

    public static String familyIDNumber(String s, String ontBase) {
        return idNumber(s, ontBase, FAMILYBASE, FAMILYMULTIPLIER);
    }

    public static String genusIDNumber(String s, String ontBase) {
        return idNumber(s, ontBase, GENUSBASE, GENUSMULTIPLIER);
    }

    public static String speciesIDNumber(String s, String ontBase) {
        return idNumber(s, ontBase, SPECIESBASE, SPECIESMULTIPLIER);
    }

    private static String idNumber(String s, String ontBase, int base, int multiplier) {
        if (s.length() == 0) return null;
        final int dotpos = s.indexOf('.');
        if (dotpos == -1) return ontBase + Integer.toString(base + multiplier * Integer.parseInt(s)); else {
            final String first = s.substring(0, dotpos);
            final String second = s.substring(dotpos + 1);
            final String result = Integer.toString(base + (multiplier * Integer.parseInt(first) + (multiplier / 10) * Integer.parseInt(second)));
            return ontBase + result;
        }
    }
}
