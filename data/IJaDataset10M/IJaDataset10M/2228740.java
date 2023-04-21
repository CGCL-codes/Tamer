package com.genia.toolbox.web.portlet.description.impl;

import org.junit.Test;
import com.genia.toolbox.basics.exception.BundledException;
import com.genia.toolbox.basics.i18n.I18nMessage;
import com.genia.toolbox.spring.initializer.AbstractI18nKeyTest;
import com.genia.toolbox.web.portlet.description.VariableDescription;

public class TestBasicsDescriptions extends AbstractI18nKeyTest {

    private static final String[] testValues = { null, "", "hello", "HELLO", "12", "-12", "2000", "-2000", "1.2", "-1,2", "true", "false", "yes", "no", "Hello", "hello world", "généalogie" };

    public void basicTest(final VariableDescription pvd) throws BundledException {
        for (final String value : testValues) {
            I18nMessage message = pvd.accept(value);
            if (message != null) {
                testMessage(message);
            }
            message = pvd.getDescription();
            if (message != null) {
                testMessage(message);
            }
            if (pvd.getValues() != null) {
                for (final I18nMessage keyMessage : pvd.getValues().keySet()) {
                    testMessage(keyMessage);
                }
            }
        }
    }

    @Test
    public void testLongDescription() throws BundledException {
        final LongVariableDescription lvd = new LongVariableDescription(-20, 20);
        basicTest(lvd);
    }

    @Test
    public void testBooleanDescription() throws BundledException {
        final BooleanVariableDescription bvd = new BooleanVariableDescription();
        basicTest(bvd);
    }
}
