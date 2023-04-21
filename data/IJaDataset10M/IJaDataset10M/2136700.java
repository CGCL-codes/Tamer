package org.eclipse.rap.flexdraw2d.examples;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.rwt.ss.CanvasHelper;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class EmptyView extends ViewPart {

    @Override
    public void createPartControl(Composite parent) {
        Canvas canvas = CanvasHelper.newFigureCanvas(parent);
        canvas.setLayout(new FillLayout());
        canvas.setBackground(ColorConstants.white);
        CanvasHelper.setContents(canvas, getContents());
    }

    protected IFigure getContents() {
        IFigure panel = new Figure();
        return panel;
    }

    @Override
    public void setFocus() {
    }
}
