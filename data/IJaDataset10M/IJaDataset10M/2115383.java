package org.nakedobjects.plugins.dnd.viewer.view.form;

import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.border.ScrollBorder;

public class InternalFormWithoutCollectionsSpecification extends WindowFormSpecification {

    @Override
    protected View decorateView(final View formView) {
        return new ScrollBorder(super.decorateView(formView));
    }

    protected boolean include(Content content, int sequence) {
        return !content.isCollection();
    }
}
