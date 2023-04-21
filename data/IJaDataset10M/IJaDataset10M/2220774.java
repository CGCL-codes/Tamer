package manage;

import dao.DataAccessException;
import dao.StudentDAO;
import java.util.List;
import model.Group;
import model.GroupStudent;
import model.Student;

/**
 *
 * @author dani
 */
public class ManageStudent {

    ManageGroupStudent manageGroupStudent = new ManageGroupStudent();

    ManageGroup manageGroup = new ManageGroup();

    StudentDAO studentDAO = new StudentDAO();

    public void save(Student student, Long groupId) throws DataAccessException {
        Group group = manageGroup.getById(groupId);
        studentDAO.save(student);
        GroupStudent groupStudent = new GroupStudent(group, student, 0.0);
        manageGroupStudent.save(groupStudent);
    }

    public Student getByLogin(String login) throws DataAccessException {
        Student student = studentDAO.getByLogin(login);
        return student;
    }

    public List<Student> getAll() throws Exception {
        return studentDAO.getAll();
    }
}
