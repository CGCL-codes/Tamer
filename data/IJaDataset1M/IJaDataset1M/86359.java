package bma.bricks.otter.model.category.site.database;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import bma.bricks.otter.model.category.feature.CategoryUtil;
import bma.bricks.otter.model.feature.database.BaseOtterEntity;

/**
 * ��վ����PO
 * 
 * 
 */
@Entity
@Table(name = "site_category")
public class SiteCategory extends BaseOtterEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
	 * ����
	 */
    @Id
    @GeneratedValue
    private Integer id;

    /**
	 * ������Id
	 */
    @Column(name = "parent_id", nullable = false)
    private int parentId;

    @Column(name = "parent_path", nullable = false)
    private String parentPath;

    /**
	 * �Ƿ񹫿�����
	 */
    @Column(name = "public_visible", nullable = false)
    private int publicVisible;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getParentId() {
        return this.parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getParentPath() {
        return this.parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public int getPublicVisible() {
        return this.publicVisible;
    }

    public void setPublicVisible(int publicVisible) {
        this.publicVisible = publicVisible;
    }

    public String getCategoryPath() {
        return CategoryUtil.buildPath(this.parentPath, this.id);
    }
}
