package writer2latex.epub;

import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import writer2latex.api.ContentEntry;
import writer2latex.api.ConverterResult;
import writer2latex.util.Misc;
import writer2latex.xmerge.DOMDocument;

/** This class creates the required NXC file for an EPUB document
 *  (see http://www.idpf.org/2007/opf/OPF_2.0_final_spec.html#Section2.4).
 */
public class NCXWriter extends DOMDocument {

    public NCXWriter(ConverterResult cr, String sUUID) {
        super("book", "ncx");
        Document contentDOM = null;
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            DOMImplementation domImpl = builder.getDOMImplementation();
            DocumentType doctype = domImpl.createDocumentType("ncx", "", "");
            contentDOM = domImpl.createDocument("http://www.daisy.org/z3986/2005/ncx/", "ncx", doctype);
        } catch (ParserConfigurationException t) {
            throw new RuntimeException(t);
        }
        Element ncx = contentDOM.getDocumentElement();
        ncx.setAttribute("version", "2005-1");
        ncx.setAttribute("xml:lang", cr.getMetaData().getLanguage());
        ncx.setAttribute("xmlns", "http://www.daisy.org/z3986/2005/ncx/");
        Element head = contentDOM.createElement("head");
        ncx.appendChild(head);
        Element uid = contentDOM.createElement("meta");
        uid.setAttribute("name", "dtb:uid");
        uid.setAttribute("content", sUUID);
        head.appendChild(uid);
        Element depth = contentDOM.createElement("meta");
        depth.setAttribute("name", "dtb:depth");
        head.appendChild(depth);
        Element totalPageCount = contentDOM.createElement("meta");
        totalPageCount.setAttribute("name", "dtb:totalPageCount");
        totalPageCount.setAttribute("content", "0");
        head.appendChild(totalPageCount);
        Element maxPageNumber = contentDOM.createElement("meta");
        maxPageNumber.setAttribute("name", "dtb:maxPageNumber");
        maxPageNumber.setAttribute("content", "0");
        head.appendChild(maxPageNumber);
        Element docTitle = contentDOM.createElement("docTitle");
        ncx.appendChild(docTitle);
        Element docTitleText = contentDOM.createElement("text");
        docTitle.appendChild(docTitleText);
        docTitleText.appendChild(contentDOM.createTextNode(cr.getMetaData().getTitle()));
        Element navMap = contentDOM.createElement("navMap");
        ncx.appendChild(navMap);
        Element currentContainer = ncx;
        int nCurrentLevel = 0;
        int nCurrentEntryLevel = 0;
        int nDepth = 0;
        int nPlayOrder = 0;
        Iterator<ContentEntry> content = cr.getContent().iterator();
        while (content.hasNext()) {
            ContentEntry entry = content.next();
            int nEntryLevel = Math.max(entry.getLevel(), 1);
            if (nEntryLevel < nCurrentLevel) {
                for (int i = nEntryLevel; i < nCurrentLevel; i++) {
                    currentContainer = (Element) currentContainer.getParentNode();
                }
                nCurrentLevel = nEntryLevel;
            } else if (nEntryLevel > nCurrentEntryLevel) {
                currentContainer = (Element) currentContainer.getLastChild();
                nCurrentLevel++;
            }
            nCurrentEntryLevel = nEntryLevel;
            Element navPoint = contentDOM.createElement("navPoint");
            navPoint.setAttribute("playOrder", Integer.toString(++nPlayOrder));
            navPoint.setAttribute("id", "text" + nPlayOrder);
            currentContainer.appendChild(navPoint);
            Element navLabel = contentDOM.createElement("navLabel");
            navPoint.appendChild(navLabel);
            Element navLabelText = contentDOM.createElement("text");
            navLabel.appendChild(navLabelText);
            navLabelText.appendChild(contentDOM.createTextNode(entry.getTitle()));
            Element navPointContent = contentDOM.createElement("content");
            String sHref = Misc.makeHref(entry.getFile().getFileName());
            if (entry.getTarget() != null) {
                sHref += "#" + entry.getTarget();
            }
            navPointContent.setAttribute("src", sHref);
            navPoint.appendChild(navPointContent);
            nDepth = Math.max(nDepth, nCurrentLevel);
        }
        if (nDepth == 0) {
        }
        depth.setAttribute("content", Integer.toString(nDepth));
        setContentDOM(contentDOM);
    }
}
