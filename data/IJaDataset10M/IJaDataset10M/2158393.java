package net.lateeye.search;

public interface SearchengineProps {

    public static final String PROPKEY_URL_ROOT = "URL_ROOT";

    public static final String PROPKEY_URL_SUFFIX = "URL_SUFFIX";

    public static final String PROPKEY_URL_PARAMNAME_QUERYKEY = "URL_PARAMNAME_QUERYKEY";

    public static final String PROPKEY_URL_PARAMNAME_NUMOFSEARCHRESULTS = "URL_PARAMNAME_NUMOFSEARCHRESULTS";

    /**
	 * CHANGED/130s/2008May15/replaced by PROPKEY_VAL_NUMOFRESULTS_SINGLEPAGE
	 * CHANGED/130s/2010Jun08/Put into use again
	 * */
    public static final String PROPKEY_VAL_NUM_SEARCHRESULTS = "VAL_NUM_SEARCHRESULTS";

    public static final String PROPKEY_URL_OTHERPARAMS_ANDVALS = "URL_OTHERPARAMS_ANDVALS";

    public static final String PROPKEY_PORT_SEARCHENGINE = "PORT_SEARCHENGINE";

    public static final String PROPKEY_INTERVAL_EXECUTEQUERY = "INTERVAL_EXECUTEQUERY";

    public static final String PROPKEY_VAL_NUMOFRESULTS_SINGLEPAGE = "NUMOFRESULTS_SINGLEPAGE";

    /**
	 * Corresponding value should be in format of second.
	 * 
	 * @since 2.0
	 */
    public static final String PROPKEY_QUERY_TIMEOUT = "QUERY_TIMEOUT";

    public static final String PROPKEY_URL_PARAMDELIMITTER = "URL_PARAMDELIMITTER";

    public static final String PROPKEY_URL_PARAMDELIMITTER_INITIAL = "URL_PARAMDELIMITTER_INITIAL";

    public static final String PROPKEY_URL_EQUAL = "URL_EQUAL";

    public static final String URL_PARAMDELIMITTER = "&";

    public static final String URL_PARAMDELIMITTER_INITIAL = "?";

    public static final String URL_EQUAL = "=";
}
