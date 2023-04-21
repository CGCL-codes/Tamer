package com.incendiaryblue.cmslite;

import com.incendiaryblue.storage.BusinessObject;
import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a reference to a Content item 
 *
 * Stupid name because interface ContentRefernce already existed and a refactor during the hibernate implementation is far from ideal.
 *
 * @author guy
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue("0")
@Table(name = "category_content")
@NamedQueries({ @NamedQuery(name = "contentReference.find", query = "SELECT cc FROM CategoryContent cc " + "WHERE cc.categoryContentPK.contentId =:contentIdParam " + "AND cc.categoryContentPK.categoryId =:categoryIdParam ") })
public class CategoryContent extends BusinessObject implements Serializable {

    public static final String GET_CONTENT_REF = "contentReference.find";

    @EmbeddedId
    private CategoryContentPK categoryContentPK;

    @Column(name = "TYPE", insertable = false, updatable = false)
    private Integer type;

    public CategoryContent() {
    }

    public CategoryContent(Integer contentId, Integer categoryId, Integer type) {
        categoryContentPK = new CategoryContentPK();
        categoryContentPK.contentId = contentId;
        categoryContentPK.categoryId = categoryId;
        this.type = type;
    }

    public Integer getContentId() {
        return categoryContentPK.contentId;
    }

    public void setContentId(Integer contentId) {
        if (categoryContentPK == null) {
            categoryContentPK = new CategoryContentPK();
        }
        categoryContentPK.contentId = contentId;
    }

    public Integer getCategoryId() {
        return categoryContentPK.categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        if (categoryContentPK == null) {
            categoryContentPK = new CategoryContentPK();
        }
        categoryContentPK.categoryId = categoryId;
    }

    public CategoryContentPK getCategoryContentPK() {
        return categoryContentPK;
    }

    public void setCategoryContentPK(CategoryContentPK categoryContentPK) {
        this.categoryContentPK = categoryContentPK;
    }

    /**
     * Composite key for content reference. Hibernate requires an ID
     *
     * @author guy
     */
    @Embeddable
    static class CategoryContentPK implements Serializable {

        @Column(name = "CONTENT_ID")
        private Integer contentId;

        @Column(name = "CATEGORY_ID")
        private Integer categoryId;

        public Integer getContentId() {
            return contentId;
        }

        public Integer getCategoryId() {
            return categoryId;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CategoryContentPK)) return false;
            CategoryContentPK that = (CategoryContentPK) o;
            if (categoryId != null ? !categoryId.equals(that.categoryId) : that.categoryId != null) return false;
            if (contentId != null ? !contentId.equals(that.contentId) : that.contentId != null) return false;
            return true;
        }

        public int hashCode() {
            int result;
            result = (contentId != null ? contentId.hashCode() : 0);
            result = 31 * result + (categoryId != null ? categoryId.hashCode() : 0);
            return result;
        }

        public String toString() {
            return "CategoryContentPK{" + "contentId=" + contentId + ", categoryId=" + categoryId + '}';
        }
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryContent)) return false;
        if (!super.equals(o)) return false;
        CategoryContent that = (CategoryContent) o;
        if (categoryContentPK != null ? !categoryContentPK.equals(that.categoryContentPK) : that.categoryContentPK != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        return true;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (categoryContentPK != null ? categoryContentPK.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    /**
     * //todo guy remove horrible getPrimary key methods and move to a standardized way, mixed at the moment
     * Following the Integer2Key convention for now
     * @return
     */
    public Object getPrimaryKey() {
        return getCategoryId() + ":" + getContentId();
    }
}
