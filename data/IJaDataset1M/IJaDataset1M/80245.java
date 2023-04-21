package cn.mmbook.platform.model.base;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import java.util.*;
import javacommon.base.*;
import javacommon.util.*;
import cn.org.rapid_framework.util.*;
import cn.org.rapid_framework.web.util.*;
import cn.org.rapid_framework.page.*;
import cn.org.rapid_framework.page.impl.*;
import cn.mmbook.platform.model.base.*;
import cn.mmbook.platform.dao.base.*;
import cn.mmbook.platform.service.base.impl.*;
import cn.mmbook.platform.service.base.*;

/**
 * <p> BaseKeycontentReal 数据类 <br> 
 * <p>   <br>
 * @author admin,
 * @version 1.0. 2010-07-08
 *
 */
public class BaseKeycontentReal extends BaseEntity {

    /**关键字与内容关联ID*/
    private java.lang.Integer id;

    /**网站内_内容ID*/
    private java.lang.String contentId;

    /**关键字ID*/
    private java.lang.Integer keywordsId;

    public BaseKeycontentReal() {
    }

    public BaseKeycontentReal(java.lang.Integer id) {
        this.id = id;
    }

    public void setId(java.lang.Integer value) {
        this.id = value;
    }

    public java.lang.Integer getId() {
        return this.id;
    }

    public void setContentId(java.lang.String value) {
        this.contentId = value;
    }

    public java.lang.String getContentId() {
        return this.contentId;
    }

    public void setKeywordsId(java.lang.Integer value) {
        this.keywordsId = value;
    }

    public java.lang.Integer getKeywordsId() {
        return this.keywordsId;
    }

    /** 数据类映射要在以下三个方法加入GET方法 toString() hashCode() equals(Object obj) */
    public String toString() {
        return new ToStringBuilder(this).append("Id", getId()).append("ContentId", getContentId()).append("KeywordsId", getKeywordsId()).toString();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).append(getContentId()).append(getKeywordsId()).toHashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof BaseKeycontentReal == false) return false;
        if (this == obj) return true;
        BaseKeycontentReal other = (BaseKeycontentReal) obj;
        return new EqualsBuilder().append(getId(), other.getId()).append(getContentId(), other.getContentId()).append(getKeywordsId(), other.getKeywordsId()).isEquals();
    }
}
