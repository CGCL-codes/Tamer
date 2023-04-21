package course.wicket.quiz.question;

import org.apache.wicket.markup.html.panel.Panel;
import course.quiz.question.Question;
import course.quiz.question.Questions;

/**
 * Slide question form panel.
 * 
 * @author Dzenan Ridjanovic
 * @version 2006-08-26
 */
public class SlideQuestionFormPanel extends Panel {

    private static final long serialVersionUID = 282110L;

    public SlideQuestionFormPanel(String id, Questions questions, Question question, String answer) {
        super(id);
        add(new SlideQuestionForm("slideQuestionForm", questions, question, answer));
        if (answer == null) {
            add(new CorrectResponsePanel("correctResponsePanel", question, false));
        } else {
            add(new CorrectResponsePanel("correctResponsePanel", question, true));
        }
    }
}
