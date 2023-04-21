package net.java.dev.classdiary.jsf;

import java.util.List;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import net.java.dev.classdiary.dao.StudentDAO;
import net.java.dev.classdiary.dto.StudentDTO;
import net.java.dev.classdiary.factory.DAOFactory;

public class StudentManager extends BaseManager {

    /** define the forward available for the student form (insert/edit actions) */
    public static final String STUDENT_FORM = "studentForm";

    /** define the forward available for the student list (list/delete actions) */
    public static final String STUDENT_LIST = "studentList";

    private static StudentDAO studentDAO = DAOFactory.getStudentDAO();

    private StudentDTO student = null;

    private DataModel studentModel;

    public StudentManager() {
        super();
    }

    public StudentDTO getStudent() {
        if (student == null) {
            student = new StudentDTO();
        }
        return student;
    }

    public DataModel getAllStudents() {
        if (studentModel == null) {
            studentModel = new ListDataModel();
        }
        setList(selectAllStudents());
        studentModel.setWrappedData(getList());
        return studentModel;
    }

    /**
     * selectAllStudents
     *
     * @return List
     */
    private List selectAllStudents() {
        return studentDAO.getStudents();
    }

    public String edit() {
        StudentDTO studentLocal = (StudentDTO) studentModel.getRowData();
        setStudent(studentLocal);
        setEditing(true);
        return STUDENT_FORM;
    }

    public String remove() {
        StudentDTO studentLocal = (StudentDTO) studentModel.getRowData();
        studentDAO.remove(studentLocal);
        studentLocal = null;
        return STUDENT_LIST;
    }

    public String insert() {
        setStudent(new StudentDTO());
        setEditing(false);
        return STUDENT_FORM;
    }

    public String save() {
        try {
            if (!isEditing()) studentDAO.insert(student); else studentDAO.update(student);
            setEditing(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return STUDENT_LIST;
    }

    public void setStudent(StudentDTO student) {
        this.student = student;
    }
}
