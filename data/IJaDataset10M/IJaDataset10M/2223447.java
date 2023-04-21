package convertemployeesfromxmltomysql;

import java.io.InputStream;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

public class ReadXMLFromURL {

    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        URL url = new URL("http://co02intranet/sgdcent/cnt/vw_ss_ma_empleados_activos_to_xml.asp");
        InputStream inputStream = url.openStream();
        Document document = db.parse(inputStream);
        System.out.println(document.toString());
        inputStream.close();
    }
}
