package mutant.test.descriptors;

import mutant.descriptors.AromaticAmineSubstituentsDescriptor;
import org.openscience.cdk.qsar.IMolecularDescriptor;

public class MRTest extends DescriptorsTest {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        addPropertiesToTest("mr2", "MR2");
        addPropertiesToTest("mr3", "MR3");
        addPropertiesToTest("mr6", "MR6");
    }

    @Override
    protected IMolecularDescriptor createDescriptorToTest() throws Exception {
        return new AromaticAmineSubstituentsDescriptor();
    }

    @Override
    public String getResultsFile() {
        return "aromatic_amines/ammTA100_results.csv";
    }

    @Override
    public String getSourceFile() {
        return "aromatic_amines/ammTA100.csv";
    }

    @Override
    public String getStructureID() {
        return "StructureID";
    }
}
