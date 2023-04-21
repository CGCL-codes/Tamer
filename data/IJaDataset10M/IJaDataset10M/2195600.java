package jgloss.ui.export;

import java.net.MalformedURLException;
import java.net.URL;
import jgloss.ui.JGlossFrameModel;
import org.w3c.dom.Element;

/**
 * UI element for choosing the XSLT export template. While the element names are different,
 * the tree structure of the XML element is identical to the list parameter. The list parameter
 * can thus be used as superclass.
 */
class TemplateChooser extends ListParameter {

    TemplateChooser(Element elem) {
        super(elem);
    }

    public Object getValue(JGlossFrameModel source, URL systemId) {
        String value = getValue();
        if (systemId != null) {
            try {
                value = new URL(systemId, value).toExternalForm();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        }
        return value;
    }
}
