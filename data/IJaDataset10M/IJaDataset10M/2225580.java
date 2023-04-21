package ee.fctwister.wc2010.DTO;

import java.io.Serializable;
import ee.fctwister.wc2010.DAO.QuestionDAO;

public class QuestionDTO implements Serializable {

    private static final long serialVersionUID = 639867303393294698L;

    private int id;

    private String question;

    private String comment;

    public QuestionDTO(int id) {
        QuestionDAO dao = new QuestionDAO();
        QuestionDTO question = new QuestionDTO();
        try {
            question = dao.getQuestionById(id);
            this.id = id;
            this.question = question.question;
            this.comment = question.comment;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public QuestionDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
