package slim3.service;

import org.slim3.tester.AppEngineTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class PingServiceTest extends AppEngineTestCase {

    private PingService service = new PingService();

    @Test
    public void test() throws Exception {
        String msg = "hello";
        assertThat(service, is(notNullValue()));
        assertEquals(msg, service.ping(msg));
    }
}
