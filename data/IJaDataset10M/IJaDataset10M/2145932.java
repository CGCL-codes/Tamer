package org.dcm4chex.archive.ejb.entity;

import javax.naming.Context;
import javax.naming.InitialContext;
import org.apache.cactus.ServletTestCase;
import org.dcm4che.data.Dataset;
import org.dcm4che.data.DcmObjectFactory;
import org.dcm4che.dict.Tags;
import org.dcm4chex.archive.ejb.interfaces.PatientLocal;
import org.dcm4chex.archive.ejb.interfaces.PatientLocalHome;
import org.dcm4chex.archive.ejb.interfaces.StudyLocal;
import org.dcm4chex.archive.ejb.interfaces.StudyLocalHome;

/**
 * @author <a href="mailto:gunter@tiani.com">Gunter Zeilinger</a>
 *
 */
public class StudyBeanTest extends ServletTestCase {

    public static final String PID = "P-999999";

    public static final String PNAME = "Test^StudyBean";

    public static final String UID_ = "1.2.40.0.13.1.1.9999.";

    private static final DcmObjectFactory dof = DcmObjectFactory.getInstance();

    private PatientLocalHome patHome;

    private StudyLocalHome studyHome;

    private Object patPk;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(StudyBeanTest.class);
    }

    protected void setUp() throws Exception {
        Context ctx = new InitialContext();
        patHome = (PatientLocalHome) ctx.lookup("java:comp/env/ejb/Patient");
        studyHome = (StudyLocalHome) ctx.lookup("java:comp/env/ejb/Study");
        ctx.close();
        Dataset ds = dof.newDataset();
        ds.putLO(Tags.PatientID, PID);
        ds.putPN(Tags.PatientName, PNAME);
        PatientLocal pat = patHome.create(ds);
        patPk = pat.getPrimaryKey();
        for (int i = 0; i < 5; ++i) {
            ds.putUI(Tags.StudyInstanceUID, UID_ + i);
            studyHome.create(ds, pat);
        }
    }

    protected void tearDown() throws Exception {
        patHome.remove(patPk);
    }

    /**
     * Constructor for StudyBeanTest.
     * @param arg0
     */
    public StudyBeanTest(String arg0) {
        super(arg0);
    }

    public void testFindByStudyIuid() throws Exception {
        for (int i = 0; i < 5; i++) {
            StudyLocal study = studyHome.findByStudyIuid(UID_ + i);
        }
    }
}
