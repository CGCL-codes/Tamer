package org.zkoss.zkmob.impl;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import org.xml.sax.Attributes;
import org.zkoss.zkmob.AbstractUiFactory;
import org.zkoss.zkmob.ZkComponent;
import org.zkoss.zkmob.ZkComponents;

/**
 * @author henrichen
 *
 */
public class StringItemFactory extends AbstractUiFactory {

    public StringItemFactory(String name) {
        super(name);
    }

    public ZkComponent create(ZkComponent parent, String tag, Attributes attrs, String hostURL) {
        final String id = attrs.getValue("id");
        final String label = attrs.getValue("lb");
        final String text = attrs.getValue("tx");
        final Zk zk = ((ZkComponent) parent).getZk();
        final ZkStringItem component = new ZkStringItem(zk, id, label, text);
        ZkComponents.applyItemProperties(parent, component, attrs);
        return component;
    }
}
