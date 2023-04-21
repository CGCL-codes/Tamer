package com.liferay.portlet.shopping.model.impl;

import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.shopping.model.ShoppingCartItem;
import com.liferay.portlet.shopping.model.ShoppingItem;

/**
 * <a href="ShoppingCartItemImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class ShoppingCartItemImpl implements ShoppingCartItem {

    public static String[] getFieldsArray(String fields) {
        return StringUtil.split(fields, "&");
    }

    public ShoppingCartItemImpl(ShoppingItem item, String fields) {
        _item = item;
        _fields = fields;
    }

    public String getCartItemId() {
        long itemId = getItem().getItemId();
        if (Validator.isNull(_fields)) {
            return String.valueOf(itemId);
        } else {
            return itemId + "|" + _fields;
        }
    }

    public ShoppingItem getItem() {
        return _item;
    }

    public String getFields() {
        return _fields;
    }

    public String[] getFieldsArray() {
        return getFieldsArray(_fields);
    }

    public int compareTo(Object obj) {
        if (obj == null) {
            return -1;
        }
        ShoppingCartItem cartItem = (ShoppingCartItem) obj;
        int value = getItem().compareTo(cartItem.getItem());
        if (value == 0) {
            value = getFields().compareTo(cartItem.getFields());
        }
        return value;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        ShoppingCartItem cartItem = (ShoppingCartItem) obj;
        if (getItem().equals(cartItem.getItem()) && getFields().equals(cartItem.getFields())) {
            return true;
        } else {
            return false;
        }
    }

    private ShoppingItem _item;

    private String _fields;
}
