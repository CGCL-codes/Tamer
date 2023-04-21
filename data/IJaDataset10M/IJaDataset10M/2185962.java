package dao;

import java.util.Collection;
import models.Student;
import org.hibernate.Session;
import utils.HbnUtil;

public class StudentDaoHbnImpl implements StudentDao {

    @Override
    public void addStudent(Student stu) {
        Session session = HbnUtil.getSession();
        session.saveOrUpdate(stu);
    }

    @Override
    public void delStudent(Long stuId) {
        Session session = HbnUtil.getSession();
        Student stu = selStudentById(stuId);
        session.delete(stu);
    }

    @Override
    public Collection<Student> selAllStudent() {
        Session session = HbnUtil.getSession();
        String hql = "from Student s";
        return session.createQuery(hql).list();
    }

    @Override
    public Student selStudentById(Long stuId) {
        Session session = HbnUtil.getSession();
        String hql = "from Student s where s.id=:stuId";
        return (Student) session.createQuery(hql).setLong("stuId", stuId).uniqueResult();
    }

    @Override
    public Student selStudentByNAP(String name, String password) {
        Session session = HbnUtil.getSession();
        String hql = "from Student s where s.name=:name and s.password=:password";
        return (Student) session.createQuery(hql).setString("name", name).setString("password", password).uniqueResult();
    }

    @Override
    public void updateStudent(Student stu) {
        addStudent(stu);
    }
}
