package com.taobao.top.domain;

import java.util.Date;
import com.taobao.top.mapping.ApiClass;
import com.taobao.top.mapping.ApiField;
import com.taobao.top.mapping.ApiListClass;

/**
 * ProductPropImg Data Structure.
 *
 * @author carver.gu
 * @since 1.0, Apr 11, 2010
 */
@ApiClass("product_prop_img")
@ApiListClass("product_prop_imgs")
public class ProductPropImg extends BaseObject {

    private static final long serialVersionUID = 1L;

    @ApiField("created")
    private Date created;

    @ApiField("id")
    private Long id;

    @ApiField("modified")
    private Date modified;

    @ApiField("position")
    private Integer position;

    @ApiField("product_id")
    private Long productId;

    @ApiField("props")
    private String props;

    @ApiField("url")
    private String url;

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getModified() {
        return this.modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Integer getPosition() {
        return this.position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProps() {
        return this.props;
    }

    public void setProps(String props) {
        this.props = props;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
