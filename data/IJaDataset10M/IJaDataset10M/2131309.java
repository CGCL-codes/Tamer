package net.sf.uibuilder.xml;

import java.util.List;
import java.util.Iterator;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.dom4j.Element;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * PopupMenuBuilderXML is the XML implementation for building a popup menu from
 * the given DOM node.
 *
 * @version   1.0 2005-3-23
 * @author    <A HREF="mailto:chyxiang@yahoo.com">Chen Xiang (Sean)</A>
 */
public class PopupMenuBuilderXML extends JComponentBuilderXML {

    private static Log _log = LogFactory.getLog(PopupMenuBuilderXML.class);

    /**
     * Constructor function
     */
    public PopupMenuBuilderXML(Element element) {
        super(element);
    }

    /**
     * Creates and builds a popup menu based on the associated JDOM element.
     * 
     * @return The built JPopupMenu object.
     */
    public Object build() {
        if (_log.isDebugEnabled()) _log.debug("Build the popup menu: " + getKey());
        JPopupMenu theMenu = (JPopupMenu) instantiateComponent("javax.swing.JPopupMenu");
        List children = _element.elements();
        for (Iterator i = children.iterator(); i.hasNext(); ) {
            Element child = (Element) i.next();
            String tagName = child.getQName().getName();
            if (tagName.equals(BuilderConstantXML.SEPARATOR)) {
                theMenu.addSeparator();
            } else if (tagName.equals(BuilderConstantXML.ACTION)) {
                String key = child.attributeValue(BuilderConstantXML.KEY_REF);
                _log.debug("Add the action: " + key);
                theMenu.add((Action) getComponents().get(key));
            } else if (tagName.equals(BuilderConstantXML.ITEM)) {
                MenuItemBuilderXML builder = new MenuItemBuilderXML(child);
                String key = builder.getKey();
                JMenuItem menuItem = (JMenuItem) builder.buildNeedParent(this);
                _log.debug("Binding the menu item component: [" + menuItem.toString() + "] to key: <<" + key + ">>");
                getComponents().put(key, menuItem);
                theMenu.add(menuItem);
            }
        }
        return theMenu;
    }
}
