package org.nakedobjects.viewer.skylark.tree;

import org.nakedobjects.viewer.skylark.Content;
import org.nakedobjects.viewer.skylark.ObjectContent;
import org.nakedobjects.viewer.skylark.Skylark;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.ViewAxis;
import org.nakedobjects.viewer.skylark.ViewSpecification;
import org.nakedobjects.viewer.skylark.core.AbstractCompositeViewSpecification;
import org.nakedobjects.viewer.skylark.special.CollectionElementBuilder;
import org.nakedobjects.viewer.skylark.special.StackLayout;
import org.nakedobjects.viewer.skylark.special.SubviewSpec;

class SimpleListSpecification extends AbstractCompositeViewSpecification implements SubviewSpec {

    public SimpleListSpecification() {
        builder = new StackLayout(new CollectionElementBuilder(this, true));
    }

    public View createSubview(Content content, ViewAxis axis) {
        ViewSpecification specification = Skylark.getViewFactory().getIconizedSubViewSpecification((ObjectContent) content);
        return specification.createView(content, axis);
    }

    public String getName() {
        return "Standard List";
    }

    public boolean canDisplay(Content content) {
        return content.isCollection();
    }
}
