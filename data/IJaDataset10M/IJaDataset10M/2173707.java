package net.sourceforge.dawnlite.script.xmlser;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.util.TreeMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import net.sourceforge.dawnlite.script.Script;
import net.sourceforge.dawnlite.script.ViewBox;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLInput extends DefaultHandler implements Constants {

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        if (name.equals(ROOT_ELEMENT_NAME)) {
            script.setImageURL(attributes.getValue("", ROOT_ATT_IMAGE_URL));
            int w, h;
            w = Integer.parseInt(attributes.getValue("", ROOT_ATT_IMAGE_WIDTH));
            h = Integer.parseInt(attributes.getValue("", ROOT_ATT_IMAGE_HEIGHT));
            script.setPictureSize(new Dimension(w, h));
            w = Integer.parseInt(attributes.getValue("", ROOT_ATT_DISPLAY_WIDTH));
            h = Integer.parseInt(attributes.getValue("", ROOT_ATT_DISPLAY_HEIGHT));
            script.setDisplaySize(new Dimension(w, h));
        } else if (name.equals(ELEMENT_GROUP)) {
        } else if (name.equals(ELEMENT_VIEWBOX)) {
            int offset = Integer.parseInt(attributes.getValue("", ATT_TIME_OFFSET));
            int start = durationSoFar + offset;
            int duration = Integer.parseInt(attributes.getValue("", ATT_DURATION));
            ViewBox viewBox = new ViewBox();
            {
                int w, h;
                w = Integer.parseInt(attributes.getValue("", ATT_WIDTH));
                h = Integer.parseInt(attributes.getValue("", ATT_HEIGHT));
                viewBox.setSize(new Dimension(w, h));
            }
            {
                int x, y;
                x = Integer.parseInt(attributes.getValue("", ATT_X));
                y = Integer.parseInt(attributes.getValue("", ATT_Y));
                viewBox.setLocation(new Point(x, y));
            }
            viewBox.setStayDurationMillis(duration);
            viewBoxSequence.put(start, viewBox);
            durationSoFar += offset + duration;
        }
    }

    Script script;

    TreeMap<Integer, ViewBox> viewBoxSequence;

    int durationSoFar;

    public Script read(InputStream in) {
        SAXParser saxParser = null;
        SAXParserFactory parserFactory;
        parserFactory = SAXParserFactory.newInstance();
        parserFactory.setNamespaceAware(true);
        try {
            saxParser = parserFactory.newSAXParser();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        script = new Script();
        viewBoxSequence = new TreeMap<Integer, ViewBox>();
        this.durationSoFar = 0;
        try {
            saxParser.parse(in, this);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        script.setViewBoxTimeMap(this.viewBoxSequence);
        return script;
    }
}
