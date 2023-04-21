package org.egdp.jclpvm.core.test.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.*;
import org.testng.annotations.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.egdp.jclpvm.core.context.CLContext;

@ContextConfiguration(locations = { "/sdk-config-opencl.xml", "/*-opencl.xml" })
public class InitializeSimpleCLContext extends AbstractTestNGSpringContextTests {

    private static Log logger = LogFactory.getLog(InitializeSimpleCLContext.class);

    @Autowired
    private CLContext clContext;

    public void setClContext(CLContext clContext) {
        this.clContext = clContext;
    }

    @Test
    public void testPrintOpenCLContext() {
        clContext.refresh();
        logger.info(clContext.getSdkContext().toString());
        logger.info("SDK Native Library Path: " + clContext.getOpenclLibPath().toString());
        logger.info(clContext.getPreferredUnitType().toString());
    }
}
