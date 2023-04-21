package org.nakedobjects.plugins.dndviewer.viewer.builder;

import java.util.Enumeration;
import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;
import org.nakedobjects.plugins.dndviewer.CollectionContent;
import org.nakedobjects.plugins.dndviewer.CompositeViewSpecification;
import org.nakedobjects.plugins.dndviewer.Content;
import org.nakedobjects.plugins.dndviewer.OneToManyField;
import org.nakedobjects.plugins.dndviewer.SubviewSpec;
import org.nakedobjects.plugins.dndviewer.View;
import org.nakedobjects.plugins.dndviewer.ViewAxis;
import org.nakedobjects.plugins.dndviewer.viewer.content.CollectionElement;
import org.nakedobjects.plugins.dndviewer.viewer.content.OneToManyFieldElementImpl;
import org.nakedobjects.plugins.dndviewer.viewer.view.simple.CompositeViewBuilder;

public class CollectionElementBuilder extends AbstractViewBuilder {

    private static final Logger LOG = Logger.getLogger(CollectionElementBuilder.class);

    private boolean canDragView = true;

    private final SubviewSpec subviewDesign;

    public CollectionElementBuilder(final SubviewSpec subviewDesign) {
        this.subviewDesign = subviewDesign;
    }

    @Override
    public void build(final View view) {
        Assert.assertEquals(view.getView(), view);
        final Content content = view.getContent();
        final OneToManyAssociation field = content instanceof OneToManyField ? ((OneToManyField) content).getOneToManyAssociation() : null;
        LOG.debug("rebuild view " + view + " for " + content);
        final CollectionContent collectionContent = ((CollectionContent) content);
        Enumeration elements;
        elements = collectionContent.allElements();
        final View[] subviews = view.getSubviews();
        final NakedObject[] existingElements = new NakedObject[subviews.length];
        for (int i = 0; i < subviews.length; i++) {
            view.removeView(subviews[i]);
            existingElements[i] = subviews[i].getContent().getNaked();
        }
        int fieldNumber = 0;
        while (elements.hasMoreElements()) {
            final NakedObject element = (NakedObject) elements.nextElement();
            View elementView = null;
            for (int i = 0; i < subviews.length; i++) {
                if (existingElements[i] == element) {
                    elementView = subviews[i];
                    existingElements[i] = null;
                    break;
                }
            }
            if (elementView == null) {
                Content elementContent;
                if (field == null) {
                    elementContent = new CollectionElement(element);
                } else {
                    final NakedObject obj = ((OneToManyField) view.getContent()).getParent();
                    final NakedObject parent = obj;
                    elementContent = new OneToManyFieldElementImpl(parent, element, field);
                }
                elementView = subviewDesign.createSubview(elementContent, view.getViewAxis(), fieldNumber++);
            }
            if (elementView != null) {
                view.addView(decorateSubview(elementView));
            }
        }
    }

    public View decorateSubview(View subview) {
        return subviewDesign.decorateSubview(subview);
    }

    public View createCompositeView(final Content content, final CompositeViewSpecification specification, final ViewAxis axis) {
        final CompositeViewBuilder view = new CompositeViewBuilder(content, specification, axis);
        view.setCanDragView(canDragView);
        return view;
    }

    public void setCanDragView(final boolean canDragView) {
        this.canDragView = canDragView;
    }
}
