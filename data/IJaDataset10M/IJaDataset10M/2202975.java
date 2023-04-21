package twoadw.website.questioncategory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelibra.IDomainModel;
import twoadw.website.Website;
import twoadw.website.questioncategory.QuestionCategory;
import twoadw.website.questioncategory.QuestionCategories;

/**
 * QuestionCategory specific entity.
 * 
 * @author TeamFcp
 * @version 2009-03-16
 */
public class QuestionCategory extends GenQuestionCategory {

    private static final long serialVersionUID = 1236738768964L;

    private static Log log = LogFactory.getLog(QuestionCategory.class);

    /**
	 * Constructs questionCategory within the domain model.
	 * 
	 * @param model
	 *            model
	 */
    public QuestionCategory(IDomainModel model) {
        super(model);
    }

    /**
	     * Constructs questionCategory within its parent(s).
	     * 
	        		* @param QuestionCategory questionCategory
			     */
    public QuestionCategory(QuestionCategory questionCategory) {
        super(questionCategory);
    }
}
