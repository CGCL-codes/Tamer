package pt.utl.ist.lucene.treceval.handlers.topics;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import pt.utl.ist.lucene.QueryConfiguration;
import pt.utl.ist.lucene.treceval.ISearchCallBack;
import pt.utl.ist.lucene.treceval.SearchConfiguration;
import pt.utl.ist.lucene.treceval.handlers.ResourceHandler;
import pt.utl.ist.lucene.treceval.handlers.collections.CXmlHandler;
import pt.utl.ist.lucene.treceval.handlers.topics.output.OutputFormatFactory;
import pt.utl.ist.lucene.treceval.util.EscapeChars;
import pt.utl.ist.lucene.utils.Dom4jUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Jorge Machado
 * @date 21/Ago/2008
 * @see pt.utl.ist.lucene.treceval.handlers
 */
public class TFlatSgmlHandler implements TDocumentHandler {

    private static final Logger logger = Logger.getLogger(CXmlHandler.class);

    public void handle(OutputFormatFactory factory, InputStream stream, String fromFile, ResourceHandler handler, ISearchCallBack callBack, Properties filehandlers, String confId, String run, String collection, String outputDir, QueryConfiguration topicsConfiguration) throws IOException {
        String xml = getXmlRootedDocument(stream);
        try {
            Document dom = Dom4jUtil.parse(xml);
            new TXmlHandler().run(factory, dom, fromFile, handler, callBack, confId, run, collection, outputDir, topicsConfiguration);
        } catch (DocumentException e) {
            logger.error(e, e);
        }
    }

    private String getXmlRootedDocument(InputStream stream) throws IOException {
        byte[] buffer = new byte[1024];
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\"?><docs>");
        int length;
        while ((length = stream.read(buffer, 0, 1024)) != -1) {
            String newStr = EscapeChars.forXMLOnlySpecialInternal(new String(buffer, 0, length));
            builder.append(newStr);
        }
        builder.append("</docs>");
        return builder.toString();
    }
}
