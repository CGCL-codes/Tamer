package plugin.lsttokens.editcontext;

import org.junit.Test;
import pcgen.cdom.base.CDOMObject;
import pcgen.core.Ability;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.UdamLst;
import plugin.lsttokens.editcontext.testsupport.AbstractIntegrationTestCase;
import plugin.lsttokens.editcontext.testsupport.TestContext;
import plugin.lsttokens.testsupport.CDOMTokenLoader;

public class UDamIntegrationTest extends AbstractIntegrationTestCase<CDOMObject> {

    static UdamLst token = new UdamLst();

    static CDOMTokenLoader<CDOMObject> loader = new CDOMTokenLoader<CDOMObject>(CDOMObject.class);

    @Override
    public Class<Ability> getCDOMClass() {
        return Ability.class;
    }

    @Override
    public CDOMLoader<CDOMObject> getLoader() {
        return loader;
    }

    @Override
    public CDOMPrimaryToken<CDOMObject> getToken() {
        return token;
    }

    @Test
    public void testRoundRobinSimple() throws PersistenceLayerException {
        verifyCleanStart();
        TestContext tc = new TestContext();
        commit(testCampaign, tc, "1,2,3,4,5,6,7,8,9");
        commit(modCampaign, tc, "1,2,3,4*form,5*form,6,7*form,8,9");
        completeRoundRobin(tc);
    }

    @Test
    public void testRoundRobinNoSet() throws PersistenceLayerException {
        verifyCleanStart();
        TestContext tc = new TestContext();
        emptyCommit(testCampaign, tc);
        commit(modCampaign, tc, "1,2,3,4,5,6,7,8,9");
        completeRoundRobin(tc);
    }

    @Test
    public void testRoundRobinNoReset() throws PersistenceLayerException {
        verifyCleanStart();
        TestContext tc = new TestContext();
        commit(testCampaign, tc, "1,2,3,4,5,6,7,8,9");
        emptyCommit(modCampaign, tc);
        completeRoundRobin(tc);
    }

    @Test
    public void testRoundRobinClearBase() throws PersistenceLayerException {
        verifyCleanStart();
        TestContext tc = new TestContext();
        commit(testCampaign, tc, ".CLEAR");
        commit(modCampaign, tc, "1,2,3,4*form,5*form,6,7*form,8,9");
        completeRoundRobin(tc);
    }

    @Test
    public void testRoundRobinClearMod() throws PersistenceLayerException {
        verifyCleanStart();
        TestContext tc = new TestContext();
        commit(testCampaign, tc, "1,2,3,4,5,6,7,8,9");
        commit(modCampaign, tc, ".CLEAR");
        completeRoundRobin(tc);
    }

    @Test
    public void testRoundRobinClearBoth() throws PersistenceLayerException {
        verifyCleanStart();
        TestContext tc = new TestContext();
        commit(testCampaign, tc, ".CLEAR");
        commit(modCampaign, tc, ".CLEAR");
        completeRoundRobin(tc);
    }

    @Test
    public void testRoundRobinClearNoSet() throws PersistenceLayerException {
        verifyCleanStart();
        TestContext tc = new TestContext();
        emptyCommit(testCampaign, tc);
        commit(modCampaign, tc, ".CLEAR");
        completeRoundRobin(tc);
    }

    @Test
    public void testRoundRobinClearNoReset() throws PersistenceLayerException {
        verifyCleanStart();
        TestContext tc = new TestContext();
        commit(testCampaign, tc, ".CLEAR");
        emptyCommit(modCampaign, tc);
        completeRoundRobin(tc);
    }
}
