package dmeduc.weblink.question;

import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelibra.Entity;
import org.modelibra.IDomainModel;
import dmeduc.weblink.WebLink;
import dmeduc.weblink.category.Categories;
import dmeduc.weblink.category.Category;

/**
 * Question generated entity. This class should not be changed manually. Use a
 * subclass to add specific code.
 * 
 * @author Dzenan Ridjanovic
 * @version 2007-11-07
 */
public abstract class GenQuestion extends Entity<Question> {

    private static final long serialVersionUID = 1171896744338L;

    private static Log log = LogFactory.getLog(GenQuestion.class);

    private Integer number;

    private String type;

    private String text;

    private String response;

    private Date creationDate;

    private Float points;

    private Long categoryOid;

    private transient Category category;

    /**
	 * Constructs question within the domain model.
	 * 
	 * @param model
	 *            model
	 */
    public GenQuestion(IDomainModel model) {
        super(model);
    }

    /**
	 * Constructs question within its parent(s).
	 * 
	 * @param Category
	 *            category
	 */
    public GenQuestion(Category category) {
        this(category.getModel());
        setCategory(category);
    }

    /**
	 * Sets number.
	 * 
	 * @param number
	 *            number
	 */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
	 * Gets number.
	 * 
	 * @return number
	 */
    public Integer getNumber() {
        return number;
    }

    /**
	 * Sets type.
	 * 
	 * @param type
	 *            type
	 */
    public void setType(String type) {
        this.type = type;
    }

    /**
	 * Gets type.
	 * 
	 * @return type
	 */
    public String getType() {
        return type;
    }

    /**
	 * Sets text.
	 * 
	 * @param text
	 *            text
	 */
    public void setText(String text) {
        this.text = text;
    }

    /**
	 * Gets text.
	 * 
	 * @return text
	 */
    public String getText() {
        return text;
    }

    /**
	 * Sets response.
	 * 
	 * @param response
	 *            response
	 */
    public void setResponse(String response) {
        this.response = response;
    }

    /**
	 * Gets response.
	 * 
	 * @return response
	 */
    public String getResponse() {
        return response;
    }

    /**
	 * Sets creationDate.
	 * 
	 * @param creationDate
	 *            creationDate
	 */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
	 * Gets creationDate.
	 * 
	 * @return creationDate
	 */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
	 * Sets points.
	 * 
	 * @param points
	 *            points
	 */
    public void setPoints(Float points) {
        this.points = points;
    }

    /**
	 * Gets points.
	 * 
	 * @return points
	 */
    public Float getPoints() {
        return points;
    }

    /**
	 * Sets categoryOid.
	 * 
	 * @param categoryOid
	 *            categoryOid
	 */
    public void setCategoryOid(Long categoryOid) {
        this.categoryOid = categoryOid;
        category = null;
    }

    /**
	 * Gets categoryOid.
	 * 
	 * @return categoryOid
	 */
    public Long getCategoryOid() {
        return categoryOid;
    }

    /**
	 * Sets category.
	 * 
	 * @param category
	 *            category
	 */
    public void setCategory(Category category) {
        this.category = category;
        if (category != null) {
            categoryOid = category.getOid().getUniqueNumber();
        } else {
            categoryOid = null;
        }
    }

    /**
	 * Gets category.
	 * 
	 * @return category
	 */
    public Category getCategory() {
        if (category == null) {
            WebLink webLink = (WebLink) getModel();
            Categories categories = webLink.getCategories();
            if (categoryOid != null) {
                category = categories.getReflexiveCategory(categoryOid);
            }
        }
        return category;
    }
}
