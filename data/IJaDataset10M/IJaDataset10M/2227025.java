package org.rbx.sims.classroom.dao.Hibernate;

import org.rbx.sims.classroom.dao.ClassroomDao;
import org.rbx.sims.model.Classroom;
import org.rbx.sims.support.dao.AbstractSimsDao;
import org.springframework.stereotype.Repository;
import java.io.Serializable;
import java.util.List;

/**
 * @author franco
  */
@Repository(value = "classroomDao")
public class HibernateClassroomDao extends AbstractSimsDao<Classroom> implements ClassroomDao {

    /**
     * Retrieves a Classroom by id
     * @param objId Classroom uuid
     * @return A Classroom
     * @throws org.springframework.dao.DataAccessException
     */
    public Classroom read(Serializable objId) {
        return (Classroom) get(Classroom.class, objId);
    }

    /**
     * @see org.rbx.sims.classroom.dao.ClassroomDao#getClassrooms()
     * @see org.rbx.sims.model.Classroom
     */
    @SuppressWarnings("unchecked")
    public List<Classroom> getClassrooms() {
        return find("from Classroom");
    }

    public Classroom getClassroomByNumber(final String number) {
        String query = "from Classroom c where c.number = ?";
        return (Classroom) find(query, number);
    }

    @SuppressWarnings("unchecked")
    public List<Classroom> getClassroomsByTermId(final Long termId) {
        return (List<Classroom>) find("from Classroom c where c.term.id = ?", termId);
    }

    public List<Classroom> getClassroomsByOrganizationId(final Long organizationId) {
        return null;
    }

    public List<Classroom> getClassroomsByCourseId(final Long courseId) {
        return (List<Classroom>) find("from Classroom c where c.courseId = ?", courseId);
    }

    public Long getCourseIdByClassroomId(final Long classroomId) {
        return null;
    }

    public void changeCourseId(final Long classroomId, final Long courseId) {
        Classroom classroom = read(classroomId);
        classroom.setCourseId(courseId);
        update(classroom);
    }
}
