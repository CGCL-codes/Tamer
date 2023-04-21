package org.trebor.util;

import org.junit.Test;

public class BoilerplateTest {

    @Test()
    public void parameterTest() {
        Boilerplate.main(new String[] { "fixtures/range.java", "fixtures/boilerplate-license.txt" });
        Boilerplate.main(new String[] { "fixtures/range-force.java", "fixtures/boilerplate-license.txt" });
    }
}
