package com.volantis.mcs.xdime.xforms;

import com.volantis.mcs.protocols.SelectOption;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.forms.FieldType;
import com.volantis.mcs.xdime.XDIMEContext;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xml.schema.model.ElementType;
import java.util.List;

/**
 * Tests the concrete class {@link XFSelectElementImpl}.
 */
public class XFSelectElementImplTestCase extends AbstractXFSelectElementImplTestAbstract {

    protected AbstractXFSelectElementImpl getSelectElementImpl(XDIMEContextInternal context) {
        return new XFSelectElementImpl(context) {

            protected FieldType getFieldType() {
                return mockFieldType;
            }

            protected VolantisProtocol getProtocol(XDIMEContext context) {
                return protocol;
            }
        };
    }

    /**
     * Verify that if multiple initial values are set, all of which map to
     * selector item values, the corresponding {@link SelectOption} instances
     * are selected on the generated protocol attributes.
     */
    public void testWithMultipleNonNullMatchingInitialValues() throws XDIMEException {
        final String[] initialValues = new String[] { OPTION1_VALUE, OPTION2_VALUE };
        List options = doTest(initialValues);
        assertTrue(((SelectOption) options.get(0)).isSelected());
        assertTrue(((SelectOption) options.get(1)).isSelected());
    }

    /**
     * Verify that if multiple initial values are set, not all of which map to
     * selector item values, only the matching ones' corresponding
     * {@link SelectOption}s are selected on the generated protocol attributes.
     */
    public void testWithMultipleNonNullMatchingInitialValues2() throws XDIMEException {
        final String[] initialValues = new String[] { OPTION1_VALUE, NON_MATCHING_VALUE };
        List options = doTest(initialValues);
        assertTrue(((SelectOption) options.get(0)).isSelected());
        assertFalse(((SelectOption) options.get(1)).isSelected());
    }

    protected boolean getMultiple() {
        return true;
    }

    protected String getTagName() {
        return "xfmuselect";
    }

    protected ElementType getElementType() {
        return XFormElements.SELECT;
    }
}
