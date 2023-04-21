package plugin.lsttokens.kit.prof;

import org.junit.Test;
import pcgen.core.WeaponProf;
import pcgen.core.kit.KitProf;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.CDOMSubLineLoader;
import pcgen.rules.persistence.token.CDOMSecondaryToken;
import plugin.lsttokens.testsupport.AbstractSubTokenTestCase;

public class ProfTokenTest extends AbstractSubTokenTestCase<KitProf> {

    static ProfToken token = new ProfToken();

    static CDOMSubLineLoader<KitProf> loader = new CDOMSubLineLoader<KitProf>("*KITTOKEN", "PROF", KitProf.class);

    @Override
    public Class<KitProf> getCDOMClass() {
        return KitProf.class;
    }

    @Override
    public CDOMSubLineLoader<KitProf> getLoader() {
        return loader;
    }

    @Override
    public CDOMSecondaryToken<KitProf> getToken() {
        return token;
    }

    private char getJoinCharacter() {
        return '|';
    }

    @Test
    public void testInvalidInputEmptyCount() throws PersistenceLayerException {
        assertTrue(parse("Fireball"));
        assertFalse(primaryContext.ref.validate(null));
    }

    @Test
    public void testRoundRobinSimple() throws PersistenceLayerException {
        primaryContext.ref.constructCDOMObject(WeaponProf.class, "Fireball");
        secondaryContext.ref.constructCDOMObject(WeaponProf.class, "Fireball");
        runRoundRobin("Fireball");
    }

    @Test
    public void testRoundRobinTwo() throws PersistenceLayerException {
        primaryContext.ref.constructCDOMObject(WeaponProf.class, "Fireball");
        secondaryContext.ref.constructCDOMObject(WeaponProf.class, "Fireball");
        primaryContext.ref.constructCDOMObject(WeaponProf.class, "English");
        secondaryContext.ref.constructCDOMObject(WeaponProf.class, "English");
        runRoundRobin("Fireball" + getJoinCharacter() + "English");
    }

    @Test
    public void testInvalidListEnd() throws PersistenceLayerException {
        assertFalse(parse("TestWP1" + getJoinCharacter()));
    }

    @Test
    public void testInvalidListStart() throws PersistenceLayerException {
        assertFalse(parse(getJoinCharacter() + "TestWP1"));
    }

    @Test
    public void testInvalidListDoubleJoin() throws PersistenceLayerException {
        assertFalse(parse("TestWP2" + getJoinCharacter() + getJoinCharacter() + "TestWP1"));
    }
}
