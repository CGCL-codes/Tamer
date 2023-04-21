package uk.ac.reload.straker.datamodel.learningdesign.method;

import org.jdom.Element;
import uk.ac.reload.straker.datamodel.DataModel;
import uk.ac.reload.straker.datamodel.learningdesign.types.AbstractCompleteType;

/**
 * Complete Play type
 * 
 * @author Phillip Beauvoir
 * @version $Id: CompletePlayType.java,v 1.6 2006/07/10 11:50:46 phillipus Exp $
 */
public class CompletePlayType extends AbstractCompleteType {

    public static String WHEN_LAST_ACT_COMPLETED = "when-last-act-completed";

    /**
     * Default constructor
     */
    public CompletePlayType(DataModel dataModel) {
        super(dataModel);
    }

    public Element marshall2XML(Element parentElement) {
        Element element = super.marshall2XML(parentElement);
        if (element != null) {
            if (WHEN_LAST_ACT_COMPLETED.equals(getChoice())) {
                Element e = new Element(WHEN_LAST_ACT_COMPLETED);
                element.addContent(e);
            }
        }
        return element;
    }

    public void unmarshallXML(Element element) {
        Element e = element.getChild(WHEN_LAST_ACT_COMPLETED);
        if (e != null) {
            setChoice(WHEN_LAST_ACT_COMPLETED);
        } else {
            super.unmarshallXML(element);
        }
    }

    public String getXMLElementName() {
        return "complete-play";
    }
}
