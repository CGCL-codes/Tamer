package plugin.lsttokens.editcontext.pcclass;

import java.net.URISyntaxException;
import org.junit.Test;
import pcgen.core.Domain;
import pcgen.core.PCClass;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.editcontext.testsupport.AbstractListIntegrationTestCase;
import plugin.lsttokens.editcontext.testsupport.TestContext;
import plugin.lsttokens.pcclass.AdddomainsToken;
import plugin.lsttokens.testsupport.CDOMTokenLoader;

public class AddDomainsIntegrationTest extends AbstractListIntegrationTestCase<PCClass, Domain> {

    static AdddomainsToken token = new AdddomainsToken();

    static CDOMTokenLoader<PCClass> loader = new CDOMTokenLoader<PCClass>(PCClass.class);

    @Override
    public void setUp() throws PersistenceLayerException, URISyntaxException {
        super.setUp();
        prefix = "CLASS:";
    }

    @Override
    public Class<PCClass> getCDOMClass() {
        return PCClass.class;
    }

    @Override
    public CDOMLoader<PCClass> getLoader() {
        return loader;
    }

    @Override
    public CDOMPrimaryToken<PCClass> getToken() {
        return token;
    }

    @Override
    public Class<Domain> getTargetClass() {
        return Domain.class;
    }

    @Override
    public boolean isTypeLegal() {
        return false;
    }

    @Override
    public char getJoinCharacter() {
        return '|';
    }

    @Test
    public void dummyTest() {
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
    public boolean isPrereqLegal() {
        return false;
    }

    @Override
    public boolean isAllLegal() {
        return false;
    }

    @Test
    public void testRoundRobinAddBracketPrereq() throws PersistenceLayerException {
        construct(primaryContext, "TestWP1");
        construct(secondaryContext, "TestWP1");
        verifyCleanStart();
        TestContext tc = new TestContext();
        commit(testCampaign, tc, "TestWP1");
        commit(modCampaign, tc, "TestWP1[PRERACE:1,Human]");
        completeRoundRobin(tc);
    }

    @Test
    public void testRoundRobinRemoveBracketPrereq() throws PersistenceLayerException {
        construct(primaryContext, "TestWP1");
        construct(secondaryContext, "TestWP1");
        verifyCleanStart();
        TestContext tc = new TestContext();
        commit(testCampaign, tc, "TestWP1[PRERACE:1,Human]");
        commit(modCampaign, tc, "TestWP1");
        completeRoundRobin(tc);
    }
}
