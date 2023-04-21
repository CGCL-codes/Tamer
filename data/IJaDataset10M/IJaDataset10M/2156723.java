package com.google.gwt.user.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.text.shared.testing.PassthroughRenderer;
import java.text.ParseException;

/**
 * Testing ValueBoxBase.
 */
public class ValueBoxBaseTest extends GWTTestCase {

    public void testParserExceptionWithEmptyString() {
        Element elm = Document.get().createTextInputElement();
        Renderer<String> renderer = PassthroughRenderer.instance();
        MockParser parser = new MockParser();
        ValueBoxBase<String> valueBoxBase = new ValueBoxBase<String>(elm, renderer, parser) {
        };
        parser.throwException = true;
        valueBoxBase.setText("");
        try {
            valueBoxBase.getValueOrThrow();
            fail("Should have thrown ParseException");
        } catch (ParseException e) {
        }
        if (!parser.parseCalled) {
            fail("Parser was not run");
        }
    }

    public void testParserExceptionWithString() {
        Element elm = Document.get().createTextInputElement();
        Renderer<String> renderer = PassthroughRenderer.instance();
        MockParser parser = new MockParser();
        ValueBoxBase<String> valueBoxBase = new ValueBoxBase<String>(elm, renderer, parser) {
        };
        parser.throwException = true;
        valueBoxBase.setText("simple string");
        try {
            valueBoxBase.getValueOrThrow();
            fail("Should have thrown ParseException");
        } catch (ParseException e) {
        }
        if (!parser.parseCalled) {
            fail("Parser was not run");
        }
    }

    public void testSpaces() throws ParseException {
        Element elm = Document.get().createTextInputElement();
        Renderer<String> renderer = PassthroughRenderer.instance();
        MockParser parser = new MockParser();
        ValueBoxBase<String> valueBoxBase = new ValueBoxBase<String>(elm, renderer, parser) {
        };
        String text = "  two space padding test  ";
        valueBoxBase.setText(text);
        assertEquals(text, valueBoxBase.getValueOrThrow());
        if (!parser.parseCalled) {
            fail("Parser was not run");
        }
    }

    @Override
    public String getModuleName() {
        return "com.google.gwt.user.User";
    }
}
