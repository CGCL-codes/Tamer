package org.likken.util.parser;

/**
 * @author Stephane Boisson <s.boisson@focal-net.com> 
 * @version $Revision: 1.1.1.1 $ $Date: 2000/12/07 00:12:41 $
 */
public interface Constants {

    public static final String _DESCRIPTOR_PATTERN = "[a-zA-Z][a-zA-Z0-9\\-;]*";

    public static final String _NUMERIC_OID_PART = "(0|([1-9][0-9]*))";

    public static final String _NUMERIC_OID_PATTERN = "(" + _NUMERIC_OID_PART + "\\.){2}" + _NUMERIC_OID_PART + "(\\." + _NUMERIC_OID_PART + ")*";

    public static final String _NUMERIC_OID_LEN_PATTERN = "(" + _NUMERIC_OID_PATTERN + ")\\{([0-9]+)\\}";

    public static final String DESCRIPTOR_PATTERN = "^" + _DESCRIPTOR_PATTERN + "$";

    public static final String NUMERIC_OID_PATTERN = "^" + _NUMERIC_OID_PATTERN + "$";

    public static final String NUMERIC_OID_LEN_PATTERN = "^" + _NUMERIC_OID_LEN_PATTERN + "$";
}
