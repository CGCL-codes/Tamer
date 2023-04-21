package org.subrecord.integration.appender;

import static org.junit.Assert.assertTrue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.subrecord.integration.AbstractKitTest;
import org.subrecord.kit.SubRecordApiException;
import org.subrecord.model.Response;
import org.subrecord.utils.Utils;

public class Log4jAppenderTest extends AbstractKitTest {

    static {
        System.setProperty("log4j.configuration", "subrecord-log4j.xml");
    }

    @Test
    public void testLogging() throws SubRecordApiException {
        Log log = LogFactory.getLog(getClass());
        log.debug("some debug message");
        log.info("some info message");
        log.error("some error message");
        String token = subrecord.login("admin", "admin");
        Response r = subrecord.findLogs(token, "foo", "$message contains 'some'");
        Utils.out(r.getId());
        assertTrue("3 logs expected, actual size=" + r.getSize(), r.getSize() == 3);
    }
}
