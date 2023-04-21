package com.android.ide.eclipse.adt.internal.editors.layout.parts;

import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;

/**
 * Graphical edit part for an {@link UiElementNode} that represents a View.
 *
 * @since GLE1
 */
public class UiViewEditPart extends UiElementEditPart {

    public UiViewEditPart(UiElementNode uiElementNode) {
        super(uiElementNode);
    }

    @Override
    protected IFigure createFigure() {
        IFigure f = new ElementFigure(null);
        f.setLayoutManager(new XYLayout());
        return f;
    }

    @Override
    protected void showSelection() {
        IFigure f = getFigure();
        if (f instanceof ElementFigure) {
            ((ElementFigure) f).setSelected(true);
        }
    }

    @Override
    protected void hideSelection() {
        IFigure f = getFigure();
        if (f instanceof ElementFigure) {
            ((ElementFigure) f).setSelected(false);
        }
    }
}
