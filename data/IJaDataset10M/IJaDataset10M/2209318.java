package org.dcm4chex.archive.ejb.jdbc;

import org.apache.cactus.ServletTestCase;
import org.dcm4che.data.Dataset;
import org.dcm4che.data.DcmObjectFactory;
import org.dcm4che.dict.Tags;

/**
 * @author <a href="mailto:gunter@tiani.com">Gunter Zeilinger</a>
 *
 */
public class QueryCmdTest extends ServletTestCase {

    private static final DcmObjectFactory dof = DcmObjectFactory.getInstance();

    public static void main(String[] args) {
        junit.textui.TestRunner.run(QueryCmdTest.class);
    }

    public QueryCmdTest(String name) {
        super(name);
    }

    public void testPatientQuery() throws Exception {
        Dataset keys = dof.newDataset();
        keys.putCS(Tags.QueryRetrieveLevel, "PATIENT");
        keys.putLO(Tags.PatientID);
        keys.putPN(Tags.PatientName);
        keys.putDA(Tags.PatientBirthDate);
        keys.putCS(Tags.PatientSex, "F");
        QueryCmd cmd = QueryCmd.create(keys, null, true, false, false, false, null);
        cmd.execute();
        try {
            while (cmd.next()) cmd.getDataset();
        } finally {
            cmd.close();
        }
    }

    public void testStudyQuery() throws Exception {
        Dataset keys = dof.newDataset();
        keys.putCS(Tags.QueryRetrieveLevel, "STUDY");
        keys.putLO(Tags.PatientID);
        keys.putPN(Tags.PatientName, "*ge*");
        keys.putUI(Tags.StudyInstanceUID);
        keys.putLO(Tags.StudyDescription);
        keys.putSH(Tags.StudyID);
        keys.putDA(Tags.StudyDate, "19970811");
        keys.putTM(Tags.StudyTime);
        keys.putCS(Tags.ModalitiesInStudy, "US");
        QueryCmd cmd = QueryCmd.create(keys, null, true, false, false, false, null);
        cmd.execute();
        try {
            while (cmd.next()) cmd.getDataset();
        } finally {
            cmd.close();
        }
    }

    public void testSeriesQuery() throws Exception {
        Dataset keys = dof.newDataset();
        keys.putCS(Tags.QueryRetrieveLevel, "SERIES");
        keys.putUI(Tags.SeriesInstanceUID);
        keys.putIS(Tags.SeriesNumber);
        keys.putDA(Tags.SeriesDate);
        keys.putTM(Tags.SeriesTime);
        keys.putCS(Tags.Modality, "US");
        QueryCmd cmd = QueryCmd.create(keys, null, true, false, false, false, null);
        cmd.execute();
        try {
            while (cmd.next()) cmd.getDataset();
        } finally {
            cmd.close();
        }
    }

    public void testImageQuery() throws Exception {
        Dataset keys = dof.newDataset();
        keys.putCS(Tags.QueryRetrieveLevel, "IMAGE");
        keys.putUI(Tags.SOPInstanceUID);
        keys.putUI(Tags.SOPClassUID);
        keys.putIS(Tags.InstanceNumber, "1");
        QueryCmd cmd = QueryCmd.create(keys, null, true, false, false, false, null);
        cmd.execute();
        try {
            while (cmd.next()) cmd.getDataset();
        } finally {
            cmd.close();
        }
    }
}
