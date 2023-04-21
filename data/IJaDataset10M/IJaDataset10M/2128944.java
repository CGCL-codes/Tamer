package stk.web.gae.page;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.t2framework.t2.navigation.Forward;
import org.t2framework.t2.spi.Navigation;
import org.t2framework.slim3.tester.PageTestCase;

public class CommentPageTest extends PageTestCase {

    @Test
    public void run() throws Exception {
        tester.start("/comment");
        Navigation navigation = tester.getNavigation();
        assertThat(navigation, is(notNullValue()));
        assertThat(navigation.getClass().getSimpleName(), is("Forward"));
        Forward forward = (Forward) navigation;
        assertThat(forward.getPath(), is("/comment.jsp"));
    }
}
