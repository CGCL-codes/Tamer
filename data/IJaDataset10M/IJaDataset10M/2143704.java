package uk.ac.ebi.intact.application.hierarchview.struts.taglibs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import uk.ac.ebi.intact.application.hierarchview.business.Constants;
import uk.ac.ebi.intact.application.hierarchview.business.IntactUserI;
import uk.ac.ebi.intact.application.hierarchview.business.graph.Network;
import uk.ac.ebi.intact.service.graph.Node;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * That class allow to initialize properly in the session the title of the graph.
 *
 * @author Nadin Neuhauser
 * @version $Id: DisplayGraphTitle.java 10934 2008-01-28 13:04:46Z baranda $
 * @since 1.6.0-SNAPSHOT
 */
public class DisplayGraphTitle extends TagSupport {

    private static final Log logger = LogFactory.getLog(DisplaySourceTag.class);

    /**
     * Skip the body content.
     */
    public int doStartTag() throws JspTagException {
        return SKIP_BODY;
    }

    /**
     * Called when the JSP encounters the end of a tag. This will create the option list.
     */
    public int doEndTag() throws JspException {
        HttpSession session = pageContext.getSession();
        try {
            IntactUserI user = (IntactUserI) session.getAttribute(Constants.USER_KEY);
            if (user == null) {
                logger.error("No existing session");
                return EVAL_PAGE;
            }
            Network in = user.getInteractionNetwork();
            if (in == null) {
                logger.error("No existing interaction network");
                return EVAL_PAGE;
            }
            StringBuffer queryToDisplay = new StringBuffer(256);
            StringBuffer additionalQueryToDisplay = new StringBuffer(256);
            StringBuffer infoToDisplay = null;
            StringBuffer errorToDisplay = null;
            if (in.getNodes() != null && in.getEdges() != null) {
                infoToDisplay = new StringBuffer(256);
                infoToDisplay.append("Result: ");
                infoToDisplay.append("<strong>").append(in.getNodes().size()).append("</strong> molecules, ");
                infoToDisplay.append("<strong>").append(in.getEdges().size()).append("</strong> interactions.");
            }
            session.setAttribute("infoToDisplay", infoToDisplay.toString());
            if (user.getMinePath() == null) {
                queryToDisplay.append("Query: ");
            } else {
                queryToDisplay.append("This is the minimal connecting network for ");
            }
            List<String> proteinURLs = new ArrayList<String>();
            queryToDisplay.append("<strong>");
            if (in.getCentralNodes() != null && !in.getCentralNodes().isEmpty()) {
                Collection<Node> centralNodes = in.getCentralNodes();
                Iterator<Node> iterator = centralNodes.iterator();
                int max = 6;
                while (iterator.hasNext()) {
                    StringBuffer proteinURL = new StringBuffer();
                    Node node = iterator.next();
                    proteinURL.append("<a href=\"");
                    proteinURL.append(user.getSearchUrl(node.getId(), false));
                    proteinURL.append("\" target=\"_blank\">");
                    proteinURL.append(node.getLabel());
                    proteinURL.append("</a>");
                    if (iterator.hasNext()) {
                        proteinURL.append(", ");
                    }
                    proteinURLs.add(proteinURL.toString());
                }
                String shortQuery = queryToDisplay.toString();
                for (int i = 0; i < max && i < proteinURLs.size(); i++) {
                    shortQuery += proteinURLs.get(i);
                }
                shortQuery += "</strong>";
                String longQuery = queryToDisplay.toString();
                for (String proteinURL : proteinURLs) {
                    longQuery += proteinURL;
                }
                longQuery += "</strong>";
                if (centralNodes.size() > max) {
                    String cmd = "var  w=window.open('" + user.getApplicationPath() + "/pages/queryWindow.jsp', " + "'DisplayQuery', " + "'width=1000,height=200,resizable=yes,scrollbars=yes'); " + "w.focus();";
                    shortQuery += "<a href=\"javascript:" + cmd + "\" > [ ... ] </a>";
                }
                session.setAttribute("queryToDisplay", shortQuery);
                session.setAttribute("additionalQueryToDisplay", longQuery);
            } else {
                String query = user.getQueryString();
                if (query == null) {
                    return EVAL_PAGE;
                }
                queryToDisplay.append(query);
                queryToDisplay.append("</strong>");
                session.setAttribute("queryToDisplay", queryToDisplay.toString());
                session.setAttribute("additionalQueryToDisplay", additionalQueryToDisplay.toString());
            }
            if (user.getErrorMessage() != null) {
                errorToDisplay = new StringBuffer();
                errorToDisplay.append("<strong><font color=\"red\">");
                errorToDisplay.append(user.getErrorMessage());
                errorToDisplay.append("</font></strong>");
                user.clearErrorMessage();
                session.setAttribute("errorToDisplay", errorToDisplay.toString());
            } else {
                session.removeAttribute("errorToDisplay");
            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
            throw new JspException("Fatal error: could not display protein associated source. <em>" + ioe + "</em>.");
        }
        return EVAL_PAGE;
    }
}
