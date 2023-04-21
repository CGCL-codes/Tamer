package org.exist.dom.memtree;

import org.exist.xquery.XPathException;
import org.exist.xquery.value.AtomicValue;
import org.exist.xquery.value.StringValue;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class CommentImpl extends NodeImpl implements Comment {

    /**
	 * @param doc
	 * @param nodeNumber
	 */
    public CommentImpl(DocumentImpl doc, int nodeNumber) {
        super(doc, nodeNumber);
    }

    public Node getFirstChild() {
        return null;
    }

    public String getStringValue() {
        return getData();
    }

    public String getLocalName() {
        return "";
    }

    public String getNamespaceURI() {
        return "";
    }

    public String getData() throws DOMException {
        return new String(document.characters, document.alpha[nodeNumber], document.alphaLen[nodeNumber]);
    }

    public AtomicValue atomize() throws XPathException {
        return new StringValue(getData());
    }

    public int getLength() {
        return getData().length();
    }

    public void setData(String arg0) throws DOMException {
    }

    public String substringData(int arg0, int arg1) throws DOMException {
        return null;
    }

    public void appendData(String arg0) throws DOMException {
    }

    public void insertData(int arg0, String arg1) throws DOMException {
    }

    public void deleteData(int arg0, int arg1) throws DOMException {
    }

    public void replaceData(int arg0, int arg1, String arg2) throws DOMException {
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("in-memory#");
        result.append("comment {");
        result.append(getData());
        result.append("} ");
        return result.toString();
    }
}
