package edu.oasis.domain;

import java.io.*;
import java.util.*;
import org.apache.commons.logging.*;
import org.hibernate.*;
import org.xml.sax.*;
import edu.oasis.config.*;
import edu.webteach.*;
import edu.webteach.data.*;
import edu.webteach.practice.data.*;
import edu.webteach.teach.bean.*;
import edu.webteach.test.quizz.*;

class DomainBuilder {

    protected Log log = LogFactory.getLog(DomainBuilder.class);

    public DomainBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public DomainBuilder(Configuration configuration, WebteachDomain domain) {
        this(configuration);
        fillDomain(domain);
    }

    protected Configuration configuration;

    protected WebteachDomain domain;

    public void fillDomain(WebteachDomain domain) {
        this.domain = domain;
        if (configuration.isAutoLoadTopics()) {
            loadTopics();
        }
        if (configuration.isAutoLoadTheory()) {
            loadTheory();
        }
        if (configuration.isAutoLoadQuestions()) {
            loadQuestions();
        }
        if (configuration.isAutoLoadQuizzes()) {
            loadQuizzes();
        }
        if (configuration.isAutoLoadUnits()) {
            loadUnits();
        }
        if (configuration.isAutoLoadMethods()) {
            loadMethods();
        }
        if (configuration.isAutoLoadCourses()) {
            loadCourses();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadTopics() {
        synchronized (domain.topics) {
            domain.topics.clear();
            Session session = Context.currentSession();
            Query query = session.createQuery("from edu.webteach.test.bean.Topic");
            List<Topic> list = query.list();
            List<Topic> topics = new ArrayList<Topic>();
            boolean changed = true;
            for (int i = 0; changed; i++) {
                changed = false;
                for (int j = 0; j < list.size(); ) {
                    boolean flagFound = false;
                    for (; j < list.size() && !flagFound; j++) {
                        int deepness = list.get(j).getDeepness();
                        if (deepness == i) {
                            flagFound = true;
                            break;
                        }
                    }
                    if (flagFound) {
                        int place = getTopicPlace(topics, list.get(j));
                        topics.add(place, list.get(j));
                        changed = true;
                        j++;
                    }
                }
            }
            for (Topic topic : topics) {
                domain.topics.add(new TopicItem(topic));
            }
            Context.closeSession();
            domain.topicsLoaded = true;
        }
    }

    private int getTopicPlace(List<Topic> topics, Topic topic) {
        int startIndex = topics.indexOf(topic.getParent());
        if (startIndex < 0) return topics.size();
        for (int i = startIndex + 1; i < topics.size(); i++) {
            if (topics.get(i).getParent() != topic.getParent()) return i;
        }
        return topics.size();
    }

    public void loadUnits() {
        synchronized (domain.units) {
            domain.units.clear();
            List<Unit> units = new ArrayList<Unit>();
            UnitsLoader loader = new UnitsLoader();
            loader.loadUnits(configuration.getWebteachPath() + "\\units");
            for (String s : Units.getNames()) {
                units.add(Units.get(s));
            }
            for (Unit unit : units) {
                domain.units.add(new UnitItem(unit.getName(), unit));
            }
            domain.unitsLoaded = true;
        }
    }

    @SuppressWarnings("unchecked")
    public void loadQuestions() {
        synchronized (domain.questions) {
            domain.questions.clear();
            Session session = Context.currentSession();
            Query query = session.createQuery("from edu.webteach.test.bean.Question");
            List<Question> questions = query.list();
            for (Question q : questions) {
                domain.questions.add(new QuestionItem(q));
            }
            Context.closeSession();
            domain.questionsLoaded = true;
        }
    }

    public void loadMethods() {
        synchronized (domain.methods) {
            domain.methods.clear();
            File methodsDir = new File(configuration.getWebteachPath() + "\\methods");
            File[] methodsFiles = methodsDir.listFiles();
            for (File methodFile : methodsFiles) {
                try {
                    if (methodFile.isFile()) {
                        domain.methods.add(new MethodItem(methodFile.getAbsolutePath()));
                    }
                } catch (Exception e) {
                    System.out.println("Cannot load " + methodFile);
                    e.printStackTrace();
                }
            }
            domain.methodsLoaded = true;
        }
    }

    public void loadCourses() {
        synchronized (domain.courses) {
            domain.courses.clear();
            CoursesParser parser = new CoursesParser(configuration.getWebteachPath() + "\\courses.xml");
            List<Link> links = parser.getCourses();
            for (Link link : links) {
                String filename = configuration.getWebteachPath() + "\\courses\\" + link.getSource() + ".xml";
                domain.courses.add(new CourseItem(filename));
            }
            domain.coursesLoaded = true;
        }
    }

    public void loadQuizzes() {
        synchronized (domain.quizzes) {
            domain.quizzes.clear();
            QuizzesParser qp = new QuizzesParser();
            try {
                FileReader reader = new FileReader(configuration.getWebteachPath() + "\\quizzes.xml");
                qp.parse(new InputSource(reader));
                reader.close();
                for (edu.webteach.test.quizz.QuizzItem quizzItem : qp.getQuizzes()) {
                    String quizzFile = configuration.getWebteachPath() + "\\quizzes\\" + quizzItem.getFilename();
                    domain.quizzes.add(new QuizzItem(quizzFile));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            domain.quizzesLoaded = true;
        }
    }

    public void loadTheory() {
        synchronized (domain.theory) {
            domain.theory.clear();
            domain.theory.add(new TheoryItem("Theory page 1", "sdjfksdhf"));
            domain.theory.add(new TheoryItem("Theory page 2", "xncvjsd f sdjfsd"));
            domain.theoryLoaded = true;
        }
    }
}
