package net.sf.l2j.gameserver.model;

import javolution.util.FastList;

/**
 * @author -Nemesiss-
 */
public class L2ExtractableItem {

    private final int _itemId;

    private final L2ExtractableProductItem[] _products;

    public L2ExtractableItem(int itemid, FastList<L2ExtractableProductItem> products) {
        _itemId = itemid;
        _products = new L2ExtractableProductItem[products.size()];
        products.toArray(_products);
    }

    public int getItemId() {
        return _itemId;
    }

    public L2ExtractableProductItem[] getProductItemsArray() {
        return _products;
    }
}
