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
import utils.ProductFullDVD;

public class DVDDetailHandler {

    public ProductFullDVD product;

    public DVDDetailHandler(String id) {
        try {
            product = new ProductFullDVD();
            URL url = new URL("http://eiffel.itba.edu.ar/hci/service/Catalog.groovy?method=GetProduct&product_id=" + id);
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
            NodeList nl = dom.getElementsByTagName("product");
            Element e = (Element) nl.item(0);
            NodeList name = e.getElementsByTagName("name");
            Element line = (Element) name.item(0);
            product.product = getCharacterDataFromElement(line);
            e = (Element) nl.item(0);
            name = e.getElementsByTagName("sales_rank");
            line = (Element) name.item(0);
            product.popularidad = getCharacterDataFromElement(line);
            e = (Element) nl.item(0);
            name = e.getElementsByTagName("price");
            line = (Element) name.item(0);
            product.precio = getCharacterDataFromElement(line);
            name = e.getElementsByTagName("image_url");
            line = (Element) name.item(0);
            product.url = getCharacterDataFromElement(line);
            name = e.getElementsByTagName("actors");
            line = (Element) name.item(0);
            product.actores = getCharacterDataFromElement(line);
            e = (Element) nl.item(0);
            name = e.getElementsByTagName("format");
            line = (Element) name.item(0);
            product.formato = getCharacterDataFromElement(line);
            e = (Element) nl.item(0);
            name = e.getElementsByTagName("language");
            line = (Element) name.item(0);
            product.language = getCharacterDataFromElement(line);
            e = (Element) nl.item(0);
            name = e.getElementsByTagName("subtitles");
            line = (Element) name.item(0);
            product.subtitulos = getCharacterDataFromElement(line);
            e = (Element) nl.item(0);
            name = e.getElementsByTagName("region");
            line = (Element) name.item(0);
            product.region = getCharacterDataFromElement(line);
            e = (Element) nl.item(0);
            name = e.getElementsByTagName("aspect_ratio");
            line = (Element) name.item(0);
            product.aspecto = getCharacterDataFromElement(line);
            e = (Element) nl.item(0);
            name = e.getElementsByTagName("number_discs");
            line = (Element) name.item(0);
            product.CDs = getCharacterDataFromElement(line);
            e = (Element) nl.item(0);
            name = e.getElementsByTagName("release_date");
            line = (Element) name.item(0);
            product.date = getCharacterDataFromElement(line);
            e = (Element) nl.item(0);
            name = e.getElementsByTagName("run_time");
            line = (Element) name.item(0);
            product.duracion = getCharacterDataFromElement(line);
            e = (Element) nl.item(0);
            name = e.getElementsByTagName("ASIN");
            line = (Element) name.item(0);
            product.ASIN = getCharacterDataFromElement(line);
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
