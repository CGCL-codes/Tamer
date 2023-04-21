package com.neurogrid.simulation.dns;

import java.text.DecimalFormat;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;
import com.neurogrid.simulation.root.Keyword;

/**
 * Copyright (c) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * DNSKeyword is an example of the kind of keyword that can be created using the
 * base abstract class.  DNSKeyword creates an ID of the form KEY_####### and each new keyword
 * that is instantiated is unique and if the most recently created keyword was KEY_00001 then the
 * next keyword will be KEY_00002. The keywords and their ids are placed in the appropriate
 * reference hashtables as provided by the base Keyword class.<br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   27/Aug/2003    paolo.gobbo       Created file<br>
 * 0.1   29/Aug/2003    sam               Refactoring to avoid code replication<br>
 * 
 * @author Paolo Gobbo (paologobbo@hotmail.com)
 */
public class DNSKeyword extends Keyword {

    private static final String cvsInfo = "$Id: DNSKeyword.java,v 1.5 2003/08/29 04:25:31 samjoseph Exp $";

    /** (non-Javadoc)
	 * @see com.neurogrid.simulation.root.Keyword#getCvsInfo()
	 */
    public static String getCvsInfo() {
        return cvsInfo;
    }

    private static Category o_cat = Category.getInstance(DNSKeyword.class.getName());

    /**
	 * initialize the logging system
	 *
	 * @param p_conf      configuration filename
	 */
    public static void init(String p_conf) {
        BasicConfigurator.configure();
        PropertyConfigurator.configure(p_conf);
        o_cat.info("DNSKeyword logging Initialized");
    }

    public static final String o_id_head = "www.domain.";

    public static final DecimalFormat o_df = new DecimalFormat("######");

    private static final int MINIMUM_INTEGER_DIGITS = 6;

    private static final int STRING_BUFFER_DEFAULT_SIZE = o_id_head.length() + MINIMUM_INTEGER_DIGITS;

    static {
        o_df.setMinimumIntegerDigits(MINIMUM_INTEGER_DIGITS);
    }

    private static StringBuffer o_buf = new StringBuffer(STRING_BUFFER_DEFAULT_SIZE);

    static {
        o_buf.append(o_id_head);
    }

    /**
	 * Get a keyword id formatted according to the KEY_###### style
	 *
	 * @param p_key - the key to format
	 * @return String Formatted keyword
	 */
    public static String getFormattedKeywordID(long p_key) {
        o_buf.delete(o_id_head.length(), o_buf.length());
        o_buf.append(o_df.format(p_key));
        return o_buf.toString();
    }

    /**
	 * check if these DNS Keywords are equal
	 *
	 * @param p_keyword keyword to compare
	 *
	 * @return boolean if this keyword is the same
	 */
    public boolean equals(DNSKeyword p_keyword) {
        return o_keyword_ID.equals(p_keyword.getKeywordID());
    }

    /** (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
    public int hashCode() {
        return super.hashCode();
    }

    /**
	 * Get String representation
	 *
	 * @return String
	 */
    public String toString() {
        return o_keyword_ID;
    }

    /**
	 * Get a new keyword id formatted according to the KEY_###### style
	 *
	 * @return String
	 */
    protected String getNewKeywordID() {
        o_key = getNewKey();
        return getFormattedKeywordID(o_key);
    }
}
