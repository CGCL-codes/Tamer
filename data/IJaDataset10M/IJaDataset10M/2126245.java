package org.allcolor.xml.parser.dom;

import java.io.Serializable;
import org.allcolor.xml.parser.CStringBuilder;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * DOCUMENT ME!
 *
 * @author Quentin Anciaux
 */
public final class CComment extends ANode implements Comment, Serializable {

    static final long serialVersionUID = -7602861300357219969L;

    /** DOCUMENT ME! */
    private String data;

    /**
	 * Creates a new CComment object.
	 *
	 * @param data DOCUMENT ME!
	 * @param ownerDocument DOCUMENT ME!
	 */
    public CComment(String data, final ADocument ownerDocument) {
        super(ownerDocument);
        name = "#comment";
        prefix = null;
        localName = null;
        nameSpace = "  ";
        isDom1 = true;
        if (data == null) {
            data = "";
        }
        this.data = data;
    }

    public String getBaseURI() {
        return null;
    }

    public String getTextContent() throws DOMException {
        return getData();
    }

    public NamedNodeMap getAttributes() {
        return null;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param data DOCUMENT ME!
	 *
	 * @throws DOMException DOCUMENT ME!
	 */
    public void setData(final String data) throws DOMException {
        this.data = data;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws DOMException DOCUMENT ME!
	 */
    public String getData() throws DOMException {
        return data;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public int getLength() {
        return data.length();
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public short getNodeType() {
        return Node.COMMENT_NODE;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param nodeValue DOCUMENT ME!
	 *
	 * @throws DOMException DOCUMENT ME!
	 */
    public void setNodeValue(final String nodeValue) throws DOMException {
        setData(nodeValue);
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws DOMException DOCUMENT ME!
	 */
    public String getNodeValue() throws DOMException {
        return getData();
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param arg DOCUMENT ME!
	 *
	 * @throws DOMException DOCUMENT ME!
	 */
    public void appendData(String arg) throws DOMException {
        if (arg == null) {
            arg = "";
        }
        data = data + arg;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param offset DOCUMENT ME!
	 * @param count DOCUMENT ME!
	 *
	 * @throws DOMException DOCUMENT ME!
	 */
    public void deleteData(final int offset, final int count) throws DOMException {
        data = data.substring(0, offset) + data.substring(offset + count);
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param offset DOCUMENT ME!
	 * @param arg DOCUMENT ME!
	 *
	 * @throws DOMException DOCUMENT ME!
	 */
    public void insertData(final int offset, final String arg) throws DOMException {
        data = data.substring(0, offset) + data.substring(offset);
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param offset DOCUMENT ME!
	 * @param count DOCUMENT ME!
	 * @param arg DOCUMENT ME!
	 *
	 * @throws DOMException DOCUMENT ME!
	 */
    public void replaceData(final int offset, final int count, final String arg) throws DOMException {
        data = data.substring(0, offset) + arg + data.substring(offset + count);
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param offset DOCUMENT ME!
	 * @param count DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws DOMException DOCUMENT ME!
	 */
    public String substringData(final int offset, final int count) throws DOMException {
        return data.substring(offset, offset + count);
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public String toString() {
        CStringBuilder result = new CStringBuilder();
        result.append("<!--");
        result.append(getData());
        result.append("-->");
        return result.toString();
    }
}
