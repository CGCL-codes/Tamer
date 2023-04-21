package com.core.test.schedule;

import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import com.core.service.schedule.JdkExecutorJob;
import org.springside.modules.log.MockLog4jAppender;
import org.springside.modules.test.spring.SpringTxTestCase;
import org.springside.modules.test.utils.DbUnitUtils;
import org.springside.modules.utils.ThreadUtils;

@DirtiesContext
@ContextConfiguration(locations = { "/applicationContext-test.xml", "/schedule/applicationContext-executor.xml" })
@TransactionConfiguration(transactionManager = "defaultTransactionManager")
public class JdkExecutorJobTest extends SpringTxTestCase {

    @Test
    public void scheduleJob() throws Exception {
        DbUnitUtils.loadData(dataSource, "/data/default-data.xml");
        MockLog4jAppender appender = new MockLog4jAppender();
        appender.addToLogger(JdkExecutorJob.class);
        ThreadUtils.sleep(3000);
        assertEquals(1, appender.getAllLogs().size());
        assertEquals("There are 6 user in database.", appender.getFirstLog().getMessage());
        DbUnitUtils.removeData(dataSource, "/data/default-data.xml");
    }
}
