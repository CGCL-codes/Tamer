package net.community.chest.net.proto.text.imap4;

import java.io.UTFDataFormatException;
import java.util.ArrayList;
import java.util.Collection;
import net.community.chest.CoVariantReturn;
import net.community.chest.ParsableString;

/**
 * <P>Copyright 2007 as per GPLv2</P>
 *
 * <P>Flags encountered when listing folders</P>
 * 
 * @author Lyor G.
 * @since Sep 20, 2007 11:07:27 AM
 */
public class IMAP4FolderFlag extends IMAP4FlagValue {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4195015255624608521L;

    public IMAP4FolderFlag() {
        super();
    }

    public IMAP4FolderFlag(String name) throws IllegalArgumentException {
        super(name);
    }

    @Override
    @CoVariantReturn
    public IMAP4FolderFlag clone() throws CloneNotSupportedException {
        return getClass().cast(super.clone());
    }

    public static final String IMAP4_NOINFERIORS_FLAG = IMAP4_SYSFLAG_CHAR + "Noinferiors";

    public static final String IMAP4_NOSELECT_FLAG = IMAP4_SYSFLAG_CHAR + "Noselect";

    public static final String IMAP4_MARKED_FLAG = IMAP4_SYSFLAG_CHAR + "Marked";

    public static final String IMAP4_UNMARKED_FLAG = IMAP4_SYSFLAG_CHAR + "Unmarked";

    public static final String IMAP4_HASCHILDREN_FLAG = IMAP4_SYSFLAG_CHAR + "HasChildren";

    public static final String IMAP4_HASNOCHILDREN_FLAG = IMAP4_SYSFLAG_CHAR + "HasNoChildren";

    public static final IMAP4FolderFlag NOINFERIORS = new IMAP4FolderFlag(IMAP4_NOINFERIORS_FLAG);

    public static final IMAP4FolderFlag NOSELECT = new IMAP4FolderFlag(IMAP4_NOSELECT_FLAG);

    public static final IMAP4FolderFlag MARKED = new IMAP4FolderFlag(IMAP4_MARKED_FLAG);

    public static final IMAP4FolderFlag UNMARKED = new IMAP4FolderFlag(IMAP4_UNMARKED_FLAG);

    public static final IMAP4FolderFlag HASCHILDREN = new IMAP4FolderFlag(IMAP4_HASCHILDREN_FLAG);

    public static final IMAP4FolderFlag HASNOCHILDREN = new IMAP4FolderFlag(IMAP4_HASNOCHILDREN_FLAG);

    /**
	 * Returns a list of flags values that may appear in the parsable string (Note: NIL is allowed)
	 * @param ps parsable string to be checked for flags values
	 * @param startIndex flags data start index
	 * @return flags array - may be NULL if "NIL" or "()" flags list found
	 * @throws UTFDataFormatException if unable to parse correctly
	 */
    public static final Collection<IMAP4FolderFlag> getFolderFlags(ParsableString ps, int startIndex) throws UTFDataFormatException {
        final Collection<String> vals = getFlags(ps, startIndex);
        final int numFlags = (null == vals) ? 0 : vals.size();
        if (numFlags <= 0) return null;
        final Collection<IMAP4FolderFlag> flags = new ArrayList<IMAP4FolderFlag>(numFlags);
        for (final String v : vals) {
            if ((null == v) || (v.length() <= 0)) continue;
            flags.add(new IMAP4FolderFlag(v));
        }
        return flags;
    }
}
