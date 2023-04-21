package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;
import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.FenxiaoDistributorsGetResponse;
import com.taobao.api.ApiRuleException;

/**
 * TOP API: taobao.fenxiao.distributors.get request
 * 
 * @author auto create
 * @since 1.0, 2011-10-24 16:00:43
 */
public class FenxiaoDistributorsGetRequest implements TaobaoRequest<FenxiaoDistributorsGetResponse> {

    private TaobaoHashMap udfParams;

    private Long timestamp;

    /** 
	* 分销商用户名列表。多个之间以“,”分隔;最多支持50个分销商用户名。
	 */
    private String nicks;

    public void setNicks(String nicks) {
        this.nicks = nicks;
    }

    public String getNicks() {
        return this.nicks;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getApiMethodName() {
        return "taobao.fenxiao.distributors.get";
    }

    public Map<String, String> getTextParams() {
        TaobaoHashMap txtParams = new TaobaoHashMap();
        txtParams.put("nicks", this.nicks);
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

    public Class<FenxiaoDistributorsGetResponse> getResponseClass() {
        return FenxiaoDistributorsGetResponse.class;
    }

    public void check() throws ApiRuleException {
        RequestCheckUtils.checkNotEmpty(nicks, "nicks");
    }
}
