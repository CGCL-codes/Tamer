package plugin.lsttokens.editcontext.pcclass;

import java.net.URISyntaxException;
import org.junit.Test;
import pcgen.core.PCClass;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.editcontext.testsupport.AbstractIntegrationTestCase;
import plugin.lsttokens.editcontext.testsupport.TestContext;
import plugin.lsttokens.pcclass.MemorizeToken;
import plugin.lsttokens.testsupport.CDOMTokenLoader;

public class MemorizeIntegrationTest extends AbstractIntegrationTestCase<PCClass> {

    static MemorizeToken token = new MemorizeToken();

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

    @Test
    public void testRoundRobinSimple() throws PersistenceLayerException {
        verifyCleanStart();
        TestContext tc = new TestContext();
        commit(testCampaign, tc, "NO");
        commit(modCampaign, tc, "YES");
        completeRoundRobin(tc);
    }

    @Test
    public void testRoundRobinRemove() throws PersistenceLayerException {
        verifyCleanStart();
        TestContext tc = new TestContext();
        commit(testCampaign, tc, "YES");
        commit(modCampaign, tc, "NO");
        completeRoundRobin(tc);
    }

    @Test
    public void testRoundRobinNoSet() throws PersistenceLayerException {
        verifyCleanStart();
        TestContext tc = new TestContext();
        emptyCommit(testCampaign, tc);
        commit(modCampaign, tc, "NO");
        completeRoundRobin(tc);
    }

    @Test
    public void testRoundRobinNoReset() throws PersistenceLayerException {
        verifyCleanStart();
        TestContext tc = new TestContext();
        commit(testCampaign, tc, "NO");
        emptyCommit(modCampaign, tc);
        completeRoundRobin(tc);
    }

    @Test
    public void testRoundRobinYesSet() throws PersistenceLayerException {
        verifyCleanStart();
        TestContext tc = new TestContext();
        emptyCommit(testCampaign, tc);
        commit(modCampaign, tc, "YES");
        completeRoundRobin(tc);
    }

    @Test
    public void testRoundRobinYesReset() throws PersistenceLayerException {
        verifyCleanStart();
        TestContext tc = new TestContext();
        commit(testCampaign, tc, "YES");
        emptyCommit(modCampaign, tc);
        completeRoundRobin(tc);
    }
}
