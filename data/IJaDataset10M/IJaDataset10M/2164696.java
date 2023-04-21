package test.web;

import java.io.IOException;
import java.net.MalformedURLException;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.BeforeClass;
import org.xml.sax.SAXException;
import scotlandyard.servlets.beans.WebUserBean;
import com.meterware.httpunit.WebConversation;

public class game_test extends WebUnitTest {

    private static final String HOST = "http://localhost:8084/sy";

    public game_test() throws ParserConfigurationException, MalformedURLException, IOException, SAXException {
        super(new WebConversation(), HOST);
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        new WebUserBean();
    }
}
