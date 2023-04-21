package org.gienah.testing.test.harness.integration;

import org.gienah.testing.test.harness.integration.junit.TestSpringRunner;
import org.gienah.testing.test.harness.integration.junit.TestStaticDependencies;
import org.gienah.testing.test.harness.integration.xml.TestTestBeanDefinitionParser;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Santiago L. Valdarrama
 * @since E0.3
 */
@RunWith(value = Suite.class)
@SuiteClasses(value = { TestSpringRunner.class, TestTestBeanDefinitionParser.class, TestStaticDependencies.class })
public class TestIntegrationHarness {
}
