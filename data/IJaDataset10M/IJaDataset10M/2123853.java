package com.taobao.top.domain;

import com.taobao.top.mapping.ApiClass;
import com.taobao.top.mapping.ApiField;
import com.taobao.top.mapping.ApiListClass;

/**
 * Brand Data Structure.
 *
 * @author carver.gu
 * @since 1.0, Apr 11, 2010
 */
@ApiClass("brand")
@ApiListClass("brands")
public class Brand extends BaseObject {

    private static final long serialVersionUID = 1L;

    @ApiField("name")
    private String name;

    @ApiField("pid")
    private Long pid;

    @ApiField("prop_name")
    private String propName;

    @ApiField("vid")
    private Long vid;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPid() {
        return this.pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getPropName() {
        return this.propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public Long getVid() {
        return this.vid;
    }

    public void setVid(Long vid) {
        this.vid = vid;
    }
}
