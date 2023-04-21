package org.avis.router;

import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.avis.io.messages.ConnRply;
import org.avis.io.messages.DropWarn;
import org.avis.util.AutoClose;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.avis.io.messages.Notify.asAttributes;
import static org.avis.router.JUTestRouter.PORT;

public class JUTestSendQueueThrottle {

    private AutoClose autoClose;

    @Before
    public void setup() {
        autoClose = new AutoClose();
    }

    @After
    public void cleanup() {
        autoClose.close();
    }

    /**
   * Test that router implements Send-Queue.Drop-Policy and
   * Send-Queue.Max-Length.
   */
    @Test
    public void throttle() throws Exception {
        Router router = new Router(PORT);
        SimpleClient client = new SimpleClient("localhost", PORT);
        autoClose.add(router);
        autoClose.add(client);
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("Send-Queue.Drop-Policy", "oldest");
        options.put("Send-Queue.Max-Length", 20 * 1024);
        ConnRply reply = client.connect(options);
        assertEquals(options.get("Send-Queue.Drop-Policy"), reply.options.get("Send-Queue.Drop-Policy"));
        assertEquals(options.get("Send-Queue.Max-Length"), reply.options.get("Send-Queue.Max-Length"));
        client.subscribe("To == 'me'");
        for (int i = 0; i < 1000; i++) client.sendNotify(asAttributes("To", "me", "Payload", new byte[1024]));
        boolean dropWarned = false;
        for (int i = 0; i < 1000 && !dropWarned; i++) {
            if (client.receive() instanceof DropWarn) dropWarned = true;
        }
        assertTrue("No DropWarn", dropWarned);
    }
}
