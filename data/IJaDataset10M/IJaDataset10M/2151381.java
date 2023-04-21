package org.mili.core.text.transformation;

import org.junit.*;
import org.mili.core.text.*;
import static org.junit.Assert.*;

/**
 * @author Michael Lieshoff
 */
public class XMLTransformatorTest {

    private XMLTransformator tranformator = XMLTransformator.create();

    @Test
    public void shouldHaveStandardSeparator() {
        assertEquals(MockFactory.getXmlString(), this.tranformator.transform(MockFactory.createTextTable()).toString().replaceAll("[\r\n]", ""));
    }
}
