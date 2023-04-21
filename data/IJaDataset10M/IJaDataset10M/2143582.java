package com.kongur.network.erp.tc;

import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;
import com.taobao.api.domain.PromotionDetail;

public class JsonTest {

    public static void main(String[] args) {
        List<PromotionDetail> pdList = new ArrayList<PromotionDetail>();
        PromotionDetail pd1 = new PromotionDetail();
        pd1.setDiscountFee("10.00");
        pd1.setGiftItemId("1000");
        pd1.setGiftItemName("��Ʒ");
        pd1.setGiftItemNum("1");
        pd1.setId(926628844L);
        pd1.setPromotionId("mjs-123024_211143");
        pdList.add(pd1);
        PromotionDetail pd2 = new PromotionDetail();
        pd2.setDiscountFee("10.00");
        pd2.setGiftItemId("1000");
        pd2.setGiftItemName("��Ʒ");
        pd2.setGiftItemNum("1");
        pd2.setId(926628844L);
        pd2.setPromotionId("mjs-123024_211143");
        pdList.add(pd2);
        JSONArray json = JSONArray.fromObject(pdList);
        System.out.println(json.toString());
    }

    public <T> T test() {
        T t = null;
        return t;
    }
}
