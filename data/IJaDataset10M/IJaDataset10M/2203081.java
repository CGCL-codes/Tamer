package com.ivis.xprocess.ui.viewpoint.parts;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.Graphics;

public class DiagramFreeformLayer extends FreeformLayer {

    @Override
    public void paint(Graphics graphics) {
        setSize(getParent().getClientArea().getSize());
        setMinimumSize(getSize());
        super.paint(graphics);
    }
}
