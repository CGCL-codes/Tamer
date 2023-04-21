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
 * <p> BaseAccessoriesAtegory 数据类 <br> 
 * <p>   <br>
 * @author admin,
 * @version 1.0. 2010-07-08
 *
 */
public class BaseAccessoriesAtegory extends BaseEntity {

    /**网站附件分类ID*/
    private java.lang.Integer id;

    /**分类名称*/
    private java.lang.String sortName;

    /**分类说明*/
    private java.lang.String sortNotes;

    /**父级分类id*/
    private java.lang.Integer parentId;

    /**是否有下级节点*/
    private java.lang.Integer lowerNode;

    /**分类格式*/
    private java.lang.String sortFormat;

    public BaseAccessoriesAtegory() {
    }

    public BaseAccessoriesAtegory(java.lang.Integer id) {
        this.id = id;
    }

    public void setId(java.lang.Integer value) {
        this.id = value;
    }

    public java.lang.Integer getId() {
        return this.id;
    }

    public void setSortName(java.lang.String value) {
        this.sortName = value;
    }

    public java.lang.String getSortName() {
        return this.sortName;
    }

    public void setSortNotes(java.lang.String value) {
        this.sortNotes = value;
    }

    public java.lang.String getSortNotes() {
        return this.sortNotes;
    }

    public void setParentId(java.lang.Integer value) {
        this.parentId = value;
    }

    public java.lang.Integer getParentId() {
        return this.parentId;
    }

    public void setLowerNode(java.lang.Integer value) {
        this.lowerNode = value;
    }

    public java.lang.Integer getLowerNode() {
        return this.lowerNode;
    }

    public void setSortFormat(java.lang.String value) {
        this.sortFormat = value;
    }

    public java.lang.String getSortFormat() {
        return this.sortFormat;
    }

    private Set baseAccessoriess = new HashSet(0);

    public void setBaseAccessoriess(Set<BaseAccessories> baseAccessories) {
        this.baseAccessoriess = baseAccessories;
    }

    public Set<BaseAccessories> getBaseAccessoriess() {
        return baseAccessoriess;
    }

    /** 数据类映射要在以下三个方法加入GET方法 toString() hashCode() equals(Object obj) */
    public String toString() {
        return new ToStringBuilder(this).append("Id", getId()).append("SortName", getSortName()).append("SortNotes", getSortNotes()).append("ParentId", getParentId()).append("LowerNode", getLowerNode()).append("SortFormat", getSortFormat()).toString();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).append(getSortName()).append(getSortNotes()).append(getParentId()).append(getLowerNode()).append(getSortFormat()).toHashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof BaseAccessoriesAtegory == false) return false;
        if (this == obj) return true;
        BaseAccessoriesAtegory other = (BaseAccessoriesAtegory) obj;
        return new EqualsBuilder().append(getId(), other.getId()).append(getSortName(), other.getSortName()).append(getSortNotes(), other.getSortNotes()).append(getParentId(), other.getParentId()).append(getLowerNode(), other.getLowerNode()).append(getSortFormat(), other.getSortFormat()).isEquals();
    }
}
