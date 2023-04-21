package plugin.lsttokens.template;

import org.junit.Test;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.enumeration.SubRegion;
import pcgen.core.PCTemplate;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.testsupport.AbstractTypeSafeTokenTestCase;
import plugin.lsttokens.testsupport.CDOMTokenLoader;

public class SubregionTokenTest extends AbstractTypeSafeTokenTestCase<PCTemplate> {

    static SubregionToken token = new SubregionToken();

    static CDOMTokenLoader<PCTemplate> loader = new CDOMTokenLoader<PCTemplate>(PCTemplate.class);

    @Override
    public Class<PCTemplate> getCDOMClass() {
        return PCTemplate.class;
    }

    @Override
    public CDOMLoader<PCTemplate> getLoader() {
        return loader;
    }

    @Override
    public CDOMPrimaryToken<PCTemplate> getToken() {
        return token;
    }

    @Override
    public Object getConstant(String string) {
        return SubRegion.getConstant(string);
    }

    @Override
    public ObjectKey<?> getObjectKey() {
        return ObjectKey.SUBREGION;
    }

    @Override
    protected boolean requiresPreconstruction() {
        return false;
    }

    @Test
    public void dummyTest() {
    }

    @Override
    public boolean isClearLegal() {
        return false;
    }

    @Test
    public void testRoundRobinYes() throws PersistenceLayerException {
        runRoundRobin("YES");
    }

    @Test
    public void testReplacementYes() throws PersistenceLayerException {
        String[] unparsed;
        if (requiresPreconstruction()) {
            getConstant("TestWP1");
        }
        if (isClearLegal()) {
            assertTrue(parse("YES"));
            unparsed = getToken().unparse(primaryContext, primaryProf);
            assertEquals(1, unparsed.length);
            assertEquals("Expected item to be equal", "TestWP2", unparsed[0]);
        }
        assertTrue(parse("TestWP1"));
        unparsed = getToken().unparse(primaryContext, primaryProf);
        assertEquals(1, unparsed.length);
        assertEquals("Expected item to be equal", "TestWP1", unparsed[0]);
        if (isClearLegal()) {
            assertTrue(parse("YES"));
            unparsed = getToken().unparse(primaryContext, primaryProf);
            assertEquals(1, unparsed.length);
            assertEquals("Expected item to be equal", "YES", unparsed[0]);
        }
    }

    @Test
    public void testOverwriteYes() throws PersistenceLayerException {
        parse("YES");
        validateUnparsed(primaryContext, primaryProf, "YES");
        parse("TestWP1");
        validateUnparsed(primaryContext, primaryProf, getConsolidationRule().getAnswer("TestWP1"));
    }

    @Test
    public void testOverwriteWithYes() throws PersistenceLayerException {
        parse("TestWP1");
        validateUnparsed(primaryContext, primaryProf, "TestWP1");
        parse("YES");
        validateUnparsed(primaryContext, primaryProf, getConsolidationRule().getAnswer("YES"));
    }
}
