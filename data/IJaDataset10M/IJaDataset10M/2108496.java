package com.core.util;

import java.util.Vector;
import com.be.bo.GlobalParameter;
import com.be.bo.UserObject;
import com.eshop.bo.Cart;
import com.eshop.bo.CartEntry;
import com.mat.bo.Facade;
import com.mat.vo.ItemVO;

public class PostalFee {

    public ItemVO getItem(Cart cart, UserObject uo) {
        double weightTotal = getWeigth(cart);
        if (weightTotal > 0.0) {
            long weightTotalKg = Math.round(Math.ceil(weightTotal));
            Facade facade = (Facade) uo.getFacade(GlobalParameter.facadeMat);
            String itemIDList = uo.getFacade().getGlobalParameter(GlobalParameter.systemDeliveryMethodPostItemIDList);
            ItemVO vo = facade.getWeigthItemVO(weightTotalKg, itemIDList);
            return vo;
        }
        return null;
    }

    public double getWeigth(Cart cart) {
        double weightTotal = 0.0;
        if (cart != null) {
            Vector<CartEntry> v = cart.cartEntries;
            for (int i = 0; i < v.size(); i++) {
                double weigth = 0;
                CartEntry ce = v.elementAt(i);
                weigth = ce.item.getWeightKg();
                weightTotal = weightTotal + weigth * ce.count;
            }
        }
        return weightTotal;
    }
}
