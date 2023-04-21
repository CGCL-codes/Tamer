package ch.gmtech.lab.datasource.db;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import ch.gmtech.lab.domain.Course;
import ch.gmtech.lab.domain.CourseName;
import ch.gmtech.lab.domain.Identificator;
import ch.gmtech.lab.domain.OrderedCollectionImpl;

public class CourseDAOTest extends TestCase {

    private CourseDAO dao;

    private JDBCConnection reggie;

    @Override
    protected void setUp() throws Exception {
        reggie = ConnectionBuilder.connectionForMySqlOnReggie();
        dao = new CourseDAO(reggie, new MySqlDialect());
        new DeleteStatement(reggie.createStatement()).executeWith(new ColumnRepository("course", new MySqlDialect()));
        String name = "Matematica";
        dao.create(new Identificator(new CourseName(name)), name, 3);
    }

    @Override
    protected void tearDown() throws Exception {
        reggie.close();
    }

    public void testCreate() throws Exception {
        Course found = dao.find("Matematica");
        Course expected = new Course("Matematica", 3);
        assertEquals(expected, found);
    }

    public void testFindAll() throws Exception {
        String name = "Storia";
        dao.create(new Identificator(new CourseName(name)), name, 6);
        List<Course> expected = new ArrayList<Course>();
        expected.add(new Course("Matematica", 3));
        expected.add(new Course(name, 6));
        assertEquals(new OrderedCollectionImpl(expected), dao.findAll());
    }

    public void testFindByName() throws Exception {
        dao.create(new Identificator(new CourseName("Storia")), "Storia", 6);
        dao.create(new Identificator(new CourseName("Name")), "Mate", 12);
        Course expected = new Course("Storia", 6);
        assertEquals(expected, dao.find("Storia"));
    }

    public void testExists() throws Exception {
        dao.create(new Identificator(new CourseName("Storia")), "Storia", 6);
        dao.create(new Identificator(new CourseName("Mate")), "Mate", 12);
        assertFalse(dao.exists(new Identificator(new CourseName("Filosofia"))));
        assertTrue(dao.exists(new Identificator(new CourseName("Storia"))));
    }

    public void testUpdate() throws Exception {
        dao.update("Matematica", 8);
        Course expected = new Course("Matematica", 8);
        Course found = dao.find("Matematica");
        assertEquals(expected, found);
    }
}
