package mangastreamdl.business.ms;

import mangastreamdl.business.ImageProperties;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Grigor Riskov - grigor@riskov.cz
 */
public class ImageGetter extends DefaultHandler {

    private String baseurl;

    private String url;

    private String location;

    private boolean inStyle;

    private ImageProperties ip = new ImageProperties();

    private String m_id;

    private boolean found;

    private boolean inIDs = false;

    private String characters = "";

    private boolean inline = false;

    private boolean first = true;

    private boolean inInline = false;

    private String divStyle = null;

    private int idCounter = 0;

    public ImageProperties getImageLink(String url) throws IOException, SAXException {
        baseurl = "http://mangastream.com";
        this.url = baseurl + url;
        Parser parser = new Parser();
        parser.setContentHandler(this);
        parser.setErrorHandler(this);
        parser.parse(this.url);
        return ip;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (localName.equals("img")) {
            String src = attributes.getValue("src");
            if (src != null && src.contains("http://img.mangastream.com")) {
                ip.addImage("0", ip.getWidth(), -1, 0, 0, 0);
                ip.setLocation("0", src);
            }
            if (found) ip.setLocation(m_id, src); else if (inline) {
                ip.setLocation(String.valueOf(idCounter - 1), src);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (localName.equals("div")) {
            found = false;
            if (!inline) inInline = false;
            inline = false;
        }
        if (localName.equals("style")) {
            inStyle = false;
            try {
                parseStyle(characters);
                characters = "";
            } catch (Exception ex) {
                Logger.getLogger(ImageGetter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if (inStyle) {
            String s = new String(ch, start, length);
            characters += s;
        }
    }

    private ImageProperties parseStyle(String s) throws IOException {
        StringReader sr = new StringReader(s);
        BufferedReader br = new BufferedReader(sr);
        String line = br.readLine();
        while (true) {
            if (line == null) break;
            if (line.contains("#page {")) {
                line = br.readLine();
                ip.setWidth(parseWidth(line));
                this.inIDs = true;
            }
            if (inIDs && line.matches(".*#[A-Za-z0-9]+.*")) {
                parseID(line);
            }
            if (inIDs && line.contains("-->")) inIDs = false;
            if (br.ready()) {
                line = br.readLine();
            } else break;
        }
        return ip;
    }

    private int parseWidth(String line) {
        String width = "";
        boolean number = false;
        char c;
        for (int i = 0; i < line.length(); i++) {
            c = line.charAt(i);
            if (number) {
                if (Character.isDigit(c)) width += c; else number = false;
            }
            if (c == ':') number = true;
        }
        return Integer.parseInt(width);
    }

    private int[] parseWidthAndHeight(String s) {
        int[] ret = new int[2];
        String[] sarr = s.split(";");
        for (int i = 0; i < sarr.length; i++) {
            if (sarr[i].contains("width")) ret[0] = parseNumber(sarr[i]); else if (sarr[i].contains("height")) ret[1] = parseNumber(sarr[i]);
        }
        return ret;
    }

    private void parseID(String line) {
        String ID = "";
        int width = 0, height = 0, top = 0, left = 0, z_index = 0;
        int i = 0;
        char c = line.charAt(i);
        while (c != '#') {
            c = line.charAt(++i);
        }
        c = line.charAt(++i);
        while (c != ' ') {
            ID += c;
            c = line.charAt(++i);
        }
        String[] sarr = line.split(";");
        for (int j = 0; j < sarr.length; j++) {
            if (sarr[j].contains("z-index")) z_index = parseNumber(sarr[j]); else if (sarr[j].contains("width")) width = parseNumber(sarr[j]); else if (sarr[j].contains("height")) height = parseNumber(sarr[j]); else if (sarr[j].contains("top")) top = parseNumber(sarr[j]); else if (sarr[j].contains("left")) left = parseNumber(sarr[j]);
        }
        ip.addImage(ID, width, height, top, left, z_index);
    }

    private int parseNumber(String string) {
        String ret = "";
        char c;
        for (int i = 0; i < string.length(); i++) {
            c = string.charAt(i);
            if (Character.isDigit(c)) ret += c;
        }
        return Integer.parseInt(ret);
    }

    private void parseInlineStyle(String line) {
        int width = 0, height = 0, top = 0, left = 0, z_index = 0;
        String[] sarr = line.split(";");
        for (int j = 0; j < sarr.length; j++) {
            if (sarr[j].contains("z-index")) z_index = parseNumber(sarr[j]); else if (sarr[j].contains("width")) width = parseNumber(sarr[j]); else if (sarr[j].contains("height")) height = parseNumber(sarr[j]); else if (sarr[j].contains("top")) top = parseNumber(sarr[j]); else if (sarr[j].contains("left")) left = parseNumber(sarr[j]);
        }
        ip.addImageKnownHeight(String.valueOf(idCounter++), width, height, top, left, z_index);
    }
}
