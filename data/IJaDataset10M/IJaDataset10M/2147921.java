package discspy;

import java.awt.Graphics;

public class FreeSpaceGraphicWrapper extends ItemInfoGraphicWrapper {

    public FreeSpaceGraphicWrapper(ItemInfo item, FilesystemTreeGraphicWrapper parentTree, ItemInfoGraphicWrapper parentItem) {
        super(item, parentTree, parentItem);
    }

    @Override
    public boolean isBigEnoughForDispalying() {
        if (rect == null) {
            return false;
        }
        if (getScaledHeight() < getMinDisplaySize() || getScaledWidth() < getMinDisplaySize()) {
            return false;
        }
        return true;
    }

    @Override
    public void paintAt(Graphics g, int depth) {
        if (!isToBePainted(depth)) {
            return;
        }
        g.drawString("Free space", (int) getScaledX() + 10, (int) (getScaledY() + getScaledHeight() / 2));
    }
}
