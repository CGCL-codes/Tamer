package net.sf.esims.model.service;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class EsimsBaseTestService extends AbstractDependencyInjectionSpringContextTests {

    public String[] getConfigLocations() {
        String classpath = "classpath*:";
        return new String[] { classpath + "actionContext.xml", classpath + "baseSetupContext.xml", classpath + "daoContext.xml", classpath + "serviceContext.xml", classpath + "testContext.xml" };
    }
}
