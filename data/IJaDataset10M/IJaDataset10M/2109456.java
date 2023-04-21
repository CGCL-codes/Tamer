package net.sf.jsfcomp.facelets.deploy;

import net.sf.jsfcomp.facelets.deploy.annotations.FaceletElFunction;
import net.sf.jsfcomp.facelets.deploy.annotations.FaceletFunctionHolder;
import org.testng.Reporter;

/**
 *
 * @author Andrew Robinson (andrew)
 */
@FaceletFunctionHolder(defaultNamespace = "http://jsf-comp.sf.net")
public class TestExplicitFunctions {

    public static int invocationCount = 0;

    public static final String RESULT = "run";

    public void ignoreMe() {
    }

    public static String explicitFunctionHidden() {
        return RESULT;
    }

    @FaceletElFunction
    public static String explicitFunctionVisible() {
        Reporter.log("explicitFunctionVisible invoked");
        invocationCount++;
        return RESULT;
    }

    @FaceletElFunction(name = "testExplicitOverload")
    public static String explicitFunctionVisible(String s) {
        Reporter.log("testExplicitOverload invoked");
        invocationCount++;
        return RESULT;
    }
}
