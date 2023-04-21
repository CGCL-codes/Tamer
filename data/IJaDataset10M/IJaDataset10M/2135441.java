package org.jwaim.modules.pre;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jwaim.core.ModuleCommunicationLine;
import org.jwaim.core.interfaces.ModuleReturnValue;
import org.jwaim.core.logger.JWAIMLogger;
import org.jwaim.core.statistics.StatisticsCollector;
import org.jwaim.core.statistics.StatisticsRow;
import org.jwaim.core.util.Variables;

/**
 * This module refuses connections from any client whose IP it contains. It listens to predefined channel 
 * through which other modules may add IP addresses. Added addresses may contain end time for blacklisting 
 */
public final class Blacklist extends IPList {

    /**
	 * Creates new elements with given communication line and logger
	 * @param communicationLine communication line to use when communicating with other modules
	 * @param logger logger to use when logging events
	 */
    public Blacklist(ModuleCommunicationLine communicationLine, JWAIMLogger logger) {
        super(communicationLine, logger);
    }

    @Override
    public final ModuleReturnValue filter(HttpServletRequest request, HttpServletResponse response, Map<String, Object> attributes) {
        String ipS = request.getRemoteAddr();
        Long ip = Variables.ipToLong(ipS, -1);
        Long endTime = getValue(ip);
        if (endTime == null || (endTime > 0 && endTime.longValue() < System.currentTimeMillis())) {
            return ModuleReturnValue.CONTINUE;
        }
        StatisticsCollector.addStats(new StatisticsRow(System.currentTimeMillis(), this.getId(), "blacklistHit"));
        try {
            response.sendError(403);
        } catch (IOException ignore) {
        }
        this.logger.logCommonEvent("Disallow: " + ipS);
        return ModuleReturnValue.DENY;
    }

    @Override
    public final Set<String> getStatisticClasses() {
        HashSet<String> ret = new HashSet<String>();
        ret.add("blacklistHit");
        return ret;
    }
}
