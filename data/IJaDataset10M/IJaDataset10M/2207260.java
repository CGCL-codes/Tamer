package winecellar.util.io;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URL;

/**
 * This class provides the possibility to transform a given XML file with
 * the given XSL style sheet into an HTML file.
 * 
 * @author Dimitrij Pankratz, Anton Musichin
 * @version 1.00
 */
public class HTMLTransformer {

    public static final String STYLE_SHEET = "data/style.xsl";

    /**
	 * Transforms the given XML file with a given style sheet into an HTML file.
	 * 
	 * @param xmlFile
	 * @param resultFile
	 * @param xslFile
	 * @throws TransformerException
	 * @throws FileNotFoundException
	 */
    public static void transform(URL xslFile, String xmlFile, String resultFile) throws TransformerException, FileNotFoundException, IOException {
        TransformerFactory fac = TransformerFactory.newInstance();
        Source xsl = new StreamSource(xslFile.openStream());
        Source xml = new StreamSource(new FileInputStream(xmlFile));
        FileOutputStream outputStream = new FileOutputStream(resultFile);
        Result output = new StreamResult(outputStream);
        Transformer transformer = fac.newTransformer(xsl);
        transformer.transform(xml, output);
        outputStream.close();
    }
}
