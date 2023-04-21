package EVEOverWatch;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author alexanagnos
 */
public class XML {

    /** Creates a new instance of XML */
    public XML() {
    }

    public static void XMLEcho() {
        Document doc = null;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String data = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode("1412045", "UTF-8");
            data += "&" + URLEncoder.encode("apiKey", "UTF-8") + "=" + URLEncoder.encode("EjmPCQNFMp73b229pXCtiNTwMNVE2q5kUftEwXUVbHHJvH25P3rOOJFqn8yUNnl2", "UTF-8");
            data += "&" + URLEncoder.encode("characterID", "UTF-8") + "=" + URLEncoder.encode("284049160", "UTF-8");
            String link = "/char/SkillInTraining.xml.aspx";
            URL url = new URL("http://api.eve-online.com" + link);
            URLConnection connection = url.openConnection();
            ((HttpURLConnection) connection).setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            PrintWriter output = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
            output.write(data);
            output.flush();
            output.close();
            doc = builder.parse(connection.getInputStream());
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (ProtocolException ex) {
            ex.printStackTrace();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (doc == null) {
            System.err.println("doc null");
            System.exit(1);
        }
        System.out.println(doc.getElementsByTagName("trainingTypeID").item(0).getFirstChild().getNodeValue());
    }

    public static void main(String[] args) {
        XMLEcho();
    }
}
