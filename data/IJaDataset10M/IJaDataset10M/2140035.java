package org.openaion.gameserver.model.items;

import gnu.trove.TIntIntHashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.openaion.commons.utils.Rnd;
import org.openaion.gameserver.model.templates.item.ItemRace;

/**
 * @author rolandas
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "WrapperItem")
public class WrapperItem {

    @XmlAttribute(name = "id")
    protected int itemId;

    @XmlAttribute(name = "count")
    protected int count;

    @XmlAttribute(name = "random")
    protected boolean isRandomCount;

    @XmlElement(name = "wrapper_item")
    protected List<WrapperItem> choiceItemList;

    @XmlElement(name = "item")
    protected List<WrappedItem> itemList;

    @XmlElement(name = "name")
    protected String name;

    public int getItemId() {
        return itemId;
    }

    public int getMaxCount() {
        return count;
    }

    public boolean hasAnyItems(ItemRace race) {
        if (choiceItemList != null) {
            for (WrapperItem wi : choiceItemList) {
                if (WrapperItem.hasRollableItems(wi.itemList, race)) return true;
            }
        }
        if (itemList != null) return WrapperItem.hasRollableItems(itemList, race);
        return false;
    }

    public TIntIntHashMap rollItems(int playerLevel, ItemRace race) {
        TIntIntHashMap itemCountMap = new TIntIntHashMap();
        int total = 0;
        int itemCount = count;
        if (isRandomCount) itemCount = Rnd.get(1, count);
        if (choiceItemList != null && choiceItemList.size() > 0) {
            List<WrapperItem> copy = new ArrayList<WrapperItem>(choiceItemList);
            Collections.shuffle(copy);
            while (true) {
                boolean hasValidItems = false;
                for (WrapperItem wrapper : copy) {
                    if (!hasValidItems && hasRollableItems(wrapper.itemList, race)) hasValidItems = true;
                    TIntIntHashMap rolled = wrapper.rollItems(playerLevel, race);
                    if (rolled.size() == 0) continue;
                    int oldTotal = total;
                    total = AddRolledItems(oldTotal, itemCount, itemCountMap, rolled);
                    if (total > oldTotal) hasValidItems = true;
                    if (total >= itemCount) {
                        rolled = null;
                        hasValidItems = false;
                        break;
                    }
                }
                if (!hasValidItems) break;
            }
        }
        if (total >= itemCount && itemCount > 0) return itemCountMap;
        if (itemList != null && hasRollableItems(itemList, race)) {
            List<WrappedItem> col = new ArrayList<WrappedItem>(itemList);
            Collections.shuffle(col);
            while (true) {
                for (WrappedItem item : col) {
                    TIntIntHashMap rolled = item.rollItem(playerLevel, race);
                    total = AddRolledItems(total, itemCount, itemCountMap, rolled);
                    if (total >= itemCount && itemCount != 0) {
                        rolled = null;
                        return itemCountMap;
                    }
                }
                if (itemCount > 0 && total < itemCount) continue;
                break;
            }
        }
        return itemCountMap;
    }

    private static boolean hasRollableItems(List<WrappedItem> list, ItemRace race) {
        if (list != null) {
            for (WrappedItem item : list) {
                if (item.getRace() != ItemRace.ALL) {
                    if (item.getRace().ordinal() != race.ordinal()) continue;
                }
                if (item.maxCount != 0 && item.minCount > item.maxCount) continue;
                if (item.maxCount > 0 || item.minCount > 0) return true;
            }
        }
        return false;
    }

    private int AddRolledItems(int currentTotal, int maxCount, TIntIntHashMap oldItems, TIntIntHashMap rolledItems) {
        for (int rolledId : rolledItems.keys()) {
            int countAdded = rolledItems.get(rolledId);
            currentTotal += countAdded;
            if (oldItems.containsKey(rolledId)) {
                int oldCount = oldItems.get(rolledId);
                oldItems.adjustValue(rolledId, oldCount + countAdded);
            } else {
                oldItems.put(rolledId, countAdded);
            }
            if (currentTotal >= maxCount && maxCount != 0) break;
        }
        return currentTotal;
    }
}
