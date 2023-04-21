package twoadw.website.questioncategory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelibra.Entity;
import org.modelibra.IDomainModel;
import org.modelibra.Oid;
import twoadw.website.Website;
import twoadw.website.questioncategory.QuestionCategory;
import twoadw.website.questioncategory.QuestionCategories;
import twoadw.website.qqcategory.QQCategories;
import twoadw.website.question.Questions;

/**
 * QuestionCategory generated entity. This class should not be changed manually. 
 * Use a subclass to add specific code.
 * 
 * @author TeamFcp
 * @version 2009-03-16
 */
public abstract class GenQuestionCategory extends Entity<QuestionCategory> {

    private static final long serialVersionUID = 1236738768962L;

    private static Log log = LogFactory.getLog(GenQuestionCategory.class);

    private String name;

    private Boolean published;

    private QuestionCategory questionCategory;

    private QuestionCategories questionCategories;

    private QQCategories qQCategories;

    /**
	 * Constructs questionCategory within the domain model.
	 * 
	 * @param model
	 *            model
	 */
    public GenQuestionCategory(IDomainModel model) {
        super(model);
        setQuestionCategories(new QuestionCategories((QuestionCategory) this));
    }

    /**
	     * Constructs questionCategory within its parent(s).
	     * 
	        		* @param QuestionCategory questionCategory
			     */
    public GenQuestionCategory(QuestionCategory questionCategory) {
        this(questionCategory.getModel());
        setQuestionCategory(questionCategory);
    }

    /**
		 * Sets name.
		 * 
		 * @param name
		 *            name
		 */
    public void setName(String name) {
        this.name = name;
    }

    /**
		 * Gets name.
		 * 
		 * @return name
		 */
    public String getName() {
        return name;
    }

    /**
		 * Sets published.
		 * 
		 * @param published
		 *            published
		 */
    public void setPublished(Boolean published) {
        this.published = published;
    }

    /**
		 * Gets published.
		 * 
		 * @return published
		 */
    public Boolean getPublished() {
        return published;
    }

    /**
		     * Sets published.
		     * 
		     * @param published
		     *            published
		     */
    public void setPublished(boolean published) {
        setPublished(new Boolean(published));
    }

    /**
	          * Checks if it is <code>true</code> or <code>false</code>.
	          * 
	          * @return <code>true</code> or <code>false</code>
	          */
    public boolean isPublished() {
        return getPublished().booleanValue();
    }

    /**
		 * Sets questionCategory.
		 * 
		 * @param questionCategory
		 *            questionCategory
		 */
    public void setQuestionCategory(QuestionCategory questionCategory) {
        this.questionCategory = questionCategory;
    }

    /**
		 * Gets questionCategory.
		 * 
		 * @return questionCategory
		 */
    public QuestionCategory getQuestionCategory() {
        return questionCategory;
    }

    /**
		 * Sets questionCategories.
		 * 
		 * @param questionCategories
		 *            questionCategories
		 */
    public void setQuestionCategories(QuestionCategories questionCategories) {
        this.questionCategories = questionCategories;
        if (questionCategories != null) {
            questionCategories.setQuestionCategory((QuestionCategory) this);
        }
    }

    /**
		 * Gets questionCategories.
		 * 
		 * @return questionCategories
		 */
    public QuestionCategories getQuestionCategories() {
        return questionCategories;
    }

    /**
			 * Sets qQCategories.
			 * 
			 * @param qQCategories
			 *            qQCategories
			 */
    public void setQQCategories(QQCategories qQCategories) {
        this.qQCategories = qQCategories;
        if (qQCategories != null) {
            qQCategories.setQuestionCategory((QuestionCategory) this);
        }
    }

    /**
			 * Gets qQCategories.
			 * 
			 * @return qQCategories
			 */
    public QQCategories getQQCategories() {
        if (qQCategories == null) {
            Website website = (Website) getModel();
            Questions questions = website.getQuestions();
            setQQCategories(questions.getQuestionCategoryQQCategories((QuestionCategory) this));
        }
        return qQCategories;
    }
}
