package parsers;

import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.lang.Math;

/**
 * 
 * The parser for parsing the JavaNCSS reports
 * 
 */
public class JavaNcssParser extends AbstractParser {

    private static ArrayList<Element> generalStats = new ArrayList<Element>();

    private int numPackages = -1;

    private int numClasses = -1;

    private int numFunctions = -1;

    private static final int INDEX_PACKAGES = 6;

    private static final int INDEX_CLASSES = 7;

    private static final int INDEX_FUNCTIONS = 8;

    public void readResults(String path) throws ParserConfigurationException, SAXException, IOException {
        parseResults(path);
        NodeList table = root.getElementsByTagName("td");
        for (int i = 0; i < table.getLength(); i++) {
            generalStats.add((Element) table.item(i));
        }
        numPackages = Math.round(Float.parseFloat(generalStats.get(INDEX_PACKAGES).getTextContent()));
        numClasses = Math.round(Float.parseFloat(generalStats.get(INDEX_CLASSES).getTextContent()));
        numFunctions = Math.round(Float.parseFloat(generalStats.get(INDEX_FUNCTIONS).getTextContent()));
        generalStats.clear();
    }

    @Override
    public void saveResults(int idVersion) throws Exception {
    }

    public int getNumPackages() {
        return numPackages;
    }

    public int getNumClasses() {
        return numClasses;
    }

    public int getNumFunctions() {
        return numFunctions;
    }
}
