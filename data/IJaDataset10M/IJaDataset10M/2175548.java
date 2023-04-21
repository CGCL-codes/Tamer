package com.taobao.api.domain;

import com.taobao.api.TaobaoObject;
import com.taobao.api.internal.mapping.ApiField;

/**
 * 淘花类目统计结构对象，  是搜索引擎统计返回回来的类目信息
 *
 * @author auto create
 * @since 1.0, null
 */
public class TaohuaCateStat extends TaobaoObject {

    private static final long serialVersionUID = 5465664774254472451L;

    /**
	 * 类目ID
	 */
    @ApiField("cate_id")
    private Long cateId;

    /**
	 * 当前类目下面的商品数量
	 */
    @ApiField("count")
    private Long count;

    /**
	 * 类目名称
	 */
    @ApiField("name")
    private String name;

    public Long getCateId() {
        return this.cateId;
    }

    public void setCateId(Long cateId) {
        this.cateId = cateId;
    }

    public Long getCount() {
        return this.count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
