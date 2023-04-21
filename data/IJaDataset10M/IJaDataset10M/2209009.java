package org.jowidgets.impl.layout;

import org.jowidgets.api.layout.IBorderLayoutFactoryBuilder;
import org.jowidgets.api.layout.ILayoutFactory;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.common.widgets.layout.ILayouter;

final class BorderLayoutFactoryBuilder implements IBorderLayoutFactoryBuilder {

    private int gapX;

    private int gapY;

    private int marginTop;

    private int marginBottom;

    private int marginLeft;

    private int marginRight;

    @Override
    public IBorderLayoutFactoryBuilder margin(final int margin) {
        this.marginTop = margin;
        this.marginBottom = margin;
        this.marginLeft = margin;
        this.marginRight = margin;
        return this;
    }

    @Override
    public IBorderLayoutFactoryBuilder gap(final int gap) {
        this.gapX = gap;
        this.gapY = gap;
        return this;
    }

    @Override
    public IBorderLayoutFactoryBuilder gapX(final int gapX) {
        this.gapX = gapX;
        return this;
    }

    @Override
    public IBorderLayoutFactoryBuilder gapY(final int gapY) {
        this.gapY = gapY;
        return this;
    }

    @Override
    public IBorderLayoutFactoryBuilder marginLeft(final int marginLeft) {
        this.marginLeft = marginLeft;
        return this;
    }

    @Override
    public IBorderLayoutFactoryBuilder marginRight(final int marginRight) {
        this.marginRight = marginRight;
        return this;
    }

    @Override
    public IBorderLayoutFactoryBuilder marginTop(final int marginTop) {
        this.marginTop = marginTop;
        return this;
    }

    @Override
    public IBorderLayoutFactoryBuilder marginBottom(final int marginBottom) {
        this.marginBottom = marginBottom;
        return this;
    }

    @Override
    public ILayoutFactory<ILayouter> build() {
        return new ILayoutFactory<ILayouter>() {

            @Override
            public ILayouter create(final IContainer container) {
                return new BorderLayout(container, marginLeft, marginRight, marginTop, marginBottom, gapX, gapY);
            }
        };
    }
}
