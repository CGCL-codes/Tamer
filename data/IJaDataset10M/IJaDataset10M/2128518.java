package net.sourceforge.xsurvey.xsviewer.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.print.attribute.standard.DateTimeAtCompleted;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import net.sourceforge.xsurvey.xsviewer.service.XSurveyService;
import net.sourceforge.xsurvey.xsviewer.utils.SourceReader;
import net.sourceforge.xsurvey.xsviewer.xjc.Answerstatus;
import net.sourceforge.xsurvey.xsviewer.xjc.Choice;
import net.sourceforge.xsurvey.xsviewer.xjc.Multichoice;
import net.sourceforge.xsurvey.xsviewer.xjc.ObjectFactory;
import net.sourceforge.xsurvey.xsviewer.xjc.Question;
import net.sourceforge.xsurvey.xsviewer.xjc.Shorttext;
import net.sourceforge.xsurvey.xsviewer.xjc.Singlechoice;
import net.sourceforge.xsurvey.xsviewer.xjc.Survey;
import net.sourceforge.xsurvey.xsviewer.xjc.Surveyanswer;
import org.apache.log4j.Logger;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.support.MarshallingSource;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.w3c.dom.Document;

/**
 *
 * @author Maciej Pawlik
 */
public class SurveyController extends AbstractController {

    @Resource
    private Marshaller marshaller;

    @Resource
    private WebServiceTemplate webServiceTemplate;

    @javax.annotation.Resource
    private XSurveyService xSurveyService;

    private final String REQUIRED_FIELD = "This field is required.";

    private final String POSITIVE_VALUES = "This field accepts only positive values.";

    private final String DISABLE_FRACTION = "This field accepts only non fraction numbers.";

    private final String MIN_VALUE = "Supplied value is too low.";

    private final String MAX_VALUE = "Supplied value is too high.";

    private final String ANSWERS_ERROR = "Error while saving answers.";

    private final String ANSWERS_SAVED = "Answers saved.";

    private final String ANSWERS_INVALID = "Some answers are invalid.";

    private final String DATE_ERROR = "Date value is invalid, please use format: day/month/year, for example: 01/05/2003.";

    private final String TIME_ERROR = "Time value is invalid, please use format: HH:mm, for example: 16:45.";

    private final String SHORTTEXT_TOO_LONG = "Text is too long for this field.";

    private final String GENDER_UNKNOWN = "Please check your gender.";

    private final String CUSTOM_CHOICE_NO_DATA = "Custom data not provided.";

    private final String BAD_DATA = "Data occured while processing request.";

    private final String ONE_REQUIRED = "At least one field is required.";

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
        Map map = new HashMap();
        logger.debug("handling request...");
        Survey survey = (Survey) arg0.getSession().getAttribute("survey");
        Source skin = (Source) arg0.getSession().getAttribute("skin");
        if (survey == null || skin == null) {
            logger.error("One of sources is null.");
            map.put("error", "No data or style supplied, please use token form on the main page and provide your token.");
            return new ModelAndView("error", map);
        }
        Map params = arg0.getParameterMap();
        if (!params.isEmpty()) {
            processParams(params, survey, arg0);
        }
        Source data = new MarshallingSource(marshaller, survey);
        map.put("data", data);
        map.put("style", skin);
        logger.debug("Displaying page...");
        return new ModelAndView("survey", map);
    }

    private void processParams(Map params, Survey survey, HttpServletRequest arg0) {
        ObjectFactory factory = new ObjectFactory();
        Surveyanswer surveyanswer = factory.createSurveyanswer();
        surveyanswer.setSid(survey.getSid());
        surveyanswer.setToken((String) arg0.getSession().getAttribute("token"));
        surveyanswer.setStatus(Answerstatus.COMPLETED);
        boolean valid = true;
        if (params != null) {
            for (Object o : params.keySet()) {
                String s = (String) o;
                logger.debug("key: " + s);
                Object oo = params.get(o);
                String[] vals = (String[]) oo;
                for (String val : vals) {
                    logger.debug("\tval: " + val);
                }
            }
        } else {
            logger.debug("NULL parameters");
        }
        List<Question> questions = survey.getSurveycontent().getQuestions();
        for (Question q : questions) {
            if (q.getAnswertype().isSetNumberfield()) {
                net.sourceforge.xsurvey.xsviewer.xjc.Number number = q.getAnswertype().getNumberfield();
                logger.debug(q.getQid() + "_numberfield");
                logger.debug(params.get(q.getQid() + "_numberfield"));
                String[] answers = (String[]) params.get(q.getQid() + "_numberfield");
                String answer = answers[0].trim();
                logger.debug(answer);
                try {
                    if (answer == null || answer.equals("")) {
                        q.setError(REQUIRED_FIELD);
                        valid = false;
                        logger.debug("unvalidate");
                    } else if (number.isSetOnlyPositiveValues() && number.isOnlyPositiveValues() == true && Double.parseDouble(answer) < 0) {
                        q.setError(POSITIVE_VALUES);
                        valid = false;
                        logger.debug("unvalidate");
                    } else if (number.isSetDisableFractionPart() && number.isDisableFractionPart() == true && (answer.contains(".") || answer.contains(","))) {
                        q.setError(DISABLE_FRACTION);
                        valid = false;
                        logger.debug("unvalidate");
                    } else if (number.isSetMinValue() && Double.parseDouble(answer) < number.getMinValue().doubleValue()) {
                        q.setError(MIN_VALUE);
                        valid = false;
                        logger.debug("unvalidate");
                    } else if (number.isSetMaxValue() && Double.parseDouble(answer) > number.getMaxValue().doubleValue()) {
                        q.setError(MAX_VALUE);
                        valid = false;
                        logger.debug("unvalidate");
                    } else {
                        Surveyanswer.Question a = factory.createSurveyanswerQuestion();
                        a.setQid(q.getQid());
                        a.setNumericanswer(new BigDecimal(answer));
                        surveyanswer.getQuestions().add(a);
                        q.setError("");
                    }
                } catch (NumberFormatException e) {
                    q.setError("This is not a number");
                    valid = false;
                    logger.debug("unvalidate");
                }
                q.setAnswer(answer);
            } else if (q.getAnswertype().isSetDate()) {
                logger.debug(q.getQid() + "_date");
                logger.debug(params.get(q.getQid() + "_date"));
                String[] answers = (String[]) params.get(q.getQid() + "_date");
                String answer = answers[0].trim();
                logger.debug(answer);
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                if (answer == null || answer.equals("")) {
                    q.setError(REQUIRED_FIELD);
                    valid = false;
                    logger.debug("unvalidate");
                } else {
                    try {
                        Date d = format.parse(answer);
                        Surveyanswer.Question a = factory.createSurveyanswerQuestion();
                        a.setQid(q.getQid());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(d);
                        a.setDateanswer(cal);
                        surveyanswer.getQuestions().add(a);
                        q.setError("");
                    } catch (ParseException e) {
                        logger.error("date parse error: " + e);
                        e.printStackTrace();
                        q.setError(DATE_ERROR);
                        valid = false;
                        logger.debug("unvalidate");
                    }
                }
                q.setAnswer(answer);
            } else if (q.getAnswertype().isSetTime()) {
                logger.debug(q.getQid() + "_time");
                logger.debug(params.get(q.getQid() + "_time"));
                String[] answers = (String[]) params.get(q.getQid() + "_time");
                String answer = answers[0].trim();
                logger.debug(answer);
                DateFormat format = new SimpleDateFormat("HH:mm");
                if (answer == null || answer.equals("")) {
                    q.setError(REQUIRED_FIELD);
                    valid = false;
                    logger.debug("unvalidate");
                } else {
                    try {
                        Date d = format.parse(answer);
                        Surveyanswer.Question a = factory.createSurveyanswerQuestion();
                        a.setQid(q.getQid());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(d);
                        a.setTimeanswer(cal);
                        surveyanswer.getQuestions().add(a);
                        q.setError("");
                    } catch (ParseException e) {
                        q.setError(TIME_ERROR);
                        valid = false;
                        logger.debug("unvalidate");
                    }
                }
                q.setAnswer(answer);
            } else if (q.getAnswertype().isSetText()) {
                logger.debug(q.getQid() + "_text");
                logger.debug(params.get(q.getQid() + "_text"));
                String[] answers = (String[]) params.get(q.getQid() + "_text");
                String answer = answers[0].trim();
                logger.debug(answer);
                if (answer == null || answer.equals("")) {
                    q.setError(REQUIRED_FIELD);
                    valid = false;
                    logger.debug("unvalidate");
                } else {
                    Surveyanswer.Question a = factory.createSurveyanswerQuestion();
                    a.setQid(q.getQid());
                    a.setTextanswer(answer);
                    surveyanswer.getQuestions().add(a);
                    q.setError("");
                }
                q.setAnswer(answer);
            } else if (q.getAnswertype().isSetShorttext()) {
                Shorttext shortText = factory.createShorttext();
                logger.debug(q.getQid() + "_shorttext");
                logger.debug(params.get(q.getQid() + "_shorttext"));
                String[] answers = (String[]) params.get(q.getQid() + "_shorttext");
                String answer = answers[0].trim();
                logger.debug(answer);
                if (answer == null || answer.equals("")) {
                    q.setError(REQUIRED_FIELD);
                    valid = false;
                    logger.debug("unvalidate");
                } else if (shortText.isSetLength() && answer.length() > shortText.getLength().intValue()) {
                    q.setError(SHORTTEXT_TOO_LONG);
                    valid = false;
                    logger.debug("unvalidate");
                } else {
                    Surveyanswer.Question a = factory.createSurveyanswerQuestion();
                    a.setQid(q.getQid());
                    a.setTextanswer(answer);
                    surveyanswer.getQuestions().add(a);
                    q.setError("");
                }
                q.setAnswer(answer);
            } else if (q.getAnswertype().isSetGender()) {
                Shorttext shortText = factory.createShorttext();
                logger.debug(q.getQid() + "_gender");
                logger.debug(params.get(q.getQid() + "_gender"));
                String[] answers = (String[]) params.get(q.getQid() + "_gender");
                String answer;
                if (answers != null && answers.length > 0) {
                    answer = answers[0].trim();
                } else {
                    answer = null;
                }
                logger.debug(answer);
                if (answer == null || answer.equals("")) {
                    q.setError(REQUIRED_FIELD);
                    valid = false;
                    logger.debug("unvalidate");
                } else if (!answer.equals("male") && !answer.equals("female")) {
                    q.setError(GENDER_UNKNOWN);
                    valid = false;
                    logger.debug("unvalidate");
                } else {
                    Surveyanswer.Question a = factory.createSurveyanswerQuestion();
                    a.setQid(q.getQid());
                    a.setGenderanswer(answer);
                    surveyanswer.getQuestions().add(a);
                    q.setError("");
                }
                q.setAnswer(answer);
            } else if (q.getAnswertype().isSetSinglechoice()) {
                logger.debug(q.getQid() + "_singlechoice");
                logger.debug(params.get(q.getQid() + "_singlechoice"));
                Surveyanswer.Question a = factory.createSurveyanswerQuestion();
                a.setQid(q.getQid());
                int numChoosed = 0;
                Singlechoice single = q.getAnswertype().getSinglechoice();
                List<Choice> choices = single.getChoices();
                String[] selecteds = (String[]) params.get(q.getQid() + "_singlechoice");
                String selected;
                if (selecteds != null && selecteds.length > 0) {
                    selected = selecteds[0].trim();
                } else {
                    selected = null;
                }
                for (Choice c : choices) {
                    String key = c.getKey().toString();
                    if (key.equals(selected)) {
                        c.setAnswer(true);
                        numChoosed++;
                        q.setAnswer("");
                        Surveyanswer.Question.Singlechoice sc = factory.createSurveyanswerQuestionSinglechoice();
                        sc.setKey(c.getKey());
                        a.setSinglechoice(sc);
                        q.setCustanswer("");
                    } else {
                        c.unsetAnswer();
                    }
                }
                if (single.isSetCustomChoice()) {
                    String[] answers = (String[]) params.get(q.getQid() + "_c_singlechoice");
                    String answer;
                    if (answers != null && answers.length > 0) {
                        answer = answers[0].trim();
                    } else {
                        answer = "";
                    }
                    q.setAnswer(answer);
                    if (selected != null && selected.equals("c")) {
                        numChoosed++;
                        q.setCustanswer("true");
                        a.setSinglechoiceCustomChoice(answer);
                    } else {
                        q.setCustanswer("");
                    }
                }
                if (numChoosed > 1) {
                    q.setError(BAD_DATA);
                    valid = false;
                    logger.debug("unvalidate");
                } else if (numChoosed == 0) {
                    q.setError(REQUIRED_FIELD);
                    valid = false;
                    logger.debug("unvalidate");
                } else {
                    surveyanswer.getQuestions().add(a);
                    q.setError("");
                }
                for (Object o : params.keySet()) {
                    String s = (String) o;
                    logger.debug("key: " + s);
                    Object oo = params.get(o);
                    String[] vals = (String[]) oo;
                    for (String val : vals) {
                        logger.debug("\tval: " + val);
                    }
                }
            } else if (q.getAnswertype().isSetMultichoice()) {
                logger.debug(q.getQid() + "_multichoice");
                logger.debug(params.get(q.getQid() + "_multichoice"));
                Surveyanswer.Question a = factory.createSurveyanswerQuestion();
                a.setQid(q.getQid());
                Surveyanswer.Question.Multichoice amulti = factory.createSurveyanswerQuestionMultichoice();
                int numChoosed = 0;
                Multichoice multi = q.getAnswertype().getMultichoice();
                List<Choice> choices = multi.getChoices();
                for (Choice c : choices) {
                    String[] selecteds = (String[]) params.get(q.getQid() + "_" + c.getKey().toString() + "_multichoice");
                    if (selecteds != null && selecteds[0] != null && !selecteds[0].isEmpty()) {
                        numChoosed++;
                        c.setAnswer(true);
                        Surveyanswer.Question.Multichoice.Choice ans = factory.createSurveyanswerQuestionMultichoiceChoice();
                        ans.setKey(c.getKey());
                        amulti.getChoices().add(ans);
                    } else {
                        c.unsetAnswer();
                    }
                }
                String[] answers = (String[]) params.get(q.getQid() + "_multichoice");
                String answer;
                if (answers != null && answers[0] != null) {
                    answer = answers[0];
                } else {
                    answer = "";
                }
                q.setAnswer(answer);
                String[] selecteds = (String[]) params.get(q.getQid() + "_c_multichoice");
                if (selecteds != null && selecteds[0] != null && !selecteds[0].isEmpty()) {
                    numChoosed++;
                    q.setCustanswer("true");
                    amulti.setCustomchoice(answer);
                } else {
                    q.setCustanswer("");
                }
                a.setMultichoice(amulti);
                if (numChoosed > 0) {
                    surveyanswer.getQuestions().add(a);
                    q.setError("");
                } else {
                    q.setError(REQUIRED_FIELD);
                    valid = false;
                    logger.debug("unvalidate");
                }
            }
        }
        if (valid) {
            try {
                logger.debug("sending...");
                String status = xSurveyService.saveSurveyAnswer(surveyanswer);
                logger.debug("Status: " + status);
                if (status.equals("OK")) {
                    survey.setMessage(ANSWERS_SAVED);
                    logger.debug("sent sucessfully!");
                } else {
                    logger.debug("status bad");
                    throw new Exception(ANSWERS_ERROR);
                }
                survey.getSurveycontent().getQuestions().clear();
            } catch (Exception e) {
                logger.debug("exception during send");
                e.printStackTrace();
                survey.setMessage(ANSWERS_ERROR);
            }
        } else {
            survey.setMessage(ANSWERS_INVALID);
        }
    }

    public WebServiceTemplate getWebServiceTemplate() {
        return webServiceTemplate;
    }

    public void setWebServiceTemplate(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    public Marshaller getMarshaller() {
        return marshaller;
    }

    public void setMarshaller(Marshaller marshaller) {
        this.marshaller = marshaller;
    }
}
