package com.ibm.icu.dev.test.normalizer;

import com.ibm.icu.dev.test.TestFmwk.TestGroup;

/**
 * Top level test used to run normalizer tests as a batch.
 */
public class TestAll extends TestGroup {

    public static void main(String[] args) {
        new TestAll().run(args);
    }

    public TestAll() {
        super(new String[] { "BasicTest", "ConformanceTest", "TestDeprecatedNormalizerAPI", "TestCanonicalIterator", "NormalizationMonkeyTest" });
    }

    public static final String CLASS_TARGET_NAME = "Normalizer";
}
