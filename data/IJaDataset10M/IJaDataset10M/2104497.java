package org.dataminx.dts.wn.util;

import static org.dataminx.dts.common.util.TestFileChooser.getTestFilePostfix;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.dataminx.dts.common.jms.JobQueueSender;
import org.dataminx.schemas.dts.x2009.x07.messages.SubmitJobRequestDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * Test that a DTS job is launched when a JMS message is posted on the DTS Job
 * Submission queue.
 *
 * @author Alex Arana
 * @author Gerson Galang
 */
@ContextConfiguration
public class ProcessDtsJobMessageIntegrationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    @Qualifier("jobQueueSender")
    private JobQueueSender mJmsQueueSender;

    @Test
    public void submitDtsJobAsDocument() throws Exception {
        final File f = new ClassPathResource("/org/dataminx/dts/wn/util/minx-dts" + getTestFilePostfix() + ".xml").getFile();
        final SubmitJobRequestDocument jobRequest = SubmitJobRequestDocument.Factory.parse(f);
        Assert.assertTrue(jobRequest.validate());
        Map<String, Object> jmsproperties = new HashMap<String, Object>();
        jmsproperties.put("ClientID", "DtsClient001");
        jmsproperties.put("DTSWorkerNodeID", "DtsWorkerNodemyhostname001");
        mJmsQueueSender.doSend(generateNewJobId(), jmsproperties, jobRequest);
    }

    private String generateNewJobId() {
        return "DTSJob_" + UUID.randomUUID();
    }
}
