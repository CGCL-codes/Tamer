package tei.cr.drivers;

import java.util.logging.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import tei.cr.pipeline.AbstractBase;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileFilter;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.Attributes;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import tei.cr.utils.FileUtils;
import tei.cr.pipeline.FilterByNames;
import tei.cr.pipeline.WrongArgsException;
import tei.cr.querydoc.FilterArguments;
import tei.cr.teiDocument.TeiDocument;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tei.cr.filters.FilterException;
import tei.cr.input.Directory;
import tei.cr.input.File2XMLFragmentProducer;

/**
 * Converting text files into XML using {@link tei.cr.input.File2XMLFragmentProducer}.
 * 
 * @author Sylvain Loiseau
 */
public class Text extends AbstractBase {

    private String absolute = "";

    private List<File2XMLFragmentProducer> fileProducers = new ArrayList<File2XMLFragmentProducer>();

    private final Logger log = Logger.getLogger(getClass().getName());

    private static final int BUFFER_SIZE = 100000;

    public void setFileProducers(List<File2XMLFragmentProducer> filenames) {
        if (filenames.size() == 0) {
            throw new IllegalArgumentException("No file to parse");
        }
        this.fileProducers = filenames;
    }

    @Override
    public void setArguments(FilterArguments fA, FilterByNames nH, TeiDocument doc) throws WrongArgsException {
        List<File2XMLFragmentProducer> filelist = new ArrayList<tei.cr.input.File2XMLFragmentProducer>();
        NodeList nodes = fA.getNodeList("*");
        if (nodes.getLength() == 0) {
            throw new WrongArgsException("No text file name provided");
        }
        log.info(nodes.getLength() + " entries (dir and file)");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node element = nodes.item(i);
            if (element.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String elementName = element.getNodeName();
            if (elementName.equals(fA.TEXT_DRIVER_DIR)) {
                String uri = fA.getTextNotNullOrEmpty(element, fA.TEXT_DRIVER_URI);
                log.info("directory: " + uri);
                String encoding = fA.getText(element, fA.TEXT_DRIVER_ENCODING);
                log.info("\tdirectory " + uri + "; encoding: " + encoding);
                String pattern = fA.getText(element, fA.TEXT_DRIVER_PATTERN);
                log.info("\tdirectory " + uri + "; pattern: " + pattern);
                String format = fA.getText(element, fA.TEXT_DRIVER_FORMAT);
                log.info("\tdirectory " + uri + "; format: " + format);
                boolean rec = fA.getBooleanValue(element, fA.TEXT_DRIVER_REC);
                log.info("\tdirectory " + uri + "; rec: " + rec);
                filelist.add(new Directory(uri, encoding, pattern, format, rec));
            } else if (elementName.equals(fA.TEXT_DRIVER_FILE)) {
                String uri = fA.getTextNotNullOrEmpty(element, fA.TEXT_DRIVER_URI);
                String encoding = fA.getText(element, fA.TEXT_DRIVER_ENCODING);
                log.info("\tfile " + uri + "; encoding: " + encoding);
                String format = fA.getText(element, fA.TEXT_DRIVER_FORMAT);
                log.info("\tfile " + uri + "; format: " + format);
                File f = new File(uri);
                if (!f.isFile()) {
                    throw new WrongArgsException(uri + " is not a file");
                }
                filelist.add(tei.cr.input.Util.getProducer(uri, encoding, format));
            } else {
                throw new WrongArgsException("Unknown element name: " + elementName);
            }
        }
        if (filelist.size() == 0) {
            throw new WrongArgsException("No text file matched the arguments provided");
        }
        setFileProducers(filelist);
        super.setArguments(fA, nH, doc);
    }

    @Override
    public void parse(InputSource input) throws SAXException, IOException {
        log.info("Number of files to be read: " + fileProducers.size());
        try {
            super.startPipeline();
            super.startDocument();
            super.startElement("", "teiCorpus", "teiCorpus", new AttributesImpl());
        } catch (SAXException e) {
            throw new FilterException(e);
        }
        for (int i = 0; i < fileProducers.size(); i++) {
            File2XMLFragmentProducer producer = fileProducers.get(i);
            producer.setContentHandler(this);
            producer.parse();
        }
        try {
            super.endElement("", "teiCorpus", "teiCorpus");
            super.endDocument();
            super.endPipeline();
        } catch (SAXException e) {
            throw new FilterException(e);
        }
    }

    @Override
    public void parse(String systemId) throws SAXException, IOException {
        parse(new InputSource(systemId));
    }
}
