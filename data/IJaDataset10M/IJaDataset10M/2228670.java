package servicesHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import utils.UserDetails;

public class GetAccountHandler {

    public static UserDetails useGetAccount(String username, String token) {
        try {
            UserDetails details = new UserDetails();
            URL url = new URL("http://eiffel.itba.edu.ar/hci/service/Security.groovy?method=GetAccount&username=" + username + "&authentication_token=" + token);
            URLConnection urlc = url.openConnection();
            urlc.setDoOutput(false);
            urlc.setAllowUserInteraction(false);
            BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            String str;
            StringBuffer sb = new StringBuffer();
            while ((str = br.readLine()) != null) {
                sb.append(str);
                sb.append("\n");
            }
            br.close();
            String response = sb.toString();
            if (response == null) {
                return null;
            }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(response));
            Document dom = db.parse(is);
            NodeList nl = dom.getElementsByTagName("account");
            Element e = (Element) nl.item(0);
            NodeList name = e.getElementsByTagName("name");
            Element line = (Element) name.item(0);
            details.name = getCharacterDataFromElement(line);
            e = (Element) nl.item(0);
            name = e.getElementsByTagName("username");
            line = (Element) name.item(0);
            details.username = getCharacterDataFromElement(line);
            e = (Element) nl.item(0);
            name = e.getElementsByTagName("email");
            line = (Element) name.item(0);
            details.email = getCharacterDataFromElement(line);
            e = (Element) nl.item(0);
            name = e.getElementsByTagName("birth_date");
            line = (Element) name.item(0);
            details.birth_date = getCharacterDataFromElement(line);
            e = (Element) nl.item(0);
            name = e.getElementsByTagName("created_date");
            line = (Element) name.item(0);
            details.created_date = getCharacterDataFromElement(line);
            e = (Element) nl.item(0);
            name = e.getElementsByTagName("last_login_date");
            line = (Element) name.item(0);
            details.last_login_date = getCharacterDataFromElement(line);
            return details;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "?";
    }
}
