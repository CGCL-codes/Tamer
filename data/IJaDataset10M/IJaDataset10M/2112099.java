package org.apache.batik.parser;

import java.io.*;
import org.apache.batik.test.*;

/**
 * To test the length parser.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: LengthParserFailureTest.java 475477 2006-11-15 22:44:28Z cam $
 */
public class LengthParserFailureTest extends AbstractTest {

    protected String sourceLength;

    /**
     * Creates a new LengthParserFailureTest.
     * @param slength The length to parse.
     */
    public LengthParserFailureTest(String slength) {
        sourceLength = slength;
    }

    public TestReport runImpl() throws Exception {
        LengthParser pp = new LengthParser();
        try {
            pp.parse(new StringReader(sourceLength));
        } catch (ParseException e) {
            return reportSuccess();
        }
        DefaultTestReport report = new DefaultTestReport(this);
        report.setErrorCode("parse.without.error");
        report.addDescriptionEntry("input.text", sourceLength);
        report.setPassed(false);
        return report;
    }
}
