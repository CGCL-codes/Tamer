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
import fr.gfi.gfinet.server.AgenceServiceException;
import fr.gfi.gfinet.server.ClassificationService;
import fr.gfi.gfinet.server.CollaboratorService;
import fr.gfi.gfinet.server.StudyLevelService;
import fr.gfi.gfinet.server.info.Agence;
import fr.gfi.gfinet.server.info.Classification;
import fr.gfi.gfinet.server.info.Collaborator;
import fr.gfi.gfinet.server.info.StudyLevel;
import fr.gfi.gfinet.server.util.ServerToolsForTest;

/**
 * AgenceServiceImplTestCase.
 */
public class AgenceServiceImplTestCase {

    private static final Log logger = LogFactory.getLog(CollaboratorServiceImplTestCase.class);

    private AgenceService agenceService;

    private CollaboratorService collaboratorService;

    private ClassificationService classificationService;

    private StudyLevelService studyLevelService;

    private Long agenceId;

    /**
	 * Executes before each test method.
	 */
    @BeforeMethod
    public void setUp() throws Exception {
        agenceService = (AgenceService) ServerToolsForTest.getAgenceService();
        collaboratorService = (CollaboratorService) ServerToolsForTest.getCollaboratorService();
        classificationService = ServerToolsForTest.getClassificationService();
        studyLevelService = ServerToolsForTest.getStudyLevelService();
    }

    /**
	 * Executes after each test method.
	 */
    @AfterMethod
    public void tearDown() throws Exception {
        agenceService = null;
        collaboratorService = null;
        classificationService = null;
        studyLevelService = null;
    }

    @Test
    public void createObjects() throws Throwable {
    }

    /**
	 * Test method.
	 */
    @Test(dependsOnMethods = "createObjects")
    public void saveAgence() throws Exception {
        Agence agence = new Agence();
        agence.setName("GFI Paris");
        agenceService.saveAgence(agence);
        agenceId = agence.getId();
        Collaborator collaborator = new Collaborator();
        collaborator.setFirstName("Tiago");
        collaborator.setLastName("Fernandez");
        collaborator.setLAgence(agence);
        collaborator.setHiringDate(Date.valueOf("2001-10-15"));
        Classification classif = new Classification("CADRE", "1.1", "210");
        classificationService.saveClassification(classif);
        collaborator.setClassification(classif);
        StudyLevel level = new StudyLevel("bac+1");
        studyLevelService.saveStudyLevel(level);
        collaborator.setStudyLevel(level);
        collaboratorService.saveCollaborator(collaborator);
        assertNotNull(agence.getId());
        assertEquals("GFI Paris", agence.getName());
    }

    /**
	 * Test method.
	 */
    @Test(dependsOnMethods = { "saveAgence" })
    public void saveAgenceWithoutName() throws Exception {
        try {
            agenceService.saveAgence(new Agence());
            fail("Saving agency without name: this can't happen!");
        } catch (AgenceServiceException ex) {
            logger.debug("Passed: the agency's name is required");
        }
    }

    /**
	 * Test method.
	 */
    @Test(dependsOnMethods = { "saveAgence" })
    public void saveCollaboratorsAgency() throws Exception {
        Agence agence = agenceService.getAgence(agenceId);
        Set<Collaborator> collaboratorList = agence.getCollaborators();
        for (Collaborator col : collaboratorList) {
            assertNotNull(col.getId());
            Agence ag = col.getAgence();
            assertNotNull(ag);
            assertNotNull(ag.getId());
            logger.info(col.fullName() + " - " + ag.getName());
        }
    }

    /**
	 * Test method.
	 */
    @Test(dependsOnMethods = { "saveCollaboratorsAgency" })
    public void listAgence() throws Exception {
        List<Agence> agenceList = agenceService.listAgence();
        assertNotNull(agenceList);
        for (Agence ag : agenceList) {
            logger.info(ag.getId() + "/" + ag.getName());
        }
    }
}
