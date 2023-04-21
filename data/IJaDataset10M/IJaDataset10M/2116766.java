package net.sf.saxon.tinytree;

import net.sf.saxon.event.Receiver;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.type.Type;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.om.SingletonIterator;
import net.sf.saxon.value.StringValue;
import net.sf.saxon.value.Value;

/**
  * TinyCommentImpl is an implementation of CommentInfo
  * @author Michael H. Kay
  */
final class TinyCommentImpl extends TinyNodeImpl {

    public TinyCommentImpl(TinyTree tree, int nodeNr) {
        this.tree = tree;
        this.nodeNr = nodeNr;
    }

    /**
    * Get the XPath string value of the comment
    */
    public final String getStringValue() {
        int start = tree.alpha[nodeNr];
        int len = tree.beta[nodeNr];
        if (len == 0) return "";
        char[] dest = new char[len];
        tree.commentBuffer.getChars(start, start + len, dest, 0);
        return new String(dest, 0, len);
    }

    /**
     * Get the typed value of this node.
     * Returns the string value, as an instance of xs:string
     */
    public SequenceIterator getTypedValue() {
        return SingletonIterator.makeIterator(new StringValue(getStringValue()));
    }

    /**
     * Get the typed value of this node.
     * Returns the string value, as an instance of xs:string
     */
    public Value atomize() {
        return new StringValue(getStringValue());
    }

    /**
    * Get the node type
    * @return Type.COMMENT
    */
    public final int getNodeKind() {
        return Type.COMMENT;
    }

    /**
    * Copy this node to a given outputter
    */
    public void copy(Receiver out, int whichNamespaces, boolean copyAnnotations, int locationId) throws XPathException {
        out.comment(getStringValue(), 0, 0);
    }
}
