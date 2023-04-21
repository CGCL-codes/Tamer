package ca.ucalgary.cpsc.ebe.fitClipse.tests.junit;

import ca.ucalgary.cpsc.ebe.fitClipse.runner.FitManager;
import junit.framework.TestCase;

public class FitManagerTest extends TestCase {

    public void testCreateTestSource() {
        FitManager fit = FitManager.getFitManager();
        fit.addClassPath("c:\\Temp\\Temp\\test");
        fit.CreateTest(".TestPage");
    }
}
