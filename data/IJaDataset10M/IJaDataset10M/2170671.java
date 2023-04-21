package gpsExtractor;

import gpsExtractor.gui.MainFrame;
import gpsExtractor.tools.gpx.GPXParser;
import java.text.*;
import java.util.*;
import java.io.FileNotFoundException;
import gpsExtractor.tools.trk.TrackResult;
import gpsExtractor.tools.trk.TrackResultPrinter;
import javax.xml.stream.XMLStreamException;

/**
 * Created by IntelliJ IDEA.
 * User: constant.petrov
 * Mailto: constant.petrov@gmail.com
 * Date: 27.11.2008
 * Time: 21:59:49
 * To change this template use File | Settings | File Templates.
 */
public class GpsExtr {

    public static MainFrame mainFrame;

    public static void main(String[] argv) throws ParseException {
        Locale.getAvailableLocales();
        Set<String> arguments = new TreeSet<String>();
        if (argv.length != 0) {
            for (String arg : argv) {
                arguments.add(arg);
            }
        }
        if (arguments.contains("-help")) {
            System.out.println("\nUsage:  java -jar gpx-e uri");
            System.out.println("   where uri is the URI of your GPX track-file.");
            System.out.println("   Sample:  java -jar gpx-e test.xml");
            System.out.println("   you need a config-file \"conf.xfg\"");
            System.out.println("   ");
            System.out.println("   Result is \"gpx-file-name\".xml");
            System.out.println("   you also need \"simple.xsl\" for your browser");
            System.out.println("\nEchoes SAX events back to the console.");
            System.exit(1);
        }
        if (arguments.contains("-nogui")) {
            for (String arg : arguments) {
                if (arg.endsWith(".gpx")) {
                    TrackResultPrinter.setResFileName(arg.substring(0, arg.lastIndexOf('.')));
                    GPXParser parser = new GPXParser();
                    ArrayList<TrackResult> results = parser.parseURI(arg);
                    for (int i = 0; i < results.size(); i++) {
                        TrackResultPrinter.printConsole(results.get(i));
                        try {
                            TrackResultPrinter.setResFileNumber(i + 1);
                            TrackResultPrinter.printXML(results.get(i));
                        } catch (XMLStreamException e) {
                            e.printStackTrace();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            mainFrame = new MainFrame();
        }
    }
}
