package com.taobao.api.model;

import java.io.File;
import java.util.Date;

/**
 * 
 * 
 * @author liupo <liupo@taobao.com>
 * @version 1.0
 **/
public class ProductPropImg extends TaobaoModel {

    private static final long serialVersionUID = -5655899416065863432L;

    private String productId;

    private String picId;

    private String props;

    private String url;

    private int position;

    private Date created;

    private Date modified;

    private File image;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProps() {
        return props;
    }

    public void setProps(String props) {
        this.props = props;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public String getPicId() {
        return picId;
    }

    public void setPicId(String picId) {
        this.picId = picId;
    }
}
