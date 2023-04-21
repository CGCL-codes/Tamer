package org.obe.server.j2ee.repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.obe.spi.service.ServerConfig;
import org.wfmc.audit.WMAAuditEntry;

/**
 * @author Adrian Price
 */
public class AuditAppender extends AppenderSkeleton {

    private static final Log _logger = LogFactory.getLog(AuditAppender.class);

    static {
        if (_logger.isDebugEnabled()) _logger.debug("<clinit>");
    }

    public AuditAppender() {
        if (_logger.isDebugEnabled()) _logger.debug("<init>");
    }

    protected void append(LoggingEvent event) {
        if (_logger.isDebugEnabled()) _logger.debug("append()");
        try {
            WMAAuditEntry entry = (WMAAuditEntry) event.getMessage();
            if (ServerConfig.isAuditNonTransactional()) EJBLocalHelper.getAuditEntryHome().create(entry); else AuditEntryDAO.getInstance().insert(entry);
        } catch (Exception e) {
            _logger.error("Exception occurred when persisting audit entry: " + e + ": " + e.getMessage(), e);
        }
    }

    public boolean requiresLayout() {
        return false;
    }

    public void close() {
    }
}
