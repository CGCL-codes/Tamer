package com.shz.netpos.dao.impl;

import java.util.HashMap;
import java.util.Map;
import com.ibatis.common.util.PaginatedList;
import com.ibatis.dao.client.DaoManager;
import com.shz.netpos.bean.Item;
import com.shz.netpos.bean.LineItem;
import com.shz.netpos.bean.Order;
import com.shz.netpos.dao.iface.ItemDao;

public class ItemSqlMapDao extends BaseSqlMapDao implements ItemDao {

    public ItemSqlMapDao(DaoManager daoManager) {
        super(daoManager);
    }

    @SuppressWarnings("unchecked")
    public void updateAllQuantitiesFromOrder(Order order) {
        for (int i = 0; i < order.getLineItems().size(); i++) {
            LineItem lineItem = (LineItem) order.getLineItems().get(i);
            String itemId = lineItem.getItemId();
            Integer increment = new Integer(lineItem.getQuantity());
            Map param = new HashMap(2);
            param.put("itemId", itemId);
            param.put("increment", increment);
            update("updateInventoryQuantity", param);
        }
    }

    public boolean isItemInStock(String itemId) {
        Integer i = (Integer) queryForObject("getInventoryQuantity", itemId);
        return (i != null && i.intValue() > 0);
    }

    public PaginatedList getItemListByProduct(String productId) {
        return queryForPaginatedList("getItemListByProduct", productId, PAGE_SIZE);
    }

    public Item getItem(String itemId) {
        Integer i = (Integer) queryForObject("getInventoryQuantity", itemId);
        Item item = (Item) queryForObject("getItem", itemId);
        item.setQuantity(i.intValue());
        return item;
    }
}
