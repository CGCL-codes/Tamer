package org.nightlabs.editor2d.impl;

import org.nightlabs.editor2d.DrawComponentContainer;
import org.nightlabs.editor2d.RectangleDrawComponent;

public class RectangleDrawComponentImpl extends ShapeDrawComponentImpl implements RectangleDrawComponent {

    private static final long serialVersionUID = 1L;

    public RectangleDrawComponentImpl() {
        super();
    }

    @Override
    public String getTypeName() {
        return "Rectangle";
    }

    @Override
    public Object clone(DrawComponentContainer parent) {
        RectangleDrawComponentImpl rect = (RectangleDrawComponentImpl) super.clone(parent);
        return rect;
    }
}
