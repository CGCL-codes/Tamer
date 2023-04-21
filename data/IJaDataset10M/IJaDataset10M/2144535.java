package tm.displayEngine;

import java.awt.Dimension;
import tm.interfaces.Datum;
import tm.interfaces.RegionInterface;
import tm.utilities.Assert;

public class StoreLayoutManager {

    private static final int ADDRESS_W = 55;

    private static final int VALUE_W = 150;

    private static final int NAME_W = 120;

    private StoreDisplay target;

    public StoreLayoutManager(StoreDisplay t) {
        target = t;
    }

    public void layoutDisplay() {
        int oldWidth = target.getViewportSize().width;
        int nameWidth = (int) (.4 * (double) (oldWidth - ADDRESS_W));
        int valueWidth = oldWidth - nameWidth - ADDRESS_W;
        if (nameWidth < NAME_W) nameWidth = NAME_W;
        if (valueWidth < VALUE_W) valueWidth = VALUE_W;
        int x = 0;
        int y = 0;
        RegionInterface region = target.getRegion();
        int kids = region.getNumChildren();
        int firstDatum = 0;
        int lastDatum = kids - 1;
        for (int i = 0; i < kids; i++) {
            Datum kid = region.getChildAt(i);
            DatumDisplay dd = DatumDisplay.getAssociated(kid, target);
            if (dd == null) return;
            dd.move(x, y);
            dd.resize(nameWidth, 0, valueWidth, ADDRESS_W);
            Expander expander = dd.getExpander();
            y += dd.getSize().height;
            if (y < target.getFirstY()) firstDatum = i;
            if (y > target.getLastY() && lastDatum == (kids - 1)) lastDatum = i;
        }
        target.setPreferredSize(new Dimension(nameWidth + valueWidth + ADDRESS_W, y + 1));
        target.setOnScreen(firstDatum, lastDatum);
    }
}
