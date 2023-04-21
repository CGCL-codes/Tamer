package de.uni_leipzig.lots.jsp.tag.install;

import de.uni_leipzig.lots.webfrontend.actions.InstallRedirectToStartAction;
import de.uni_leipzig.lots.webfrontend.app.SetupConfig;
import static de.uni_leipzig.lots.webfrontend.app.SetupConfig.Method;
import static de.uni_leipzig.lots.webfrontend.app.SetupConfig.State;
import de.uni_leipzig.lots.jsp.tag.AbstractTag;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Displays the sidebar navigation at the installation.
 *
 * @author Alexander Kiel
 * @version $Id: SideStepsTag.java,v 1.8 2007/10/23 06:30:28 mai99bxd Exp $
 */
public class SideStepsTag extends AbstractTag {

    @Override
    public int doStartTag() throws JspException {
        ServletResponse response = pageContext.getResponse();
        InstallRedirectToStartAction.DefaultPageData pageData = (InstallRedirectToStartAction.DefaultPageData) pageContext.findAttribute("pageData");
        SetupConfig.Method method = pageData.getSetupConfig().getMethod();
        SetupConfig.State state = pageData.getSetupConfig().getState();
        int i = 1;
        try {
            PrintWriter out = response.getWriter();
            out.write("<div class=\"box sidesteps\">\n<h2>");
            out.write(getMessage("leftPortlet.headline"));
            out.write("</h2>\n<ul>");
            printListItem(out, State.method, state, i++);
            if (method == Method.custom) {
                printListItem(out, State.database, state, i++);
                printListItem(out, State.content, state, i++);
            }
            printListItem(out, State.admin, state, i++);
            if (method == Method.custom) {
                printListItem(out, State.library, state, i++);
            }
            printListItem(out, State.mail, state, i++);
            printListItem(out, State.complete, state, i);
            out.write("</ul>\n</div>\n");
        } catch (IOException e) {
            throw new JspException("IOException while writing.", e);
        }
        return EVAL_PAGE;
    }

    private void printListItem(PrintWriter out, SetupConfig.State itemState, SetupConfig.State currentState, int index) {
        SetupConfig.State before = State.values()[itemState.ordinal() - 1];
        out.write("<li");
        if (currentState.compareTo(before) >= 0) {
            out.write(" class=\"");
            out.write(currentState.compareTo(before) >= 1 ? "ok" : "active");
            out.write("\"");
        }
        out.write(">");
        out.write(String.valueOf(index));
        out.write(". ");
        out.write(getMessage("leftPortlet." + itemState));
        out.write("</li>");
    }
}
