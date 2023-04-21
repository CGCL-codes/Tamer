package com.taobao.api.model;

import java.util.Date;

/**
 * The response result of the calling of the taobao.item.update.showcase,
 * which are iid of the item and the modified time. 
 * 
 * @author biyi
 * 
 */
public class ItemUpdateShowcaseResponse extends ItemRecommendAddResponse {

    private static final long serialVersionUID = 4221038364368073968L;

    public ItemUpdateShowcaseResponse() {
        super();
    }

    public ItemUpdateShowcaseResponse(TaobaoResponse rsp) {
        super(rsp);
    }
}
