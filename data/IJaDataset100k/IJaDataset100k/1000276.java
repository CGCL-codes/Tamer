package org.docflower.page.taghandlers.containers;

import javax.xml.stream.*;
import org.docflower.page.PageResources;
import org.docflower.page.bind.*;
import org.docflower.page.pagecontrols.Placeholder;
import org.docflower.page.taghandlers.base.BindableControlTagHandler;
import org.docflower.ui.INavigableUIPart;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.Composite;

public class PlaceholderTagHandler extends BindableControlTagHandler {

    @Override
    public AbstractBinding createBinding(INavigableUIPart parent, XMLStreamReader xmlr) throws XMLStreamException {
        return new PlaceholderBinding(parent, xmlr.getAttributeValue(null, "bind"), xmlr.getAttributeValue(null, "selector"), xmlr.getAttributeValue(null, "bindIsExpression"));
    }

    @Override
    public Object handle(Object parent, AbstractBinding binding, XMLStreamReader xmlr, String[] visualSets, PageResources pageResources) throws XMLStreamException {
        Placeholder result = null;
        if (parent instanceof Composite) {
            Composite parentComposite = (Composite) parent;
            Composite placeholder = getToolkit().createComposite(parentComposite);
            GridData gd = getFieldGridData(xmlr);
            gd.verticalIndent = 0;
            gd.horizontalIndent = 0;
            placeholder.setLayoutData(gd);
            GridLayout cgl = getContainerGridLayout(xmlr);
            cgl.horizontalSpacing = 0;
            cgl.verticalSpacing = 0;
            cgl.marginWidth = 0;
            cgl.marginHeight = 0;
            placeholder.setLayout(cgl);
            getToolkit().paintBordersFor(placeholder);
            result = new Placeholder(placeholder);
            result.setDefaultPageId(xmlr.getAttributeValue(null, "defaultPageId"));
        }
        return result;
    }
}
