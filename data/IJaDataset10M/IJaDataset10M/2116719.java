package plugin.lsttokens.editcontext;

import org.junit.Test;
import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.enumeration.IntegerKey;
import pcgen.cdom.inst.PCClassLevel;
import pcgen.core.Ability;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.AddLst;
import plugin.lsttokens.editcontext.testsupport.AbstractIntegrationTestCase;
import plugin.lsttokens.editcontext.testsupport.TestContext;
import plugin.lsttokens.testsupport.CDOMTokenLoader;

public class AddIntegrationTest extends AbstractIntegrationTestCase<CDOMObject> {

    static AddLst token = new AddLst();

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
        commit(testCampaign, tc, ".CLEAR");
        commit(modCampaign, tc, ".CLEAR");
        completeRoundRobin(tc);
    }

    @Test
    public void testRoundRobinNoSet() throws PersistenceLayerException {
        verifyCleanStart();
        TestContext tc = new TestContext();
        emptyCommit(testCampaign, tc);
        commit(modCampaign, tc, ".CLEAR");
        completeRoundRobin(tc);
    }

    @Test
    public void testRoundRobinNoReset() throws PersistenceLayerException {
        verifyCleanStart();
        TestContext tc = new TestContext();
        commit(testCampaign, tc, ".CLEAR");
        emptyCommit(modCampaign, tc);
        completeRoundRobin(tc);
    }

    @Test
    public void testRoundRobinLevelSimple() throws PersistenceLayerException {
        primaryProf = new PCClassLevel();
        primaryProf.put(IntegerKey.LEVEL, 1);
        secondaryProf = new PCClassLevel();
        secondaryProf.put(IntegerKey.LEVEL, 1);
        verifyCleanStart();
        TestContext tc = new TestContext();
        commit(testCampaign, tc, ".CLEAR.LEVEL1");
        commit(modCampaign, tc, ".CLEAR.LEVEL1");
        completeRoundRobin(tc);
    }

    @Test
    public void testRoundRobinLevelNoSet() throws PersistenceLayerException {
        primaryProf = new PCClassLevel();
        primaryProf.put(IntegerKey.LEVEL, 1);
        secondaryProf = new PCClassLevel();
        secondaryProf.put(IntegerKey.LEVEL, 1);
        verifyCleanStart();
        TestContext tc = new TestContext();
        emptyCommit(testCampaign, tc);
        commit(modCampaign, tc, ".CLEAR.LEVEL1");
        completeRoundRobin(tc);
    }

    @Test
    public void testRoundRobinLevelNoReset() throws PersistenceLayerException {
        primaryProf = new PCClassLevel();
        primaryProf.put(IntegerKey.LEVEL, 1);
        secondaryProf = new PCClassLevel();
        secondaryProf.put(IntegerKey.LEVEL, 1);
        verifyCleanStart();
        TestContext tc = new TestContext();
        commit(testCampaign, tc, ".CLEAR.LEVEL1");
        emptyCommit(modCampaign, tc);
        completeRoundRobin(tc);
    }
}
