package skycastle.sculptor.branch;

import junit.framework.TestCase;
import skycastle.function.functions.SolidFunction;
import skycastle.sculptor.branch.structure.PlantStructureImpl;
import skycastle.function.InterpolatedFunction1D;

/**
 * Test class for BranchSculptorImpl.
 *
 * @author Hans H�ggstr�m
 */
@SuppressWarnings({ "ProhibitedExceptionDeclared", "MagicNumber", "InstanceVariableOfConcreteClass" })
public class BranchSculptorImplTest extends TestCase {

    private BranchImpl myBranchSculptorImpl = null;

    /**
     * Constructs an instance of ClassTest using default parameters.
     */
    public BranchSculptorImplTest(String name) {
        super(name);
    }

    public void testCreateBranchSkeleton() throws Exception {
        final int numberOfSegments = 8;
        myBranchSculptorImpl.setNumberOfSegments(numberOfSegments);
        myBranchSculptorImpl.setBranchForkProfile(new SolidFunction(1));
        final PlantStructureImpl plantStructure = new PlantStructureImpl();
        myBranchSculptorImpl.buildBranchSkeleton(plantStructure);
        assertEquals("Should have correct nr of nodes", numberOfSegments, plantStructure.getBranchSystem().getNodes().size());
        assertEquals("Should have correct nr of edges", numberOfSegments - 1, plantStructure.getBranchSystem().getEdges().size());
    }

    public void testCreateBranchSkeletonWithForks() throws Exception {
        final int numberOfSegments = 8;
        myBranchSculptorImpl.setNumberOfSegments(numberOfSegments);
        final int numberOfForks = 3;
        myBranchSculptorImpl.setBranchForkProfile(new SolidFunction(numberOfForks));
        final PlantStructureImpl plantStructure = new PlantStructureImpl();
        myBranchSculptorImpl.buildBranchSkeleton(plantStructure);
        assertEquals("Should have correct nr of nodes", 1 + (numberOfSegments - 1) * numberOfForks, plantStructure.getBranchSystem().getNodes().size());
        assertEquals("Should have correct nr of edges", (numberOfSegments - 1) * numberOfForks, plantStructure.getBranchSystem().getEdges().size());
    }

    public void testCreateBranchSkeletonWithVariableNumberOfForks() throws Exception {
        assertVariableNumberOfForksWorks(5, 1);
    }

    private void assertVariableNumberOfForksWorks(final int lowerHalfForkNum, final int upperHalfForkNum) {
        final InterpolatedFunction1D branchForkProfile = new InterpolatedFunction1D();
        branchForkProfile.addGuidePoint(-0.001f, lowerHalfForkNum);
        branchForkProfile.addGuidePoint(0, lowerHalfForkNum);
        branchForkProfile.addGuidePoint(0.4998f, lowerHalfForkNum);
        branchForkProfile.addGuidePoint(0.4999f, upperHalfForkNum);
        branchForkProfile.addGuidePoint(1.0f, upperHalfForkNum);
        final int numberOfSegments = 6;
        myBranchSculptorImpl.setNumberOfSegments(numberOfSegments);
        myBranchSculptorImpl.setBranchForkProfile(branchForkProfile);
        final PlantStructureImpl plantStructure = new PlantStructureImpl();
        myBranchSculptorImpl.buildBranchSkeleton(plantStructure);
        final int lowerHalfSegments = numberOfSegments / 2;
        final int upperHalfSegments = numberOfSegments / 2;
        final int expected = lowerHalfSegments * lowerHalfForkNum + upperHalfSegments * upperHalfForkNum - (lowerHalfForkNum - 1);
        assertEquals("Should have correct nr of nodes", expected, plantStructure.getBranchSystem().getNodes().size());
        assertEquals("Should have correct nr of edges", expected - 1, plantStructure.getBranchSystem().getEdges().size());
    }

    /**
     * Sets up the fixture, for example, open a network connection.
     * This method is called before a test is executed.
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myBranchSculptorImpl = new BranchImpl();
    }

    /**
     * Tears down the fixture, for example, close a network connection.
     * This method is called after a test is executed.
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
