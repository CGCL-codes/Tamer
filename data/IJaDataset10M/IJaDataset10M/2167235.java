package edu.ncsu.csc.itrust.action;

import java.util.List;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.HealthRecord;
import edu.ncsu.csc.itrust.beans.forms.HealthRecordForm;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.testutils.EvilDAOFactory;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class EditHealthHistoryActionTest extends TestCase {

    private DAOFactory factory = TestDAOFactory.getTestInstance();

    private TestDataGenerator gen;

    private EditHealthHistoryAction action;

    @Override
    protected void setUp() throws Exception {
        gen = new TestDataGenerator();
        gen.clearAllTables();
        gen.patient1();
        gen.patient2();
        action = new EditHealthHistoryAction(factory, 9000000000L, "2");
    }

    public void testAddToEmptyRecordSimple() throws Exception {
        assertEquals(1, action.getAllHealthRecords(1L).size());
        HealthRecordForm hr = new HealthRecordForm();
        hr.setBloodPressureN("999");
        hr.setBloodPressureD("999");
        hr.setCholesterolHDL("50");
        hr.setCholesterolLDL("50");
        hr.setCholesterolTri("499");
        hr.setHeight("65.2");
        hr.setWeight("9999.9");
        hr.setIsSmoker("1");
        String confirm = action.addHealthRecord(1L, hr);
        assertEquals("Information Recorded", confirm);
        List<HealthRecord> records = action.getAllHealthRecords(1L);
        assertEquals(2, records.size());
    }

    public void testTotalCholesterol() throws Exception {
        HealthRecordForm hr = new HealthRecordForm();
        hr.setBloodPressureN("999");
        hr.setBloodPressureD("999");
        hr.setCholesterolHDL("50");
        hr.setCholesterolLDL("50");
        hr.setCholesterolTri("500");
        hr.setHeight("65.2");
        hr.setWeight("9999.9");
        action.addHealthRecord(2L, hr);
        hr.setCholesterolTri("501");
        try {
            action.addHealthRecord(2L, hr);
            fail("exception should have been thrown");
        } catch (FormValidationException e) {
            assertEquals("Total cholesterol must be in [100,600]", e.getErrorList().get(0));
            assertEquals(1, e.getErrorList().size());
        }
    }

    public void testPatientNameNull() throws Exception {
        assertNotNull(action.getPatientName());
    }

    public void testAddHealthRecordEvil() throws Exception {
        HealthRecordForm hr = new HealthRecordForm();
        hr.setBloodPressureN("999");
        hr.setBloodPressureD("999");
        hr.setCholesterolHDL("50");
        hr.setCholesterolLDL("50");
        hr.setCholesterolTri("499");
        hr.setHeight("65.2");
        hr.setWeight("9999.9");
        hr.setIsSmoker("1");
        action = new EditHealthHistoryAction(new EvilDAOFactory(1), 9000000000L, "2");
        try {
            action.addHealthRecord(1l, hr);
        } catch (iTrustException e) {
            DBException dbe = (DBException) e;
            assertEquals(EvilDAOFactory.MESSAGE, dbe.getSQLException().getMessage());
        }
    }
}
