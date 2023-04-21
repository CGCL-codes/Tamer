package de.anhquan.quiz.server;

import org.restlet.resource.ServerResource;
import de.anhquan.quiz.shared.QuizItem;
import de.anhquan.quiz.shared.QuizResource;

/**
 * The server side implementation of the Restlet resource.
 */
public class QuizServerResource extends ServerResource implements QuizResource {

    private Integer curQuizId;

    public QuizServerResource() {
        curQuizId = null;
    }

    @Override
    public QuizItem getQuizItemById(Integer id) {
        System.out.println("Goto: " + id);
        if (id < 1) id = 1;
        QuizItem item = QuizDB.getQuizById(id);
        System.out.println("Goto: " + id + " - item " + item);
        return item;
    }

    @Override
    public QuizItem gotoItem(Integer id) {
        System.out.println("Goto: " + id);
        QuizItem item = QuizDB.getQuizById(id);
        System.out.println("Goto: " + id + " - item " + item);
        return item;
    }
}
