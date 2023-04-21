package currency;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class eWorldCurrency {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        File f = new File("F:\\erep\\countries.xml");
        Table table = new Table();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(f);
        NodeList nl = doc.getElementsByTagName("country");
        String name = null, currency = null;
        Double buy = 0.0, sell = 0.0;
        String id = null;
        DecimalFormat df = new DecimalFormat("###0.00");
        table.setVisible(true);
        table.setLocationRelativeTo(null);
        while (true) {
            for (int i = 0; i < nl.getLength(); i++) {
                Node country = nl.item(i);
                NodeList info = country.getChildNodes();
                for (int j = 0; j < info.getLength(); j++) {
                    Node temp = info.item(j);
                    if (temp.getNodeName().equals("name")) {
                        name = temp.getTextContent();
                        System.out.print(name + "\t");
                    }
                    if (temp.getNodeName().equals("currency")) {
                        currency = temp.getTextContent();
                    }
                    if (temp.getNodeName().equals("id")) {
                        id = temp.getTextContent();
                        System.out.print(id + "\n");
                        break;
                    }
                }
                Document doc1 = builder.parse("http://api.erepublik.com/v2/feeds/exchange/GOLD/" + currency);
                NodeList offers = doc1.getElementsByTagName("offer");
                Node offer = offers.item(0);
                NodeList details = offer.getChildNodes();
                Node price = details.item(5);
                sell = Double.parseDouble(df.format(Double.parseDouble(price.getTextContent())));
                doc1 = builder.parse("http://api.erepublik.com/v2/feeds/exchange/" + currency + "/GOLD");
                offers = doc1.getElementsByTagName("offer");
                offer = offers.item(0);
                details = offer.getChildNodes();
                price = details.item(5);
                buy = Double.parseDouble(price.getTextContent());
                buy = Double.parseDouble(df.format(1 / buy));
                if (buy - sell > 10 && !currency.equals("FRF") && !currency.equals("HUF") && !currency.equals("SEK") && !currency.equals("GRD") && !currency.equals("KRW") && !currency.equals("BOB") && !currency.equals("EEK")) {
                    Message msg = new Message("make " + df.format(buy - sell) + currency + " per gold at " + name + " now!!", "http://www.erepublik.com/en/exchange#buy_currencies=62;sell_currencies=" + id + ";page=1", "http://www.erepublik.com/en/exchange#buy_currencies=" + id + ";sell_currencies=62;page=1");
                    msg.setVisible(true);
                    msg.setLocationRelativeTo(null);
                }
                table.model.addRow(new String[] { name, currency, df.format(buy), df.format(sell), df.format(buy - sell) });
            }
            table.model.getDataVector().removeAllElements();
            if (table.exit) {
                table.dispose();
                break;
            }
        }
    }
}
