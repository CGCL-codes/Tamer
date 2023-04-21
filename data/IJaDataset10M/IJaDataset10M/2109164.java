package ch.heuscher.simple.util.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import ch.heuscher.simple.util.ExtractXmlInputStream;

public class TestExtractXmlInputStream extends DefaultInputStreamTests {

    protected String sTag = "start";

    protected String sRubish = "<\0" + this.sTag + " ";

    protected String sEndTag = "</" + this.sTag + ">";

    protected String sFirstXml = "<" + this.sTag + " >some xml content <subtag></subtag>" + this.sEndTag;

    protected String sTestXml = this.sRubish + this.sFirstXml + this.sRubish + this.sEndTag;

    protected byte[] byTest = new byte[] { 1 };

    public void testGetXml() throws IOException {
        ExtractXmlInputStream extractXmlInputStream = getDefaultExtractXmlInputStream();
        assertEquals(this.sRubish.length(), extractXmlInputStream.getXmlStartPosition());
        String xml = new String(extractXmlInputStream.getXml());
        assertEquals(this.sFirstXml, xml);
    }

    public void testEmpty() throws IOException {
        ExtractXmlInputStream extractXmlInputStream = new ExtractXmlInputStream(new ByteArrayInputStream(byTest), this.sTag);
        String xml = new String(extractXmlInputStream.getXml());
        assertEquals(xml, "");
    }

    protected ExtractXmlInputStream getDefaultExtractXmlInputStream() {
        return new ExtractXmlInputStream(new ByteArrayInputStream(getDefaultResult()), this.sTag);
    }

    protected InputStream getDefaultStream() {
        return getDefaultExtractXmlInputStream();
    }

    protected byte[] getDefaultResult() {
        return this.sTestXml.getBytes();
    }
}
