package ru.ssau.university.persistence.dao;

import java.util.List;
import org.hibernate.Query;
import ru.ssau.hibernate.util.AbstractDAO;
import ru.ssau.university.persistence.entity.Teacher;

public class TeacherDAO extends AbstractDAO<Teacher> {

    public TeacherDAO() {
        this.objectClass = Teacher.class;
    }

    @SuppressWarnings("unchecked")
    public List<Teacher> getTeachersByDepartment(Long departmentId) {
        Query q = createQuery("select t from Teacher t where t.department.id=:departmentId");
        q.setLong("departmentId", departmentId);
        return q.list();
    }

    public Teacher getTeacherByIdAndPassword(Long teacherId, String password) {
        Query q = createQuery("select t from Teacher t where t.id=:teacherId and t.password = :password");
        q.setLong("teacherId", teacherId);
        q.setString("password", password);
        return (Teacher) q.uniqueResult();
    }
}
