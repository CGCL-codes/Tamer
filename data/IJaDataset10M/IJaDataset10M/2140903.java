package net.sf.cplab.corpus;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import net.sf.cplab.converter.Converter;
import net.sf.cplab.converter.FileConverter;
import net.sf.cplab.ui.AboutMenuItem;
import net.sf.cplab.ui.GUI;
import net.sf.cplab.ui.PlatformTools;

/**
 * @author jtse
 *
 */
public class CorpusToTextConverter implements Converter {

    private XCESHandler handler = new XCESHandler();

    private SAXParserFactory factory = SAXParserFactory.newInstance();

    public boolean convert(FileInputStream in, FileOutputStream out) throws IOException {
        handler.clear();
        SAXParser parser;
        try {
            parser = factory.newSAXParser();
            parser.parse(in, handler);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return false;
        } catch (SAXException e) {
            e.printStackTrace();
            return false;
        }
        PrintStream ps = new PrintStream(out);
        ps.print(handler.getStringBuffer().toString());
        return true;
    }

    public String getFileExtension() {
        return "txt";
    }

    public static final void main(String[] args) {
        final String appName = "CorpusToText Converter";
        PlatformTools.setApplicationName(appName);
        Converter[] converters = { new CorpusToTextConverter() };
        FileConverter app = new FileConverter(appName, converters);
        AboutMenuItem about = new AboutMenuItem(app.getName(), "0.01");
        GUI.setAboutMenuItem(about);
        GUI.launch(app);
    }

    public String toString() {
        return "XCES to Plain-Text";
    }
}
