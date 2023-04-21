package com.idna.common.dao;

import java.util.UUID;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import com.idna.common.domain.CommsLog;

public class CommsLogDaoTest extends BaseDaoTestCase {

    @Autowired
    private CommsLogDao commsLogDao;

    @Rollback(true)
    public void testInsertCommsLog() {
        String searchFeatureID = "42697683-9b70-449c-8b57-e65c5e399a10";
        String ref = UUID.randomUUID().toString();
        CommsLog commsLog = new CommsLog();
        commsLog.setSearchFeatureID(searchFeatureID);
        commsLog.setRequest("request");
        commsLog.setResponse("response");
        commsLog.setExecTime(1000);
        commsLog.setHTTPResponseCode("responseCode");
        commsLog.setIsError(0);
        commsLog.setRef(ref);
        commsLog.setRequestType("1");
        commsLog.setResponseType("1");
        commsLog.setMode(1);
        commsLog.setTpCode("1");
        int rowNumberBefore = countRowsInTable("CommunicationLog");
        logger.debug(rowNumberBefore + " rows before inserting.");
        String commsLogId = commsLogDao.recordCommunicationCall(commsLog);
        logger.debug(commsLogId + " inserted. ");
        int rowNumberAfter = countRowsInTable("CommunicationLog");
        logger.debug(rowNumberAfter + " rows after inserting.");
        assertNotNull(commsLogId);
        assertEquals(rowNumberBefore, rowNumberAfter - 1);
    }

    @Rollback(true)
    @Ignore
    public void testGetCommsLog() {
        String futureId = "42697683-9b70-449c-8b57-e65c5e399a10";
        String tpCode = "1";
        CommsLog commsLog = commsLogDao.getCommsLog(futureId, tpCode);
        logger.debug(commsLog);
        assertNotNull(commsLog);
    }
}
