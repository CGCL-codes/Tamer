package org.nextframework.view.combo;

import org.nextframework.view.LogicalTag;
import org.nextframework.view.PanelTag;
import org.nextframework.view.PropertyTag;

/**
 * @author rogelgarcia
 * @since 01/02/2006
 * @version 1.1
 */
public class PropertyPanelTag extends ComboTag implements LogicalTag {

    protected String name;

    @Override
    protected void doComponent() throws Exception {
        PanelTag panelTag = new PanelTag();
        PropertyTag propertyTag = new PropertyTag();
        propertyTag.setName(name);
        TagHolder panelHolder = new TagHolder(panelTag);
        panelHolder.addChild(new TagHolder(propertyTag));
        propertyTag.setJspBody(getJspBody());
        invoke(panelHolder);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
