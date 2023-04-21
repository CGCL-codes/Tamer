package com.taobao.top.domain;

import com.taobao.top.mapping.ApiClass;
import com.taobao.top.mapping.ApiField;
import com.taobao.top.mapping.ApiListClass;

/**
 * Feature Data Structure.
 *
 * @author carver.gu
 * @since 1.0, Apr 11, 2010
 */
@ApiClass("feature")
@ApiListClass("features")
public class Feature extends BaseObject {

    private static final long serialVersionUID = 1L;

    @ApiField("attr_key")
    private String attrKey;

    @ApiField("attr_value")
    private String attrValue;

    public String getAttrKey() {
        return this.attrKey;
    }

    public void setAttrKey(String attrKey) {
        this.attrKey = attrKey;
    }

    public String getAttrValue() {
        return this.attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }
}
