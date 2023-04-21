package org.nobject.datadigger.cron;

import java.util.HashMap;
import java.util.Map;

/**
 * Constance
 * 
 * @author bianrongjun
 * @version 1.0
 */
public class Constance {

    /**
	 * config file path
	 */
    public static String config = "";

    /**
	 * defaultHandler
	 */
    public static final String default_handler = "org.nobject.TagHandler";

    /**
	 * default_tagsName
	 */
    public static final String default_tagsName = "defaultTags";

    /**
	 * sys_debug
	 */
    public static final int sys_debug = 5;

    /**
	 * default_taglib
	 */
    public static String default_taglib = null;

    /**
	 * taglibs
	 */
    public static Map taglibs = new HashMap();
}
