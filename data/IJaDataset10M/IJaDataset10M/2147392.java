package com.dcivision.form.dao;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.operation.DatabaseOperation;
import junit.framework.Test;
import junit.framework.TestSuite;
import com.dcivision.form.bean.FormGroup;
import com.dcivision.form.bean.FormPage;
import com.dcivision.form.bean.FormRecord;
import com.dcivision.form.bean.FormSection;
import com.dcivision.form.bean.MtmFormPageFormSection;
import com.dcivision.form.utility.FormObjectFactory;
import com.dcivision.form.utility.FormUtility;
import com.dcivision.framework.SuperDAOTest;
import com.dcivision.framework.TLog;

public class FormSectionDAOTest extends SuperDAOTest {

    private FormSectionDAObject formSectionDAO;

    public FormSectionDAOTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        formSectionDAO = new FormSectionDAObject(getSessionCtn(), getConnection());
        DatabaseOperation.DELETE_ALL.execute(new DatabaseConnection(getConnection(), ""), FormUtility.getDeleteAllDataSet());
    }

    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite();
        suite.addTest(new FormSectionDAOTest("testFormSectionOperate"));
        return suite;
    }

    /**
   * Test add��query��update and delete operate of form section.
   * 
   * @throws Exception
   * @author Bill
   */
    public void testFormSectionOperate() throws Exception {
        FormGroup formGroup = FormObjectFactory.getNewFormGroup("TestFormGroup");
        FormGroupDAObject formGroupDAO = new FormGroupDAObject(getSessionCtn(), getConnection());
        FormGroup newFormGroup = (FormGroup) formGroupDAO.insert(formGroup);
        Integer userRecordID = getSessionCtn().getUserRecordID();
        FormUtility.setFormGroupPermission(newFormGroup, userRecordID, getSessionCtn(), getConnection());
        FormRecord newFormRecord = FormObjectFactory.getNewFormRecord("TestFormRecord");
        newFormRecord.setFormGroupID(newFormGroup.getID());
        newFormRecord.setGroupName(newFormGroup.getGroupName());
        FormRecord testInsertFormRecord;
        FormRecordDAObject formRecordDAO = new FormRecordDAObject(getSessionCtn(), getConnection());
        testInsertFormRecord = (FormRecord) formRecordDAO.insert(newFormRecord);
        FormPage newFormPage = FormObjectFactory.getNewFormPage("TestFormPageTitle");
        FormPage testInsertFormPage;
        newFormPage.setFormRecordID(testInsertFormRecord.getID());
        FormPageDAObject formPageDAO = new FormPageDAObject(getSessionCtn(), getConnection());
        testInsertFormPage = (FormPage) formPageDAO.insert(newFormPage);
        FormSection newFormSection = FormObjectFactory.getNewFormSection("TestFormSectionTitle");
        FormSection testInsertFormSection;
        testInsertFormSection = (FormSection) formSectionDAO.insert(newFormSection);
        MtmFormPageFormSectionDAObject mtmFormPageFormSectionDAO = new MtmFormPageFormSectionDAObject(getSessionCtn(), getConnection());
        MtmFormPageFormSection mtmFormPageFormSection = FormObjectFactory.getNewMtmFormPageFormSection();
        mtmFormPageFormSection.setPageID(testInsertFormPage.getID());
        mtmFormPageFormSection.setSectionID(testInsertFormSection.getID());
        mtmFormPageFormSectionDAO.insert(mtmFormPageFormSection);
        assertNotNull("Return form section is null!", testInsertFormSection);
        assertEquals("Insert form section is error!", newFormSection.getTitle(), testInsertFormSection.getTitle());
        FormSection testQueryFormSection;
        testQueryFormSection = (FormSection) formSectionDAO.getByID(testInsertFormSection.getID());
        assertNotNull("Return form section is null!", testQueryFormSection);
        assertEquals("Query form section is error!", testInsertFormSection.getID(), testQueryFormSection.getID());
        FormSection testUpdateFormSection;
        String testUpdateFormSectionTitle = "TestUpdateFormSectionTitle";
        testQueryFormSection.setTitle(testUpdateFormSectionTitle);
        testUpdateFormSection = (FormSection) formSectionDAO.update(testQueryFormSection);
        assertNotNull("Update form section is null!", testUpdateFormSection);
        assertEquals("Update form section is error!", testQueryFormSection.getID(), testUpdateFormSection.getID());
        assertEquals("Update form section is error!", testUpdateFormSectionTitle, testUpdateFormSection.getTitle());
        FormSection testDeleteFormSection;
        testDeleteFormSection = (FormSection) formSectionDAO.delete(testUpdateFormSection);
        assertNotNull("Delete form section is null!", testDeleteFormSection);
        assertEquals("Delete form section is error!", testUpdateFormSection.getID(), testDeleteFormSection.getID());
        try {
            formSectionDAO.getByID(testUpdateFormSection.getID());
            fail("This form section is not delete!");
        } catch (Exception ex) {
            TLog.log.info("Delete form section success.");
        }
    }
}
