package plugin.lsttokens.pcclass;

import java.net.URISyntaxException;
import org.junit.Before;
import org.junit.Test;
import pcgen.cdom.enumeration.CDOMAbilityCategory;
import pcgen.cdom.inst.CDOMAbility;
import pcgen.cdom.inst.CDOMPCClass;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.CDOMTokenLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.testsupport.AbstractListTokenTestCase;
import plugin.lsttokens.testsupport.TokenRegistration;
import plugin.pretokens.parser.PreClassParser;
import plugin.pretokens.parser.PreRaceParser;
import plugin.pretokens.writer.PreClassWriter;
import plugin.pretokens.writer.PreRaceWriter;

public class VFeatTokenTest extends AbstractListTokenTestCase<CDOMPCClass, CDOMAbility> {

    static VfeatToken token = new VfeatToken();

    static CDOMTokenLoader<CDOMPCClass> loader = new CDOMTokenLoader<CDOMPCClass>(CDOMPCClass.class);

    PreClassParser preclass = new PreClassParser();

    PreClassWriter preclasswriter = new PreClassWriter();

    PreRaceParser prerace = new PreRaceParser();

    PreRaceWriter preracewriter = new PreRaceWriter();

    @Override
    @Before
    public void setUp() throws PersistenceLayerException, URISyntaxException {
        super.setUp();
        TokenRegistration.register(preclass);
        TokenRegistration.register(preclasswriter);
        TokenRegistration.register(prerace);
        TokenRegistration.register(preracewriter);
    }

    @Override
    public char getJoinCharacter() {
        return '|';
    }

    @Override
    public Class<CDOMAbility> getTargetClass() {
        return CDOMAbility.class;
    }

    @Override
    public boolean isTypeLegal() {
        return false;
    }

    @Override
    public boolean isAllLegal() {
        return false;
    }

    @Override
    public boolean isClearDotLegal() {
        return false;
    }

    @Override
    public boolean isClearLegal() {
        return false;
    }

    @Override
    public Class<CDOMPCClass> getCDOMClass() {
        return CDOMPCClass.class;
    }

    @Override
    public CDOMLoader<CDOMPCClass> getLoader() {
        return loader;
    }

    @Override
    public CDOMPrimaryToken<CDOMPCClass> getToken() {
        return token;
    }

    @Override
    protected void construct(LoadContext loadContext, String one) {
        CDOMAbility obj = loadContext.ref.constructCDOMObject(CDOMAbility.class, one);
        loadContext.ref.reassociateCategory(CDOMAbilityCategory.FEAT, obj);
    }

    @Test
    public void testInvalidInputEmpty() {
        assertFalse(token.parse(primaryContext, primaryProf, ""));
        assertNoSideEffects();
    }

    @Test
    public void testInvalidInputOnlyPre() {
        construct(primaryContext, "TestWP1");
        try {
            assertFalse(token.parse(primaryContext, primaryProf, "PRECLASS:1,Fighter=1"));
        } catch (IllegalArgumentException e) {
        }
        assertNoSideEffects();
    }

    @Test
    public void testInvalidInputEmbeddedPre() {
        construct(primaryContext, "TestWP1");
        assertFalse(token.parse(primaryContext, primaryProf, "TestWP1|PRECLASS:1,Fighter=1|TestWP2"));
        assertNoSideEffects();
    }

    @Test
    public void testInvalidInputDoublePipePre() {
        construct(primaryContext, "TestWP1");
        assertFalse(token.parse(primaryContext, primaryProf, "TestWP1||PRECLASS:1,Fighter=1"));
        assertNoSideEffects();
    }

    @Test
    public void testInvalidInputPostPrePipe() {
        construct(primaryContext, "TestWP1");
        assertFalse(token.parse(primaryContext, primaryProf, "TestWP1|PRECLASS:1,Fighter=1|"));
        assertNoSideEffects();
    }

    @Test
    public void testRoundRobinPre() throws PersistenceLayerException {
        construct(primaryContext, "TestWP1");
        construct(secondaryContext, "TestWP1");
        runRoundRobin("TestWP1|PRECLASS:1,Fighter=1");
    }

    @Test
    public void testRoundRobinTwoPre() throws PersistenceLayerException {
        construct(primaryContext, "TestWP1");
        construct(secondaryContext, "TestWP1");
        runRoundRobin("TestWP1|!PRERACE:1,Human|PRECLASS:1,Fighter=1");
    }

    @Test
    public void testRoundRobinNotPre() throws PersistenceLayerException {
        construct(primaryContext, "TestWP1");
        construct(secondaryContext, "TestWP1");
        runRoundRobin("TestWP1|!PRECLASS:1,Fighter=1");
    }

    @Test
    public void testRoundRobinWWoPre() throws PersistenceLayerException {
        construct(primaryContext, "TestWP1");
        construct(primaryContext, "TestWP2");
        construct(secondaryContext, "TestWP1");
        construct(secondaryContext, "TestWP2");
        runRoundRobin("TestWP1|PRECLASS:1,Fighter=1", "TestWP2");
    }

    @Test
    public void testRoundRobinDupe() throws PersistenceLayerException {
        construct(primaryContext, "TestWP1");
        construct(secondaryContext, "TestWP1");
        runRoundRobin("TestWP1|TestWP1");
    }

    @Test
    public void testRoundRobinDupeOnePrereq() throws PersistenceLayerException {
        construct(primaryContext, "TestWP1");
        construct(secondaryContext, "TestWP1");
        runRoundRobin("TestWP1|TestWP1|PRERACE:1,Human");
    }

    @Test
    public void testRoundRobinDupeDiffPrereqs() throws PersistenceLayerException {
        System.err.println("=");
        construct(primaryContext, "TestWP1");
        construct(secondaryContext, "TestWP1");
        runRoundRobin("TestWP1", "TestWP1|PRERACE:1,Human");
    }

    @Test
    public void testRoundRobinDupeTwoDiffPrereqs() throws PersistenceLayerException {
        construct(primaryContext, "TestWP1");
        construct(secondaryContext, "TestWP1");
        construct(primaryContext, "TestWP2");
        construct(secondaryContext, "TestWP2");
        runRoundRobin("TestWP1|TestWP1|PRERACE:1,Human", "TestWP2|TestWP2|PRERACE:1,Elf");
    }
}
