package utility.webmailclient.app.usermanagement.gwt.registration.rpc.command;

import java.util.ArrayList;
import utility.webmailclient.app.usermanagement.gwt.registration.rpc.Response;
import utility.webmailclient.app.usermanagement.gwt.registration.rpc.model.Question;

public class RetrieveQuestionListResponse implements Response {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public ArrayList<Question> questionList;

    public ArrayList<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(ArrayList<Question> questionList) {
        this.questionList = questionList;
    }
}
