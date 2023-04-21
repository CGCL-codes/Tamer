package picassawad;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 *
 * @author jaszczur
 */
public class Main {

    /** Creates a new instance of Main */
    public Main() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] argv) {
        if (argv.length != 1) {
            System.err.println("Usage: cmd filename");
            System.exit(1);
        }
        String infile = argv[0];
        String dir = ".";
        ImageList handler = new ImageList();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(new File(infile), handler);
        } catch (Exception e) {
            System.err.println("Błąd podczas czytania pliku " + infile);
            System.exit(1);
        }
        System.out.println("Teraz pobiorę " + handler.getUrls().size() + " obrazków.");
        ImageDownloader id = new ImageDownloader(handler.getUrls());
        try {
            id.downloadTo(new File(dir));
        } catch (IOException ex) {
            System.err.println("Błąd wejścia/wyjścia");
        } catch (IllegalArgumentException ex) {
            System.err.println("Podaj nazwę folderu");
        }
    }
}
