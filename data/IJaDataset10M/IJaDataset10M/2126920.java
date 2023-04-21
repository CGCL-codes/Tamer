package ws.spider.achievementPoints;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class RealmFirsts {

    public static final String ACH_FILE = "data/realmFirsts.txt";

    private ArrayList<String> info;

    public RealmFirsts() {
        info = new ArrayList<String>();
        try {
            Document d = getPage("http://www.wowarmory.com/achievement-firsts.xml?r=hakkar");
            NodeList n = d.getElementsByTagName("achievement");
            for (int k = 0; k < n.getLength(); k++) {
                String title = null;
                String dateCompleted = null;
                NamedNodeMap attrs = n.item(k).getAttributes();
                for (int i = 0; i < attrs.getLength(); i++) {
                    Attr attribute = (Attr) attrs.item(i);
                    if (attribute.getName().equals("title")) title = attribute.getValue();
                    if (attribute.getName().equals("dateCompleted")) dateCompleted = attribute.getValue();
                }
                NamedNodeMap attrs2 = n.item(k).getFirstChild().getAttributes();
                String name = null;
                for (int i = 0; i < attrs2.getLength(); i++) {
                    Attr attribute = (Attr) attrs2.item(i);
                    if (attribute.getName().equals("name")) name = attribute.getValue();
                }
                if (n.item(k).getFirstChild().getNodeName().equals("guild")) info.add("G" + ";" + name + ";" + title + ";" + dateCompleted); else info.add("P" + ";" + name + ";" + title + ";" + dateCompleted);
            }
            saveInfo(ACH_FILE, info);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        new RealmFirsts();
    }

    private static Document getPage(String url) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource();
        URL loc = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) loc.openConnection();
        String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; ";
        userAgent += "rv:1.8.1.2) Gecko/20070219 Firefox/2.0.0.2";
        conn.setRequestProperty("User-agent", userAgent);
        conn.setConnectTimeout(800);
        InputStream input = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line = reader.readLine();
        String buffer = "";
        while (line != null) {
            line = line.trim();
            buffer += line;
            line = reader.readLine();
        }
        reader.close();
        is.setCharacterStream(new StringReader(buffer));
        return db.parse(is);
    }

    private void saveInfo(String fileName, ArrayList<String> data) {
        File archivoFactura = new File(fileName);
        try {
            PrintWriter out = new PrintWriter(archivoFactura);
            for (String s : data) out.println(s);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
