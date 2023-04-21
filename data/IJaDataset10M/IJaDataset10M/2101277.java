package edu.rice.cs.drjava.model.definitions.indent;

import junit.framework.*;
import edu.rice.cs.drjava.model.definitions.DefinitionsDocument;
import edu.rice.cs.drjava.model.definitions.reducedmodel.*;

/**
 * Tests whether the current line is empty.
 * 
 * @version $Id: QuestionCurrLineEmptyTest.java 577 2002-03-30 08:38:28Z csreis $
 */
public class QuestionCurrLineEmptyTest extends IndentRulesTestCase {

    public QuestionCurrLineEmptyTest(String name) {
        super(name);
    }

    static IndentRuleQuestion _rule = new QuestionCurrLineEmpty(null, null);

    public void testEmpty() throws javax.swing.text.BadLocationException {
        _setDocText("/*\n\n*/");
        assertTrue("nothing on line", _rule.applyRule(_doc, 3));
    }

    public void testSpaces() throws javax.swing.text.BadLocationException {
        _setDocText("/*\n        \n*/");
        assertTrue("only spaces", _rule.applyRule(_doc, 6));
    }

    static String stuffExample = "/*\n   foo   \n*/";

    public void testStuffBefore() throws javax.swing.text.BadLocationException {
        _setDocText(stuffExample);
        assertTrue("text before the cursor", !_rule.applyRule(_doc, 3));
    }

    public void testStuffAfter() throws javax.swing.text.BadLocationException {
        _setDocText(stuffExample);
        assertTrue("text after the cursor", !_rule.applyRule(_doc, 11));
    }

    public void testLineWithStar() throws javax.swing.text.BadLocationException {
        _setDocText("/*\n * foo\n */");
        assertTrue("line with a star", !_rule.applyRule(_doc, 5));
    }
}
