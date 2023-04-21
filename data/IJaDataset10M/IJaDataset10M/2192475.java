package org.waveprotocol.wave.client.editor.content;

import org.waveprotocol.wave.client.editor.extract.InconsistencyException.HtmlMissing;
import org.waveprotocol.wave.client.editor.testing.TestEditors;

/**
 * @author danilatos@google.com (Daniel Danilatos)
 */
public class ContentElementGwtTest extends ContentTestBase {

    public void testImplDataSumsTextNodes() throws HtmlMissing {
        ContentDocument dom = TestEditors.createTestDocument();
        c = dom.debugGetRawDocument();
        ContentElement root = c.getDocumentElement();
        ContentTextNode t1 = c.createTextNode("hello", root, null);
        t1.setImplNodelet(null);
        root.zipChildren(t1, t1, null);
        assertEquals("hello", t1.getImplData());
    }
}
