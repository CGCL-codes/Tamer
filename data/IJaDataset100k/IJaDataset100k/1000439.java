package anmeldung.web;

import anmeldung.fachlogik.Eintrag;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 *
 * @author Rudolf Radlbauer
 */
public class EintragTag extends TagSupport {

    private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm");

    private String feld;

    /**
     * Get the value of feld
     *
     * @return the value of feld
     */
    public String getFeld() {
        return feld;
    }

    /**
     * Set the value of feld
     *
     * @param feld new value of feld
     */
    public void setFeld(String feld) {
        this.feld = feld;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            Eintrag eintrag = (Eintrag) pageContext.getAttribute("eintrag");
            if ("email".equals(feld)) pageContext.getOut().print(eintrag.getEmail()); else if ("zeitpunkt".equals(feld)) pageContext.getOut().print(DATEFORMAT.format(eintrag.getZeitpunkt())); else if ("zusage".equals(feld)) pageContext.getOut().print((eintrag.isZusage()) ? "ja" : "nein");
            return SKIP_BODY;
        } catch (IOException ex) {
            throw new JspException(ex);
        }
    }
}
