package org.panopticode.doclet;

import junit.framework.TestCase;
import com.sun.javadoc.LanguageVersion;

public class PanopticodeDocletTest extends TestCase {

    public void testOptionLengthIdentifiesParametersCorrectly() {
        assertEquals(2, PanopticodeDoclet.optionLength("-outputFile"));
        assertEquals(2, PanopticodeDoclet.optionLength("-projectName"));
        assertEquals(2, PanopticodeDoclet.optionLength("-projectVersion"));
    }

    public void testOptionLengthRejectsUnknownParameters() {
        assertEquals(0, PanopticodeDoclet.optionLength("-unknownParameterName"));
    }

    public void testLanguageVersion() {
        assertSame(LanguageVersion.JAVA_1_5, PanopticodeDoclet.languageVersion());
    }
}
