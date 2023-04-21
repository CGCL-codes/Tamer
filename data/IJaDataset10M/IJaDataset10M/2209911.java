package org.grassfield.common.dao;

import org.grassfield.common.entity.AuditLog;

/**
 * The Class AuditLogDAO.
 */
public class AuditLogDAO extends BaseDAO implements IAuditLogDAO {

    /** The Constant FIELD_LENGTH. */
    private static final int FIELD_LENGTH = 255;

    @Override
    public final AuditLog add(final AuditLog auditLog) {
        String message = auditLog.getMessage();
        if (message != null) {
            if (message.length() >= AuditLogDAO.FIELD_LENGTH) {
                message = message.substring(0, AuditLogDAO.FIELD_LENGTH);
                auditLog.setMessage(message);
            }
        }
        return (AuditLog) super.save(auditLog);
    }
}
