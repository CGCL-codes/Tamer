package com.liferay.portlet.chat.util;

import com.liferay.portal.kernel.util.StackTraceUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <a href="Logger.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class Logger extends com.lyrisoft.util.io.Logger {

    public static final int LOG_LEVEL_TRACE = 1;

    public static final int LOG_LEVEL_DEBUG = 2;

    public static final int LOG_LEVEL_INFO = 3;

    public static final int LOG_LEVEL_WARN = 4;

    public static final int LOG_LEVEL_ERROR = 5;

    public static final int LOG_LEVEL_FATAL = 6;

    public Logger(int level) {
        super(System.out);
        _level = level;
    }

    public void run() {
    }

    public void log(String s) {
        switch(_level) {
            case LOG_LEVEL_TRACE:
                _log.trace(s);
                break;
            case LOG_LEVEL_DEBUG:
                _log.debug(s);
            case LOG_LEVEL_INFO:
                _log.info(s);
                break;
            case LOG_LEVEL_WARN:
                _log.warn(s);
                break;
            case LOG_LEVEL_ERROR:
                _log.error(s);
                break;
            case LOG_LEVEL_FATAL:
                _log.fatal(s);
        }
    }

    public void log(Exception e) {
        String stackTrace = StackTraceUtil.getStackTrace(e);
        log(stackTrace);
    }

    private static Log _log = LogFactory.getLog(ChatServer.class);

    private int _level;
}
