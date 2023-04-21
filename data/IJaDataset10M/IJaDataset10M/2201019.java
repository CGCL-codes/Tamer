package net.sf.brightside.xlibrary.pages;

import java.util.List;
import net.sf.brightside.xlibrary.core.spring.ApplicationContextProviderSingleton;
import net.sf.brightside.xlibrary.core.tapestry.SpringBean;
import net.sf.brightside.xlibrary.metamodel.Faculty;
import net.sf.brightside.xlibrary.metamodel.Student;
import net.sf.brightside.xlibrary.service.Get;
import net.sf.brightside.xlibrary.service.studentRegistration.StudentRegistrationCommand;
import net.sf.brightside.xlibrary.util.FacultyEncoder;
import net.sf.brightside.xlibrary.util.FacultySelectModel;
import org.apache.tapestry.SelectModel;
import org.apache.tapestry.ValueEncoder;
import org.apache.tapestry.annotations.ApplicationState;
import org.apache.tapestry.annotations.InjectPage;
import org.apache.tapestry.annotations.OnEvent;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.ioc.annotations.Inject;
import org.springframework.context.ApplicationContext;

public class UpdateStudent {

    @ApplicationState
    private Student student;

    private List<Faculty> listFaculties;

    @InjectPage
    private Start startPage;

    @Persist
    private String message;

    @Inject
    @SpringBean("net.sf.brightside.xlibrary.util.FacultyEncoder")
    private FacultyEncoder facultyEncoder;

    @Inject
    @SpringBean("net.sf.brightside.xlibrary.service.Get")
    private Get<Faculty> getAllCommand;

    @Inject
    @SpringBean("net.sf.brightside.xlibrary.service.studentRegistration.StudentRegistrationCommand")
    private StudentRegistrationCommand<Student> studentRegistrationCommand;

    private ApplicationContext applicationContext() {
        return new ApplicationContextProviderSingleton().getContext();
    }

    public Student createStudent() {
        return (Student) applicationContext().getBean(Student.class.getName());
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @SuppressWarnings("finally")
    @OnEvent(value = "submit", component = "updateStudentForm")
    public Object onFormSubmit() {
        System.out.println(student);
        studentRegistrationCommand.setStudent(student);
        if (student.getFaculty() != null) {
            studentRegistrationCommand.setFaculty(student.getFaculty());
            Student student = studentRegistrationCommand.execute();
            this.setMessage("Uspesno izmenjeni podaci " + student.getName() + " " + student.getSurname());
            return this;
        } else {
            message = ("You haven't fill all fields correctly, you haven't selected faculty, please register it if it doesn't exist");
            return this;
        }
    }

    @SuppressWarnings("unchecked")
    public ValueEncoder getFacultyEncoder() {
        return (ValueEncoder) facultyEncoder;
    }

    public SelectModel getFacultyModel() {
        return new FacultySelectModel(listFaculties);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SuppressWarnings("unchecked")
    public boolean completeDataFaculty() {
        getAllCommand.setType(Faculty.class);
        listFaculties = (List<Faculty>) getAllCommand.execute();
        if (listFaculties.size() > 0 && student != null) {
            return true;
        }
        return false;
    }
}
