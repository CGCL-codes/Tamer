package org.agileinsider.concordion.specifications;

import org.agileinsider.concordion.ConcordionPlusAcceptanceTest;
import org.agileinsider.concordion.junit.ConcordionPlus;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
public class ConcordionPlusTest extends ConcordionPlusAcceptanceTest {

    @RunWith(ConcordionPlus.class)
    public static class TestAnnotatedWithConcordionPlus {

        public String getText() {
            return "unexpected text";
        }
    }

    @Override
    public Object getFixture() {
        return new TestAnnotatedWithConcordionPlus();
    }
}
