package org.itsnat.impl.core.markup.render;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;
import java.util.Vector;
import org.apache.xml.serialize.ElementState;
import org.apache.xml.serialize.HTMLdtd;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XHTMLSerializer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * @author jmarranz
 */
public class ItsNatXercesXHTMLSerializer extends XHTMLSerializer {

    protected boolean _xhtml = true;

    protected String fUserXHTMLNamespace = null;

    /**
     * Creates a new instance of ItsNatXercesXHTMLSerializer
     */
    public ItsNatXercesXHTMLSerializer(Writer writer, OutputFormat format) {
        super(writer, format);
    }

    public static boolean isNotXHTMLNamespace(String nameSpace) {
        return ((nameSpace != null) && !nameSpace.equals(XHTMLNamespace));
    }

    public static boolean isNotXHTMLNamespace(Attr attr) {
        String nameSpace = attr.getNamespaceURI();
        if (nameSpace == null) {
            Element elem = attr.getOwnerElement();
            nameSpace = elem.getNamespaceURI();
        }
        return isNotXHTMLNamespace(nameSpace);
    }

    public void serializeElement(Element elem) throws IOException {
        Attr attr;
        NamedNodeMap attrMap;
        int i;
        Node child;
        ElementState state;
        boolean preserveSpace;
        String name;
        String value;
        String tagName;
        tagName = elem.getTagName();
        state = getElementState();
        if (isDocumentState()) {
            if (!_started) startDocument(tagName);
        } else {
            if (state.empty) _printer.printText('>');
            if (_indenting && !state.preserveSpace && (state.empty || state.afterElement)) _printer.breakLine();
        }
        preserveSpace = state.preserveSpace;
        _printer.printText('<');
        if (_xhtml) {
            if (isNotXHTMLNamespace(elem.getNamespaceURI())) _printer.printText(tagName); else _printer.printText(tagName.toLowerCase(Locale.ENGLISH));
        } else _printer.printText(tagName);
        _printer.indent();
        attrMap = elem.getAttributes();
        if (attrMap != null) {
            for (i = 0; i < attrMap.getLength(); ++i) {
                attr = (Attr) attrMap.item(i);
                if (isNotXHTMLNamespace(attr)) name = attr.getName(); else name = attr.getName().toLowerCase(Locale.ENGLISH);
                value = attr.getValue();
                if (attr.getSpecified()) {
                    _printer.printSpace();
                    if (_xhtml) {
                        if (value == null) {
                            _printer.printText(name);
                            _printer.printText("=\"\"");
                        } else {
                            _printer.printText(name);
                            _printer.printText("=\"");
                            printEscaped(value);
                            _printer.printText('"');
                        }
                    } else {
                        if (value == null) {
                            value = "";
                        }
                        if (!_format.getPreserveEmptyAttributes() && value.length() == 0) _printer.printText(name); else if (HTMLdtd.isURI(tagName, name)) {
                            _printer.printText(name);
                            _printer.printText("=\"");
                            _printer.printText(escapeURI(value));
                            _printer.printText('"');
                        } else if (HTMLdtd.isBoolean(tagName, name)) _printer.printText(name); else {
                            _printer.printText(name);
                            _printer.printText("=\"");
                            printEscaped(value);
                            _printer.printText('"');
                        }
                    }
                }
            }
        }
        if (HTMLdtd.isPreserveSpace(tagName)) preserveSpace = true;
        if (elem.hasChildNodes() || !HTMLdtd.isEmptyTag(tagName)) {
            state = enterElementState(null, null, tagName, preserveSpace);
            if (tagName.equalsIgnoreCase("A") || tagName.equalsIgnoreCase("TD")) {
                state.empty = false;
                _printer.printText('>');
            }
            if (tagName.equalsIgnoreCase("SCRIPT") || tagName.equalsIgnoreCase("STYLE")) {
                if (_xhtml) {
                    state.doCData = true;
                } else {
                    state.unescaped = true;
                }
            }
            child = elem.getFirstChild();
            while (child != null) {
                serializeNode(child);
                child = child.getNextSibling();
            }
            endElementIO(elem.getNamespaceURI(), null, tagName);
        } else {
            _printer.unindent();
            if (_xhtml) _printer.printText(" />"); else _printer.printText('>');
            state.afterElement = true;
            state.empty = false;
            if (isDocumentState()) _printer.flush();
        }
    }

    public void endElementIO(String namespaceURI, String localName, String rawName) throws IOException {
        ElementState state;
        String htmlName;
        _printer.unindent();
        state = getElementState();
        if (state.namespaceURI == null || state.namespaceURI.length() == 0) htmlName = state.rawName; else {
            if (state.namespaceURI.equals(XHTMLNamespace) || (fUserXHTMLNamespace != null && fUserXHTMLNamespace.equals(state.namespaceURI))) htmlName = state.localName; else htmlName = null;
        }
        if (_xhtml) {
            if (state.empty) {
                _printer.printText(" />");
            } else {
                if (state.inCData) _printer.printText("]]>");
                _printer.printText("</");
                if (isNotXHTMLNamespace(namespaceURI)) _printer.printText(rawName); else _printer.printText(state.rawName.toLowerCase(Locale.ENGLISH));
                _printer.printText('>');
            }
        } else {
            if (state.empty) _printer.printText('>');
            if (htmlName == null || !HTMLdtd.isOnlyOpening(htmlName)) {
                if (_indenting && !state.preserveSpace && state.afterElement) _printer.breakLine();
                if (state.inCData) _printer.printText("]]>");
                _printer.printText("</");
                _printer.printText(state.rawName);
                _printer.printText('>');
            }
        }
        state = leaveElementState();
        if (htmlName == null || (!htmlName.equalsIgnoreCase("A") && !htmlName.equalsIgnoreCase("TD"))) state.afterElement = true;
        state.empty = false;
        if (isDocumentState()) _printer.flush();
    }

    public void comment(String text) throws IOException {
        int index;
        ElementState state;
        if (_format.getOmitComments()) return;
        state = content();
        index = text.indexOf("-->");
        if (index >= 0) fStrBuffer.append("<!--").append(text.substring(0, index)).append("-->"); else fStrBuffer.append("<!--").append(text).append("-->");
        {
            if (_indenting && !state.preserveSpace) _printer.breakLine();
            _printer.indent();
            printText(fStrBuffer.toString(), true, true);
            _printer.unindent();
            if (_indenting) state.afterElement = true;
        }
        fStrBuffer.setLength(0);
        state.afterComment = true;
        state.afterElement = false;
    }
}
