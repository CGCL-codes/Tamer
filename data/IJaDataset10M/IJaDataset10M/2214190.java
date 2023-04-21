package lebah.test;

import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.VelocityContext;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class TestAction2 implements lebah.portal.action.ActionTemplate_deprecated {

    public void doAction(HttpServletRequest req, HttpServletResponse res, VelocityContext context) throws Exception {
        Vector v = new Vector();
        v.addElement("Four");
        v.addElement("Five");
        v.addElement("Six");
        context.put("counts", v);
    }
}
