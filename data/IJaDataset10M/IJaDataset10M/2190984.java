package ru.adv.xml.formatter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import ru.adv.cache.Includable;
import ru.adv.cache.Include;
import ru.adv.io.InputOutputException;
import ru.adv.io.UnsupportedEncodingException;
import ru.adv.util.ErrorCodeException;
import ru.adv.xml.Result;

/**
 * This formatter is used to serialize into HTML content.
 *
 * @author <a href="mailto:support@adv.ru">ADV</a>
 * @version $Revision: 1.14 $
 */
public class HTMLFormatter extends Formatter implements Includable {

    private String encoding;

    private Transformer transformer;

    private boolean removeNsAttrs = true;

    public HTMLFormatter(String encoding) {
        this.encoding = encoding;
        if (encoding != null) {
            setContentType("text/html; charset=" + this.encoding);
        } else {
            setContentType("text/html");
        }
        Properties format = getOutputProperties(encoding);
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperties(format);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerFactoryConfigurationError e) {
            e.printStackTrace();
        }
        addFormatterCallback(new NamespaseRemoverCallback());
    }

    public boolean isRemoveNsAttrs() {
        return removeNsAttrs;
    }

    public void setRemoveNsAttrs(boolean removeNsAttrs) {
        this.removeNsAttrs = removeNsAttrs;
    }

    /**
	 * output properties for {@link Transformer} 
	 * @param encoding
	 * @return
	 */
    protected Properties getOutputProperties(String encoding) {
        Properties format = new Properties();
        format.put(OutputKeys.ENCODING, encoding);
        format.put(OutputKeys.METHOD, "html");
        format.put(OutputKeys.MEDIA_TYPE, "text/html");
        format.put(OutputKeys.INDENT, "no");
        format.put(OutputKeys.VERSION, "4.01");
        format.put(OutputKeys.OMIT_XML_DECLARATION, "yes");
        return format;
    }

    @Override
    public void setIndenting(boolean indenting) {
        super.setIndenting(indenting);
        transformer.setOutputProperty(OutputKeys.INDENT, isIndenting() ? "yes" : "no");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
    }

    public void format(Result result, OutputStream out) throws InputOutputException {
        format(result.getDocument(), out);
    }

    public void format(Document document, OutputStream out) throws InputOutputException {
        try {
            OutputStreamWriter outWriter = new OutputStreamWriter(out, getEncoding());
            format(document, outWriter);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new UnsupportedEncodingException(encoding);
        }
    }

    public void format(Document document, Writer outWriter) throws InputOutputException {
        try {
            if (getDocTypePrefix() != null) {
                outWriter.write(getDocTypePrefix() + "\n");
            }
            formatNode(document, outWriter);
        } catch (IOException e) {
            throw new InputOutputException(e, (String) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void format(DocumentFragment frag, OutputStream outstr) throws InputOutputException {
        OutputStreamWriter outWriter;
        try {
            outWriter = new OutputStreamWriter(outstr, getEncoding());
        } catch (java.io.UnsupportedEncodingException e) {
            throw new UnsupportedEncodingException(getEncoding());
        }
        format(frag, outWriter);
    }

    public void format(DocumentFragment frag, Writer outWriter) throws InputOutputException {
        try {
            formatNode(frag, outWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void formatNode(Node node, Writer outWriter) throws Exception {
        applayFomatterCallbacks(node);
        transformer.transform(new DOMSource(node), new StreamResult(outWriter));
    }

    public String getEncoding() {
        return encoding;
    }

    /**
	 * 
	 * Callback to remove namespaces
	 * 
	 * @author vic
	 *
	 */
    private final class NamespaseRemoverCallback implements FormatterCallback {

        @Override
        public boolean formatNode(Node node) throws ErrorCodeException {
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element elem = (Element) node;
                if ("meta".equalsIgnoreCase(elem.getLocalName()) && elem.getParentNode() != null && "Content-Type".equalsIgnoreCase(elem.getAttribute("http-equiv"))) {
                    elem.getParentNode().removeChild(elem);
                    return false;
                }
                if (isRemoveNsAttrs()) {
                    removeNamespaceAttrs(elem);
                }
            }
            return true;
        }

        private void removeNamespaceAttrs(Element elem) {
            NamedNodeMap attrs = elem.getAttributes();
            for (int i = attrs.getLength() - 1; i >= 0; i--) {
                Attr attr = (Attr) attrs.item(i);
                if (attr.getNamespaceURI() != null) {
                    elem.removeAttributeNode(attr);
                }
            }
        }

        @Override
        public void addInclude(Include include) {
        }

        @Override
        public void addIncludes(Collection<Include> includes) {
        }

        @Override
        public Set<Include> getIncludes() {
            return Collections.emptySet();
        }
    }
}
