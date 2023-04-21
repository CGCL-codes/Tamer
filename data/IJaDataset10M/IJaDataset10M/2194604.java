package org.bee.testcase.exp;

import java.io.IOException;
import org.bee.testcase.BasicTestCase;
import org.bee.tl.core.BeeException;
import org.bee.tl.core.Template;
import org.bee.tl.samples.User;

public class SafeOutputTestCase extends BasicTestCase {

    public SafeOutputTestCase() {
        super();
    }

    public void testBasic() throws IOException, BeeException {
        Template t = this.gt.getFileTemplate("/exp/safe_output_template.html");
        User user = new User();
        t.set("user", user);
        String str = t.getTextAsString();
        System.out.println("result=" + str);
        t = this.gt.getFileTemplate("/exp/safe_output_template.html");
        t.set("user", user);
        str = t.getTextAsString();
        this.assertEquals(this.getFileContent("/exp/safe_output_expected.html"), str);
    }
}
