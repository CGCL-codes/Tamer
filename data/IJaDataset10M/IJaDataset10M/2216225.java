package com.gencom.fun.ogame.client.audit;

import java.util.Date;
import javax.annotation.Resource;
import com.gencom.fun.ogame.client.dao.AuditLogDao;

public class AuditLog {

    @Resource(name = "auditLogDao")
    private AuditLogDao auditLogDao;

    public void log(String message, AuditLogEntryType type) {
        AuditLogEntry entry = new AuditLogEntry(message, type, new Date());
        auditLogDao.createAuditLogEntry(entry);
    }
}
