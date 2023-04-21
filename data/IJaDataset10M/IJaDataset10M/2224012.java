package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.dissection.DissectionConstants;
import junit.framework.TestCase;

/**
 * Tests the GenericContainerValidator class.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class GenericContainerValidatorTestCase extends TestCase implements ContainerActions {

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    public GenericContainerValidatorTestCase(String name) {
        super(name);
    }

    /**
     * This method tests the constructors for
     * com.volantis.mcs.protocols.trans.GenericContainerValidator.
     */
    public void testConstructors() {
        Element form = domFactory.createElement();
        Element div = domFactory.createElement();
        Element dissectableContents = domFactory.createElement();
        Element blockquote = domFactory.createElement();
        Element p = domFactory.createElement();
        Element table = domFactory.createElement();
        form.setName("form");
        div.setName("div");
        dissectableContents.setName(DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT);
        blockquote.setName("blockquote");
        p.setName("p");
        table.setName("table");
        ContainerValidator cv = new GenericContainerValidator(RETAIN) {

            protected void initialize() {
                containerActionMap.put("form", new Integer(REMAP));
                containerActionMap.put("div", new Integer(PROMOTE));
                containerActionMap.put(DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT, new Integer(INVERSE_REMAP));
                containerActionMap.put("blockquote", new Integer(RETAIN));
            }
        };
        assertEquals("defaultAction variant did not return as", RETAIN, cv.getAction(p, table));
        assertEquals("defaultAction variant did not return as", REMAP, cv.getAction(form, table));
        assertEquals("defaultAction variant did not return as", PROMOTE, cv.getAction(div, table));
        assertEquals("defaultAction variant did not return as", INVERSE_REMAP, cv.getAction(dissectableContents, table));
        assertEquals("defaultAction variant did not return as", RETAIN, cv.getAction(blockquote, table));
        cv = new GenericContainerValidator() {

            protected void initialize() {
                containerActionMap.put("form", new Integer(REMAP));
                containerActionMap.put("div", new Integer(PROMOTE));
                containerActionMap.put(DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT, new Integer(INVERSE_REMAP));
                containerActionMap.put("blockquote", new Integer(RETAIN));
            }
        };
        assertEquals("Simple variant did not return as", PROMOTE, cv.getAction(p, table));
        assertEquals("Simple variant did not return as", REMAP, cv.getAction(form, table));
        assertEquals("Simple variant did not return as", PROMOTE, cv.getAction(div, table));
        assertEquals("Simple variant did not return as", INVERSE_REMAP, cv.getAction(dissectableContents, table));
        assertEquals("Simple variant did not return as", RETAIN, cv.getAction(blockquote, table));
    }
}
