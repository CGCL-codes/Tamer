package com.google.gwt.uibinder.attributeparsers;

import com.google.gwt.uibinder.attributeparsers.FieldReferenceConverter.IllegalFieldReferenceException;
import com.google.gwt.uibinder.attributeparsers.StrictAttributeParser.FieldReferenceDelegate;
import junit.framework.TestCase;

/**
 * Tests StrictAttributeParser. Actually, tests its static inner class which
 * does all of the actual work, so that we don't have to struggle to mock
 * UiBinderWriter.
 */
public class StrictAttributeParserTest extends TestCase {

    FieldReferenceConverter converter = new FieldReferenceConverter(null);

    public void testSimple() {
        String before = "{able.baker.charlie.prawns}";
        String expected = "able.baker().charlie().prawns()";
        assertEquals(expected, converter.convert(before, new FieldReferenceDelegate(null)));
    }

    public void testNoneShouldFail() {
        String before = "able.baker.charlie.prawns";
        try {
            converter.convert(before, new FieldReferenceDelegate(null));
            fail();
        } catch (IllegalFieldReferenceException e) {
        }
    }

    public void testTooManyShouldFail() {
        String before = "{able.baker.charlie} {prawns.are.yummy}";
        try {
            converter.convert(before, new FieldReferenceDelegate(null));
            fail();
        } catch (IllegalFieldReferenceException e) {
        }
    }

    public void testMixedShouldFail() {
        String before = "{able.baker.charlie} prawns are still yummy}";
        try {
            converter.convert(before, new FieldReferenceDelegate(null));
            fail();
        } catch (IllegalFieldReferenceException e) {
        }
    }
}
