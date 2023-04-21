package whf.survey.web;

import java.util.List;
import java.util.Map;
import whf.framework.entity.AbstractEntity;
import whf.framework.entity.Entity;
import whf.framework.security.entity.Dept;
import whf.framework.security.entity.User;
import whf.framework.security.service.DeptService;
import whf.framework.security.service.DeptServiceImp;
import whf.framework.util.StringUtils;
import whf.framework.util.Utils;
import whf.framework.web.HttpHelper;
import whf.framework.web.struts.WebData;
import whf.framework.web.struts.WebDispatchAction;
import whf.framework.web.struts.WebForward;
import whf.survey.entity.AnswerResult;
import whf.survey.entity.Survey;
import whf.survey.entity.SurveyAnswer;
import whf.survey.entity.SurveyPublish;
import whf.survey.entity.SurveyTopic;
import whf.survey.entity.SurveyTopicChoice;
import whf.survey.service.SurveyAnswerService;
import whf.survey.service.SurveyAnswerServiceImp;
import whf.survey.service.SurveyService;
import whf.survey.service.SurveyServiceImp;
import whf.survey.service.SurveyTopicChoiceService;
import whf.survey.service.SurveyTopicChoiceServiceImp;
import whf.survey.service.SurveyTopicService;
import whf.survey.service.SurveyTopicServiceImp;
import whf.survey.util.Constants;

/**
 * @author wanghaifeng
 * @email king@126.com
 * @modify 2006-09-12
 */
public class SurveyAction extends WebDispatchAction {

    @Override
    public WebForward create(AbstractEntity webEntity, WebData data) throws Exception {
        Survey survey = (Survey) webEntity;
        survey.setStatus(Constants.SURVEY_STATUS_CREATED);
        return super.create(webEntity, data);
    }

    @Override
    public WebForward delete(AbstractEntity webEntity, WebData data) throws Exception {
        SurveyService service = SurveyServiceImp.getSurveyService();
        long ids[] = data.getLongParameters("id", -1);
        if (ids != null) for (long id : ids) {
            if (id == -1) continue;
            Survey survey = service.findByPrimaryKey(id);
            service.remove(survey);
        }
        return data.forward("list");
    }

    @Override
    public WebForward list(AbstractEntity webEntity, WebData data) throws Exception {
        return super.list(webEntity, data);
    }

    public WebForward deployedList(AbstractEntity webEntity, WebData data) throws Exception {
        User user = data.getCurrentUser();
        long deptId = 0;
        if (user != null && user.getDept() != null) {
            deptId = user.getDept().getId();
        }
        String queryString = "t.status='" + Constants.SURVEY_STATUS_PUBLISHED + "'";
        if (deptId > 0) {
            queryString += " and t.id in( select t1.survey.id from " + SurveyPublish.class.getName() + " t1 where t1.dept.id=" + deptId + ")";
        }
        queryString += " order by t.startDate desc ";
        data.setAttribute("queryString", queryString);
        return data.forward("deployedList");
    }

    @Override
    public WebForward prepareCreate(AbstractEntity webEntity, WebData data) throws Exception {
        Survey survey = (Survey) webEntity;
        survey.setStatus(Constants.SURVEY_STATUS_CREATED);
        return super.prepareCreate(webEntity, data);
    }

    public WebForward viewOrEdit(AbstractEntity webEntity, WebData data) throws Exception {
        SurveyService service = SurveyServiceImp.getSurveyService();
        Survey survey = service.findByPrimaryKey(webEntity.getId());
        if (StringUtils.equalsIgnoreCase(survey.getStatus(), Constants.SURVEY_STATUS_CREATED)) {
            return prepareUpdate(survey, data);
        } else {
            return view(survey, data);
        }
    }

    public WebForward pub(AbstractEntity webEntity, WebData data) throws Exception {
        String deptIds = data.getParameter("deptIds");
        if (deptIds != null) {
            String[] deptIdArray = StringUtils.split(deptIds, ",");
            DeptService ds = DeptServiceImp.getDeptService();
            List<Dept> depts = Utils.newArrayList();
            for (String deptId : deptIdArray) {
                depts.add(ds.findByPrimaryKey(Long.parseLong(deptId)));
            }
            SurveyService service = SurveyServiceImp.getSurveyService();
            Survey survey = service.findByPrimaryKey(webEntity.getId());
            service.publish(survey, depts);
        }
        return list(webEntity, data);
    }

    public WebForward viewTopics(AbstractEntity webEntity, WebData data) throws Exception {
        webEntity.setWebMethod("answer");
        return data.forward("viewTopics");
    }

    public WebForward answer(AbstractEntity webEntity, WebData data) throws Exception {
        SurveyService surveyService = SurveyServiceImp.getSurveyService();
        SurveyTopicService topicService = SurveyTopicServiceImp.getSurveyTopicService();
        SurveyTopicChoiceService choiceService = SurveyTopicChoiceServiceImp.getSurveyTopicChoiceService();
        SurveyAnswerService answerService = SurveyAnswerServiceImp.getSurveyAnswerService();
        User user = data.getCurrentUser();
        Survey survey = surveyService.findByPrimaryKey(webEntity.getId());
        long[] topicIds = data.getLongParameters("topic", 0);
        if (topicIds != null) for (long topicId : topicIds) {
            if (topicId == 0) continue;
            SurveyTopic topic = topicService.findByPrimaryKey(topicId);
            String option = topic.getChoiceType();
            if ("01".equals(option)) {
                long choiceId = data.getLongParameter("topic" + topicId + "id", 0);
                if (choiceId == 0) continue;
                SurveyTopicChoice choice = choiceService.findByPrimaryKey(choiceId);
                SurveyAnswer answer = new SurveyAnswer(survey, topic, user, choice);
                answerService.create(answer);
            } else if ("02".equals(option)) {
                long[] choiceIds = data.getLongParameters("topic" + topicId + "id", 0);
                if (choiceIds == null) continue;
                for (long choiceId : choiceIds) {
                    SurveyTopicChoice choice = choiceService.findByPrimaryKey(choiceId);
                    SurveyAnswer answer = new SurveyAnswer(survey, topic, user, choice);
                    answerService.create(answer);
                }
            } else {
                String answerResult = data.getParameter("topic" + topicId + "id");
                if (!StringUtils.isEmpty(answerResult)) {
                    SurveyAnswer answer = new SurveyAnswer(survey, topic, user, answerResult);
                    answerService.create(answer);
                }
            }
        }
        return answers(webEntity, data);
    }

    public WebForward answers(AbstractEntity webEntity, WebData data) throws Exception {
        SurveyService surveyService = SurveyServiceImp.getSurveyService();
        SurveyTopicService topicService = SurveyTopicServiceImp.getSurveyTopicService();
        SurveyAnswerService answerService = SurveyAnswerServiceImp.getSurveyAnswerService();
        Survey survey = surveyService.findByPrimaryKey(webEntity.getId());
        data.setAttribute(survey);
        List<SurveyTopic> topics = topicService.findWithoutLazy("t.survey=? order by t.sortOrder", survey);
        data.setAttribute("topics", topics);
        Map<SurveyTopic, List<AnswerResult>> topicChoices = Utils.newHashMap();
        for (Entity bo : topics) {
            SurveyTopic topic = (SurveyTopic) bo;
            String status = topic.getChoiceType();
            List<AnswerResult> results = Utils.newArrayList();
            if ("01".equals(status) || "02".equals(status)) {
                List<SurveyTopicChoice> choices = topic.getSortChoices();
                for (SurveyTopicChoice choice : choices) {
                    int count = answerService.getRowCount("from " + SurveyAnswer.class.getName() + " t where t.choice=?", new Object[] { choice });
                    results.add(new AnswerResult(status, choice, count));
                }
                int totalCount = 0;
                for (AnswerResult ar : results) {
                    totalCount += ar.getCount();
                }
                for (AnswerResult ar : results) {
                    ar.setTotalCount(totalCount);
                }
            } else {
                List<SurveyAnswer> textAnswers = answerService.find("t.topic=?", topic);
                for (Entity tAnswer : textAnswers) {
                    String t = ((SurveyAnswer) tAnswer).getAnswer();
                    if (!StringUtils.isEmpty(t)) {
                        results.add(new AnswerResult("00", t));
                    }
                }
            }
            topicChoices.put(topic, results);
        }
        data.setAttribute("topicAnswers", topicChoices);
        return data.forward("answers");
    }
}
