package practical4;

import junit.framework.*;
import com.meterware.httpunit.*;

public class WebTest1 extends TestCase {

    public void testGet() throws Exception {
        WebConversation wc = new WebConversation();
        WebRequest req = new GetMethodWebRequest("http://localhost/practical4/login.html");
        WebResponse res = wc.getResponse(req);
        assertTrue(res.getText().indexOf("id") != -1);
        assertTrue(res.getText().indexOf("txtpass") != -1);
    }

    public void test1() throws Exception {
        WebConversation wc = new WebConversation();
        WebRequest req = new GetMethodWebRequest("http://localhost/practical4/login.html");
        WebResponse res = wc.getResponse(req);
        WebForm form = res.getForms()[0];
        form.setParameter("id", "frank");
        form.setParameter("txtpass", "bananaman");
        form.submit();
        res = wc.getCurrentPage();
        assertEquals(res.getURL().toString(), "http://localhost/servlet/Login");
        assertTrue(res.getText().indexOf("bananaman") != -1);
    }
}
