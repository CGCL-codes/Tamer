package wilos.test.hibernate.spem2.guide;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wilos.hibernate.spem2.guide.GuidanceDao;
import wilos.model.spem2.guide.Guidance;
import wilos.test.TestConfiguration;

public class GuidanceDaoTest {

    public static final String guid = "le_guid";

    public static final String name = "le_nom";

    public static final String description = "la_description";

    private GuidanceDao guidanceDAO = (GuidanceDao) TestConfiguration.getInstance().getApplicationContext().getBean("GuidanceDao");

    private Guidance guidance;

    @Before
    public void setUp() {
        this.guidance = new Guidance();
        this.guidance.setDescription(description);
        this.guidance.setName(name);
        this.guidance.setGuid(guid);
        this.guidance.setInsertionOrder(1);
    }

    @After
    public void tearDown() {
        this.guidance = null;
    }

    @Test
    public void testSaveOrUpdateGuidance() {
        this.guidanceDAO.saveOrUpdateGuidance(this.guidance);
        String id = this.guidance.getId();
        Guidance guidanceTmp = (Guidance) this.guidanceDAO.getGuidance(id);
        assertNotNull(guidanceTmp);
        this.guidanceDAO.deleteGuidance(this.guidance);
    }

    @Test
    public void testGetAllGuidances() {
        this.guidanceDAO.saveOrUpdateGuidance(this.guidance);
        List<Guidance> guidances = this.guidanceDAO.getAllGuidances();
        assertNotNull(guidances);
        assertTrue(guidances.size() >= 1);
        this.guidanceDAO.deleteGuidance(this.guidance);
    }

    @Test
    public void testGetGuidance() {
        this.guidanceDAO.saveOrUpdateGuidance(this.guidance);
        String id = guidance.getId();
        Guidance guidanceTmp = this.guidanceDAO.getGuidance(id);
        assertNotNull(guidanceTmp);
        assertEquals("GUID", guidanceTmp.getGuid(), guid);
        assertEquals("Name", guidanceTmp.getName(), name);
        assertEquals("Name", guidanceTmp.getDescription(), description);
        this.guidanceDAO.deleteGuidance(this.guidance);
    }

    @Test
    public void testDeleteGuidance() {
        this.guidanceDAO.saveOrUpdateGuidance(this.guidance);
        String id = this.guidance.getId();
        this.guidanceDAO.deleteGuidance(this.guidance);
        Guidance guidanceTmp = (Guidance) this.guidanceDAO.getGuidance(id);
        assertNull(guidanceTmp);
    }
}
