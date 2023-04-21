package weblife.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.microedition.io.HttpConnection;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import weblife.object.ObjectInterface;

/**
 * XML Parser f�r Szene1 API
 * Verwendet wird kxml-Parser
 * @link {@link http://kxml.sourceforge.net}
 * @author Knoll
 *
 */
public class MyParser implements ParserInterface {

    private String section;

    private String methode;

    private boolean status = false;

    private ObjectInterface object;

    private static String WEBLIFE1OBJECTS = "weblife.object.Object";

    /**
	 * Initialisierung der Section und Methode
	 * @param section Name der Section
	 * @param methode Name der Methode
	 */
    public MyParser(String section, String methode) {
        this.section = section;
        this.methode = methode;
        this.StartParsing();
    }

    /**
	 * Lest die Informationen aus dem Request und speichert das jeweilige WeblifeObjekt
	 * @param hc Verbindung zur WeblifeAPI
	 * */
    public void parse(HttpConnection hc) {
        try {
            this.object = (ObjectInterface) Class.forName(WEBLIFE1OBJECTS + this.section.substring(0, 1).toUpperCase() + this.section.substring(1)).newInstance();
        } catch (ClassNotFoundException e) {
            System.out.println("Error " + e);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println("Section: " + this.section);
        System.out.println("Object: " + this.object.getClass().getName());
        try {
            KXmlParser parser = new KXmlParser();
            InputStream in = hc.openInputStream();
            InputStreamReader reader;
            try {
                reader = new InputStreamReader(in, "UTF8");
            } catch (Exception e) {
                reader = new InputStreamReader(in);
                System.out.println(e);
            }
            parser.setInput(reader);
            parser.require(XmlPullParser.START_DOCUMENT, null, null);
            parser.nextTag();
            if (parser.getName().equals("error")) {
                parser.nextTag();
                String name = parser.getName();
                String value = parser.nextText();
                System.out.println("<" + name + ">" + value + "<" + name + ">");
                object.parseParams(this.methode, name, value);
                parser.nextTag();
                name = parser.getName();
                value = parser.nextText();
                System.out.println("<" + name + ">" + value + "<" + name + ">");
                object.parseParams(this.methode, name, value);
            } else {
                parser.require(XmlPullParser.START_TAG, null, this.section);
                parser.nextToken();
                while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                    readXMLData(parser, object, parser.getName());
                }
            }
            in.close();
            reader.close();
        } catch (XmlPullParserException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
        this.FinishParsing();
    }

    /**
	 * Lest die Daten rekursiv aus einem XML Baum
	 * @param parser der Parser selbst
	 * @param object Das Weblife Objekt
	 * @param name Name des des Header Tags
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
    private void readXMLData(KXmlParser parser, ObjectInterface object, String name) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        String value = null;
        switch(eventType) {
            case XmlPullParser.START_TAG:
                String currentname = parser.getName();
                parser.next();
                readXMLData(parser, object, currentname);
                break;
            case XmlPullParser.END_TAG:
                parser.nextTag();
                break;
            case XmlPullParser.TEXT:
                value = parser.getText();
                if (parser.nextToken() == XmlPullParser.END_TAG) {
                    if (name != null && value != null) {
                        System.out.println("<" + name + ">" + value + "<" + name + ">");
                        object.parseParams(this.methode, name, value);
                    } else {
                        System.out.println("Parameter oder name ist null");
                    }
                } else {
                }
                break;
            default:
                System.out.println("Default: " + parser.getEventType());
                parser.nextToken();
        }
    }

    /**
	 * Liefert das jeweilige WeblifeObjekt	
	 * @return
	 */
    public ObjectInterface getWeblifeObject() {
        synchronized (this) {
            return this.object;
        }
    }

    /**
	 * 
	 * @return gibt den Status wieder, ob fertig oder nicht. Fertig = true
	 */
    public synchronized boolean getStatus() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Hole Status");
        return this.status;
    }

    public synchronized void StartParsing() {
        System.out.println("Parsing Started");
    }

    public synchronized void FinishParsing() {
        System.out.println("Parsing Finished");
        status = true;
        notifyAll();
    }
}
