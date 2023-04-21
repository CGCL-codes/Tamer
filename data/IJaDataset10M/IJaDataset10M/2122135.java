package jp.co.ziro.kanbe.controller.mng.mail;

import org.slim3.tester.ControllerTestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class FindControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.start("/mng/mail/find");
        FindController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is(nullValue()));
    }
}
