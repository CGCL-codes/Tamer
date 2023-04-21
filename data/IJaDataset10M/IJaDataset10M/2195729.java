package servicesHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import utils.Language;

public class LanguageHandler {

    public ArrayList<Language> languages = new ArrayList<Language>();

    public LanguageHandler() {
        try {
            URL url = new URL("http://eiffel.itba.edu.ar/hci/service/Common.groovy?method=GetLanguageList");
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
                return;
            }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(response));
            Document dom = db.parse(is);
            NodeList nl = dom.getElementsByTagName("language");
            for (int i = 0; i < nl.getLength(); i++) {
                Element e = (Element) nl.item(i);
                String id = e.getAttribute("id");
                NodeList name = e.getElementsByTagName("name");
                Element line = (Element) name.item(0);
                String nameS = getCharacterDataFromElement(line);
                NodeList code = e.getElementsByTagName("code");
                line = (Element) code.item(0);
                String codeS = getCharacterDataFromElement(line);
                languages.add(new Language(id, codeS, nameS));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
