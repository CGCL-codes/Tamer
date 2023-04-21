package edu.uah.elearning.qti.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.qtitools.playr.PlayrCoordinator;
import org.qtitools.playr.R2Q2Exception;
import org.qtitools.playr.rendering.core.RenderingException;
import org.qtitools.qti.exception.QTIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.uah.elearning.qti.QTIContext;
import edu.uah.elearning.qti.dao.TuserTestDAO;
import edu.uah.elearning.qti.model.Answer;
import edu.uah.elearning.qti.model.QTIPackage;
import edu.uah.elearning.qti.model.Question;
import edu.uah.elearning.qti.model.Tuser;
import edu.uah.elearning.qti.model.TuserTest;
import edu.uah.elearning.qti.model.TuserTrole;
import edu.uah.elearning.qti.service.QTIRenderer;
import edu.uah.elearning.qti.service.QTITestManager;
import edu.uah.elearning.qti.service.QtiTestService;
import edu.uah.elearning.qti.service.UserService;

@Service
public class QtiTestServiceJQTI implements QtiTestService {

    private String KEYCONTEXT = "context";

    private QTIRenderer renderer = null;

    private String contextPath = null;

    private String realPath = null;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private QTITestManager qtiTestManager = null;

    @Autowired
    @Qualifier(value = "userService")
    private UserService userService;

    @Autowired
    private TuserTestDAO tuserTestDAO;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public int startQtiTest() {
        return init();
    }

    @Transactional
    public void finishTest() {
        QTIContext context = getContext();
        if (context == null) return;
        TuserTest tuserTest = context.getTuserTest();
        tuserTest.setFinishDate(new Date());
        tuserTest.setAnswers(null);
        boolean persist = true;
        for (TuserTrole tuserTrole : userService.getUserInSession().getTuserTroleCollection()) {
            if (tuserTrole.getRoleId().getRoleId().equals(UserService.TROLE_TEACHER_ID)) persist = false;
        }
        if (persist) {
            List<String> report = getReport();
            tuserTest.setReportTittle(report.get(0));
            tuserTest.setReport(report.get(1));
            tuserTestDAO.persist(tuserTest);
        }
    }

    public void setQTIRenderer(QTIRenderer renderer) {
        this.renderer = renderer;
    }

    String rend = "default (xhtml)";

    String view = "*";

    String jSession = "98048C39F8F70023A3115664ECB5FFFE";

    public int init() {
        QTIPackage qtiPackage = qtiTestManager.getCurrentQTIPackage();
        if (qtiPackage == null) {
            logger.warn("No existe ningún cuestionario dado de alta.");
            return ERROR;
        }
        String completePath = qtiPackage.getPath();
        String catalina = System.getProperty("catalina.home");
        if (catalina == null) {
            String file = catalina + "/asdel";
            File asdelPAth = new File(file);
            if (!asdelPAth.exists()) System.getProperties().setProperty("catalina.home", new String(realPath + contextPath));
        }
        String file = qtiPackage.getFileName();
        try {
            PlayrCoordinator playrCoordinator = new PlayrCoordinator(file, completePath, rend, view, jSession);
            playrCoordinator.getNextQuestion(false);
            TuserTest tuserTest = new TuserTest();
            tuserTest.setAnswers(new ArrayList<Answer>());
            tuserTest.setCreationDate(new Date());
            tuserTest.setTuser(userService.getUserInSession());
            QTIContext context = new QTIContext();
            context.setTuserTest(tuserTest);
            context.setPlayrCoordinator(playrCoordinator);
            userService.setSessionProperty(KEYCONTEXT, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OK;
    }

    protected QTIContext getContext() {
        QTIContext context = null;
        Object object = userService.getSessionProperty(KEYCONTEXT);
        if (object != null) context = (QTIContext) object;
        return context;
    }

    public Question getCurrentQuestion() {
        PlayrCoordinator playrCoordinator = getPlayrCoordinator();
        QTIContext context = getContext();
        if (context == null) return null;
        Question question = context.getCurrentQuestion();
        if (question == null) {
            question = new Question();
            question.setXhtml(null);
            try {
                question.setFile(playrCoordinator.getTestDirectory() + "/" + playrCoordinator.getTestController().getCurrentItemHREF());
            } catch (QTIException e) {
                logger.error(e.getMessage(), e);
            }
            try {
                if (playrCoordinator.getTestController().getCurrentItemHREF() == null) return null;
            } catch (QTIException e) {
                logger.error(e.getMessage(), e);
            }
            String rend = renderer.render(question);
            rend = manageImages(rend);
            question.setXhtml(rend);
            getContext().setCurrentQuestion(question);
        }
        return question;
    }

    private String manageImages(String xhtml) {
        String path = qtiTestManager.getCurrentQTIPackage().getPath();
        path = path.substring(path.indexOf("webapps") + 7, path.length());
        xhtml = xhtml.replaceAll("<img src=\"", "<img width=\"440px\" src=\"" + path + "/");
        return xhtml;
    }

    public boolean isCompleted() {
        PlayrCoordinator playrCoordinator = getPlayrCoordinator();
        if (playrCoordinator == null) return true;
        return playrCoordinator.isCompleted();
    }

    protected PlayrCoordinator getPlayrCoordinator() {
        QTIContext context = getContext();
        PlayrCoordinator coordinator = null;
        if (context != null) coordinator = context.getPlayrCoordinator();
        return coordinator;
    }

    public void answer(Map<String, List<String>> answer) {
        setResponse(answer);
    }

    @Transactional
    public List<String> getReport() {
        Tuser tuser = userService.getUserInSession();
        List<TuserTest> list = tuserTestDAO.findByTUser(tuser);
        List<String> report = null;
        if (list != null && list.size() > 0) {
            report = new ArrayList<String>();
            report.add(list.get(0).getReportTittle());
            report.add(list.get(0).getReport());
        } else {
            PlayrCoordinator playrCoordinator = getPlayrCoordinator();
            if (playrCoordinator == null) return null;
            report = playrCoordinator.getTestController().getAssessmentFeedback();
            if (report.size() == 1) {
                String aux = report.get(0);
                int index = aux.indexOf("<table");
                String uno = aux.substring(0, index);
                String dos = aux.substring(index, aux.length());
                report.set(0, uno);
                report.add(dos);
            }
        }
        return report;
    }

    protected Question getNextQuestion() {
        try {
            getPlayrCoordinator().getNextQuestion(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (R2Q2Exception e) {
            e.printStackTrace();
        } catch (QTIException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (RenderingException e) {
            e.printStackTrace();
        }
        getContext().setCurrentQuestion(null);
        return getCurrentQuestion();
    }

    public void setResponse(Map<String, List<String>> answer) {
        try {
            PlayrCoordinator playrCoordinator = getPlayrCoordinator();
            if (playrCoordinator == null) return;
            playrCoordinator.setCurrentResponse(answer);
            List<String> list2 = playrCoordinator.getTestController().getTestPartFeedback();
            QTIContext context = getContext();
            Question question = context.getCurrentQuestion();
            Answer newAnswer = new Answer();
            newAnswer.setQuestion(question.getFile());
            newAnswer.setAnswer(answer.toString());
            context.getTuserTest().getAnswers().add(newAnswer);
            getNextQuestion();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (R2Q2Exception e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (QTIException e) {
            e.printStackTrace();
        } catch (RenderingException e) {
            e.printStackTrace();
        }
    }

    public void skipCurrentQuestion() {
        try {
            getPlayrCoordinator().skipCurrentQuestion();
            getNextQuestion();
        } catch (R2Q2Exception e) {
            e.printStackTrace();
        } catch (QTIException e) {
            e.printStackTrace();
        } catch (RenderingException e) {
            e.printStackTrace();
        }
    }

    public void previousQuestion() {
        try {
            getPlayrCoordinator().getPreviousQuestion(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (R2Q2Exception e) {
            e.printStackTrace();
        } catch (QTIException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (RenderingException e) {
            e.printStackTrace();
        }
    }

    public void setContextPath(String path) {
        this.contextPath = path;
    }

    public void setRealPath(String realPath) {
        String c = File.separator;
        int index = realPath.lastIndexOf(c);
        String string = realPath.substring(0, index);
        this.realPath = string;
    }

    public void setQtiTestManager(QTITestManager qtiTestManager) {
        this.qtiTestManager = qtiTestManager;
    }

    public void setTuserTestDAO(TuserTestDAO tuserTestDAO) {
        this.tuserTestDAO = tuserTestDAO;
    }

    @Transactional
    public boolean isTestFinished() {
        Tuser tuser = userService.getUserInSession();
        List<TuserTest> list = tuserTestDAO.findByTUser(tuser);
        if (list.size() == 0) return false; else return true;
    }
}
