package net.sourceforge.vigilog.parse;

import net.sourceforge.vigilog.model.LogEntry;
import net.sourceforge.vigilog.model.LogLevel;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

/**
 *
 */
public class JavaLoggingXMLFileLogFileParserTest {

    @Test
    public void testParse() throws LogFileParserException, URISyntaxException {
        JavaLoggingXMLFileLogFileParser parser = new JavaLoggingXMLFileLogFileParser();
        List<LogEntry> logEntries = parser.parse(new File(getClass().getResource("sample-java-utils-log.xml").toURI()));
        Assert.assertEquals(logEntries.size(), 2);
        LogEntry logEntry = logEntries.get(0);
        Assert.assertEquals(logEntry.getId(), 1256);
        Assert.assertEquals(logEntry.getLevel(), LogLevel.INFO);
        Assert.assertEquals(logEntry.getLogger(), "kgh.test.fred");
        Assert.assertEquals(logEntry.getMessage(), "Hello world");
        Assert.assertEquals(logEntry.getThread(), "10");
        Assert.assertNull(logEntry.getThrowable());
        Assert.assertEquals(logEntry.getTimestamp(), 967083665789L);
    }
}
