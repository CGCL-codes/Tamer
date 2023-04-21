package plugin.lsttokens.editcontext.template;

import org.junit.Test;
import pcgen.core.PCTemplate;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.editcontext.testsupport.AbstractIntegrationTestCase;
import plugin.lsttokens.editcontext.testsupport.TestContext;
import plugin.lsttokens.template.FaceToken;
import plugin.lsttokens.testsupport.CDOMTokenLoader;

public class FaceIntegrationTest extends AbstractIntegrationTestCase<PCTemplate> {

    static FaceToken token = new FaceToken();

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

    @Test
    public void testRoundRobinSimple() throws PersistenceLayerException {
        verifyCleanStart();
        TestContext tc = new TestContext();
        commit(testCampaign, tc, "10");
        commit(modCampaign, tc, "5");
        completeRoundRobin(tc);
    }

    @Test
    public void testRoundRobinComplex() throws PersistenceLayerException {
        verifyCleanStart();
        TestContext tc = new TestContext();
        commit(testCampaign, tc, "5");
        commit(modCampaign, tc, "5,10");
        completeRoundRobin(tc);
    }

    @Test
    public void testRoundRobinRemove() throws PersistenceLayerException {
        verifyCleanStart();
        TestContext tc = new TestContext();
        commit(testCampaign, tc, "5,20");
        commit(modCampaign, tc, "10");
        completeRoundRobin(tc);
    }

    @Test
    public void testRoundRobinNoSet() throws PersistenceLayerException {
        verifyCleanStart();
        TestContext tc = new TestContext();
        emptyCommit(testCampaign, tc);
        commit(modCampaign, tc, "5,10");
        completeRoundRobin(tc);
    }

    @Test
    public void testRoundRobinNoReset() throws PersistenceLayerException {
        verifyCleanStart();
        TestContext tc = new TestContext();
        commit(testCampaign, tc, "10,5");
        emptyCommit(modCampaign, tc);
        completeRoundRobin(tc);
    }
}
