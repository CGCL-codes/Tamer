package org.homemotion.ui.widgets;

public class WindowWidget extends AbstractPositionedItemWidget {

    private boolean open;

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public void refresh() {
    }
}
