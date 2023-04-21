package pub.servlets.summary;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import pub.utils.Log;
import pub.db.*;
import pub.tree.*;
import pub.servlets.*;
import pub.beans.*;
import pub.utils.StringUtils;
import pub.utils.Graph;

public class Term_Graph implements RequestHandler {

    private PubConnection conn;

    private RequestHandlerState state;

    public void handleRequest(RequestHandlerState state) throws IOException, ServletException {
        this.conn = state.getConn();
        this.state = state;
        TermBean termBean = (TermBean) pub.beans.BeanFactory.getBean(conn, "" + state.getAttribute("key"));
        if (!isRootedTerm(termBean)) {
            return;
        }
        Writer writer = state.getWriter();
        if (termBean.isNull()) {
            writer.write("Cannot product term graph for the null bean.  Please check accession: " + state.getAttribute("key"));
        } else {
            try {
                writeTermTree(state.getRequest(), termBean, writer, state);
            } catch (TermTable.LookupException e) {
                writer.write("Cannot find term: " + e);
            }
        }
    }

    private boolean isRootedTerm(TermBean termBean) {
        return (!termBean.getRootTerm().isNull());
    }

    private void writeTermTree(HttpServletRequest req, TermBean termBean, Writer writer, RequestHandlerState state) throws TermTable.LookupException {
        Graph graph = (Graph) state.getResourceManager().get("pub.db.Term2TermTable.Graph");
        TermTreeBrowser browser = new TermTreeBrowser(findRoot(termBean), conn, graph);
        revealTermAndAncestors(browser, browser.find(termBean.getPubAccession()));
        NodeRenderer nodeRenderer = new TermNodeRenderer(state.getBaseContextPath(), writer, termBean);
        TreeRenderer treeRenderer = new HtmlTableTreeRenderer(state.getBaseContextPath(), writer, nodeRenderer);
        treeRenderer.process(browser.iterator());
    }

    private void revealTermAndAncestors(TreeBrowser browser, Node node) {
        browser.reveal(node);
        Node[] parents = node.parents();
        for (int i = 0; i < parents.length; i++) {
            revealTermAndAncestors(browser, parents[i]);
        }
    }

    private TermBean findRoot(TermBean termBean) throws TermTable.LookupException {
        return termBean.getRootTerm();
    }
}
