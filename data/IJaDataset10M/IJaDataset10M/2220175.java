package calclipse.core.disp.menu;

import calclipse.core.disp.InputTrapAdapter;

/**
 * A menu item.
 * @author T. Sommerland
 */
public abstract class Item extends InputTrapAdapter {

    private ItemContext context;

    public Item() {
    }

    public void setContext(final ItemContext context) {
        this.context = context;
    }

    public ItemContext getContext() {
        return context;
    }

    public abstract String getText();

    /**
     * A sequence of characters that selects this item
     * (null to disable this feature).
     */
    public abstract String getAccelerator();

    public abstract void setSelected(boolean b);
}
