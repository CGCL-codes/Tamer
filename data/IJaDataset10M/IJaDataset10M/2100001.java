package org.santeplanning.tag;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import org.santeplanning.model.TypeHoraire;
import org.stateengine.util.StringUtil;

public class SplitTag extends TagSupport {

    private static final long serialVersionUID = 1L;

    private String nb;

    public String getNb() {
        return nb;
    }

    public void setNb(String nb) {
        this.nb = nb;
    }

    /**
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
    public int doStartTag() throws JspException {
        try {
            JspWriter out = pageContext.getOut();
            int iNb = 10;
            try {
                iNb = Integer.parseInt(nb);
            } catch (Throwable th) {
            }
            out.print("<p class='split'>");
            for (int x = 0; x < iNb; x++) {
                out.print("-");
            }
            out.print("</p>");
        } catch (IOException io) {
            io.printStackTrace();
            throw new JspException(io);
        }
        return SKIP_BODY;
    }

    public static String convertHtml(TypeHoraire value) {
        StringBuffer out = new StringBuffer();
        out.append("<span class='typeHoraire'>" + value.getLabel());
        if (value.getAfficheHeure() != null && (value.getAfficheHeure().intValue() == 1)) {
            out.append("<span class='smallHoraire'>");
            out.append(value.getHeureDebut() + "h" + value.getMinuteDebut());
            out.append("-" + value.getHeureFin() + "h" + value.getMinuteFin());
            out.append("</span>");
        }
        out.append("</span>");
        return out.toString();
    }
}
