package org.jowidgets.spi.impl.swt.common.layout;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.spi.impl.swt.common.util.DimensionConvert;

public class LayoutImpl extends Layout {

    private final ILayouter layouter;

    public LayoutImpl(final ILayouter layouter) {
        this.layouter = layouter;
    }

    @Override
    protected Point computeSize(final Composite composite, final int wHint, final int hHint, final boolean flushCache) {
        if (flushCache) {
            layouter.invalidate();
        }
        final Dimension size = layouter.getPreferredSize();
        if (size != null) {
            return DimensionConvert.convert(size);
        } else {
            return null;
        }
    }

    @Override
    protected void layout(final Composite composite, final boolean flushCache) {
        if (flushCache) {
            layouter.invalidate();
        }
        layouter.layout();
    }
}
