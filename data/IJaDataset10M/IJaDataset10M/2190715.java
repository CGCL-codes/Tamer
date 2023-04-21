package course.wicket.app.home;

import org.apache.wicket.markup.html.panel.Panel;
import org.modelibra.wicket.concept.EntityDisplayMinPanel;
import org.modelibra.wicket.container.DmPage;
import org.modelibra.wicket.view.View;
import org.modelibra.wicket.view.ViewModel;
import course.Course;
import course.quiz.Quiz;
import course.quiz.question.Question;
import course.quiz.question.Questions;
import course.quiz.test.Tests;
import course.reference.Reference;
import course.reference.countrylanguage.CountryLanguages;
import course.wicket.app.CourseApp;
import course.wicket.quiz.test.TestDisplayTablePanel;

/**
 * Application home page.
 * 
 * @author Dzenan Ridjanovic
 * @version 2007-04-19
 */
@SuppressWarnings("serial")
public class HomePage extends DmPage {

    public HomePage() {
        CourseApp courseApp = (CourseApp) getApplication();
        Course course = courseApp.getCourse();
        Quiz quiz = course.getQuiz();
        Reference reference = course.getReference();
        add(new HomePageHeaderPanel("headerSection", "Quiz"));
        ViewModel mainMenuViewModel = new ViewModel();
        mainMenuViewModel.setModel(reference);
        CountryLanguages languages = reference.getCountryLanguages();
        mainMenuViewModel.setEntities(languages);
        View mainMenuView = new View();
        mainMenuView.setPage(this);
        mainMenuView.setWicketId("menuSection");
        add(new HomePageMenuPanel(mainMenuViewModel, mainMenuView));
        Tests tests = quiz.getTests();
        Tests orderedQuizzes = tests.getTestsOrderedByName(true);
        add(new TestDisplayTablePanel("quizTableSection", orderedQuizzes));
        Panel randomQuestionSection = null;
        ViewModel questionViewModel = new ViewModel();
        questionViewModel.setModel(quiz);
        Questions questions = quiz.getQuestions();
        if (questions.isEmpty()) {
            randomQuestionSection = new Panel("randomQuestionSection");
            randomQuestionSection.setVisible(false);
        } else {
            Question question = questions.random();
            questionViewModel.setEntity(question);
            View questionView = new View();
            questionView.setWicketId("randomQuestionSection");
            randomQuestionSection = new EntityDisplayMinPanel(questionViewModel, questionView);
        }
        add(randomQuestionSection);
        add(new HomePageFooterPanel("footerSection"));
    }
}
