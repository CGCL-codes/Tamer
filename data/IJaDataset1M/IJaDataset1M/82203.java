package org.jcvi.autoTasker.feature;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestDefaultAssemblyFeature.class, TestDefaultContigFeature.class, TestDefaultScaffoldFeature.class })
public class AllFeatureUnitTests {
}
