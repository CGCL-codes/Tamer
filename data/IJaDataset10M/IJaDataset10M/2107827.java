package org.xmlcml.cml.base;

import org.junit.Test;
import org.xmlcml.cml.base.CMLBuilder;

public class JCampBuildTest {

    @Test
    public void regressionTestJCampConverted() throws Exception {
        CMLBuilder builder = new CMLBuilder();
        builder.build(getClass().getClassLoader().getResourceAsStream("nmr/dw9960.cml.xml"));
        builder.build(getClass().getClassLoader().getResourceAsStream("nmr/ebprotfx.cml.xml"));
    }
}
