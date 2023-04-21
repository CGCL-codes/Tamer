package whf.survey.service;

import whf.framework.exception.ServiceNotFoundException;
import whf.framework.util.BeanFactory;
import whf.survey.dao.SurveyAnswerDAO;
import whf.survey.entity.SurveyAnswer;

/**
 * @author wanghaifeng
 * @email king@126.com
 * @modify 2006-09-12
 */
public class SurveyAnswerServiceImp extends whf.framework.service.ServiceImp<SurveyAnswer> implements SurveyAnswerService {

    private SurveyAnswerDAO surveyAnswerDAO;

    /**
	 * @modify wanghaifeng Sep 12, 2006 10:26:46 PM
	 * @param surveyAnswerDAO
	 */
    public void setSurveyAnswerDAO(SurveyAnswerDAO surveyAnswerDAO) {
        this.surveyAnswerDAO = surveyAnswerDAO;
    }

    public static SurveyAnswerService getSurveyAnswerService() throws ServiceNotFoundException {
        return (SurveyAnswerService) BeanFactory.getService(SurveyAnswerServiceImp.class);
    }
}
