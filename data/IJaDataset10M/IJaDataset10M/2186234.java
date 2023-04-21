package com.idna.gav.rules.international.impl;

public class GavResponseItalyRuleTest extends AbstractGavResponseCountriesRuleTest {

    public void testGavResponseItalyRuleIsSuccessful() throws Exception {
        GavResponseItalyRule rule = (GavResponseItalyRule) getApplicationContext().getBean("GAVIT");
        boolean isValid = rule.isValidCountryPostCode("00144");
        assertTrue(isValid);
    }
}
