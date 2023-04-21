package kentriko;

import java.io.File;
import java.util.StringTokenizer;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

public class XMLReader {

    FileDownload fd;

    private String url;

    String localXML;

    private JLabel myLabel;

    public XMLReader(String inPath, JLabel inLabel) {
        fd = null;
        localXML = "temp.xml";
        url = inPath;
        myLabel = inLabel;
        fd = new FileDownload(inPath, localXML, myLabel);
        fd.run();
    }

    public String getResult() {
        String result = "";
        try {
            File file = new File(localXML);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            NodeList video = doc.getElementsByTagName("video");
            Node fstNode = video.item(0);
            String full = fstNode.getAttributes().getNamedItem("ipodLink").toString();
            StringTokenizer st = new StringTokenizer(full);
            st.nextToken("\"");
            result = st.nextToken();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error parsing the XML");
            e.printStackTrace();
        }
        return result;
    }
}
