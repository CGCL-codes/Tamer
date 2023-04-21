package elearning.controller.admin.regist;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.slim3.tester.ControllerTestCase;

public class DeleteQuestionControllerTest extends ControllerTestCase {

    @Test
    public void run() throws Exception {
        tester.start("/admin/regist/deleteQuestion");
        DeleteQuestionController controller = tester.getController();
        assertThat(controller, is(notNullValue()));
        assertThat(tester.isRedirect(), is(true));
        assertThat(tester.getDestinationPath(), is("/admin/regist/"));
    }
}
