package net.sf.jradius.log;

import net.sf.jradius.exception.RadiusException;
import net.sf.jradius.server.JRadiusRequest;
import net.sf.jradius.session.JRadiusSession;

/**
 * JRadius Server Logger Class.
 * @author David Bird
 */
public final class RadiusLog {

    private static RadiusLogger radiusLogger = new BaseRadiusLog();

    public static void debug(Object o) {
        radiusLogger.debug(o);
    }

    public static void info(Object o) {
        radiusLogger.info(o);
    }

    public static void warn(Object o) {
        radiusLogger.warn(o);
    }

    public static void error(Object o) {
        radiusLogger.error(o);
    }

    public static void problem(JRadiusRequest request, JRadiusSession session, RadiusException exception, String mesg) {
        radiusLogger.problem(request, session, exception, mesg);
    }

    /**
     * @return Returns the radiusLogger.
     */
    public static RadiusLogger getRadiusLogger() {
        return radiusLogger;
    }

    /**
     * @param radiusLogger The radiusLogger to set.
     */
    public static void setRadiusLogger(RadiusLogger radiusLogger) {
        RadiusLog.radiusLogger = radiusLogger;
    }
}
