package plugin.lsttokens;

import java.net.URISyntaxException;
import org.junit.Before;
import org.junit.Test;
import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.enumeration.IntegerKey;
import pcgen.cdom.inst.PCClassLevel;
import pcgen.core.PCTemplate;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.testsupport.AbstractGlobalTokenTestCase;
import plugin.lsttokens.testsupport.CDOMTokenLoader;
import plugin.lsttokens.testsupport.ConsolidationRule;

public class AddTokenTest extends AbstractGlobalTokenTestCase {

    static CDOMPrimaryToken<CDOMObject> token = new AddLst();

    static CDOMTokenLoader<PCTemplate> loader = new CDOMTokenLoader<PCTemplate>(PCTemplate.class);

    @Override
    @Before
    public void setUp() throws PersistenceLayerException, URISyntaxException {
        super.setUp();
    }

    @Override
    public CDOMLoader<PCTemplate> getLoader() {
        return loader;
    }

    @Override
    public Class<PCTemplate> getCDOMClass() {
        return PCTemplate.class;
    }

    @Override
    public CDOMPrimaryToken<CDOMObject> getToken() {
        return token;
    }

    @Test
    public void testInvalidEmpty() throws PersistenceLayerException {
        assertFalse(parse(""));
        assertNoSideEffects();
    }

    @Test
    public void testInvalidClearLevel() throws PersistenceLayerException {
        assertFalse(parse(".CLEAR.LEVEL1"));
        assertNoSideEffects();
    }

    @Test
    public void testValidClear() throws PersistenceLayerException {
        assertTrue(parse(".CLEAR"));
    }

    @Test
    public void testInvalidLevelNonClearLevel() throws PersistenceLayerException {
        primaryProf = new PCClassLevel();
        primaryProf.put(IntegerKey.LEVEL, 1);
        secondaryProf = new PCClassLevel();
        secondaryProf.put(IntegerKey.LEVEL, 1);
        assertFalse(parse(".CLEAR"));
        assertNoSideEffects();
    }

    @Test
    public void testInvalidLevelClearWrongLevel() throws PersistenceLayerException {
        primaryProf = new PCClassLevel();
        primaryProf.put(IntegerKey.LEVEL, 1);
        secondaryProf = new PCClassLevel();
        secondaryProf.put(IntegerKey.LEVEL, 1);
        assertFalse(parse(".CLEAR.LEVEL2"));
        assertNoSideEffects();
    }

    @Test
    public void testInvalidLevelClearLevelNaN() throws PersistenceLayerException {
        primaryProf = new PCClassLevel();
        primaryProf.put(IntegerKey.LEVEL, 1);
        secondaryProf = new PCClassLevel();
        secondaryProf.put(IntegerKey.LEVEL, 1);
        assertFalse(parse(".CLEAR.LEVELx"));
        assertNoSideEffects();
    }

    @Test
    public void testValidClearLevel() throws PersistenceLayerException {
        primaryProf = new PCClassLevel();
        primaryProf.put(IntegerKey.LEVEL, 1);
        secondaryProf = new PCClassLevel();
        secondaryProf.put(IntegerKey.LEVEL, 1);
        assertTrue(parse(".CLEAR.LEVEL1"));
    }

    @Override
    protected String getAlternateLegalValue() {
        return null;
    }

    @Override
    protected ConsolidationRule getConsolidationRule() {
        return null;
    }

    @Override
    protected String getLegalValue() {
        return null;
    }

    @Override
    public void testOverwrite() throws PersistenceLayerException {
    }
}
