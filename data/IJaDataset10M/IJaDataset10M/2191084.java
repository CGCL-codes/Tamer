package com.taobao.top.request;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.taobao.top.util.FileItem;
import com.taobao.top.util.TopHashMap;

/**
 * TOP API: taobao.itemextra.add
 * 
 * @author carver.gu
 * @since 1.0, Apr 11, 2010
 */
public class ItemextraAddRequest implements TopUploadRequest {

    private String approveStatus;

    private Date delistTime;

    private String desc;

    private String feature;

    private String iid;

    private FileItem image;

    private Date listTime;

    private String memo;

    private Long numIid;

    private Long options;

    private String picPath;

    private String reservePrice;

    private String sellerCids;

    private Long shopId;

    private String skuExtraIds;

    private String skuIds;

    private String skuMemos;

    private String skuPrices;

    private String skuProperties;

    private String skuQuantities;

    private String title;

    private String type;

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public void setDelistTime(Date delistTime) {
        this.delistTime = delistTime;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }

    public void setImage(FileItem image) {
        this.image = image;
    }

    public void setListTime(Date listTime) {
        this.listTime = listTime;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setNumIid(Long numIid) {
        this.numIid = numIid;
    }

    public void setOptions(Long options) {
        this.options = options;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public void setReservePrice(String reservePrice) {
        this.reservePrice = reservePrice;
    }

    public void setSellerCids(String sellerCids) {
        this.sellerCids = sellerCids;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public void setSkuExtraIds(String skuExtraIds) {
        this.skuExtraIds = skuExtraIds;
    }

    public void setSkuIds(String skuIds) {
        this.skuIds = skuIds;
    }

    public void setSkuMemos(String skuMemos) {
        this.skuMemos = skuMemos;
    }

    public void setSkuPrices(String skuPrices) {
        this.skuPrices = skuPrices;
    }

    public void setSkuProperties(String skuProperties) {
        this.skuProperties = skuProperties;
    }

    public void setSkuQuantities(String skuQuantities) {
        this.skuQuantities = skuQuantities;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getApiName() {
        return "taobao.itemextra.add";
    }

    public Map<String, String> getTextParams() {
        TopHashMap params = new TopHashMap();
        params.put("approve_status", this.approveStatus);
        params.put("delist_time", this.delistTime);
        params.put("desc", this.desc);
        params.put("feature", this.feature);
        params.put("iid", this.iid);
        params.put("list_time", this.listTime);
        params.put("memo", this.memo);
        params.put("num_iid", this.numIid);
        params.put("options", this.options);
        params.put("pic_path", this.picPath);
        params.put("reserve_price", this.reservePrice);
        params.put("seller_cids", this.sellerCids);
        params.put("shop_id", this.shopId);
        params.put("sku_extra_ids", this.skuExtraIds);
        params.put("sku_ids", this.skuIds);
        params.put("sku_memos", this.skuMemos);
        params.put("sku_prices", this.skuPrices);
        params.put("sku_properties", this.skuProperties);
        params.put("sku_quantities", this.skuQuantities);
        params.put("title", this.title);
        params.put("type", this.type);
        return params;
    }

    public Map<String, FileItem> getFileParams() {
        Map<String, FileItem> params = new HashMap<String, FileItem>();
        params.put("image", this.image);
        return params;
    }
}
