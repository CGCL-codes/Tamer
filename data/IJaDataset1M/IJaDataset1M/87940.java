package com.android.ide.eclipse.adt.internal.editors.layout.parts;

import com.android.ide.eclipse.adt.internal.editors.layout.parts.UiElementsEditPartFactory.IOutlineProvider;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

/**
 * The figure used to draw basic elements.
 * <p/>
 * The figure is totally empty and transparent except for the selection border.
 *
 * @since GLE1
 */
class ElementFigure extends Figure {

    private boolean mIsSelected;

    private Rectangle mInnerBounds;

    private final IOutlineProvider mProvider;

    public ElementFigure(IOutlineProvider provider) {
        mProvider = provider;
        setOpaque(false);
    }

    public void setSelected(boolean isSelected) {
        if (isSelected != mIsSelected) {
            mIsSelected = isSelected;
            repaint();
        }
    }

    @Override
    public void setBounds(Rectangle rect) {
        super.setBounds(rect);
        mInnerBounds = getBounds().getCopy();
        if (mInnerBounds.width > 0) {
            mInnerBounds.width--;
        }
        if (mInnerBounds.height > 0) {
            mInnerBounds.height--;
        }
    }

    public Rectangle getInnerBounds() {
        return mInnerBounds;
    }

    @Override
    protected void paintBorder(Graphics graphics) {
        super.paintBorder(graphics);
        if (mIsSelected || (mProvider != null && mProvider.hasOutline())) {
            graphics.setLineWidth(1);
            graphics.setLineStyle(SWT.LINE_SOLID);
            graphics.setForegroundColor(mIsSelected ? ColorConstants.red : ColorConstants.white);
            graphics.drawRectangle(getInnerBounds());
        }
    }
}
