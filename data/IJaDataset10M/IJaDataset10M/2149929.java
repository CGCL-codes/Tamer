package org.merlotxml.util.xml;

/**
 *  provides constant values
 * 
 *
 * @author Kelly A. Campbell
 */
public interface DTDConstants {

    public static final int NONE = -1;

    public static final int IMPLIED = 0;

    public static final int REQUIRED = 1;

    public static final int FIXED = 2;

    public static final int ANY = 10;

    public static final int EMPTY = 11;

    public static final int TOKEN_GROUP = 12;

    public static final int NMTOKEN = 13;

    public static final int NMTOKENS = 14;

    public static final int ID = 15;

    public static final int IDREF = 16;

    public static final int IDREFS = 17;

    public static final int ENTITY = 18;

    public static final int ENTITIES = 19;

    public static final int NOTATION = 24;

    public static final int CDATA = 20;

    public static final int PCDATA = 21;

    public static final int COMMENT = 22;

    public static final int PROCESSING_INSTRUCTION = 23;

    public static final int GROUP = 30;

    public static final int CONTENT_LEAF = '-';

    public static final int CONTENT_GROUP = '(';

    public static final int CONTENT_OR = '|';

    public static final int CONTENT_CONCAT = ',';

    public static final int CONTENT_ONEMAX = '?';

    public static final int CONTENT_SINGLE = '=';

    public static final int CONTENT_STAR = '*';

    public static final int CONTENT_PLUS = '+';

    public static final String PCDATA_KEY = "#text";

    public static final String COMMENT_KEY = "#comment";

    public static final String PROCESSING_INSTRUCTION_KEY = "#processing_instruction";
}
