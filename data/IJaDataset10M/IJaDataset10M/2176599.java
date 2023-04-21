package consciouscode.seedling.config.properties;

import static consciouscode.seedling.NodeReference.forChild;
import consciouscode.seedling.SimpleFactory;
import consciouscode.seedling.tree.StandardBranch;

/**
 *
 */
public class AbsoluteFactoryTest extends DynamicFactoryTestCase {

    @SuppressWarnings("hiding")
    public static final String AUTO_SUITES = "common,factories";

    private String myFactoryPath;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        StandardBranch branch = getScratchBranch();
        branch.installChild("Factory", new SimpleFactory());
        myFactoryPath = forChild(branch, "Factory").toLocalPath().toString();
    }

    @Override
    protected String getFactoryPath() {
        return myFactoryPath;
    }
}
