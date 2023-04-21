package com.ma_la.CustomTags;

import javax.servlet.jsp.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author mala
 * Date: 30.03.2002
 * @version 1.5
 * Package: com.ma_la.CustomTags
 */
public class LinkGenerator extends FormFields {

    private String strAim = "";

    private String strTarget = "";

    private String strTextLink = "";

    private String strImageName = "";

    private String strOnMouseOver = "";

    private int intExternal = 0;

    private int intShowLinkSymbol = 1;

    public LinkGenerator() {
    }

    public void setAim(String strIn) {
        strAim = strIn;
    }

    public void setTarget(String strIn) {
        strTarget = strIn;
    }

    public void setTextLink(String strIn) {
        strTextLink = strIn;
    }

    public void setOnMouseOver(String strIn) {
        strOnMouseOver = strIn;
    }

    public void setImageName(String strIn) {
        strImageName = strIn;
    }

    public void setExternal(int intIn) {
        intExternal = intIn;
    }

    public void setExternal(String strIn) {
        if (strIn.equals("1")) {
            setExternal(1);
        } else {
            setExternal(0);
        }
    }

    public void setShowLinkSymbol(int intIn) {
        intShowLinkSymbol = intIn;
    }

    public void setShowLinkSymbol(String strIn) {
        if (strIn.equals("1")) {
            setShowLinkSymbol(1);
        } else {
            setShowLinkSymbol(0);
        }
    }

    private boolean checkShowLinkSymbol() {
        return (intShowLinkSymbol == 1);
    }

    public int getShowLinkSymbol() {
        return intShowLinkSymbol;
    }

    private int checkExternal() {
        return intExternal;
    }

    public int getExternal() {
        return intExternal;
    }

    public String getAim() {
        if (checkExternal() == 1) {
            return "../misc/redirectLink.jsp?externalLink=" + strAim;
        } else if (checkExternal() == 2) {
            return "../misc/redirectLink.jsp?veranstaltung=" + strAim;
        } else if (checkExternal() == 3) {
            return "../misc/redirectLink.jsp?register=1&veranstaltung=" + strAim;
        } else if (checkExternal() == 4) {
            return "../misc/redirectLink.jsp?orgHp=1&veranstaltung=" + strAim;
        }
        return strAim;
    }

    public String getTextLink() {
        return strTextLink;
    }

    public String getTarget() {
        return strTarget;
    }

    public String getOnMouseOver() {
        return strOnMouseOver;
    }

    public String getImageName() {
        return strImageName;
    }

    public void checkTarget() {
        if (getTarget().length() > 0) {
            JspWriter out = pageContext.getOut();
            try {
                out.print(" target=\"" + getTarget() + "\"");
            } catch (IOException ioe) {
                System.err.println("IO-Error im CustomTag LinkGenerator, Methode checkTarget: " + ioe.toString());
            }
        }
    }

    public void checkTextLink() {
        if (getTextLink().length() > 0) {
            JspWriter out = pageContext.getOut();
            try {
                out.print(getTextLink());
            } catch (IOException ioe) {
                System.err.println("IO-Error im CustomTag LinkGenerator, Methode checkTextLink: " + ioe.toString());
            }
        }
    }

    public void checkOnMouseOver() {
        String onMouseOver = "";
        JspWriter out = pageContext.getOut();
        if (getOnMouseOver().length() > 0) {
            onMouseOver = getOnMouseOver();
            try {
                out.print(" onmouseover=\"return overlib(\'" + onMouseOver + "\');\" onmouseout=\"return nd();\"");
            } catch (IOException ioe) {
                System.err.println("IO-Error im CustomTag LinkGenerator, Methode checkOnMouseOver: " + ioe.toString());
            }
        } else if (getTextLink() != null && getTextLink().length() > 0) {
            onMouseOver = getTextLink();
            try {
                out.print(" title=\"" + onMouseOver + "\" onmouseover=\"javascript:window.status='" + onMouseOver + "';return true\" onmouseout=\"javascript:window.status=''\"");
            } catch (IOException ioe) {
                System.err.println("IO-Error im CustomTag LinkGenerator, Methode checkOnMouseOver: " + ioe.toString());
            }
        }
    }

    public void checkImageLink() {
        if (getImageName().length() > 0) {
            JspWriter out = pageContext.getOut();
            try {
                String altText = getTextLink();
                if (altText == null || altText.length() == 0) altText = getOnMouseOver();
                out.print("<img src=\"" + getImageName() + "\" altText=\"" + altText + "\" border=\"0\">&nbsp;");
            } catch (IOException ioe) {
                System.err.println("IO-Error im CustomTag LinkGenerator, Methode checkImageLink: " + ioe.toString());
            }
        }
    }

    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        try {
            out.print("<a href=\"" + ((HttpServletResponse) pageContext.getResponse()).encodeURL(getAim()) + "\"");
            checkTarget();
            checkOnMouseOver();
            out.print(">");
            if (checkShowLinkSymbol()) {
                out.print("<img src=\"../images/arrow_red_link.gif\" border=\"0\" alt=\"\">");
            }
            checkImageLink();
            checkTextLink();
            out.print("</a>");
        } catch (IOException ioe) {
            System.err.println("IO-Error im CustomTag LinkGenerator, Methode doStartTag: " + ioe.toString());
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
}
