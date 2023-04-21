package org.tastefuljava.sceyefi.conf;

import org.tastefuljava.sceyefi.capture.conf.EyeFiConf;
import org.tastefuljava.sceyefi.capture.conf.EyeFiCard;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EyeFiConfTest {

    private EyeFiConf conf;

    @Before
    public void setUp() throws IOException {
        conf = EyeFiConf.load(getClass().getResource("Settings.xml"));
    }

    @Test
    public void testGetCard() {
        System.out.println("getCard");
        EyeFiCard card = conf.getCard("001856417729");
        assertNotNull(conf);
    }
}
