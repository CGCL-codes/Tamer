package pt.utl.ist.lucene.treceval.handlers.topics;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.xml.sax.InputSource;
import pt.utl.ist.lucene.QueryConfiguration;
import pt.utl.ist.lucene.treceval.ISearchCallBack;
import pt.utl.ist.lucene.treceval.Globals;
import pt.utl.ist.lucene.treceval.SearchConfiguration;
import pt.utl.ist.lucene.treceval.handlers.IdMap;
import pt.utl.ist.lucene.treceval.handlers.ResourceHandler;
import pt.utl.ist.lucene.treceval.handlers.topics.output.OutputFormat;
import pt.utl.ist.lucene.treceval.handlers.topics.output.OutputFormatFactory;
import pt.utl.ist.lucene.treceval.handlers.topics.output.Topic;
import pt.utl.ist.lucene.treceval.handlers.topics.output.impl.FieldsTopic;
import pt.utl.ist.lucene.utils.Dom4jUtil;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Properties;

/**
 * Handles a XML Document and select all resources from it invoking the handler for each one
 *
 * @author Jorge Machado
 * @date 21/Ago/2008
 * @see pt.utl.ist.lucene.treceval.handlers.adhoc
 */
public class TXmlHandler implements TDocumentHandler {

    private static final Logger logger = Logger.getLogger(TXmlHandler.class);

    public void handle(OutputFormatFactory factory, InputStream stream, String fromFile, ResourceHandler handler, ISearchCallBack callBack, Properties filehandlers, String confId, String run, String collection, String outputDir, QueryConfiguration topicsConfiguration) throws IOException {
        InputSource source = new InputSource(stream);
        try {
            Document dom = Dom4jUtil.parse(source);
            run(factory, dom, fromFile, handler, callBack, confId, run, collection, outputDir, topicsConfiguration);
        } catch (DocumentException e) {
            logger.error(e, e);
        } catch (MalformedURLException e) {
            logger.error(e, e);
        } catch (IOException e) {
            logger.error(e, e);
        }
    }

    public void run(OutputFormatFactory factory, Document dom, String fromFile, ResourceHandler handler, ISearchCallBack callBack, String confId, String run, String collection, String outputDir, QueryConfiguration topicsConfiguration) throws IOException {
        FileOutputStream outputStream;
        try {
            String runName;
            if (Globals.RUN_OUTPUT_FILE == null) runName = outputDir + "/" + confId; else runName = Globals.RUN_OUTPUT_FILE;
            outputStream = new FileOutputStream(runName);
        } catch (FileNotFoundException e) {
            logger.error(e, e);
            return;
        }
        OutputFormat outputFormat = factory.createNew(outputStream);
        XPath resourceXpath = dom.createXPath(handler.getResourceXpath());
        List<Element> resources = resourceXpath.selectNodes(dom.getRootElement());
        for (Element element : resources) {
            try {
                IdMap idMap = handler.handle(element);
                Topic topic = new FieldsTopic(idMap.getId(), idMap.getTextFields(), outputFormat, topicsConfiguration);
                callBack.searchCallback(topic);
            } catch (IOException e) {
                throw e;
            }
        }
        outputFormat.close();
        outputStream.flush();
        outputStream.close();
    }
}
