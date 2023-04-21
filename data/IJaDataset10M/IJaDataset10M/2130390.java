package net.sf.uibuilder.xml;

import javax.swing.Box;
import org.dom4j.Element;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**  
 * GlueBuilderXML - 
 *
 * @version   1.0 2002-7-12
 * @author <A HREF="mailto:chyxiang@yahoo.com">Chen Xiang (Sean)</A>
 */
public class GlueBuilderXML extends ResourcesBuilderXML {

    private static Log _log = LogFactory.getLog(GlueBuilderXML.class);

    /**
     * Creates a glue builder according to the given JDOM element.
     * 
     * @param element The given element.
     */
    public GlueBuilderXML(Element element) {
        super(element);
    }

    /**
     * Creates a glue component.
     * 
     * @return The built glue component.
     */
    public Object build() {
        String direction = _element.attributeValue(BuilderConstantXML.DIRECTION);
        _log.debug("Builds a " + direction + " Glue component.");
        if (direction.equals(BuilderConstantXML.HORIZONTAL)) {
            return Box.createHorizontalGlue();
        } else if (direction.equals(BuilderConstantXML.VERTICAL)) {
            return Box.createVerticalGlue();
        } else {
            _log.debug("No such kind of Glue component.");
            return null;
        }
    }
}
