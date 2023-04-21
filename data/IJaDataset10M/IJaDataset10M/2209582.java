package com.taobao.api.request;

import java.util.Map;
import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.HotelOrderGetResponse;
import com.taobao.api.ApiRuleException;

/**
 * TOP API: taobao.hotel.order.get request
 * 
 * @author auto create
 * @since 1.0, 2011-10-24 16:00:47
 */
public class HotelOrderGetRequest implements TaobaoRequest<HotelOrderGetResponse> {

    private TaobaoHashMap udfParams;

    private Long timestamp;

    /** 
	* 是否需要返回该订单的入住人列表。可选值：true，false。
	 */
    private Boolean needGuest;

    /** 
	* 酒店订单oid，必须为数字。oid，tid必须传一项，同时传递的情况下，作为查询条件的优先级由高到低依次为oid，tid。
	 */
    private Long oid;

    /** 
	* 淘宝订单tid，必须为数字。oid，tid必须传一项，同时传递的情况下，作为查询条件的优先级由高到低依次为oid，tid。
	 */
    private Long tid;

    public void setNeedGuest(Boolean needGuest) {
        this.needGuest = needGuest;
    }

    public Boolean getNeedGuest() {
        return this.needGuest;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public Long getOid() {
        return this.oid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Long getTid() {
        return this.tid;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getApiMethodName() {
        return "taobao.hotel.order.get";
    }

    public Map<String, String> getTextParams() {
        TaobaoHashMap txtParams = new TaobaoHashMap();
        txtParams.put("need_guest", this.needGuest);
        txtParams.put("oid", this.oid);
        txtParams.put("tid", this.tid);
        if (udfParams != null) {
            txtParams.putAll(this.udfParams);
        }
        return txtParams;
    }

    public void putOtherTextParam(String key, String value) {
        if (this.udfParams == null) {
            this.udfParams = new TaobaoHashMap();
        }
        this.udfParams.put(key, value);
    }

    public Class<HotelOrderGetResponse> getResponseClass() {
        return HotelOrderGetResponse.class;
    }

    public void check() throws ApiRuleException {
    }
}
