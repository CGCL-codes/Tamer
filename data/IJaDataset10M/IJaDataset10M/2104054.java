package org.jjazz.leadsheet.chordleadsheet;

import java.util.ArrayList;
import org.jjazz.leadsheet.chordleadsheet.api.item.ChordLeadSheetItem;

/**
 * A special array with additional functions to deal with ChordLeadSheetItems and CLI_Blocks.
 * Items are stored ordered by position. We guarantee that CLI_Block is always the first item for a bar.
 * @todo If required optimize by caching blocks indexes etc.
 */
public class ItemArray extends ArrayList<ChordLeadSheetItem> {

    /**
     * Insert an item at the appropriate position.
     * If 2 items on same position, normal items are inserted last, BarSingleItems are inserted first.
     * @param item
     * @return The index where item has been inserted.
     */
    public int insertOrdered(ChordLeadSheetItem item) {
        int i;
        for (i = 0; i < size(); i++) {
            ChordLeadSheetItem cli = get(i);
            int compare = item.getPosition().compareTo(cli.getPosition());
            if (compare < 0 || (compare == 0 && item.isBarSingleItem())) {
                add(i, item);
                break;
            }
        }
        if (i == size()) {
            add(item);
        }
        return i;
    }

    /**
     * The index of the first item found at barIndex or after barIndex.
     * @param barIndex
     * @return -1 if no item found from barIndex
     */
    public int getItemIndex(int barIndex) {
        int index = 0;
        for (ChordLeadSheetItem item : this) {
            if (item.getPosition().getBar() >= barIndex) {
                return index;
            }
            index++;
        }
        return -1;
    }
}
