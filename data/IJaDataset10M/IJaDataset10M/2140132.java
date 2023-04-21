package org.jvprocess.process.test;

import java.util.HashMap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvprocess.process.OrchestratedProcess;
import org.jvprocess.process.ProcessFactRegistry;
import org.jvprocess.process.ProcessFactRegistryUtil;
import org.jvprocess.shared.test.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "sp-config.xml" })
public class ProcessTest {

    @Autowired
    private OrchestratedProcess process;

    /**
	 * Although there are no assertions, Spring Test autowiring makes life simpler
	 * 
	 * This example demonstrates invocation of a simple process which consists of only two activities 
	 * with a very simple execution rules:
	 * <sp:activity id="fooActivityFilter" always-unless="A OR B" activity-ref="fooActivity"/>
	 * <sp:activity id="barActivityFilter" never-unless="B" activity-ref="barActivity" persistent="true"/>
	 * 
	 * It also demonstrates the statefull nature of the process where re-invoking the process
	 * will not duplicate invocation of the activities unless allowed by the rules.
	 */
    @Test
    public void testProcess() {
        Payload payload = new Payload();
        Object result = process.execute(payload);
        process.execute(payload);
        ProcessFactRegistry factRegistry = payload.getFactRegistry();
        factRegistry.registerFact("B");
        process.execute(payload);
    }
}
