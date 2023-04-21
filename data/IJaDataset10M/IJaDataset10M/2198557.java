package org.ala.client.appender;

import org.apache.log4j.Level;

/**
 * Custom Level for Log4J (higher than FATAL level)
 * 
 * @author MOK011
 *
 */
public class RestLevel extends Level {

    private static final long serialVersionUID = -3158526197894230134L;

    public static final RestLevel REMOTE = new RestLevel(60000, "REMOTE", 0);

    /**
	 * Constructor
	 * 
	 * @param level
	 * @param levelStr
	 * @param syslogEquivalent
	 */
    public RestLevel(int level, String levelStr, int syslogEquivalent) {
        super(level, levelStr, syslogEquivalent);
    }

    /** 
	* 
	* @see Level#toLevel(int, org.apache.log4j.Level) 
	*/
    public static RestLevel toLevel(int val, Level defaultLevel) {
        return REMOTE;
    }

    /** 
	*
	* @see Level#toLevel(java.lang.String, org.apache.log4j.Level) 
	*/
    public static RestLevel toLevel(String sArg, Level defaultLevel) {
        return REMOTE;
    }
}
