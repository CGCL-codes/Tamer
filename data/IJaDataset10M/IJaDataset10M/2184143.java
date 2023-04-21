package backend.parser.kegg.xml;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import backend.event.type.DataFileMissing;
import backend.parser.kegg.Parser;

/**
 * @author Jan
 *
 */
public class Resolver implements EntityResolver {

    private String pathToDTD;

    public Resolver(String pathToDTD) {
        this.pathToDTD = pathToDTD;
    }

    public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException {
        InputSource inputSource = null;
        try {
            FileReader fileReader = new FileReader(pathToDTD);
            inputSource = new InputSource(fileReader);
        } catch (FileNotFoundException fnfe) {
            Parser.propagateEventOccurred(new DataFileMissing("No DTD file was found. (path: " + pathToDTD + ")"));
            System.exit(1);
        }
        return inputSource;
    }
}
