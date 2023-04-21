package net.sf.otrcutmp4.controller.batch.audio;

import java.io.IOException;
import net.sf.otrcutmp4.controller.exception.OtrConfigurationException;
import net.sf.otrcutmp4.test.AbstractClientTest;
import net.sf.otrcutmp4.util.TestOtrConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestAc3ToAac extends AbstractClientTest {

    static final Logger logger = LoggerFactory.getLogger(TestAc3ToAac.class);

    private Ac3ToAac ac3ToAac;

    private String testFile;

    @Before
    public void init() throws IOException {
        TestOtrConfig tC = TestOtrConfig.factory();
        ac3ToAac = new Ac3ToAac(tC.getOtrConfig());
        testFile = "myTest";
    }

    @Test
    public void checkFail() throws OtrConfigurationException {
        String actual = ac3ToAac.create(testFile);
        logger.debug(actual);
        String expected = "dir.tools/tool.ffmpeg -i dir.hd.ac3/" + testFile + ".ac3 -vn -r 30000/1001 -acodec aac -strict experimental -ac 6 -ar 48000 -ab 448k dir.tmp/aac.aac";
        Assert.assertEquals(expected, actual);
    }
}
