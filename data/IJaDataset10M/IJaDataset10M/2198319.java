package com.ecyrd.jspwiki.tags;

import java.io.IOException;
import java.util.StringTokenizer;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.jsp.JspWriter;
import mase.system.MaseSystem;
import mase.util.WikipageService;
import com.ecyrd.jspwiki.WikiPage;

/**
 
 */
public class WritePermissionBoxTag extends WikiTagBase {

    private static final long serialVersionUID = -4107359648231149486L;

    private static MaseSystem ms;

    static {
        try {
            Context ctx = new InitialContext();
            ms = (MaseSystem) ctx.lookup(WikipageService.jndiLookUp);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public final int doWikiStartTag() throws IOException {
        JspWriter out = pageContext.getOut();
        WikiPage thePage = m_wikiContext.getPage();
        StringTokenizer st = new StringTokenizer(thePage.getName(), ".");
        boolean isProjPart = false;
        if (st.hasMoreTokens()) {
            String prjName = st.nextToken();
            int type = ms.getWikiPageType("." + prjName);
            if ((type != 0) && (type != -1)) {
                isProjPart = true;
            }
        }
        out.print("<input name=\"writePermission\" type=\"radio\" value=\"0\" ");
        check(out, thePage, 0);
        out.print("/> Anonymous<br/>");
        out.print("<input name=\"writePermission\" type=\"radio\" value=\"1\" ");
        check(out, thePage, 1);
        out.print("/> Owner<br/>");
        out.print("<input name=\"writePermission\" type=\"radio\" value=\"2\" ");
        check(out, thePage, 2);
        out.print("/> Logged In<br/>");
        if (isProjPart) {
            out.print("<input name=\"writePermission\" type=\"radio\" value=\"3\" ");
            check(out, thePage, 3);
            out.print("/> Project Member<br/>");
            out.print("<input name=\"writePermission\" type=\"radio\" value=\"4\" ");
            check(out, thePage, 4);
            out.print("/> Project Manager");
        }
        return SKIP_BODY;
    }

    public void check(JspWriter out, WikiPage p, int val) throws IOException {
        if (p != null) {
            if (p.getWritePermission() == val) {
                out.print(" checked=\"checked\" ");
            }
        } else {
            String pageName = p.getName();
            int lastdot = pageName.lastIndexOf(WikiPage.PATH_SEPARATOR);
            if (lastdot > 0) {
                String parentName = pageName.substring(0, lastdot);
                WikiPage parent = m_wikiContext.getEngine().getPage(parentName);
                if (parent.getWritePermission() == val) {
                    out.print(" checked=\"checked\" ");
                }
            } else {
                if (val == 2) out.print(" checked=\"checked\" ");
            }
        }
    }
}
