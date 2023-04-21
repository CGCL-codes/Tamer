package project.dao.institute;

import java.sql.Date;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import project.entities.institute.Student;

public class StudentDAOImpl extends HibernateDaoSupport implements StudentDAO {

    @Override
    public Student createStudent(Date acceptanceDate, Date deductionDate, int group, String number, int person, String testBook) {
        Student student = new Student(acceptanceDate, deductionDate, group, number, person, testBook);
        return (Student) getHibernateTemplate().save(student);
    }

    @Override
    public Student createStudent(Student student) {
        return (Student) getHibernateTemplate().save(student);
    }

    @Override
    public void removeStudent(Student student) {
        getHibernateTemplate().delete(student);
    }

    @Override
    public void updateStudent(Student student) {
        getHibernateTemplate().saveOrUpdate(student);
    }

    @Override
    public Student findStudent(int person) {
        return null;
    }
}
