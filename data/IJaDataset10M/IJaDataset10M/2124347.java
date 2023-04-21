package net.sf.syncopate.rc;

import java.io.StringBufferInputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import javax.xml.transform.TransformerException;
import com.sun.org.apache.xpath.internal.XPathAPI;
import net.sf.syncopate.util.xpath.XPath;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.tidy.Tidy;

public class DocumentInfo {

    @Language("Properties")
    private static final String ID_XPATH = "//*[@id = ''{0}'']";

    private static Document parseDocument(String html) {
        StringBufferInputStream in = new StringBufferInputStream(html);
        Tidy tidy = new Tidy();
        return tidy.parseDOM(in, null);
    }

    private int docId;

    @NotNull
    private Document doc;

    private Map<String, List<String>> xPaths;

    public DocumentInfo(int docId, Document doc) {
        this.docId = docId;
        this.doc = doc;
        xPaths = new HashMap<String, List<String>>();
    }

    public DocumentInfo(int docId, String html) {
        this(docId, parseDocument(html));
    }

    public int getDocumentId() {
        return docId;
    }

    @NotNull
    public Document getDocument() {
        return doc;
    }

    public List<String> geXPaths(String nodeId) {
        List<String> retVal = xPaths.get(nodeId);
        if (retVal == null) {
            String xPath = MessageFormat.format(ID_XPATH, nodeId);
            Element node;
            try {
                node = (Element) XPathAPI.selectSingleNode(doc, xPath);
            } catch (TransformerException e) {
                e.printStackTrace();
                return new ArrayList<String>();
            }
            Queue<XPath> paths = XPath.getXPaths(doc.getDocumentElement(), node);
            retVal = new ArrayList<String>(paths.size());
            for (XPath path : paths) {
                retVal.add(path.getDisplay());
            }
            xPaths.put(nodeId, retVal);
        }
        return retVal;
    }
}
