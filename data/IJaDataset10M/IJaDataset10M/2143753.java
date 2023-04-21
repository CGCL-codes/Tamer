package com.volantis.mcs.xdime.widgets;

import com.volantis.mcs.protocols.widgets.attributes.TableAttributes;
import com.volantis.mcs.xdime.xhtml2.XHTML2Elements;
import com.volantis.mcs.xml.schema.validation.DocumentValidator;

/**
 * Test the widget:table element
 */
public class TableTestCase extends WidgetElementTestCaseAbstract {

    protected void setUp() throws Exception {
        super.setUp();
        addDefaultElementExpectations(TableAttributes.class);
    }

    protected String getElementName() {
        return WidgetElements.TABLE.getLocalName();
    }

    public void testWidget() throws Exception {
        startDocumentAndElement(getElementName());
        DocumentValidator validator = xdimeContext.getDocumentValidator();
        validator.open(XHTML2Elements.THEAD);
        validator.open(XHTML2Elements.TR);
        validator.open(XHTML2Elements.TD);
        validator.close(XHTML2Elements.TD);
        validator.close(XHTML2Elements.TR);
        validator.close(XHTML2Elements.THEAD);
        validator.open(XHTML2Elements.TFOOT);
        validator.open(XHTML2Elements.TR);
        validator.open(XHTML2Elements.TD);
        validator.close(XHTML2Elements.TD);
        validator.close(XHTML2Elements.TR);
        validator.close(XHTML2Elements.TFOOT);
        validator.open(XHTML2Elements.TBODY);
        validator.open(XHTML2Elements.TR);
        validator.open(XHTML2Elements.TD);
        validator.close(XHTML2Elements.TD);
        validator.close(XHTML2Elements.TR);
        validator.close(XHTML2Elements.TBODY);
        validator.open(WidgetElements.TBODY);
        validator.open(XHTML2Elements.TR);
        validator.open(XHTML2Elements.TD);
        validator.close(XHTML2Elements.TD);
        validator.close(XHTML2Elements.TR);
        validator.close(WidgetElements.TBODY);
        validator.open(WidgetElements.TBODY);
        validator.open(WidgetElements.LOAD);
        validator.close(WidgetElements.LOAD);
        validator.open(XHTML2Elements.TR);
        validator.open(XHTML2Elements.TD);
        validator.close(XHTML2Elements.TD);
        validator.close(XHTML2Elements.TR);
        validator.close(WidgetElements.TBODY);
        endElementAndDocument(getElementName());
    }
}
