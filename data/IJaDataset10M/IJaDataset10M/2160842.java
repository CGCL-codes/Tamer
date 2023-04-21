package twoadw.website.questioncategory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelibra.IDomainModel;
import twoadw.website.questioncategory.QuestionCategory;

/**
 * QuestionCategory specific entities.
 * 
 * @author TeamFcp
 * @version 2009-03-16
 */
public class QuestionCategories extends GenQuestionCategories {

    private static final long serialVersionUID = 1236738768965L;

    private static Log log = LogFactory.getLog(QuestionCategories.class);

    /**
	 * Constructs questionCategories within the domain model.
	 * 
	 * @param model
	 *            model
	 */
    public QuestionCategories(IDomainModel model) {
        super(model);
    }

    /**
		 * Constructs questionCategories for the questionCategory parent.
		 * 
		 * @param questionCategory
		 *            questionCategory
		 */
    public QuestionCategories(QuestionCategory questionCategory) {
        super(questionCategory);
    }
}
