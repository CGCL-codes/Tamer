package project.dao.institute;

import java.sql.Date;
import project.entities.institute.Student;

public interface StudentDAO {

    public Student createStudent(Date acceptanceDate, Date deductionDate, int group, String number, int person, String testBook);

    public Student createStudent(Student student);

    public void removeStudent(Student student);

    public void updateStudent(Student student);

    public Student findStudent(int person);
}
