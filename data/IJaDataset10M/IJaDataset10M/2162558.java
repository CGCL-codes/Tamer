package com.taobao.api.request;

import com.taobao.api.internal.util.RequestCheckUtils;
import java.util.Map;
import com.taobao.api.TaobaoRequest;
import com.taobao.api.internal.util.TaobaoHashMap;
import com.taobao.api.response.TaohuaAudioreaderSearchResponse;
import com.taobao.api.ApiRuleException;

/**
 * TOP API: taobao.taohua.audioreader.search request
 * 
 * @author auto create
 * @since 1.0, 2011-10-24 16:00:46
 */
public class TaohuaAudioreaderSearchRequest implements TaobaoRequest<TaohuaAudioreaderSearchResponse> {

    private TaobaoHashMap udfParams;

    private Long timestamp;

    /** 
	* 类目id
	 */
    private Long cid;

    /** 
	* 是否免费，如果为true则表示只搜索免费的商品
	 */
    private Boolean free;

    /** 
	* 查询关键字,超过60个字符则自动截断为60个字符. 允许为空
	 */
    private String keyword;

    /** 
	* 当前页码
	 */
    private Long pageNo;

    /** 
	* 每页个数
	 */
    private Long pageSize;

    /** 
	* 排序值: 1. 评分排序：ratescoredesc, 2. 价格升序：priceasc, 3. 价格降序：pricedesc, 4. 最新发布：shelvesdate, 5. 最多浏览：viewcount, 6. 销量升序：saleasc, 7. 销量降序：saledesc, 8. 最受欢迎：favoritedesc, 9. 默认排序：default
	 */
    private String sort;

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Long getCid() {
        return this.cid;
    }

    public void setFree(Boolean free) {
        this.free = free;
    }

    public Boolean getFree() {
        return this.free;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setPageNo(Long pageNo) {
        this.pageNo = pageNo;
    }

    public Long getPageNo() {
        return this.pageNo;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageSize() {
        return this.pageSize;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSort() {
        return this.sort;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getApiMethodName() {
        return "taobao.taohua.audioreader.search";
    }

    public Map<String, String> getTextParams() {
        TaobaoHashMap txtParams = new TaobaoHashMap();
        txtParams.put("cid", this.cid);
        txtParams.put("free", this.free);
        txtParams.put("keyword", this.keyword);
        txtParams.put("page_no", this.pageNo);
        txtParams.put("page_size", this.pageSize);
        txtParams.put("sort", this.sort);
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

    public Class<TaohuaAudioreaderSearchResponse> getResponseClass() {
        return TaohuaAudioreaderSearchResponse.class;
    }

    public void check() throws ApiRuleException {
        RequestCheckUtils.checkMaxValue(pageNo, 100L, "pageNo");
        RequestCheckUtils.checkMinValue(pageNo, 1L, "pageNo");
        RequestCheckUtils.checkMaxValue(pageSize, 100L, "pageSize");
        RequestCheckUtils.checkMinValue(pageSize, 1L, "pageSize");
    }
}
