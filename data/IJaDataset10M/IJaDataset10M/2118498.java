package org.docflower.page.taghandlers.base;

import javax.xml.stream.*;
import org.docflower.page.bind.AbstractBinding;
import org.docflower.ui.INavigableUIPart;

public abstract class SingleValueLabeledControlTagHandler extends LabeledControlTagHandler {

    @Override
    public AbstractBinding createBinding(INavigableUIPart parent, XMLStreamReader xmlr) throws XMLStreamException {
        return SingleValueBindingUtil.createSingleValueBinding(parent, xmlr);
    }
}
