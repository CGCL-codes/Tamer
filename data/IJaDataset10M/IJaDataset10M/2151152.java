package org.illico.web.widget.common;

import org.illico.common.html.Node;
import org.illico.common.html.NodeAppender;
import org.illico.common.html.ScriptStringAppendable;
import org.illico.common.text.TextAppendable;
import org.illico.common.text.TextUtils;
import org.illico.common.text.appender.Appender;
import org.illico.common.util.InstanceName;
import org.illico.common.widget.Widget;
import org.illico.web.common.Context;
import org.illico.web.common.RpcUtils;

public abstract class AbstractAppender<W extends Widget> implements Appender<W> {

    private static final String APPEND_PARTIAL_PARENT_NODE_ATTRIBUTE_NAME = AbstractAppender.class.getName() + ".appendPartialParentNode";

    private NodeAppender getHtmlNodeAppender() {
        return (NodeAppender) TextUtils.getAppenderFactory().getSingleton(new InstanceName<Node>(Node.class));
    }

    protected abstract void appendWidget(TextAppendable appendable, W widget) throws Exception;

    protected void appendStart(Appendable appendable, Node node) throws Exception {
        getHtmlNodeAppender().appendStart(appendable, node);
    }

    protected void appendEnd(Appendable appendable, Node node) throws Exception {
        getHtmlNodeAppender().appendEnd(appendable, node);
    }

    protected Node getPartialParentNode() {
        return null;
    }

    public final void append(TextAppendable appendable, W widget) throws Exception {
        Object contextAppendPartialParentNode = Context.isInitialized() ? Context.getAttribute(APPEND_PARTIAL_PARENT_NODE_ATTRIBUTE_NAME) : new Object();
        try {
            Node partialParentNode = getPartialParentNode();
            boolean appendPartialParentNode;
            if (contextAppendPartialParentNode != null) {
                appendPartialParentNode = true;
            } else {
                Context.setAttribute(APPEND_PARTIAL_PARENT_NODE_ATTRIBUTE_NAME, new Object());
                appendPartialParentNode = RpcUtils.isFullRequest();
            }
            appendPartialParentNode = appendPartialParentNode && partialParentNode != null;
            if (appendPartialParentNode) {
                partialParentNode.addAttribute("id", widget.getName());
                appendStart(appendable, partialParentNode);
            }
            if (RpcUtils.isFullRequest()) {
                appendWidget(appendable, widget);
            } else {
                appendable.append("setInnerHtml('");
                appendable.append(widget.getName());
                appendable.append("', '");
                appendWidget(new ScriptStringAppendable(appendable), widget);
                appendable.append("');");
            }
            if (appendPartialParentNode) {
                appendEnd(appendable, partialParentNode);
            }
        } finally {
            if (Context.isInitialized()) {
                Context.setAttribute(APPEND_PARTIAL_PARENT_NODE_ATTRIBUTE_NAME, null);
            }
        }
    }
}
