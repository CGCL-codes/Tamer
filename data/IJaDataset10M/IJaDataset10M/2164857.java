package org.jdmp.weka;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ org.jdmp.weka.classifier.AllTests.class, org.jdmp.weka.clusterer.AllTests.class, org.jdmp.weka.wrappers.AllTests.class })
public class AllTests {
}
