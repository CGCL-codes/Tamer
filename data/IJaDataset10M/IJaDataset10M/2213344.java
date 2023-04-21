package abbot.swt.wtp.dom;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import abbot.swt.finder.NotFoundException;
import abbot.swt.matcher.Matcher;

public class DOMNodeFinderImpl implements DOMNodeFinder {

    protected static final int DEFAULT_WHAT_TO_SHOW = NodeFilter.SHOW_ELEMENT;

    protected static final NodeFilter DEFAULT_NODE_FILTER = null;

    protected static final boolean DEFAULT_ENTITY_REFERENCE_EXPANSION = false;

    protected final Document document;

    protected final int whatToShow;

    protected final NodeFilter filter;

    protected final boolean entityReferenceExpansion;

    public DOMNodeFinderImpl(Document document) {
        this(document, DEFAULT_WHAT_TO_SHOW, DEFAULT_NODE_FILTER, DEFAULT_ENTITY_REFERENCE_EXPANSION);
    }

    public DOMNodeFinderImpl(Document document, int whatToShow) {
        this(document, whatToShow, DEFAULT_NODE_FILTER, DEFAULT_ENTITY_REFERENCE_EXPANSION);
    }

    public DOMNodeFinderImpl(Document document, int whatToShow, NodeFilter filter, boolean entityReferenceExpansion) {
        this.document = document;
        this.whatToShow = whatToShow;
        this.filter = filter;
        this.entityReferenceExpansion = entityReferenceExpansion;
    }

    public Node find(Matcher<Node> matcher) throws NotFoundException {
        return find(document, matcher);
    }

    public Node find(Node root, Matcher<Node> matcher) throws NotFoundException {
        NodeIterator iter = ((DocumentTraversal) document).createNodeIterator(root, whatToShow, filter, entityReferenceExpansion);
        for (; ; ) {
            Node node = iter.nextNode();
            if (node == null) throw new NotFoundException("Nothing found using " + matcher);
            if (matcher.matches(node)) return node;
        }
    }
}
