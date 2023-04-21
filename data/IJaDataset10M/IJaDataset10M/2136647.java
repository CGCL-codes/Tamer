package tei.cr.utils.sax.occurrence;

import java.util.logging.Logger;
import org.jaxen.JaxenException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import tei.cr.filters.FilterException;
import tei.cr.filters.ReceiveOccurrence;
import tei.cr.pipeline.AbstractBufferingBase;
import tei.cr.utils.sax.eventFilter.XPathEventFilter;
import tei.cr.utils.stax.StAXBuffer;
import tei.cr.utils.stax.StAXNode;
import tei.cr.utils.stax.WellFormednessException;
import tei.cr.utils.stax.jaxen.StAXXPath;

public class XPathValueOccurrencePattern extends AbstractBufferingBase implements OccurrencePattern {

    public static final String patternName = "evaluate";

    private Logger log = Logger.getLogger(getClass().getName());

    private final XPathEventFilter rootElXpathFilter;

    private boolean isInElement;

    private int depth;

    private StAXXPath queryTree = null;

    protected ReceiveOccurrence target;

    protected String prefix;

    protected boolean usePrefix;

    protected XPathEventFilter match;

    protected boolean useMatch;

    public void setReceiveOccurrence(ReceiveOccurrence target) {
        this.target = target;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        this.usePrefix = true;
    }

    public void setMatch(XPathEventFilter match) {
        this.match = match;
        this.useMatch = true;
    }

    public XPathValueOccurrencePattern(String useXpath, String rootElXpath) {
        super();
        setReceiveOccurrence(target);
        try {
            queryTree = new StAXXPath(useXpath);
        } catch (JaxenException e) {
            IllegalArgumentException iAE = new IllegalArgumentException("Cannot instanciate XPath pattern: " + e.getMessage());
            iAE.initCause(e);
            throw iAE;
        }
        try {
            rootElXpathFilter = new XPathEventFilter(rootElXpath);
        } catch (JaxenException e) {
            IllegalArgumentException iAE = new IllegalArgumentException("Cannot instanciate XPath pattern: " + e.getMessage());
            iAE.initCause(e);
            throw iAE;
        }
    }

    public void startElement(String uri, String localName, String qName, Attributes att) throws SAXException {
        if (!isInElement) {
            if (!rootElXpathFilter.accept(uri, localName, qName, att)) {
                return;
            }
            isInElement = true;
            depth = 0;
            super.startBufferising();
        } else {
            depth++;
        }
        super.startElement(uri, localName, qName, att);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (isInElement) {
            if (depth == 0) {
                isInElement = false;
                StAXBuffer xmlFragment = endBufferising();
                StAXNode treeRoot;
                try {
                    treeRoot = xmlFragment.getTreeRoot();
                } catch (WellFormednessException wFE) {
                    log.warning("State of the sub tree before error: " + xmlFragment.toString());
                    throw new FilterException("The sub tree is not well formed.", wFE);
                }
                Object phenomenon;
                try {
                    phenomenon = queryTree.evaluate(treeRoot);
                } catch (JaxenException jE) {
                    throw new FilterException("The XPath engine encouter error while applying XPath expression on sub tree." + jE);
                }
                target.occurrence(phenomenon);
            } else {
                depth--;
            }
        }
    }
}
