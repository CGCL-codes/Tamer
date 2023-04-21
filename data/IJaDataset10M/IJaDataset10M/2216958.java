package uk.ac.ebi.intact.plugins.ebeye;

import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import java.io.File;
import uk.ac.ebi.intact.context.IntactSession;
import uk.ac.ebi.intact.context.IntactContext;
import uk.ac.ebi.intact.context.impl.StandaloneSession;
import uk.ac.ebi.intact.config.impl.CustomCoreDataConfig;
import uk.ac.ebi.intact.core.unit.IntactUnit;
import uk.ac.ebi.intact.core.unit.IntactMockBuilder;
import uk.ac.ebi.intact.core.persister.PersisterHelper;

/**
 * ExportExternalServicesIndexesMojo Tester.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @since <pre>11/28/2006</pre>
 * @version 1.0
 */
public class ExportExternalServicesIndexesMojoTest extends AbstractMojoTestCase {

    public void testGetProject() throws Exception {
        File hibernateConfig = new File(ExportExternalServicesIndexesMojoTest.class.getResource("/test-hibernate.cfg.xml").getFile());
        IntactContext.initStandaloneContext(hibernateConfig);
        IntactUnit iu = new IntactUnit();
        iu.createSchema(true);
        IntactMockBuilder mockBuilder = new IntactMockBuilder(IntactContext.getCurrentInstance().getInstitution());
        PersisterHelper.saveOrUpdate(mockBuilder.createExperimentRandom(2));
        IntactContext.getCurrentInstance().getConfig().setReadOnlyApp(true);
        File pluginXmlFile = new File(getBasedir(), "src/test/plugin-configs/ebi-search-engine-config.xml");
        ExportExternalServicesIndexesMojo mojo = (ExportExternalServicesIndexesMojo) lookupMojo("generate-ebi-indexes", pluginXmlFile);
        mojo.setLog(new SystemStreamLog());
        mojo.execute();
    }
}
