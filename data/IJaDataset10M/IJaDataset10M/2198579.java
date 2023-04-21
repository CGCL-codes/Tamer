package org.w3c.tidy;

import java.io.StringWriter;
import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabrizio Giustina
 * @version $Revision: 812 $ ($Author: fgiust $)
 */
public class ConfigurationTest extends TestCase {

    /**
     * logger.
     */
    private static Logger log = LoggerFactory.getLogger(ConfigurationTest.class);

    /**
     * Test for -help-config.
     * @throws Exception any exception thrown during test
     */
    public void testPrintConfig() throws Exception {
        Tidy tidy = new Tidy();
        Configuration configuration = tidy.getConfiguration();
        StringWriter writer = new StringWriter();
        configuration.printConfigOptions(writer, false);
        String result = writer.toString();
        assertTrue(result.length() > 200);
        log.debug(result);
    }

    /**
     * Test for -show-config.
     * @throws Exception any exception thrown during test
     */
    public void testPrintActualConfig() throws Exception {
        Tidy tidy = new Tidy();
        tidy.getConfiguration().tt.defineTag(Dict.TAGTYPE_INLINE, "something");
        tidy.getConfiguration().tt.defineTag(Dict.TAGTYPE_INLINE, "second");
        Configuration configuration = tidy.getConfiguration();
        StringWriter writer = new StringWriter();
        configuration.printConfigOptions(writer, true);
        String result = writer.toString();
        assertTrue(result.length() > 200);
        log.debug(result);
    }

    /**
     * Test for configuration getters and setters.
     * @throws Exception any exception thrown during test
     */
    public void testGetSet() throws Exception {
        Tidy tidy = new Tidy();
        tidy.setAltText("alt");
        assertEquals("alt", tidy.getAltText());
        tidy.setAsciiChars(false);
        assertEquals(false, tidy.getAsciiChars());
        tidy.setBreakBeforeBR(true);
        assertEquals(true, tidy.getBreakBeforeBR());
        tidy.setBurstSlides(true);
        assertEquals(true, tidy.getBurstSlides());
        tidy.setDropEmptyParas(false);
        assertEquals(false, tidy.getDropEmptyParas());
        tidy.setDropFontTags(true);
        assertEquals(true, tidy.getDropFontTags());
        tidy.setDropProprietaryAttributes(true);
        assertEquals(true, tidy.getDropProprietaryAttributes());
        tidy.setEmacs(true);
        assertEquals(true, tidy.getEmacs());
        tidy.setEncloseBlockText(true);
        assertEquals(true, tidy.getEncloseBlockText());
        tidy.setEncloseText(true);
        assertEquals(true, tidy.getEncloseText());
        tidy.setEscapeCdata(true);
        assertEquals(true, tidy.getEscapeCdata());
        tidy.setFixBackslash(true);
        assertEquals(true, tidy.getFixBackslash());
        tidy.setFixComments(true);
        assertEquals(true, tidy.getFixComments());
        tidy.setFixUri(true);
        assertEquals(true, tidy.getFixUri());
        tidy.setForceOutput(true);
        assertEquals(true, tidy.getForceOutput());
        tidy.setHideComments(true);
        assertEquals(true, tidy.getHideComments());
        tidy.setHideEndTags(true);
        assertEquals(true, tidy.getHideEndTags());
        tidy.setIndentAttributes(true);
        assertEquals(true, tidy.getIndentAttributes());
        tidy.setIndentCdata(true);
        assertEquals(true, tidy.getIndentCdata());
        tidy.setIndentContent(true);
        assertEquals(true, tidy.getIndentContent());
        tidy.setJoinClasses(true);
        assertEquals(true, tidy.getJoinClasses());
        tidy.setJoinStyles(true);
        assertEquals(true, tidy.getJoinStyles());
        tidy.setKeepFileTimes(true);
        assertEquals(true, tidy.getKeepFileTimes());
        tidy.setLiteralAttribs(true);
        assertEquals(true, tidy.getLiteralAttribs());
        tidy.setLogicalEmphasis(true);
        assertEquals(true, tidy.getLogicalEmphasis());
        tidy.setLowerLiterals(true);
        assertEquals(true, tidy.getLowerLiterals());
        tidy.setMakeBare(true);
        assertEquals(true, tidy.getMakeBare());
        tidy.setMakeClean(true);
        assertEquals(true, tidy.getMakeClean());
        tidy.setNumEntities(true);
        assertEquals(true, tidy.getNumEntities());
        tidy.setOnlyErrors(true);
        assertEquals(true, tidy.getOnlyErrors());
        tidy.setPrintBodyOnly(true);
        assertEquals(true, tidy.getPrintBodyOnly());
        tidy.setQuiet(true);
        assertEquals(true, tidy.getQuiet());
        tidy.setQuoteAmpersand(true);
        assertEquals(true, tidy.getQuoteAmpersand());
        tidy.setQuoteMarks(true);
        assertEquals(true, tidy.getQuoteMarks());
        tidy.setQuoteNbsp(true);
        assertEquals(true, tidy.getQuoteNbsp());
        tidy.setRawOut(true);
        assertEquals(true, tidy.getRawOut());
        tidy.setReplaceColor(true);
        assertEquals(true, tidy.getReplaceColor());
        tidy.setShowWarnings(true);
        assertEquals(true, tidy.getShowWarnings());
        tidy.setSmartIndent(true);
        assertEquals(true, tidy.getSmartIndent());
        tidy.setTidyMark(true);
        assertEquals(true, tidy.getTidyMark());
        tidy.setTrimEmptyElements(true);
        assertEquals(true, tidy.getTrimEmptyElements());
        tidy.setUpperCaseAttrs(true);
        assertEquals(true, tidy.getUpperCaseAttrs());
        tidy.setUpperCaseTags(true);
        assertEquals(true, tidy.getUpperCaseTags());
        tidy.setWord2000(true);
        assertEquals(true, tidy.getWord2000());
        tidy.setWrapAsp(true);
        assertEquals(true, tidy.getWrapAsp());
        tidy.setWrapAttVals(true);
        assertEquals(true, tidy.getWrapAttVals());
        tidy.setWrapJste(true);
        assertEquals(true, tidy.getWrapJste());
        tidy.setWrapPhp(true);
        assertEquals(true, tidy.getWrapPhp());
        tidy.setWrapScriptlets(true);
        assertEquals(true, tidy.getWrapScriptlets());
        tidy.setWrapSection(true);
        assertEquals(true, tidy.getWrapSection());
        tidy.setWraplen(5);
        assertEquals(5, tidy.getWraplen());
        tidy.setWriteback(true);
        assertEquals(true, tidy.getWriteback());
        tidy.setXHTML(true);
        assertEquals(true, tidy.getXHTML());
        tidy.setXmlOut(true);
        assertEquals(true, tidy.getXmlOut());
        tidy.setXmlPi(true);
        assertEquals(true, tidy.getXmlPi());
        tidy.setXmlPIs(true);
        assertEquals(true, tidy.getXmlPIs());
        tidy.setXmlSpace(true);
        assertEquals(true, tidy.getXmlSpace());
        tidy.setXmlTags(true);
        assertEquals(true, tidy.getXmlTags());
        tidy.setTabsize(5);
        assertEquals(5, tidy.getTabsize());
        tidy.setOutputEncoding("UTF8");
        assertEquals("UTF8", tidy.getOutputEncoding());
        tidy.setInputEncoding("UTF8");
        assertEquals("UTF8", tidy.getInputEncoding());
        tidy.setRepeatedAttributes(Configuration.KEEP_FIRST);
        assertEquals(Configuration.KEEP_FIRST, tidy.getRepeatedAttributes());
        tidy.setShowErrors(10);
        assertEquals(10, tidy.getShowErrors());
        tidy.setDocType("strict");
        assertEquals("strict", tidy.getDocType());
        tidy.setErrfile("errfile");
        assertEquals("errfile", tidy.getErrfile());
        tidy.setSpaces(5);
        assertEquals(5, tidy.getSpaces());
        tidy.setInputStreamName("inputname");
        assertEquals("inputname", tidy.getInputStreamName());
    }
}
