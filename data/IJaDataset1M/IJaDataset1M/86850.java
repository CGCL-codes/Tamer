package iTrustTests;

import java.io.IOException;
import java.net.MalformedURLException;
import org.xml.sax.SAXException;
import com.meterware.httpunit.*;
import junit.framework.*;

public class iTrustSQLIATest extends TestCase {

    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(iTrustSQLIATest.class);
    }

    public iTrustSQLIATest(String name) {
        super(name);
    }

    public void testiTrustSQLIA() throws SAXException, IOException, MalformedURLException {
        String url = "http://localhost:8080/iTrust";
        WebConversation conversation = new WebConversation();
        WebRequest request = new PostMethodWebRequest(url);
        WebResponse response = conversation.getResponse(request);
        WebForm[] forms = response.getForms();
        WebForm vulnerableForm = forms[0];
        vulnerableForm.setParameter("mid", "9000000001");
        vulnerableForm.setParameter("pw", "' OR 1=1 #");
        Button[] buttons = vulnerableForm.getButtons();
        SubmitButton submitButton = (SubmitButton) buttons[buttons.length - 1];
        response = vulnerableForm.submit(submitButton);
        System.out.println(response.getText());
        assertFalse(true);
    }
}
