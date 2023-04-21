package com.ibm.icu.dev.test.lang;

import com.ibm.icu.dev.test.TestFmwk.TestGroup;

/**
 * Top level test used to run character property tests.
 */
public class TestAll extends TestGroup {

    public static void main(String[] args) throws Exception {
        new TestAll().run(args);
    }

    public TestAll() {
        super(new String[] { "TestCharacter", "TestUScript", "TestUScriptRun" }, "Character and Script Tests");
    }

    public static final String CLASS_TARGET_NAME = "Property";
}
