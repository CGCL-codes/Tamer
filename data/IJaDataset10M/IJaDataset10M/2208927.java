package joodin.impl.widgets.internal;

import joodin.impl.widgets.VaadinComposite;
import org.jowidgets.common.widgets.factory.IGenericWidgetFactory;
import com.vaadin.ui.Panel;

public class CompositeWidgetWrapper extends VaadinComposite {

    public CompositeWidgetWrapper(final IGenericWidgetFactory factory, final Panel uiReference) {
        super(factory, uiReference);
    }

    @Override
    public Panel getUiReference() {
        return (Panel) super.getUiReference();
    }
}
