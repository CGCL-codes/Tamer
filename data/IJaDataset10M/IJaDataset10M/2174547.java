package com.idna.gav.service.volt;

import org.junit.Ignore;
import com.idna.gav.exceptions.VoltMissingORInvalidInputException;
import com.idna.gav.rules.international.volt.impl.GermanyRule;

@Ignore
public class GermanyRuleTest extends AbstractBaseCountryRuleTest {

    private GermanyRule DEU;

    protected void onSetUp() {
        setUpDoc();
        changeNodeValue("DEU", countryCodeElementName, doc);
        changeNodeValue("Erika", forenameElementName, doc);
        changeNodeValue("AÜbraham", surnameElementName, doc);
        changeNodeValue("034", premiseElementName, doc);
        changeNodeValue("Etlaswinder Weg", streetElementName, doc);
        changeNodeValue("BÜtjadingen", localityElementName, doc);
        changeNodeValue("15263", postcodeElementName, doc);
    }

    public void testApplyCountrySpecificRulesForStreetTypeIsRemovedIsSuccessful() throws VoltMissingORInvalidInputException {
        DEU.applyRules(doc);
        findStreet(doc);
        assertEquals("Etlaswinder".toUpperCase(), getStreet());
    }

    public void testApplyCountrySpecificRulesForLocalityAddPeriodIsSuccessful() throws VoltMissingORInvalidInputException {
        DEU.applyRules(doc);
        findLocality(doc);
        assertEquals("", getLocality());
    }

    public void testApplyCountrySpecificRulesForSpecialCharactersAreReplacedIsSuccessful() throws VoltMissingORInvalidInputException {
        DEU.applyRules(doc);
        findSurname(doc);
        assertEquals("AUEbraham", getSurname());
    }

    public void testApplyCountrySpecificRulesForPostcodeIsRemovedIsSuccessful() throws VoltMissingORInvalidInputException {
        DEU.applyRules(doc);
        findPostcode(doc);
        assertEquals("", getPostcode());
    }

    public GermanyRule getDEU() {
        return DEU;
    }

    public void setDEU(GermanyRule deu) {
        DEU = deu;
    }
}
