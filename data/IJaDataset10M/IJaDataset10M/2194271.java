package intellibitz.sted.io;

import intellibitz.sted.fontmap.FontMap;
import intellibitz.sted.util.Resources;
import org.xml.sax.InputSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;

public class FontMapXMLWriter {

    private FontMapXMLWriter() {
    }

    public static void write(FontMap fontMap) throws TransformerException {
        final File selectedFile = fontMap.getFontMapFile();
        final Transformer transformer = TransformerFactory.newInstance().newTransformer();
        final StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(selectedFile.getName());
        stringBuffer.append(Resources.NEWLINE_DELIMITER);
        stringBuffer.append(Resources.getVersion());
        stringBuffer.append(Resources.NEWLINE_DELIMITER);
        stringBuffer.append(fontMap.toString());
        final SAXSource source = new SAXSource(new FontMapXMLReader(), new InputSource(new BufferedReader(new StringReader(stringBuffer.toString()))));
        transformer.transform(source, new StreamResult(selectedFile));
    }
}
