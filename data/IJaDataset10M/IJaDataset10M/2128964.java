package fr.gfi.gfinet.server.service;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.fail;
import java.sql.Date;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import fr.gfi.gfinet.server.AgenceService;
import fr.gfi.gfinet.server.ClassificationService;
import fr.gfi.gfinet.server.CostCenterService;
import fr.gfi.gfinet.server.CostCenterServiceException;
import fr.gfi.gfinet.server.StudyLevelService;
import fr.gfi.gfinet.server.info.Agence;
import fr.gfi.gfinet.server.info.Classification;
import fr.gfi.gfinet.server.info.Collaborator;
import fr.gfi.gfinet.server.info.CostCenter;
import fr.gfi.gfinet.server.info.StudyLevel;
import fr.gfi.gfinet.server.util.ServerToolsForTest;

/**
 * Test CostCenterServiceImpl class.
 */
public class CostCenterServiceImplTestCase {

    private static final Log logger = LogFactory.getLog(CostCenterServiceImplTestCase.class);

    private CostCenterService costCenterService;

    private AgenceService agenceService;

    private ClassificationService classificationService;

    private StudyLevelService studyLevelService;

    private Long classifId;

    private Long levelId;

    private CostCenter costCenter1, costCenter2;

    /**
	 * Executes before each test method.
	 */
    @BeforeMethod
    public void setUp() throws Exception {
        costCenterService = (CostCenterService) ServerToolsForTest.getCostCenterService();
        agenceService = (AgenceService) ServerToolsForTest.getAgenceService();
        classificationService = ServerToolsForTest.getClassificationService();
        studyLevelService = ServerToolsForTest.getStudyLevelService();
    }

    /**
	 * Executes after each test method.
	 */
    @AfterMethod
    public void tearDown() throws Exception {
        costCenterService = null;
        agenceService = null;
        classificationService = null;
        studyLevelService = null;
    }

    @Test
    public void createObjects() throws Throwable {
        Classification classif = new Classification();
        classif.setStatus("CADRE");
        classif.setClassification("1.1");
        classif.setCoefficient("210");
        classificationService.saveClassification(classif);
        classifId = classif.getId();
        StudyLevel studyLevel = new StudyLevel();
        studyLevel.setLevel("bac+1");
        studyLevelService.saveStudyLevel(studyLevel);
        levelId = studyLevel.getId();
    }

    /**
	 * Test method.
	 */
    @Test(dependsOnMethods = "createObjects")
    public void saveCostCenter() throws Exception {
        costCenter1 = new CostCenter("123456", "test1");
        costCenterService.saveCostCenter(costCenter1);
        assertNotNull(costCenter1.getId());
        assertEquals("123456", costCenter1.getPortalId());
        Collaborator collaborator = new Collaborator();
        collaborator.setFirstName("Tiago");
        collaborator.setLastName("Fernandez");
        collaborator.setLeCostCenter(costCenter1);
        collaborator.setHiringDate(Date.valueOf("2000-02-015"));
        collaborator.setClassification(classificationService.getClassification(classifId));
        collaborator.setStudyLevel(studyLevelService.getStudyLevel(levelId));
        costCenter2 = new CostCenter("1457987", "test2");
        costCenterService.saveCostCenter(costCenter2);
        Agence ag = new Agence("titi l''agence");
        ag.addCostCenter(costCenter1);
        ag.addCostCenter(costCenter2);
        agenceService.saveAgence(ag);
        Set<CostCenter> ccs = agenceService.getAgence(ag.getId()).getCostCenters();
        assertNotNull(ccs);
        assertEquals(2, ccs.size());
        Set<Agence> ags = costCenterService.getCostCenter(costCenter1.getId()).getAgences();
        assertNotNull(ags);
        assertEquals(1, ags.size());
    }

    /**
	 * Teste la suppression d'un graphe d'objets.
	 * @throws Throwable
	 */
    @Test
    public void deleteCostCenter() throws Throwable {
        costCenter1 = new CostCenter("cc1 " + Math.random(), "test1" + Math.random());
        costCenterService.saveCostCenter(costCenter1);
        costCenter2 = new CostCenter("cc2 " + Math.random(), "test2" + Math.random());
        costCenterService.saveCostCenter(costCenter2);
        Agence ag = new Agence("ag1 " + Math.random());
        ag.addCostCenter(costCenter1);
        agenceService.saveAgence(ag);
        assertEquals(1, agenceService.getAgence(ag.getId()).getCostCenters().size());
        ag.addCostCenter(costCenter2);
        assertEquals(1, agenceService.getAgence(ag.getId()).getCostCenters().size());
        agenceService.saveAgence(ag);
        assertEquals(2, agenceService.getAgence(ag.getId()).getCostCenters().size());
        try {
            costCenterService.deleteCostCenter(costCenter1.getId());
            fail("Merde. La suppression aurait d� �chouer car il y a des d�pendances.");
        } catch (CostCenterServiceException ex) {
            logger.debug("OK. La suppression � �chou�e car toutes les d�pendances n'ont pas �t� supprim�es.");
        }
        ag.removeCostCenter(costCenter1);
        agenceService.saveAgence(ag);
        costCenterService.deleteCostCenter(costCenter1.getPortalId());
        costCenter1 = costCenterService.getCostCenter(costCenter1.getId());
        if (costCenter1 == null) {
            logger.debug("OK. La suppression � march�e.");
        } else {
            fail("Merde. La suppression aurait d� r�ussir.");
        }
    }

    /**
	 * Test method.
	 */
    @Test(dependsOnMethods = { "saveCostCenter" })
    public void findCostCenter() throws Exception {
        CostCenter cc = costCenterService.getCostCenterByPortalId("123456");
        assertNotNull(cc);
        assertNotNull(cc.getId());
        assertEquals("123456", cc.getPortalId());
        cc.setName("toto");
        costCenterService.saveCostCenter(cc);
        cc = costCenterService.getCostCenterByName("toto");
        assertNotNull(cc);
        assertNotNull(cc.getId());
        assertEquals("123456", cc.getPortalId());
    }

    /**
	 * Test method.
	 */
    @Test(dependsOnMethods = { "saveCostCenter", "findCostCenter" })
    public void saveCollaboratorsCostCenters() throws Exception {
        CostCenter cc = costCenterService.getCostCenterByPortalId("123456");
        Set<Collaborator> collaboratorList = cc.getCollaborators();
        for (Collaborator col : collaboratorList) {
            assertNotNull(col.getId());
            CostCenter cc2 = col.getCostCenter();
            assertNotNull(cc2);
            assertNotNull(cc2.getId());
            logger.info(col.fullName() + " - " + cc2.getPortalId());
        }
    }

    /**
	 * Test method.
	 */
    @Test(dependsOnMethods = { "saveCollaboratorsCostCenters" })
    public void listCostCenters() throws Exception {
        List<CostCenter> ccList = costCenterService.listCostCenter();
        assertNotNull(ccList);
        for (CostCenter cc : ccList) {
            logger.info(cc.getId() + "/" + cc.getPortalId());
        }
    }

    /**
	 * Test method.
	 */
    @Test
    public void saveCostCenterWithoutCCID() throws Exception {
        try {
            costCenterService.saveCostCenter(new CostCenter());
            fail("Saving CostCenter without costCenterId: this can't happen!");
        } catch (CostCenterServiceException ex) {
            logger.debug("Passed: the CC's ID is required" + ex.getMessage());
        }
    }
}
