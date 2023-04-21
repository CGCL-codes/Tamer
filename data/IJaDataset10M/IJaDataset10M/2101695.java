package de.crysandt.audio.mpeg7audio.mci;

import org.w3c.dom.*;
import de.crysandt.xml.*;

/**
 * @author Michael.Lambertz@rwth-aachen.de
 *
 */
public class ControlledTermUse extends InlineTermDefinition {

    private String href;

    public ControlledTermUse(String href) {
        super();
        this.href = href;
    }

    public Element toXML(Document doc, String name) {
        Element term_use_ele;
        term_use_ele = super.toXML(doc, name);
        term_use_ele.setAttributeNS(Namespace.XSI, "xsi:type", "ControlledTermUseType");
        term_use_ele.setAttribute("href", href);
        return (term_use_ele);
    }

    public String getHref() {
        return (href);
    }

    public void setHref(String href) {
        this.href = href;
    }
}
