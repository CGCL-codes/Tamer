package siouxsie.app.dao.hibernate;

import java.util.Calendar;
import java.util.List;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.hibernate.Hibernate;
import siouxsie.app.dao.AbstractDAOTest;
import siouxsie.app.dao.IOeuvreDAO;
import siouxsie.app.domain.Composer;
import siouxsie.app.domain.Oeuvre;

/**
 * @author Arnaud Cogoluegnes
 * @version $Id: TestOeuvreDAO.java 97 2007-10-06 20:01:57Z acogo $
 */
public class TestOeuvreDAO extends AbstractDAOTest {

    private IOeuvreDAO dao = (IOeuvreDAO) getBean("oeuvreDAO");

    public void testSelect() {
        List<Oeuvre> res = dao.select(null);
        assertEquals(3, res.size());
        Oeuvre template = new Oeuvre();
        template.setName("pho");
        res = dao.select(template);
        assertEquals(1, res.size());
        assertEquals("Symphony no. 4", res.get(0).getName());
        template = new Oeuvre();
        template.setClassical(Boolean.TRUE);
        res = dao.select(template);
        assertEquals(2, res.size());
        template = new Oeuvre();
        template.setClassical(Boolean.FALSE);
        res = dao.select(template);
        assertEquals(1, res.size());
        Composer composer = new Composer();
        composer.setId(Long.valueOf(-3));
        template = new Oeuvre();
        template.setComposer(composer);
        res = dao.select(template);
        assertEquals(2, res.size());
    }

    public void testCreate() {
        compareTableCount("composer", 3);
        Composer composer = new Composer();
        composer.setId(Long.valueOf(-3));
        Oeuvre oeuvre = new Oeuvre();
        oeuvre.setClassical(Boolean.TRUE);
        oeuvre.setComposer(composer);
        oeuvre.setName("Bagatelle");
        dao.create(oeuvre);
        assertNotNull(oeuvre.getId());
        compareTableCount("oeuvre", 4);
    }

    public void testGet() {
        Oeuvre oeuvre = dao.get(Long.valueOf(-2));
        assertEquals(Long.valueOf(-2), oeuvre.getId());
        assertEquals("Fur Elise", oeuvre.getName());
        assertTrue(Hibernate.isPropertyInitialized(oeuvre, "composer"));
    }

    public void testUpdate() throws Exception {
        Oeuvre oeuvre = dao.get(Long.valueOf(-2));
        oeuvre.setName("test");
        dao.update(oeuvre);
        IDataSet databaseDataSet = getConnection().createDataSet();
        ITable currentTable = databaseDataSet.getTable("oeuvre");
        boolean found = false;
        for (int i = 0, n = currentTable.getRowCount(); i < n; i++) {
            if ("-2".equals(currentTable.getValue(i, "id").toString())) {
                found = true;
                assertEquals("test", currentTable.getValue(i, "name"));
            }
        }
        assertTrue("should have found the updated row", found);
    }

    @Override
    protected String getDataSetResource() {
        return "/integration/dataset/injection/oeuvre.xml";
    }
}
