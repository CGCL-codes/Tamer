package org.freelords.army;

import static org.junit.Assert.assertEquals;
import java.io.File;
import org.freelords.xmlmanager.XmlManager;
import org.freelords.xmlmanager.utils.XPathHelper;
import org.freelords.xmlmanager.utils.XmlTestHelper;
import org.junit.Before;
import org.junit.Test;

public class CalculatorTest {

    private int BASE_MAXIMUM_MOVE_POINTS;

    private int BASE_UPKEEP;

    private File modifierFile = new File(this.getClass().getResource("files/modifier.xml").getFile());

    private File statCalculatorFile = new File(this.getClass().getResource("files/statCalculator.xml").getFile());

    private StatCalculator statCalculator;

    private Modifier modifier1;

    private Modifier modifier2;

    @Before
    public void setUp() throws Exception {
        XmlManager xmlManager = XmlTestHelper.createXmlManagerFor(Modifier.class, StatCalculator.class);
        modifier1 = xmlManager.unmarshall(modifierFile);
        modifier2 = xmlManager.unmarshall(modifierFile);
        statCalculator = xmlManager.unmarshall(statCalculatorFile);
        XPathHelper xPathHelper = new XPathHelper(statCalculatorFile);
        BASE_MAXIMUM_MOVE_POINTS = xPathHelper.evaluateXPathAsInteger("//entry[@stat='MAXIMUM_MOVE_POINTS']/@value");
        BASE_UPKEEP = xPathHelper.evaluateXPathAsInteger("//entry[@stat='UPKEEP']/@value");
    }

    @Test
    public void testCalculatorReturnsRawStatsWithNoModifiers() {
        verifyRawStatsOnCalculator();
    }

    /**
	 * Test that you cannot add the same modifier instance twice.
	 */
    @Test(expected = IllegalArgumentException.class)
    public void testCalculatorThrowsExceptionOnAddingTheSameModifierTwice() {
        statCalculator.addModifier(modifier1);
        statCalculator.addModifier(modifier1);
    }

    /**
	 * Test that you can add the same modifier type twice.
	 */
    @Test
    public void testCalculatorCanGetSameModifierTypeTwice() throws Exception {
        statCalculator.addModifier(modifier1);
        statCalculator.addModifier(modifier2);
    }

    @Test
    public void testCalculatorWithModifiers() throws Exception {
        statCalculator.addModifier(modifier1);
        statCalculator.addModifier(modifier2);
        assertEquals("Calculated movement points do not match.", BASE_MAXIMUM_MOVE_POINTS + 2 * modifier1.getStatValue(UnitStat.MAXIMUM_MOVE_POINTS), statCalculator.calculate(UnitStat.MAXIMUM_MOVE_POINTS));
        assertEquals("Calculated upkeep does not match.", BASE_UPKEEP + 2 * modifier1.getStatValue(UnitStat.UPKEEP), statCalculator.calculate(UnitStat.UPKEEP));
    }

    @Test
    public void testCalculatorAddAndRemoveModifier() throws Exception {
        statCalculator.addModifier(modifier1);
        assertEquals("Calculated upkeep does not match.", BASE_UPKEEP + modifier1.getStatValue(UnitStat.UPKEEP), statCalculator.calculate(UnitStat.UPKEEP));
        statCalculator.removeModifier(modifier1);
        verifyRawStatsOnCalculator();
    }

    private void verifyRawStatsOnCalculator() {
        assertEquals("Calculated movement points do not match.", BASE_MAXIMUM_MOVE_POINTS, statCalculator.calculate(UnitStat.MAXIMUM_MOVE_POINTS));
        assertEquals("Calculated upkeep does not match.", BASE_UPKEEP, statCalculator.calculate(UnitStat.UPKEEP));
    }
}
