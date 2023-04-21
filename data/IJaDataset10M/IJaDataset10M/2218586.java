package org.goodoldai.jeff.report.xml;

import org.goodoldai.jeff.explanation.TextExplanationChunk;
import java.util.Iterator;
import junit.framework.TestCase;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @author Boris Horvat
 */
public class XMLUtilityTest extends TestCase {

    TextExplanationChunk textEchunk1;

    TextExplanationChunk textEchunk2;

    Document document;

    /**
     * Creates a explanation.TextExplanationChunk, explanation.ImageExplanationChunk,
     * explanation.DataExplanationChunk, and org.dom4j.Document instances that are
     * used for testing
     */
    @Override
    protected void setUp() {
        String[] tags = { "tag1", "tag2" };
        textEchunk1 = new TextExplanationChunk("testing");
        textEchunk2 = new TextExplanationChunk(-10, "testGroup", "testRule", tags, "test text");
        document = DocumentHelper.createDocument();
        document.addElement("root");
    }

    @Override
    protected void tearDown() {
        textEchunk1 = null;
        textEchunk2 = null;
        document = null;
    }

    /**
     * Test of insertExplanationInfo method, of class XMLUtility.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that only has content.
     */
    public void testInsertExplenationInfoFirstConstructor() {
        Element root = document.getRootElement();
        XMLChunkUtility.insertExplanationInfo(textEchunk1, root);
        assertEquals(1, root.attributes().size());
        assertEquals(0, root.elements().size());
        for (Iterator it = root.attributeIterator(); it.hasNext(); ) {
            Attribute attribute = (Attribute) it.next();
            assertEquals("INFORMATIONAL".toLowerCase(), attribute.getText());
        }
    }

    /**
     * Test of insertExplanationInfo method, of class XMLUtility.
     * Test case: successful insertion of data using the ExplanationChunk constructor
     * that has all elements.
     */
    public void testInsertExplenationInfoSecondConstructor() {
        Element root = document.getRootElement();
        String[] names = { "rule", "group", "context" };
        String[] values = { "testRule", "testGroup", "error" };
        String[] tags = { "tag1", "tag2" };
        XMLChunkUtility.insertExplanationInfo(textEchunk2, root);
        assertEquals(3, root.attributes().size());
        assertEquals(1, root.elements().size());
        assertEquals(2, root.element("tags").elements().size());
        int i = 0;
        for (Iterator it = root.attributeIterator(); it.hasNext(); ) {
            Attribute attribute = (Attribute) it.next();
            assertEquals(names[i], attribute.getName());
            assertEquals(values[i++], attribute.getText());
        }
        for (Iterator it = root.elementIterator(); it.hasNext(); ) {
            Element element = (Element) it.next();
            assertEquals("tags", element.getName());
        }
        int j = 0;
        for (Iterator it = root.element("tags").elementIterator(); it.hasNext(); ) {
            Element element = (Element) it.next();
            assertEquals("tag", element.getName());
            assertEquals(tags[j++], element.getText());
        }
    }

    /**
     * Test of translateContext method, of class XMLUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is not predefined
     */
    public void testTranslateContextTypeNotRecognized() {
        int context = -555;
        String result = XMLChunkUtility.translateContext(context, textEchunk1);
        assertEquals(String.valueOf(context), result);
    }

    /**
     * Test of translateContext method, of class XMLUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "INFORMATIONAL"
     */
    public void testTranslateContextKnownTypeInformational() {
        int context = 0;
        String result = XMLChunkUtility.translateContext(context, textEchunk1);
        String expResult = "INFORMATIONAL".toLowerCase();
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class XMLUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "WARNING"
     */
    public void testTranslateContextKnownTypeInformationalWarning() {
        int context = -5;
        String result = XMLChunkUtility.translateContext(context, textEchunk1);
        String expResult = "WARNING".toLowerCase();
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class XMLUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "ERROR"
     */
    public void testTranslateContextKnownTypeError() {
        int context = -10;
        String result = XMLChunkUtility.translateContext(context, textEchunk1);
        String expResult = "ERROR".toLowerCase();
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class XMLUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "POSITIVE"
     */
    public void testTranslateContextKnownTypePositive() {
        int context = 1;
        String result = XMLChunkUtility.translateContext(context, textEchunk1);
        String expResult = "POSITIVE".toLowerCase();
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class XMLUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "VERY_POSITIVE"
     */
    public void testTranslateContextKnownTypeVeryPositive() {
        int context = 2;
        String result = XMLChunkUtility.translateContext(context, textEchunk1);
        String expResult = "VERY_POSITIVE".toLowerCase();
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class XMLUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "NEGATIVE"
     */
    public void testTranslateContextKnownTypeNegative() {
        int context = -1;
        String result = XMLChunkUtility.translateContext(context, textEchunk1);
        String expResult = "NEGATIVE".toLowerCase();
        assertEquals(expResult, result);
    }

    /**
     * Test of translateContext method, of class XMLUtility.
     * Test case: successful transformation of context from the TextExplanationChunk
     * when the context is predefined ant it is "VERY_NEGATIVE"
     */
    public void testTranslateContextKnownTypeVeryNegative() {
        int context = -2;
        String result = XMLChunkUtility.translateContext(context, textEchunk1);
        String expResult = "VERY_NEGATIVE".toLowerCase();
        assertEquals(expResult, result);
    }
}
