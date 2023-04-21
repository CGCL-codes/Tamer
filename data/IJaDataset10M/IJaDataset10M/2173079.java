package com.taobao.top.domain;

import com.taobao.top.mapping.ApiClass;
import com.taobao.top.mapping.ApiField;
import com.taobao.top.mapping.ApiListClass;

/**
 * TaobaokeShop Data Structure.
 *
 * @author carver.gu
 * @since 1.0, Apr 11, 2010
 */
@ApiClass("taobaoke_shop")
@ApiListClass("taobaoke_shops")
public class TaobaokeShop extends BaseObject {

    private static final long serialVersionUID = 1L;

    @ApiField("click_url")
    private String clickUrl;

    @ApiField("commission_rate")
    private String commissionRate;

    @ApiField("shop_title")
    private String shopTitle;

    @ApiField("user_id")
    private String userId;

    public String getClickUrl() {
        return this.clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public String getCommissionRate() {
        return this.commissionRate;
    }

    public void setCommissionRate(String commissionRate) {
        this.commissionRate = commissionRate;
    }

    public String getShopTitle() {
        return this.shopTitle;
    }

    public void setShopTitle(String shopTitle) {
        this.shopTitle = shopTitle;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
