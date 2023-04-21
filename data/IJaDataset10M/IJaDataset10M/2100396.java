package org.docflower.page.taghandlers.compoundcontrols;

import javax.xml.stream.*;
import org.docflower.model.DataModelDescriptor;
import org.docflower.page.*;
import org.docflower.page.bind.AbstractBinding;
import org.docflower.page.pagecontrols.*;
import org.docflower.page.pagecontrols.labeled.PageCombo;
import org.docflower.page.taghandlers.base.UnbindableControlTagHandler;

public class ListItemTagHandler extends UnbindableControlTagHandler {

    @Override
    public Object handle(Object parent, AbstractBinding binding, XMLStreamReader xmlr, String[] visualSets, PageResources pageResources) throws XMLStreamException {
        if (parent instanceof PageCombo) {
            DataModelDescriptor desc = pageResources.getDescriptor();
            PageCombo pageCombo = (PageCombo) parent;
            PageComboItem item = new PageComboItem();
            item.setImage(pageResources.getImage(desc.getMessage(xmlr.getAttributeValue(null, "image"))));
            item.setValue(desc.getMessage(xmlr.getAttributeValue(null, "value")));
            item.setLabel(desc.getMessage(xmlr.getElementText()));
            if (item.getValue() == null) {
                item.setValue(item.getLabel());
            }
            pageCombo.getItems().put(item.getValue(), item);
        }
        return null;
    }
}
