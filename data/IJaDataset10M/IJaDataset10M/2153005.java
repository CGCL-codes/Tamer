package uk.org.ogsadai.resource.dataresource.dqp;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.UnmarshalException;
import junit.framework.TestCase;
import uk.org.ogsadai.dqp.common.CompilerConfiguration;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.execute.CoordinatorExtension;
import uk.org.ogsadai.dqp.execute.ExtensionProcessingException;
import uk.org.ogsadai.dqp.execute.partition.Partition;
import uk.org.ogsadai.dqp.execute.workflow.ActivityPipelineBuilder;
import uk.org.ogsadai.dqp.execute.workflow.ProductSelectBuilder;
import uk.org.ogsadai.dqp.execute.workflow.ThetaJoinBuilder;
import uk.org.ogsadai.dqp.lqp.OperatorID;
import uk.org.ogsadai.dqp.lqp.cardinality.SimpleCardinalityEstimator;
import uk.org.ogsadai.dqp.lqp.operators.extra.OuterUnionOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.QueryNormaliser;
import uk.org.ogsadai.dqp.lqp.optimiser.implosion.TableScanImplosionOptimiser;
import uk.org.ogsadai.dqp.lqp.optimiser.join.FilteredTableScanOptimiser;
import uk.org.ogsadai.dqp.lqp.optimiser.join.JoinAnnotation;
import uk.org.ogsadai.dqp.lqp.optimiser.join.JoinOrderingOptimiser;
import uk.org.ogsadai.dqp.lqp.optimiser.partitioner.PartitioningOptimiser;
import uk.org.ogsadai.dqp.lqp.optimiser.project.ProjectPullUpOptimiser;
import uk.org.ogsadai.dqp.lqp.optimiser.rename.RenamePullUpOptimiser;
import uk.org.ogsadai.dqp.lqp.optimiser.select.SelectPushDownOptimiser;
import uk.org.ogsadai.test.unit.UnitTestHelper;

/**
 * Tests for the XMLCompilerConfiguration class.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class XMLCompilerConfigurationTest extends TestCase {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2008";

    /**
     * Constructor.
     * 
     * @param name
     */
    public XMLCompilerConfigurationTest(String name) {
        super(name);
    }

    /**
     * Test reading configuration with all tags used.
     * 
     * @throws Exception
     */
    public void testSimple() throws Exception {
        File xml = UnitTestHelper.getFileFromClassName(getClass(), "DQPCompilerConfig.xml");
        XMLCompilerConfiguration conf = new XMLCompilerConfiguration(xml.getAbsolutePath());
        Map<String, ActivityPipelineBuilder> map = conf.getBuilders(OperatorID.INNER_THETA_JOIN);
        assertTrue(map.get(CompilerConfiguration.DEFAULT_BUILDER) instanceof ProductSelectBuilder);
        assertTrue(map.get("PRIMARY_EXPRESSION") instanceof ThetaJoinBuilder);
        assertTrue(conf.getCardinalityEstimator() instanceof SimpleCardinalityEstimator);
        int types = 0;
        if (conf.getCoordinatorExtensions().get(0).getType() == CoordinatorExtension.Type.POST) types--; else types++;
        if (conf.getCoordinatorExtensions().get(1).getType() == CoordinatorExtension.Type.POST) types--; else types++;
        assertEquals("Expected one pre and one post extension type", 0, types);
        assertEquals(OuterUnionOperator.class.getCanonicalName(), conf.getFunctionOperatorClass("outerUnion").getCanonicalName());
        Class optimiserClasses[] = { QueryNormaliser.class, SelectPushDownOptimiser.class, RenamePullUpOptimiser.class, ProjectPullUpOptimiser.class, JoinOrderingOptimiser.class, JoinAnnotation.class, PartitioningOptimiser.class, TableScanImplosionOptimiser.class, FilteredTableScanOptimiser.class };
        List<uk.org.ogsadai.dqp.lqp.optimiser.Optimiser> optimisers = conf.getOptimisationChain();
        for (int i = 0; i < optimisers.size(); i++) {
            uk.org.ogsadai.dqp.lqp.optimiser.Optimiser o = optimisers.get(i);
            assertEquals(optimiserClasses[i], o.getClass());
        }
    }

    /**
     * Test minimal configuration.
     * 
     * @throws Exception
     */
    public void testMinimal() throws Exception {
        File xml = UnitTestHelper.getFileFromClassName(getClass(), "DQPCompilerConfigMinimal.xml");
        XMLCompilerConfiguration conf = new XMLCompilerConfiguration(xml.getAbsolutePath());
    }

    /**
     * Test validation against the schema.
     * 
     * @throws Exception
     */
    public void testValidation() throws Exception {
        try {
            File xml = UnitTestHelper.getFileFromClassName(getClass(), "DQPCompilerConfigInvalid.xml");
            XMLCompilerConfiguration conf = new XMLCompilerConfiguration(xml.getAbsolutePath());
            fail("Expected UnmarshalException");
        } catch (CompilerConfigurationException e) {
            assertTrue(e.getCause() instanceof UnmarshalException);
        }
    }

    /**
     * Coordinator extension stub for testing. POST type.
     * 
     * @author The OGSA-DAI Project Team.
     */
    public static class Post implements CoordinatorExtension {

        /** Copyright notice. */
        private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2008";

        public Type getType() {
            return Type.POST;
        }

        public void process(Set<Partition> partitions, RequestDetails requestDetails) throws ExtensionProcessingException {
        }
    }

    /**
     * Coordinator extension stub for testing. PRE type.
     * 
     * @author The OGSA-DAI Project Team.
     */
    public static class Pre implements CoordinatorExtension {

        /** Copyright notice. */
        private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2008";

        public Type getType() {
            return Type.PRE;
        }

        public void process(Set<Partition> partitions, RequestDetails requestDetails) throws ExtensionProcessingException {
        }
    }
}
