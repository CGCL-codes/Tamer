package de.cologneintelligence.fitgoodies.alias;

import java.text.ParseException;
import de.cologneintelligence.fitgoodies.FitGoodiesTestCase;
import de.cologneintelligence.fitgoodies.alias.AliasHelper;
import de.cologneintelligence.fitgoodies.alias.SetupFixture;
import fit.Parse;

/**
 * $Id: SetupFixtureTest.java 46 2011-09-04 14:59:16Z jochen_wierum $
 * @author jwierum
 */
public class SetupFixtureTest extends FitGoodiesTestCase {

    public final void testParsing() throws ParseException {
        Parse table = new Parse("<table><tr><td>ignore</td></tr>" + "<tr><td>asdf</td><td>java.lang.String</td></tr>" + "</table>");
        SetupFixture fixture = new SetupFixture();
        fixture.doTable(table);
        assertEquals(0, fixture.counts.exceptions);
        assertEquals("java.lang.String", AliasHelper.instance().getClazz("asdf"));
        table = new Parse("<table><tr><td>ignore</td></tr>" + "<tr><td>i</td><td>java.lang.Integer</td></tr>" + "</table>");
        fixture.doTable(table);
        assertEquals("java.lang.Integer", AliasHelper.instance().getClazz("i"));
    }

    public final void testError() throws ParseException {
        Parse table = new Parse("<table><tr><td>ignore</td></tr>" + "<tr><td>x</td></tr>" + "</table>");
        SetupFixture fixture = new SetupFixture();
        fixture.doTable(table);
        assertEquals(0, fixture.counts.exceptions);
        assertEquals(1, fixture.counts.ignores);
    }
}
