package org.eclipse.ufacekit.core.databinding.sse.dom.xpath;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import org.eclipse.core.databinding.observable.IObserving;
import org.eclipse.ufacekit.core.databinding.instance.observable.ILazyObserving;
import org.w3c.dom.Node;

public abstract class AbstractXPathObserving implements ILazyObserving {

    private Node root;

    private XPathExpression xPathExpression;

    private boolean lazy = false;

    public AbstractXPathObserving(Node root, String xpath, boolean lazy) throws XPathExpressionException {
        this(root, xpath, null, lazy);
    }

    public AbstractXPathObserving(Node root, String xpath, NamespaceContext namespaceContext, boolean lazy) throws XPathExpressionException {
        init(root, createXPathExpression(xpath, namespaceContext), lazy);
    }

    public AbstractXPathObserving(Node root, XPathExpression xPathExpression, boolean lazy) {
        init(root, xPathExpression, lazy);
    }

    private void init(Node root, XPathExpression xPathExpression, boolean lazy) {
        this.root = root;
        this.xPathExpression = xPathExpression;
        this.lazy = lazy;
    }

    public Object getObserved() {
        try {
            if (lazy == false) {
                return xPathExpression.evaluate(root, XPathConstants.NODE);
            } else {
                Object result = xPathExpression.evaluate(root, XPathConstants.NODE);
                if (result == null) {
                }
                return result;
            }
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract XPathExpression createXPathExpression(String expression, NamespaceContext namespaceContext) throws XPathExpressionException;
}
