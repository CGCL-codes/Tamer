package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;
import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.TaobaokeItemsConvertResponse;
import com.taobao.api.ApiRuleException;

/**
 * TOP API: taobao.taobaoke.items.convert request
 * 
 * @author auto create
 * @since 1.0, 2011-10-24 16:00:43
 */
public class TaobaokeItemsConvertRequest implements TaobaoRequest<TaobaokeItemsConvertResponse> {

    private TaobaoHashMap udfParams;

    private Long timestamp;

    /** 
	* 需返回的字段列表.可选值:num_iid,title,nick,pic_url,price,click_url,commission,ommission_rate,commission_num,commission_volume,shop_click_url,seller_credit_score,item_location,volume
;字段之间用","分隔.
	 */
    private String fields;

    /** 
	* 标识一个应用是否来在无线或者手机应用,如果是true则会使用其他规则加密点击串.如果不穿值,则默认是false.
	 */
    private Boolean isMobile;

    /** 
	* 推广者的淘宝会员昵称.注：指的是淘宝的会员登录名
	 */
    private String nick;

    /** 
	* 淘宝客商品数字id串.最大输入40个.格式如:"value1,value2,value3" 用" , "号分隔商品数字id
	 */
    private String numIids;

    /** 
	* 自定义输入串.格式:英文和数字组成;长度不能大于12个字符,区分不同的推广渠道,如:bbs,表示bbs为推广渠道;blog,表示blog为推广渠道.
	 */
    private String outerCode;

    /** 
	* 淘客用户的pid,用于生成点击串.nick和pid都传入的话,以pid为准
	 */
    private Long pid;

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getFields() {
        return this.fields;
    }

    public void setIsMobile(Boolean isMobile) {
        this.isMobile = isMobile;
    }

    public Boolean getIsMobile() {
        return this.isMobile;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNumIids(String numIids) {
        this.numIids = numIids;
    }

    public String getNumIids() {
        return this.numIids;
    }

    public void setOuterCode(String outerCode) {
        this.outerCode = outerCode;
    }

    public String getOuterCode() {
        return this.outerCode;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getPid() {
        return this.pid;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getApiMethodName() {
        return "taobao.taobaoke.items.convert";
    }

    public Map<String, String> getTextParams() {
        TaobaoHashMap txtParams = new TaobaoHashMap();
        txtParams.put("fields", this.fields);
        txtParams.put("is_mobile", this.isMobile);
        txtParams.put("nick", this.nick);
        txtParams.put("num_iids", this.numIids);
        txtParams.put("outer_code", this.outerCode);
        txtParams.put("pid", this.pid);
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

    public Class<TaobaokeItemsConvertResponse> getResponseClass() {
        return TaobaokeItemsConvertResponse.class;
    }

    public void check() throws ApiRuleException {
        RequestCheckUtils.checkNotEmpty(fields, "fields");
        RequestCheckUtils.checkNotEmpty(numIids, "numIids");
        RequestCheckUtils.checkMaxListSize(numIids, 50, "numIids");
    }
}
