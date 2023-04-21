package ru.spbspu.staub.bean.system;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.security.Identity;
import ru.spbspu.staub.bean.GenericBean;
import ru.spbspu.staub.entity.Group;
import ru.spbspu.staub.entity.Student;
import ru.spbspu.staub.service.GroupService;
import ru.spbspu.staub.service.StudentService;
import java.util.List;

/**
 * Webbean for logging into the system as <code>Student</code>.
 *
 * @author Konstantin Grigoriev
 */
@Name("loginStudentBean")
@Scope(ScopeType.SESSION)
public class LoginStudentBean extends GenericBean {

    private static final long serialVersionUID = 5740399344448977004L;

    private String username;

    private String password;

    private List<Group> groups;

    private List<Student> students;

    @In
    private Identity identity;

    @In
    private StudentService studentService;

    @In
    private GroupService groupService;

    @Out(scope = ScopeType.SESSION, required = false)
    private Student student;

    private Student selectedStudent;

    private Group group;

    @Create
    public void initBean() {
        groups = groupService.findAll();
    }

    public String login() {
        identity.setUsername(username);
        identity.setPassword(password);
        identity.setAuthenticateMethod(Expressions.instance().createMethodExpression("#{loginStudentBean.authenticate}"));
        String outcome = identity.login();
        if (student == null) {
            addFacesMessageFromResourceBundle("system.loginStudent.error.password");
        }
        return outcome;
    }

    public boolean authenticate() {
        logger.debug(">>> Authentinicating student(username=#{identity.username}), password=#{identity.password}");
        student = studentService.findByNameAndCode(identity.getUsername(), identity.getPassword());
        logger.debug("<<< Authentinicating ok(result=#0)", student != null);
        return student != null;
    }

    public void refreshStudents() {
        if (group != null) {
            getFacesMessages().clear();
            students = studentService.find(group);
        }
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Student> getStudents() {
        return students;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
        if (group == null) {
            username = null;
            password = null;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Student getSelectedStudent() {
        return selectedStudent;
    }

    public void setSelectedStudent(Student selectedStudent) {
        this.selectedStudent = selectedStudent;
        if (selectedStudent != null) {
            setUsername(selectedStudent.getName());
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
