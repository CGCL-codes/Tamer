package org.docflower.page.taghandlers.compoundcontrols;

import javax.xml.stream.*;
import org.docflower.model.DataModelDescriptor;
import org.docflower.page.*;
import org.docflower.page.bind.AbstractBinding;
import org.docflower.page.pagecontrols.RadioGroup;
import org.docflower.page.taghandlers.base.SingleValueControlTagHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class RadioGroupTagHandler extends SingleValueControlTagHandler {

    @Override
    public Object handle(Object parent, AbstractBinding binding, XMLStreamReader xmlr, String[] visualSets, PageResources pageResources) throws XMLStreamException {
        RadioGroup result = null;
        if (parent instanceof Composite) {
            DataModelDescriptor desc = pageResources.getDescriptor();
            Composite parentComposite = (Composite) parent;
            Group group = new Group(parentComposite, SWT.NONE);
            group.setLayoutData(getFieldGridData(xmlr));
            group.setBackground(parentComposite.getBackground());
            GridLayout gl = getContainerGridLayout(xmlr);
            group.setLayout(gl);
            group.setText(desc.getMessage(xmlr.getAttributeValue(null, "label")));
            result = new RadioGroup(group);
        }
        return result;
    }
}
