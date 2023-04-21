package com.unitedinternet.portal.selenium.utils.logging;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.Writer;
import java.text.MessageFormat;
import org.junit.Test;

public class HtmlResultFormatterTest {

    @Test
    public void messageFormatTest() {
        String res = MessageFormat.format("{0,number,#}", 1234);
        assertThat(res).isEqualTo("1234");
    }

    @Test
    public void formatHeader() {
        TestMetricsBean testMetrics = new TestMetricsBean();
        testMetrics.setUserAgent("UserAgentTestString");
        HtmlResultFormatter testFormatter = new HtmlResultFormatter(null);
        String header = testFormatter.formatHeader(testMetrics);
        assertNotNull(header);
        assertThat(header).contains("UserAgentTestString");
        assertThat(header).excludes("'''");
        assertThat(header).excludes("'{'");
        assertThat(header).excludes("'}'");
    }

    @Test
    public void formatHeader_correctEncoding() {
        TestMetricsBean testMetrics = new TestMetricsBean();
        testMetrics.setUserAgent("UserAgentTestString");
        HtmlResultFormatter testFormatter = new HtmlResultFormatter(null, "UTF-8");
        String header = testFormatter.formatHeader(testMetrics);
        assertNotNull(header);
        assertThat(header).contains("UserAgentTestString");
        assertThat(header).contains("UTF-8");
    }

    @Test
    public void formatHeader_correctNumberFormats() {
        long testDuration = 1111L;
        TestMetricsBean testMetrics = new TestMetricsBean();
        long currentMillis = System.currentTimeMillis();
        testMetrics.setStartTimeStamp(currentMillis);
        testMetrics.setEndTimeStamp(currentMillis + testDuration);
        testMetrics.setCommandsProcessed(2222L);
        testMetrics.setVerificationsProcessed(3333L);
        HtmlResultFormatter testFormatter = new HtmlResultFormatter(null);
        String header = testFormatter.formatHeader(testMetrics);
        assertThat(header).isNotNull().contains(Long.toString(testDuration)).contains("2222").contains("3333");
    }

    @Test
    public void formatScreenshot_correctDurationNumberFormat() {
        LoggingBean testLBean = new LoggingBean();
        testLBean.setArgs(new String[] { "" });
        testLBean.setCmdStartMillis(0);
        testLBean.setCmdEndMillis(1111);
        HtmlResultFormatter testFormatter = new HtmlResultFormatter(null);
        String result = testFormatter.formatScreenshot(testLBean, "");
        assertThat(result).isNotNull().contains("1111");
    }

    @Test
    public void formatScreenshotFileImgTag_RelativePathEndswithSlash() {
        HtmlResultFormatter testFormatter = new HtmlResultFormatter(null, "UTF-8");
        String scBaseUri = "blah/blah/";
        String scFileName = "shotTest.png";
        testFormatter.setScreenShotBaseUri(scBaseUri);
        String result = testFormatter.formatScreenshotFileImgTag("file://foo/bar/" + scFileName);
        assertThat(result).contains(scBaseUri + scFileName);
        assertThat(result).excludes(scBaseUri + "/" + scFileName);
    }

    @Test
    public void formatScreenshotFileImgTag_RelativePathEndswithNoSlash() {
        HtmlResultFormatter testFormatter = new HtmlResultFormatter(null, "UTF-8");
        String scBaseUri = "blah/blah";
        String scFileName = "shotTest.png";
        testFormatter.setScreenShotBaseUri(scBaseUri);
        String result = testFormatter.formatScreenshotFileImgTag("file://foo/bar/" + scFileName);
        assertThat(result).contains(scBaseUri + "/" + scFileName);
        assertThat(result).excludes(scBaseUri + "//" + scFileName);
    }

    @Test
    public void formatScreenshotFileImgTag_AbsolutePathHttpEndswithSlash() {
        HtmlResultFormatter testFormatter = new HtmlResultFormatter(null, "UTF-8");
        String scBaseUri = "http://my.domain.notthere/blah/blah/";
        String scFileName = "shotTest.png";
        testFormatter.setScreenShotBaseUri(scBaseUri);
        String result = testFormatter.formatScreenshotFileImgTag("file://foo/bar/" + scFileName);
        assertThat(result).contains(scBaseUri + scFileName);
        assertThat(result).excludes(scBaseUri + "/" + scFileName);
    }

    @Test
    public void formatScreenshotFileImgTag_AbsolutePathHttpEndswithNoSlash() {
        HtmlResultFormatter testFormatter = new HtmlResultFormatter(null, "UTF-8");
        String scBaseUri = "http://my.domain.notthere/blah/blah";
        String scFileName = "shotTest.png";
        testFormatter.setScreenShotBaseUri(scBaseUri);
        String result = testFormatter.formatScreenshotFileImgTag("file://foo/bar/" + scFileName);
        assertThat(result).contains(scBaseUri + "/" + scFileName);
        assertThat(result).excludes(scBaseUri + "//" + scFileName);
    }

    @Test
    public void formatScreenshotFileImgTag_AbsolutePathFileEndswithSlash() {
        HtmlResultFormatter testFormatter = new HtmlResultFormatter(null, "UTF-8");
        String scBaseUri = "file://my.domain.notthere/blah/blah/";
        String scFileName = "shotTest.png";
        testFormatter.setScreenShotBaseUri(scBaseUri);
        String result = testFormatter.formatScreenshotFileImgTag("file://foo/bar/" + scFileName);
        assertThat(result).contains(scBaseUri + scFileName);
        assertThat(result).excludes(scBaseUri + "/" + scFileName);
    }

    @Test
    public void formatScreenshotFileImgTag_AbsolutePathFileEndswithNoSlash() {
        HtmlResultFormatter testFormatter = new HtmlResultFormatter(null, "UTF-8");
        String scBaseUri = "file://my.domain.notthere/blah/blah";
        String scFileName = "shotTest.png";
        testFormatter.setScreenShotBaseUri(scBaseUri);
        String result = testFormatter.formatScreenshotFileImgTag("file://foo/bar/" + scFileName);
        assertThat(result).contains(scBaseUri + "/" + scFileName);
        assertThat(result).excludes(scBaseUri + "//" + scFileName);
    }

    private Writer setUpMockWriter(final StringBuffer writeBuffer) {
        return new Writer() {

            public void flush() {
            }

            public void close() {
            }

            public void write(char[] cbuf, int off, int len) {
                writeBuffer.append(cbuf);
            }
        };
    }

    @Test
    public void commentLogEvent_twoArgs() {
        final StringBuffer writeBuffer = new StringBuffer();
        Writer mockWriter = setUpMockWriter(writeBuffer);
        LoggingBean testBean = new LoggingBean();
        testBean.setArgs(new String[] { "arg1", "arg2" });
        HtmlResultFormatter testFormatter = new HtmlResultFormatter(mockWriter);
        testFormatter.commentLogEvent(testBean);
        assertThat(writeBuffer.toString()).startsWith("<tr class=\"title\"><td colspan=\"7\">arg1" + "<span style=\"font-size:9px;font-family:arial,verdana,sans-serif;\">arg2</span>" + "</td></tr>\n");
    }

    @Test
    public void formatMetrics_failedCommands() {
        TestMetricsBean testMetrics = new TestMetricsBean();
        testMetrics.setFailedCommands(1);
        HtmlResultFormatter testFormatter = new HtmlResultFormatter(null);
        String metrics = testFormatter.formatMetrics(testMetrics);
        assertNotNull(metrics);
        assertThat(metrics).contains("<tr class=\"status_failed\"><td>failed commands:</td><td>1</td></tr>");
    }

    @Test
    public void headerLogEvent() {
        final StringBuffer writeBuffer = new StringBuffer();
        Writer mockWriter = setUpMockWriter(writeBuffer);
        TestMetricsBean testMetrics = new TestMetricsBean();
        testMetrics.setUserAgent("UserAgentTestString");
        HtmlResultFormatter testFormatter = new HtmlResultFormatter(mockWriter);
        testFormatter.headerLogEvent(testMetrics);
        String loggingResult = writeBuffer.toString();
        assertThat(loggingResult).contains("UserAgentTestString");
        assertThat(loggingResult).excludes("'''");
        assertThat(loggingResult).excludes("'{'");
        assertThat(loggingResult).excludes("'}'");
    }

    @Test
    public void footerLogEvent() {
        final StringBuffer writeBuffer = new StringBuffer();
        Writer mockWriter = setUpMockWriter(writeBuffer);
        HtmlResultFormatter testFormatter = new HtmlResultFormatter(mockWriter);
        testFormatter.footerLogEvent();
        String loggingResult = writeBuffer.toString();
        assertTrue(loggingResult.startsWith(HtmlResultFormatter.HTML_FOOTER));
    }

    @Test
    public void extraInformationLogEvent() {
        HtmlResultFormatter testFormatter = new HtmlResultFormatter(null);
        String result = testFormatter.extraInformationLogEvent(null);
        assertThat(result).isEqualTo("");
    }

    @Test
    public void commandLogEvent_commandFailed_noScreenshot() {
        final StringBuffer writeBuffer = new StringBuffer();
        Writer mockWriter = setUpMockWriter(writeBuffer);
        LoggingBean testBean = new LoggingBean();
        HtmlResultFormatter testFormatter = new HtmlResultFormatter(mockWriter);
        testFormatter.commandLogEvent(testBean);
        String result = writeBuffer.toString();
        assertThat(result).contains("status_failed");
    }

    @Test
    public void commandLogEvent_commandSuccess_noScreenshot() {
        final StringBuffer writeBuffer = new StringBuffer();
        Writer mockWriter = setUpMockWriter(writeBuffer);
        LoggingBean testBean = new LoggingBean();
        testBean.setCommandSuccessful(true);
        HtmlResultFormatter testFormatter = new HtmlResultFormatter(mockWriter);
        testFormatter.commandLogEvent(testBean);
        String result = writeBuffer.toString();
        assertThat(result).contains("status_done");
    }

    @Test
    public void commandLogEvent_screenshot() {
        final StringBuffer writeBuffer = new StringBuffer();
        Writer mockWriter = setUpMockWriter(writeBuffer);
        LoggingBean testBean = new LoggingBean();
        testBean.setCommandName("captureScreenshot");
        testBean.setArgs(new String[] { "arg1" });
        HtmlResultFormatter testFormatter = new HtmlResultFormatter(mockWriter);
        testFormatter.commandLogEvent(testBean);
        String result = writeBuffer.toString();
        assertThat(result).contains("arg1");
        assertThat(result).contains("Selenium Screenshot");
    }

    @Test
    public void commandLogEvent_screenshotWindowsPath() {
        final String providedImgSrc = "E:\\somepath\\someImg.png";
        final StringBuffer writeBuffer = new StringBuffer();
        Writer mockWriter = setUpMockWriter(writeBuffer);
        LoggingBean testBean = new LoggingBean();
        testBean.setCommandName("captureScreenshot");
        testBean.setArgs(new String[] { providedImgSrc });
        HtmlResultFormatter testFormatter = new HtmlResultFormatter(mockWriter);
        testFormatter.localFsPathSeparator = "\\";
        testFormatter.setScreenShotBaseUri("myExtraPath/");
        testFormatter.commandLogEvent(testBean);
        String result = writeBuffer.toString();
        assertThat(result).contains("Selenium Screenshot");
        assertThat(result).contains("src=\"myExtraPath/someImg.png\"");
    }

    @Test
    public void commandLogEvent_cmdExcludedFromLogging() {
        final String cmdToBeExcluded = "getHtmlSource";
        final String cmdArgToBeExcluded = "SHOULD-NOT-BE-IN-RESULT";
        final StringBuffer writeBuffer = new StringBuffer();
        Writer mockWriter = setUpMockWriter(writeBuffer);
        LoggingBean testBean = new LoggingBean();
        testBean.setCommandName(cmdToBeExcluded);
        testBean.setExcludeFromLogging(true);
        testBean.setArgs(new String[] { cmdArgToBeExcluded });
        HtmlResultFormatter testFormatter = new HtmlResultFormatter(mockWriter);
        testFormatter.commandLogEvent(testBean);
        String result = writeBuffer.toString();
        assertThat(result).excludes(cmdToBeExcluded);
        assertThat(result).excludes(cmdArgToBeExcluded);
    }

    @Test
    public void booleanCommandLogEvent_commandPass() {
        final StringBuffer writeBuffer = new StringBuffer();
        Writer mockWriter = setUpMockWriter(writeBuffer);
        LoggingBean testBean = new LoggingBean();
        testBean.setCommandSuccessful(true);
        HtmlResultFormatter testFormatter = new HtmlResultFormatter(mockWriter);
        testFormatter.booleanCommandLogEvent(testBean);
        String result = writeBuffer.toString();
        assertThat(result).contains("status_passed");
    }

    @Test
    public void booleanCommandLogEvent_commandFail() {
        final StringBuffer writeBuffer = new StringBuffer();
        Writer mockWriter = setUpMockWriter(writeBuffer);
        LoggingBean testBean = new LoggingBean();
        testBean.setCommandSuccessful(false);
        HtmlResultFormatter testFormatter = new HtmlResultFormatter(mockWriter);
        testFormatter.booleanCommandLogEvent(testBean);
        String result = writeBuffer.toString();
        assertThat(result).contains("status_maybefailed");
    }

    @Test
    public void generateEmptyColumns_loopZero() {
        String result = HtmlResultFormatter.generateEmptyColumns(0);
        assertThat(result).isEqualTo("");
    }

    @Test
    public void generateEmptyColumns_loopOneTime() {
        String result = HtmlResultFormatter.generateEmptyColumns(1);
        assertEquals(HtmlResultFormatter.HTML_EMPTY_COLUMN, result);
    }

    @Test
    public void formatCommandAsHtml_argsNotNull() {
        LoggingBean testBean = new LoggingBean();
        testBean.setArgs(new String[] {});
        HtmlResultFormatter testFormatter = new HtmlResultFormatter(null);
        String result = testFormatter.formatCommandAsHtml(testBean, "testClass", "testToolTipp");
        assertThat(result).startsWith("<tr class=\"testClass\" title=\"testToolTipp\" alt=\"testToolTipp\"><td>");
        assertThat(result).doesNotMatch("<tr class=\"testClass\" title=\"testToolTipp\" alt=\"testToolTipp\"><td></td><td>arg1</td><td>&nbsp;</td>");
    }

    @Test
    public void formatCommandAsHtml_argsNotEmpty() {
        LoggingBean testBean = new LoggingBean();
        testBean.setArgs(new String[] { "arg1" });
        HtmlResultFormatter testFormatter = new HtmlResultFormatter(null);
        String result = testFormatter.formatCommandAsHtml(testBean, "testClass", "testToolTipp");
        assertThat(result).startsWith("<tr class=\"testClass\" title=\"testToolTipp\" alt=\"testToolTipp\"><td></td><td>arg1</td><td>&nbsp;</td>");
        assertThat(result).doesNotMatch("<tr class=\"testClass\" title=\"testToolTipp\" alt=\"testToolTipp\"><td></td><td>&nbsp;</td>");
    }

    @Test
    public void formatCommandAsHtml_manyArgs() {
        LoggingBean testBean = new LoggingBean();
        testBean.setArgs(new String[] { "arg1", "arg2" });
        HtmlResultFormatter testFormatter = new HtmlResultFormatter(null);
        String result = testFormatter.formatCommandAsHtml(testBean, "testClass", "testToolTipp");
        assertThat(result).startsWith("<tr class=\"testClass\" title=\"testToolTipp\" alt=\"testToolTipp\"><td></td><td>arg1</td><td>arg2</td>");
    }
}
