package com.intellij.uiDesigner.lw;

import org.jdom.Element;
import com.intellij.uiDesigner.UIFormXmlConstants;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * @author yole
 */
public class LwButtonGroup implements IButtonGroup {

    private String myName;

    private ArrayList myComponentIds = new ArrayList();

    private boolean myBound;

    public void read(final Element element) {
        myName = element.getAttributeValue(UIFormXmlConstants.ATTRIBUTE_NAME);
        myBound = LwXmlReader.getOptionalBoolean(element, UIFormXmlConstants.ATTRIBUTE_BOUND, false);
        for (Iterator i = element.getChildren().iterator(); i.hasNext(); ) {
            final Element child = (Element) i.next();
            myComponentIds.add(child.getAttributeValue(UIFormXmlConstants.ATTRIBUTE_ID));
        }
    }

    public String getName() {
        return myName;
    }

    public String[] getComponentIds() {
        return (String[]) myComponentIds.toArray(new String[myComponentIds.size()]);
    }

    public boolean isBound() {
        return myBound;
    }
}
