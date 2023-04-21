package org.dyno.visual.swing.widgets.grouplayout.anchor;

import java.awt.Component;
import java.awt.Rectangle;
import javax.swing.JComponent;
import org.dyno.visual.swing.layouts.Alignment;
import org.dyno.visual.swing.widgets.grouplayout.Anchor;

public abstract class VerticalAnchor extends Anchor {

    protected VerticalAnchor(JComponent target) {
        super(target);
    }

    @Override
    public Alignment createBottomAxis(Component me, Rectangle bounds, Alignment lastAxis) {
        return null;
    }

    @Override
    public Alignment createTopAxis(Component me, Rectangle bounds, Alignment lastAxis) {
        return null;
    }
}
