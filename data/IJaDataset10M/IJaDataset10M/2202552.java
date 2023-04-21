package org.netbeans.server.snapshots.complexres;

import org.netbeans.server.uihandler.*;
import java.io.File;
import javax.persistence.EntityManager;
import org.netbeans.modules.exceptions.entity.Slowness;
import org.netbeans.server.componentsmatch.Matcher;

/**
 *
 * @author Jindrich Sedek
 */
public class ComplexDuplicatesTest extends DatabaseTestCase {

    private ExceptionsData excData;

    private static Integer reportID = 1;

    public ComplexDuplicatesTest(String name) {
        super(name);
    }

    public void testCheckDuplicates() throws Exception {
        EntityManager em = perUtils.createEntityManager();
        em.getTransaction().begin();
        setUpInnocents(em);
        em.getTransaction().commit();
        em.close();
        Matcher.getDefault().reload();
        addNext(1, false, 1, "org.netbeans.modules.editor.java.JavaCodeTemplateFilter.<init>", "java", "editor");
        addNext(2, true, 1);
        addNext(3, false, 3, "org.netbeans.modules.j2ee.metadata.model.api.support.annotation.AnnotationModelHelper.runJavaSourceTask", "j2ee", "code");
        addNext(4, false, 4, "org.netbeans.modules.editor.java.JavaCodeTemplateFilter$1.run", "java", "editor");
        addNext(5, false, 5, "org.netbeans.modules.java.navigation.actions.OpenAction.actionPerformed", "java", "navigation");
        addNext(6, false, 6, "org.netbeans.modules.java.hints.infrastructure.JavaHintsPositionRefresher.getErrorDescriptionsAt", "java", "hints");
        addNext(7, false, 7, "org.netbeans.modules.web.struts.StrutsConfigUtilities.getActionServlet", "web", "struts");
        addNext(8, false, 8, "org.netbeans.modules.editor.java.JavaCompletionItem$ClassItem.substituteText", "java", "editor");
        addNext(9, true, 1);
        addNext(10, true, 3);
        addNext(11, true, 6);
        addNext(12, false, 1, "org.netbeans.modules.websvc.rest.support.SourceGroupSupport.getFileObjectFromClassName", "websvc", "rest");
    }

    private void addNext(int index, boolean duplicate, int id) throws Exception {
        addNext(index, duplicate, id, null, null, null);
    }

    private void addNext(int index, boolean duplicate, int id, String method, String comp, String subcomp) throws Exception {
        File dataUploadDir = new File(Utils.getUploadDirPath(Utils.getRootUploadDirPath(), "NB2040811605"));
        File log = LogsManagerTest.extractResourceAs(ComplexDuplicatesTest.class, dataUploadDir, "uilog-" + Integer.toString(index) + ".xml", "NB2040811605." + reportID);
        File npsUploadDir = new File(Utils.getUploadDirPath(Utils.getSlownessRootUploadDir(), "NB2040811605"));
        File npsLog = LogsManagerTest.extractResourceAs(ComplexDuplicatesTest.class, npsUploadDir, "snapshot-" + Integer.toString(index) + ".nps", "NB2040811605." + reportID);
        excData = addLog(log, "NB2040811605");
        assertNotNull("MAPA SHOULD BE NULL FOR NO SESSIONS", excData);
        assertEquals("THIS ISSUE SHOULD (NOT) BE A DUPLICATE", duplicate, excData.getReportId() != 0);
        assertEquals("ISSUE SHOULD INCREASE", reportID, excData.getSubmitId());
        if (duplicate) {
            assertAreDuplicates("These issues should be duplicates ", id, excData.getSubmitId().intValue());
        } else {
            assertFalse(excData.getReportId() != 0);
        }
        if (comp != null) {
            EntityManager em = perUtils.createEntityManager();
            Slowness sbm = em.find(Slowness.class, reportID);
            assertEquals(method, sbm.getSuspiciousMethod().getName());
            assertEquals(comp, sbm.getReportId().getComponent());
            assertEquals(subcomp, sbm.getReportId().getSubcomponent());
            em.close();
        }
        reportID++;
    }
}
