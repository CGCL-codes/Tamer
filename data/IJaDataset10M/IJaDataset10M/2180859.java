package test.dao;

import dao.TeacherDAO;
import java.util.ArrayList;
import java.util.List;
import model.Teacher;
import org.junit.Test;
import test.util.BaseDBTestCase;
import static org.junit.Assert.*;

/**
 *
 * @author erica
 */
public class TeacherDAOTest extends BaseDBTestCase {

    public TeacherDAOTest(String name) {
        super(name);
    }

    private final TeacherDAO teacherDAO = new TeacherDAO();

    @Test
    public void testGetAll() throws Exception {
        List<Teacher> expectedTeachers = new ArrayList<Teacher>();
        expectedTeachers.add(entityFactory.getTeacher(1L));
        assertEquals("List of teachers was different than expected", expectedTeachers, teacherDAO.getAll());
    }

    @Test
    public void testSave() throws Exception {
        Teacher teacher = new Teacher(null, "Monokuro Boo", "monokuro", "monokuro", "Rua: Manoel Alves", "4576-3675", "monokuroBoo@boo.com.br", "675897645455", "Matemática");
        teacherDAO.save(teacher);
        assertEquals("Teacher was different than expected", teacher, teacherDAO.getById(teacher.getId()));
    }

    @Test
    public void testGetById() throws Exception {
        Teacher expectedTeacher = entityFactory.getTeacher(1L);
        assertEquals("Teacher is different than expected", expectedTeacher, teacherDAO.getById(1L));
    }

    @Test
    public void testDelete() throws Exception {
        Teacher teacher = new Teacher(null, "testSave2", "testSave2", "testSave", "endereco", "4576-3675", "testSave@test.com", "675897645", "Matemática");
        teacherDAO.save(teacher);
        teacherDAO.delete(teacher);
        assertNull(teacherDAO.getById(teacher.getId()));
    }

    public void testUpdate() throws Exception {
        Teacher teacher = entityFactory.getTeacher(1L);
        teacher.setName("Erica V Sales");
        teacherDAO.update(teacher);
        assertEquals("Teacher was different than expected", teacher.getName(), teacherDAO.getById(teacher.getId()).getName());
    }
}
