package de.gsolutions.web.gui.taglib;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
 * @author mala
 */
public class DataCellTag extends Tag {

    private String height = "";

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHeight() {
        return height;
    }

    public String checkFormat() {
        String strTmpClass;
        switch(super.getFormat()) {
            case 0:
                {
                    strTmpClass = "datacell";
                    break;
                }
            case 1:
                {
                    strTmpClass = "datacell2";
                    break;
                }
            case 2:
                {
                    strTmpClass = "white";
                    break;
                }
            case 3:
                {
                    strTmpClass = "prio";
                    break;
                }
            case 4:
                {
                    strTmpClass = "notnull";
                    break;
                }
            case 5:
                {
                    strTmpClass = "blueborder";
                    break;
                }
            default:
                strTmpClass = "datacell";
        }
        return " class=\"" + strTmpClass + "\"";
    }

    public String checkHeight() {
        if (getHeight().length() > 0) {
            return " height=\"" + getHeight() + "\"";
        }
        return "";
    }

    /**
	 * diese Methode wird nach der Initialisierung und Eigenschaftsbelegung aufgerufen
	 */
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        try {
            out.print("<td");
            out.print(checkFormat());
            out.print(checkAlign());
            out.print(checkVAlign());
            out.print(checkColspan());
            out.print(checkWidth());
            out.print(checkHeight());
            out.println(">\n");
        } catch (IOException ioe) {
            System.err.println("Error im DataCellTag doStartTag: " + ioe.toString());
        }
        return EVAL_BODY_INCLUDE;
    }

    /**
	 * diese Methode wird nach der Abarbeitung der Funktionalitaet aufgerufen
	 */
    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();
        try {
            out.print(checkInfotext());
            out.print(checkHelptext());
            out.println("\n</td>");
        } catch (IOException ioe) {
            System.err.println("Error im DataCellTag doEndTag: " + ioe.toString());
        }
        return EVAL_PAGE;
    }
}
