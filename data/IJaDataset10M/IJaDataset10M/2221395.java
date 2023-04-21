package tudresden.ocl20.pivot.tools.template.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tudresden.ocl20.pivot.tools.template.test.tests.TestStringTemplateEngine;
import tudresden.ocl20.pivot.tools.template.test.tests.TestTemplateGroup;
import tudresden.ocl20.pivot.tools.template.test.tests.TestTemplateEngineRegistry;

/**
 * This TestSuite runs all Tests testing the packages
 * <code>tudresden.ocl20.pivot.tools.template</code> of the Dresden OCL2 Toolkit
 * 
 * @author Bjoern Freitag
 * 
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ TestTemplateEngineRegistry.class, TestStringTemplateEngine.class, TestTemplateGroup.class })
public class AllTestsTemplate {
}
