package org.w4tj.controller.util;

import java.util.Iterator;
import org.springframework.stereotype.Component;
import org.w4tj.model.Entity;
import org.w4tj.model.mapping.PropertyInfo;
import org.w4tj.zul.FormattedLabel;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

@Component
public class ListboxRenderer implements ListitemRenderer {

    @Override
    public void render(Listitem item, Object data) throws Exception {
        for (final Iterator<?> iter = item.getListbox().getListhead().getChildren().iterator(); iter.hasNext(); ) {
            final Listheader header = (Listheader) iter.next();
            final PropertyInfo property = (PropertyInfo) header.getAttribute(ComponentUtils.ATTRIB_PROPERTY_INFO);
            if (property != null) {
                final Object value = property.getValue((Entity) data);
                final Listcell cell = new Listcell();
                cell.setParent(item);
                cell.setStyle("white-space: nowrap");
                if (value != null) {
                    HtmlBasedComponent comp;
                    if (Boolean.class.isInstance(value)) {
                        comp = new Checkbox();
                        comp.setParent(cell);
                        ((Checkbox) comp).setChecked((Boolean) value);
                        ((Checkbox) comp).setDisabled(true);
                    } else {
                        comp = new FormattedLabel(cell, property, value);
                    }
                }
            }
        }
    }
}
