package wrappers;

import wrappers.interfaces.StudentInterface;
import models.User;

/**
 *
 * @author Marten
 */
public class StudentWrapper extends UserWrapper implements StudentInterface {

    public StudentWrapper() {
        super();
    }

    public StudentWrapper(User user) {
        super(user);
    }

    public StudentWrapper(int userId) {
        super(userId);
    }

    public StudentWrapper(String username, String password) {
        super(username, password);
    }

    @Override
    public void joinCourse() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void signupExam() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
