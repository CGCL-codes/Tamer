package squirrels.action.images;

import java.io.ByteArrayInputStream;
import java.util.List;
import squirrels.persistence.bean.CategoryImage;
import squirrels.persistence.dao.CategoryImageDAO;
import squirrels.persistence.example.CategoryImageExample;

/**
 * CategoryImageAction
 */
public class CategoryImageAction extends AbstractImageDownloadAction {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = -2985961943270918184L;

    private Integer id;

    private Integer categoryId;

    private String kind;

    /**
	 * @return the id
	 */
    public Integer getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
	 * @return the categoryId
	 */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
	 * @param categoryId the categoryId to set
	 */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
	 * @return the kind
	 */
    public String getKind() {
        return kind;
    }

    /**
	 * @param kind the kind to set
	 */
    public void setKind(String kind) {
        this.kind = kind;
    }

    /**
	 * @return the kind
	 */
    public String getK() {
        return kind;
    }

    /**
	 * @param kind the kind to set
	 */
    public void setK(String kind) {
        this.kind = kind;
    }

    /**
	 * @return the categoryId
	 */
    public Integer getCid() {
        return categoryId;
    }

    /**
	 * @param categoryId the categoryId to set
	 */
    public void setCid(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
	 * image
	 * @return result name
	 * @throws Exception if an error occurs
	 */
    public String image() throws Exception {
        CategoryImageExample example = new CategoryImageExample();
        if (id != null) {
            example.andIdEqualTo(id);
        } else if (categoryId != null) {
            if (kind == null) {
                kind = getText("category.image.size.default", IMAGE_SIZE_MEDIUM);
            }
            example.andKindEqualTo(kind).andCategoryIdEqualTo(categoryId);
        }
        if (!example.getRestrictions().isEmpty()) {
            CategoryImageDAO dao = new CategoryImageDAO(getSqlMapClient());
            List<CategoryImage> list = dao.selectByExample(example, 0, 1);
            if (list.size() > 0) {
                CategoryImage ci = list.get(0);
                setImageStream(new ByteArrayInputStream(ci.getImage().getData()));
                setImageName("C" + ci.getId() + ".jpg");
                return SUCCESS;
            }
        }
        return getNoImageResult(kind == null ? IMAGE_SIZE_SMALL : kind);
    }
}
