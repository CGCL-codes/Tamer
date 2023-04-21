package utils;

import core.transport.CVSURL;
import core.transport.TransportIF;
import core.transport.TransportManager;
import de.nava.informa.exporters.RSS_1_0_Exporter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;

/**
 * @author jp
 * 
 * Return the XML of a channel
 */
public class TextFetch {

    /**
     * Constructor for TextFetch.
     */
    public TextFetch() {
        super();
    }

    public static void main(String[] args) throws Exception {
        CVSURL.init();
        System.out.println(new TextFetch().fetch(new URL(args[0])));
    }

    public String fetch(URL url) throws Exception {
        TransportIF transportIF = TransportManager.getInstance();
        Writer writer = new StringWriter();
        RSS_1_0_Exporter exporter = new RSS_1_0_Exporter(writer, "utf-8");
        exporter.write(transportIF.fetch(url));
        return writer.toString();
    }
}
