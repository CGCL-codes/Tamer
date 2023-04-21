package org.eclipse.gef.examples.flow.parts;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;

/**
 * The dummy layout class does nothing during normal layouts.  The Graph layout is
 * entirely performed in one place: {@link GraphLayoutManager}, on the diagram's figure.
 * During animation, THIS layout will playback the intermediate steps between the two
 * invocations of the graph layout.
 * @author hudsonr
 */
public class DummyLayout extends AbstractLayout {

    /**
 * @see org.eclipse.draw2d.AbstractLayout#calculatePreferredSize(org.eclipse.draw2d.IFigure, int, int)
 */
    protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
        return null;
    }

    /**
 * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
 */
    public void layout(IFigure container) {
        GraphAnimation.playbackState(container);
    }
}
