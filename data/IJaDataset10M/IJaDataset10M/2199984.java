package net.sourceforge.acts20_2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xerces.jaxp.SAXParserFactoryImpl;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * A class to update and manage the XML verse list, which needs to be working
 * before version 0.4 is released.
 * 
 * @author Matt
 */
public class XMLVerseReader {

    public static ArrayList<Verse> read() {
        XMLHandler handler = new XMLHandler();
        try {
            new SAXParserFactoryImpl().newSAXParser().parse(new File("xml/verses.xml"), handler);
            Log.info("Parsing started");
        } catch (SAXParseException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        if (!handler.isStarted()) {
            throw new RuntimeException("The handler wasn't started");
        }
        while (!handler.isFinished()) {
            System.out.println("waiting...");
        }
        return handler.getResult();
    }

    /**
	 * A method to read from the TestHandler as opposed to the XMLHandler. 100%
	 * guaranteed to throw an exception once you try to do something with the
	 * result.
	 * 
	 * @return null
	 */
    public static ArrayList<Verse> readFromTest() {
        TestHandler handler = new TestHandler();
        try {
            new SAXParserFactoryImpl().newSAXParser().parse(new File("xml/verses.xml"), handler);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * Copies the server copy of the verse list to this computer.
	 */
    public static void update() {
        try {
            URL fileURL = new URL("http://acts202.sourceforge.net/update/verses-" + PrefsManager.getLang() + ".xml");
            InputStream input = fileURL.openStream();
            File eventualDestination = new File("xml/verses.xml");
            OutputStream output = new FileOutputStream(eventualDestination);
            int i = 0;
            int total = 0;
            while (i < 10) {
                total = input.available();
                if (total == 0) {
                    i++;
                } else {
                    i = 0;
                    System.out.println("Downloading: " + total + " bytes");
                    byte temp[] = new byte[total];
                    input.read(temp);
                    output.write(temp);
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            input.close();
            output.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
