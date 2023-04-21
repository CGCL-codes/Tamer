package com.taobao.api.domain;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 淘宝客商品详情
 *
 * @author auto create
 * @since 1.0, null
 */
public class TaobaokeItemDetail extends TaobaoObject {

    private static final long serialVersionUID = 8585793969644145568L;

    /**
	 * 商品推广URL
	 */
    @ApiField("click_url")
    private String clickUrl;

    /**
	 * 商品详细信息. fields中需要设置Item下的字段,如设置:iid,detail_url等; 只设置item_detail,则不返回的Item下的所有信息.
	 */
    @ApiField("item")
    private Item item;

    /**
	 * 商品所属卖家的信用等级
	 */
    @ApiField("seller_credit_score")
    private Long sellerCreditScore;

    /**
	 * 商品所在的店铺的推广URL
	 */
    @ApiField("shop_click_url")
    private String shopClickUrl;

    public String getClickUrl() {
        return this.clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Long getSellerCreditScore() {
        return this.sellerCreditScore;
    }

    public void setSellerCreditScore(Long sellerCreditScore) {
        this.sellerCreditScore = sellerCreditScore;
    }

    public String getShopClickUrl() {
        return this.shopClickUrl;
    }

    public void setShopClickUrl(String shopClickUrl) {
        this.shopClickUrl = shopClickUrl;
    }
}
