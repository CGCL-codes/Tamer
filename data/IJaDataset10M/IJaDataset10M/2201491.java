package com.volantis.mcs.xml.schema.validation;

import com.volantis.mcs.xml.schema.impl.validation.ValidationMessages;
import com.volantis.mcs.xml.schema.model.AbstractSchema;
import com.volantis.mcs.xml.schema.model.CompositeModel;
import com.volantis.mcs.xml.schema.model.ElementSchema;

/**
 * Test that exclusion mechanism works correctly.
 */
public class ExclusionTestCase extends ModelValidationTestAbstract {

    public ExclusionTestCase() {
        super(new ExclusionSchema());
    }

    private static class ExclusionSchema extends AbstractSchema {

        ElementSchema a = createElementSchema(TestElements.A);

        ElementSchema b = createElementSchema(TestElements.B);

        ElementSchema c = createElementSchema(TestElements.C);

        public ExclusionSchema() {
            c.setContentModel(bounded(choice().add(a).add(b)));
            b.setContentModel(bounded(choice().add(a).add(b).add(PCDATA)));
            CompositeModel a_content = choice().add(b).add(PCDATA);
            a_content.exclude(choice().add(a));
            a.setContentModel(bounded(a_content));
        }
    }

    public void testExclusionOk() throws Exception {
        checkValidationFromFile("xml/exclusion-ok.xml");
    }

    public void testExclusionError() throws Exception {
        checkValidationFailsFromFile("xml/exclusion-error.xml", ValidationMessages.EXCLUDED_CONTENT, new Object[] { TestElements.A, TestElements.A });
    }
}
