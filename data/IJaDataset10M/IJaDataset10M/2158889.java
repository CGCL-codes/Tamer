package eu.vph.predict.vre.in_silico;

import javax.annotation.Resource;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Qualifier;
import eu.vph.predict.vre.base.entity.lookup.LookupType;
import eu.vph.predict.vre.base.service.LookupService;
import eu.vph.predict.vre.in_silico.entity.application.AbstractSimulationEnvironment;
import eu.vph.predict.vre.in_silico.entity.application.interfacing.ApplicationInterface;
import eu.vph.predict.vre.in_silico.service.ApplicationService;
import eu.vph.predict.vre.tool.database.DatabaseEntityCreator;

/**
 *
 *
 * @author Geoff Williams
 */
public class AbstractInitialisedSystemEntitiesTests extends AbstractAuthenticatedUserJUnit4SpringContextTests {

    @Resource
    @Qualifier(ApplicationService.COMPONENT_APPLICATION_SERVICE)
    protected ApplicationService applicationService;

    @Resource
    @Qualifier(LookupService.COMPONENT_LOOKUP_SERVICE)
    protected LookupService lookupService;

    @BeforeClass
    public static void oneTimeSetup() {
    }

    @Before
    public void setup() {
        super.setup();
        try {
            for (final LookupType lookupType : DatabaseEntityCreator.createLookups()) {
                lookupService.save(lookupType);
            }
            for (final ApplicationInterface applicationInterface : DatabaseEntityCreator.createApplicationInterfaces(lookupService)) {
                applicationService.saveApplicationInterface(applicationInterface);
            }
            for (final AbstractSimulationEnvironment simulationEnvironment : DatabaseEntityCreator.createSimulationEnvironments(applicationService, lookupService)) {
                applicationService.saveApplication(simulationEnvironment);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception saving LookupTypes, LookupValues, Applications, ApplicationInterfaces [" + e.getMessage() + "]");
        }
    }

    @After
    public void tearDown() {
        super.tearDown();
    }
}
