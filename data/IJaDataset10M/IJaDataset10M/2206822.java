package org.expasy.jpl.io.mol.fasta;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Object that defines a specific Fasta header format.
 * 
 * @author nikitin
 * 
 * @version 1.0
 * 
 */
public final class FastaHeaderFormat {

    /** the regular expression defining the protein id in the first group */
    private Pattern regexProtID;

    /** the regex defining the type of header (decoy, swissprot, ipi,) */
    private Pattern regexHeaderType;

    /**
	 * Constructor
	 * 
	 * @param regexProtID
	 * @param regexHeaderType
	 */
    public FastaHeaderFormat(final String regexProtID, final String regexHeaderType) {
        this.regexProtID = Pattern.compile(regexProtID);
        this.regexHeaderType = Pattern.compile(regexHeaderType);
    }

    public Pattern getRegexProtID() {
        return regexProtID;
    }

    public Pattern getRegexHeaderType() {
        return regexHeaderType;
    }

    /**
	 * Return true if the given String is of this format type.
	 * 
	 * @param header the fasta header to search this signature in.
	 * @return false if it does not match this signature.
	 */
    public boolean isheaderSignatureMatch(final String header) {
        final Matcher m = regexHeaderType.matcher(header);
        return m.find();
    }

    /**
	 * Get the protein ID from the header line.
	 * 
	 * @param header the header to search information.
	 * @param field the field index.
	 * @return header information.
	 */
    public final String getProtID(final String header) {
        final Matcher matcher = regexProtID.matcher(header);
        String protID = "";
        if (matcher.find()) {
            protID = matcher.group(1);
        }
        return protID;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("regExpProtID: '" + regexProtID.toString() + "', ");
        sb.append("regexHeaderType: '" + regexHeaderType.toString());
        return sb.toString();
    }
}
