package itest.org.fao.geonet.services.main;

import itest.org.fao.geonet.Utils;
import jeeves.utils.Xml;
import org.junit.Test;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.apache.commons.httpclient.HttpClient;
import junit.framework.TestCase;
import java.io.IOException;
import java.util.List;

public class UserTest extends TestCase {

    public UserTest() {
        Utils.setSequential();
    }

    private void init() {
        Utils.cleanCatalogue();
        Utils.addSamples();
    }

    private String buildUserUpdateRequest(String username, String password, String name, String surname, String group, String profile, String id, String operation) {
        String request = "user.update?" + "&operation=" + operation + "&username=" + username + "&password=" + password + "&password2=" + password + "&groups=" + group + "&profile=" + profile + "&state=CA" + "&name=" + name + "&surname=" + surname + "&org=ESRKINE" + "&kind=gov" + "&address=390BlueHillsSt" + "&city=Bluelands" + "&country=USA" + "&zip=92373" + "&email=userone@userone.com";
        if (id != null) request += "&id=" + id;
        return request;
    }

    @Test
    public void testUserGroups() {
        String uuid = "da165110-88fd-11da-a88f-000d939bc5d8";
        try {
            init();
            final HttpClient c = new HttpClient();
            Utils.dropAllNonAdminUsers(true, c);
            String username = "JodyMoredanger";
            String password = "Moreland?";
            String name = "Jody";
            String surname = "Moreland";
            String group = "2";
            String profile = "UserAdmin";
            String id = null;
            String operation = "newuser";
            String request = buildUserUpdateRequest(username, password, name, surname, group, profile, id, operation);
            Utils.sendRequest(request, false, c);
            String response = Utils.sendRequest("xml.user.list", false, c);
            Element resp = Xml.loadString(response, false);
            List<Element> users = resp.getChildren();
            boolean found = false;
            for (Element user : users) {
                String testname = user.getChildText("username");
                if (testname.equals(username)) {
                    id = user.getChildText("id");
                    found = true;
                    break;
                }
            }
            assertTrue(found);
            response = Utils.sendRequestToFail(request, false, c);
            assertTrue(response.contains("IllegalArgumentException"));
            name = "Jodie";
            surname = "Moredanger";
            operation = "editinfo";
            request = buildUserUpdateRequest(username, password, name, surname, group, profile, id, operation);
            Utils.sendRequest(request, false, c);
            response = Utils.sendRequest("xml.user.list", false, c);
            resp = Xml.loadString(response, false);
            users = resp.getChildren();
            found = false;
            for (Element user : users) {
                String testname = user.getChildText("username");
                if (testname.equals(username)) {
                    String testFirstname = user.getChildText("name");
                    String testSurname = user.getChildText("surname");
                    if (testFirstname.equals(name) && testSurname.equals(surname)) {
                        found = true;
                        break;
                    }
                }
            }
            assertTrue(found);
            password = "cobblers";
            operation = "resetpw";
            request = buildUserUpdateRequest(username, password, name, surname, group, profile, id, operation);
            Utils.sendRequest(request, false, c);
            Utils.sendLogin(c, username, password);
            username = "cropper";
            password = "BenBen?";
            name = "Rhys";
            surname = "Cropper";
            group = "1";
            profile = "Editor";
            id = null;
            operation = "newuser";
            request = buildUserUpdateRequest(username, password, name, surname, group, profile, id, operation);
            response = Utils.sendRequestToFail(request, false, c);
            assertTrue(response.contains("IllegalArgumentException"));
            group = "2";
            request = buildUserUpdateRequest(username, password, name, surname, group, profile, id, operation);
            Utils.sendRequest(request, false, c);
            response = Utils.sendRequest("xml.user.list", false, c);
            resp = Xml.loadString(response, false);
            users = resp.getChildren();
            found = false;
            for (Element user : users) {
                String testname = user.getChildText("username");
                if (testname.equals(username)) {
                    id = user.getChildText("id");
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            fail();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        } catch (JDOMException je) {
            je.printStackTrace();
            fail();
        } finally {
            Utils.cleanCatalogue();
        }
    }
}
