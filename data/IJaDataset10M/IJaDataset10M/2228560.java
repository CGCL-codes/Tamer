package jeeves.server.dispatchers;

import java.util.ArrayList;
import java.util.List;
import org.jdom.*;
import jeeves.constants.ConfigFile;
import jeeves.utils.*;

/** This class represents a single output page of a service
  */
public class OutputPage extends AbstractPage {

    private String forward;

    private boolean isFile, isBLOB;

    private List<String> preStyleSheets = new ArrayList<String>();

    /**
	 * Gets the style sheets that will be applied <strong>BEFORE</strong> the
	 * primary stylesheet.
	 */
    public List<String> getPreStyleSheets() {
        return preStyleSheets;
    }

    /**
     * Sets the style sheets that will be applied <strong>BEFORE</strong> the
     * primary stylesheet.
     */
    public void setPreStyleSheets(List<Element> preStyleSheets) {
        this.preStyleSheets = new ArrayList<String>();
        for (Element element : preStyleSheets) {
            this.preStyleSheets.add(element.getAttributeValue(ConfigFile.Output.Attr.SHEET));
        }
    }

    /** If the output page is a forward returns the service, otherwise returns null
	  */
    public String getForward() {
        return forward;
    }

    /** If the output page is a binary file returns the element name, otherwise returns null
	  */
    public boolean isFile() {
        return isFile;
    }

    public boolean isBLOB() {
        return isBLOB;
    }

    public void setForward(String f) {
        forward = f;
    }

    public void setFile(boolean yesno) {
        isFile = yesno;
    }

    public void setBLOB(boolean yesno) {
        isBLOB = yesno;
    }

    /** Returns true if the service output has attributes that match this page
	  */
    public boolean matches(Element el) throws Exception {
        String test = getTestCondition();
        if (test == null) return true; else return Xml.selectBoolean(el, test);
    }
}
