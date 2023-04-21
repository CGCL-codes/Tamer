package org.jowidgets.impl.widgets.composed.factory.internal;

import java.util.Collection;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IInputControl;
import org.jowidgets.api.widgets.descriptor.ICombinedCollectionInputFieldDescriptor;
import org.jowidgets.impl.widgets.composed.CombinedCollectionInputFieldImpl;
import org.jowidgets.tools.widgets.factory.AbstractCompositeWidgetFactory;

public class CombinedCollectionInputFieldFactory<ELEMENT_TYPE> extends AbstractCompositeWidgetFactory<IInputControl<Collection<ELEMENT_TYPE>>, ICombinedCollectionInputFieldDescriptor<ELEMENT_TYPE>> {

    @Override
    protected IInputControl<Collection<ELEMENT_TYPE>> createWidget(final IComposite composite, final ICombinedCollectionInputFieldDescriptor<ELEMENT_TYPE> descriptor) {
        return new CombinedCollectionInputFieldImpl<ELEMENT_TYPE>(composite, descriptor);
    }
}
