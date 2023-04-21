package fr.gfi.gfinet.server.service;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import fr.gfi.gfinet.server.StudyLevelService;
import fr.gfi.gfinet.server.info.StudyLevel;

/**
 * StudyLevelServiceImplTestBean.java
 * 
 * @author Jean DAT
 * @since 18 sept. 07
 */
public class StudyLevelServiceImplTestBean {

    private StudyLevelService studyLevelService;

    private Log logger = LogFactory.getLog(StudyLevelServiceImplTestBean.class);

    /** Cr�e des objets r�utilis�s par les autres m�thodes. */
    public void createObjects() throws Throwable {
        StudyLevel level1 = new StudyLevel("BAC + 1");
        studyLevelService.saveStudyLevel(level1);
        StudyLevel level2 = new StudyLevel("BAC + 2");
        studyLevelService.saveStudyLevel(level2);
        StudyLevel level3 = new StudyLevel("BAC + 3");
        studyLevelService.saveStudyLevel(level3);
    }

    /** Test study levels research method. */
    public void testSeekStudyLevels() throws Throwable {
        List<StudyLevel> results = studyLevelService.seekStudyLevel("BAC + 2");
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    /** Test study levels list method. */
    public void testListStudyLevels() throws Throwable {
        List<StudyLevel> results = studyLevelService.listStudyLevel();
        assertNotNull(results);
        assertEquals(true, results.size() >= 3);
    }

    /**
	 * @param studyLevelService the studyLevelService to set
	 */
    public void setStudyLevelService(StudyLevelService studyLevelService) {
        this.studyLevelService = studyLevelService;
    }
}
