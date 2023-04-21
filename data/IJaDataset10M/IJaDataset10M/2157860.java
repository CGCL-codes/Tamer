package ca.ucalgary.cpsc.ebe.fitClipse.tests.subLayers;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import ca.ucalgary.cpsc.ebe.fitClipse.FitClipseProject;
import ca.ucalgary.cpsc.ebe.fitClipse.core.factories.FrameworkFactory;
import ca.ucalgary.cpsc.ebe.fitClipse.core.factories.GreenPepperFactory;

@RunWith(value = Suite.class)
@SuiteClasses(value = { JunitTestSuite.class })
public class AllJUnitAsGreenPepper {

    private static FrameworkFactory factory = new GreenPepperFactory(new FitClipseProject("FlowerShop", "TestWorkspace/FlowerShop", "TestWorkspace/FlowerShop/.acceptanceTests/HtmlResults", "TestWorkspace/FlowerShop/.acceptanceTests/HtmlSource", "TestWorkspace/FlowerShop/AcceptanceTests", "TestWorkspace/FlowerShop/.acceptanceTests/MergedResults", 0));

    @AfterClass
    public static void after() throws Exception {
    }

    @BeforeClass
    public static void before() {
        System.out.println("Running Green Pepper JUnit Tests!!!");
        FrameworkFactory.setFactory(AllJUnitAsGreenPepper.factory);
    }
}
