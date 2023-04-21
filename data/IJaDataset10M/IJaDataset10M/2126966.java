package org.jowidgets.spi.impl.dummy.dummyui;

public class UIDScrollPane extends UIDContainer {

    private boolean verticalBar;

    private boolean horizontalBar;

    private boolean alwaysShowBars;

    public boolean isVerticalBar() {
        return verticalBar;
    }

    public void setVerticalBar(final boolean verticalBar) {
        this.verticalBar = verticalBar;
    }

    public boolean isHorizontalBar() {
        return horizontalBar;
    }

    public void setHorizontalBar(final boolean horizontalBar) {
        this.horizontalBar = horizontalBar;
    }

    public boolean isAlwaysShowBars() {
        return alwaysShowBars;
    }

    public void setAlwaysShowBars(final boolean alwaysShowBars) {
        this.alwaysShowBars = alwaysShowBars;
    }
}
