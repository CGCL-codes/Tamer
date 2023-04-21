package org.jsserv.example.tag;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import org.jsserv.example.control.Node;

/**
 * Tag der einen DndTree server-seitig rendert. Dies muss nicht unbedingt
 * schneller sein(!) und ist auf jeden Fall mit einem höheren Datenaufkommen
 * verbunden. Was in der einzelnen Situation besser ist, muss man durch
 * Ausprobieren herausfinden.
 */
public class TreeRenderTag extends TagSupport {

    private static final long serialVersionUID = 2703195872866004154L;

    /**
     * Name, unter dem die Baumdaten in einem der Kontexte verfügbar sind.
     */
    private String var, context;

    public void setVar(String var) {
        this.var = var;
    }

    @Override
    public void setPageContext(PageContext pageContext) {
        super.setPageContext(pageContext);
        context = ((HttpServletRequest) pageContext.getRequest()).getContextPath();
    }

    private String url(String uri) {
        return context + uri;
    }

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        try {
            Node n = null;
            try {
                n = (Node) pageContext.findAttribute(var);
            } catch (ClassCastException e) {
            }
            if (n == null) throw new JspException("no tree");
            out.print("<ul id=\"" + n.getId() + "\" class=\"tree prerendered\">");
            for (Node c : n.getChildren()) {
                renderNode(n, c, out);
            }
            out.print("</ul>");
        } catch (IOException e) {
            throw new JspException(e);
        }
        return SKIP_BODY;
    }

    private void renderNode(Node rootNode, Node node, JspWriter out) throws IOException {
        out.print("<li id=\"" + node.getId() + "\">");
        if (node.getChildren().length > 0) {
            out.print("<img class=\"collapse\" alt=\"+/-\" src=\"" + url("/image/spacer.png") + "\" /> ");
        }
        if (node.getLink() != null) {
            out.print("<a href=\"" + node.getLink() + "\" class=\"ext\">");
        }
        out.print(node.getName());
        if (node.getLink() != null) {
            out.print("</a>");
        }
        Node[] children = node.getChildren();
        if (children.length > 0) {
            out.print("<ul>");
            for (Node child : children) {
                renderNode(rootNode, child, out);
            }
            out.print("</ul>");
        }
        out.print("</li>");
    }
}
