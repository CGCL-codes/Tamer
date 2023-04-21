package org.nakedobjects.nos.client.dnd.view.specification;

import org.nakedobjects.noa.adapter.NakedReference;
import org.nakedobjects.noa.spec.Features;
import org.nakedobjects.nof.core.context.NakedObjectsContext;
import org.nakedobjects.nos.client.dnd.Content;
import org.nakedobjects.nos.client.dnd.ContentDrag;
import org.nakedobjects.nos.client.dnd.Drag;
import org.nakedobjects.nos.client.dnd.DragStart;
import org.nakedobjects.nos.client.dnd.Toolkit;
import org.nakedobjects.nos.client.dnd.UserActionSet;
import org.nakedobjects.nos.client.dnd.View;
import org.nakedobjects.nos.client.dnd.ViewAxis;
import org.nakedobjects.nos.client.dnd.ViewDrag;
import org.nakedobjects.nos.client.dnd.ViewSpecification;
import org.nakedobjects.nos.client.dnd.border.ObjectBorder;
import org.nakedobjects.nos.client.dnd.content.OptionFactory;
import org.nakedobjects.nos.client.dnd.drawing.Offset;
import org.nakedobjects.nos.client.dnd.util.Properties;
import org.nakedobjects.nos.client.dnd.view.graphic.IconGraphic;
import org.nakedobjects.nos.client.dnd.view.simple.DragViewOutline;
import org.nakedobjects.nos.client.dnd.view.simple.Icon;
import org.nakedobjects.nos.client.dnd.view.text.ObjectTitleText;

public class ResourceIconSpecification implements ViewSpecification {

    private static class ResourceIcon extends Icon {

        public ResourceIcon(Content content, ViewSpecification specification, ViewAxis axis) {
            super(content, specification, axis);
            setTitle(new ObjectTitleText(this, Toolkit.getText("large-icon")));
            setSelectedIcon(new IconGraphic(this, iconSize));
            setVertical(true);
        }

        public void contentMenuOptions(final UserActionSet options) {
            options.setColor(Toolkit.getColor("background.content-menu"));
            OptionFactory.addObjectMenuOptions((NakedReference) getContent().getNaked(), options);
        }

        public Drag dragStart(final DragStart drag) {
            if (drag.isCtrl()) {
                View dragOverlay = Toolkit.getViewFactory().getContentDragSpecification().createView(getContent(), null);
                return new ContentDrag(this, drag.getLocation(), dragOverlay);
            } else {
                View dragOverlay = new DragViewOutline(getView());
                return new ViewDrag(this, new Offset(drag.getLocation()), dragOverlay);
            }
        }
    }

    private static final int iconSize;

    private static final int LARGE_ICON_SIZE = 34;

    private static final String LARGE_ICON_SIZE_PROPERTY;

    static {
        LARGE_ICON_SIZE_PROPERTY = Properties.PROPERTY_BASE + "large-icon-size";
        iconSize = NakedObjectsContext.getConfiguration().getInteger(LARGE_ICON_SIZE_PROPERTY, LARGE_ICON_SIZE);
    }

    public boolean canDisplay(final Content content) {
        return content.isObject() && Features.isResource(content.getNaked().getSpecification());
    }

    public View createView(final Content content, final ViewAxis axis) {
        Icon icon = new ResourceIcon(content, this, axis);
        return new ObjectBorder(icon);
    }

    public String getName() {
        return "resource icon";
    }

    public boolean isAligned() {
        return false;
    }

    public boolean isOpen() {
        return false;
    }

    public boolean isReplaceable() {
        return false;
    }

    public boolean isSubView() {
        return false;
    }
}
