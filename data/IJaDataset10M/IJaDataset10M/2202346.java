package edina.chalice.place.domain;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import org.hsqldb.jdbc.jdbcDataSource;
import org.hsqldb.util.SqlFile;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import edina.chalice.test.domain.AbstractDaoTest;

public class PlaceDaoImplTest extends AbstractDaoTest {

    /**
    * Test save and get account.
    */
    @Test
    public void testGetPlace() {
        Place place = new Place();
        place.setName("testName");
        place.setParent(Integer.parseInt("1"));
        place.setAltForm(Integer.parseInt("1"));
        place.setGeoId(Integer.parseInt("1"));
        place.setOsRef("testNameOsRef");
        place.setLevel("testNameLevel");
        place.setXmlId("1.0.1");
        PlaceDaoImpl placeDao = (PlaceDaoImpl) applicationContext.getBean("placeDao");
        placeDao.saveOrUpdate(place);
        String query = "from Place where name = :name";
        Place placeResult = (Place) placeDao.getHibernateTemplate().findByNamedParam(query, "name", "testName").get(0);
        Place getPlaceResult = (Place) placeDao.getPlace(placeResult.getNameId());
        assertEquals(getPlaceResult.getNameId(), placeResult.getNameId());
    }

    /**
    * Test delete account.
    */
    @Test
    public void testLoadUserByUsername() {
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @Override
    @After
    public void tearDown() {
    }
}
