package org.openxava.qamanager.test;

import org.openxava.tests.ModuleTestBase;

public class QaProjectsTest extends ModuleTestBase {

    private static final String projectID = "TEP";

    private static final String projectName = "Test Execution Project";

    private static final String customerID = "TC";

    private static final String projectTypeID = "832EF2BA7F0000010071EDC8E93430CE";

    public QaProjectsTest(String testName) {
        super(testName, "QAProjectManager", "QaProjects");
    }

    public void testCreateReadUpdateDelete() throws Exception {
        execute("CRUD.new");
        setValue("id", projectID);
        setValue("name", projectName);
        setValue("customer.id", customerID);
        setValue("projectType.id", projectTypeID);
        execute("CRUD.save");
        assertNoErrors();
        assertValue("id", "");
        assertValue("name", "");
        setValue("id", projectID);
        setValue("name", projectName);
        execute("CRUD.search");
        assertValue("id", projectID);
        assertValue("name", projectName);
        setValue("name", projectName + " MODIFIED");
        execute("CRUD.save");
        assertNoErrors();
        assertValue("id", "");
        assertValue("name", "");
        setValue("id", projectID);
        execute("CRUD.search");
        assertValue("id", projectID);
        assertValue("name", projectName + " MODIFIED");
        execute("CRUD.delete");
        assertNoErrors();
        assertMessage("Project deleted successfully");
    }

    public void testCreateQAProjectWithValidValues() throws Exception {
        execute("CRUD.new");
        setValue("id", projectID);
        setValue("name", projectName);
        setValue("customer.id", customerID);
        setValue("projectType.id", projectTypeID);
        execute("Sections.change", "activeSection=0");
        setValue("scheduledStart", "09/02/07");
        setValue("actualStart", "09/03/07");
        setValue("scheduledEnd", "09/05/07");
        setValue("actualEnd", "09/06/07");
        setValue("reasonDelay", "Resource shortage");
        execute("Sections.change", "activeSection=1");
        setValue("environmentDetails", "MySQL 5.1 , Java 1.4, Ant 1.6");
        execute("Sections.change", "activeSection=2");
        setValue("skillRequired", "Java Basic, SQA concepts, Shell scripting");
        execute("Sections.change", "activeSection=3");
        setValue("description", "Testing project's description goes here");
        execute("CRUD.save");
        assertNoErrors();
        execute("CRUD.new");
        setValue("id", projectID);
        execute("CRUD.search");
        execute("Sections.change", "activeSection=4");
        assertCollectionRowCount("contacts", 0);
        execute("Collection.new", "viewObject=xava_view_section4_contacts");
        setValue("contacts.name", "Contact Name");
        setValue("contacts.telephone", "94712944444");
        setValue("contacts.email", "janesh@users.sourceforge.net");
        execute("Collection.save", "viewObject=xava_view_section4_contacts");
        assertCollectionRowCount("contacts", 1);
        assertValueInCollection("contacts", 0, 0, "Contact Name");
        assertValueInCollection("contacts", 0, 1, "94712944444");
        assertValueInCollection("contacts", 0, 2, "janesh@users.sourceforge.net");
        execute("Collection.edit", "row=0,viewObject=xava_view_section4_contacts");
        execute("Collection.remove", "viewObject=xava_view_section4_contacts");
        assertCollectionRowCount("contacts", 0);
        execute("CRUD.new");
        setValue("id", projectID);
        execute("CRUD.search");
        execute("Sections.change", "activeSection=5");
        assertCollectionRowCount("projectDiaries", 0);
        execute("Collection.new", "viewObject=xava_view_section5_projectDiaries");
        setValue("projectDiaries.date", "09/05/07");
        setValue("projectDiaries.title", "Test title");
        setValue("projectDiaries.remarks", "Test Remark");
        execute("Collection.save", "viewObject=xava_view_section5_projectDiaries");
        assertCollectionRowCount("projectDiaries", 1);
        assertValueInCollection("projectDiaries", 0, 1, "Test title");
        assertValueInCollection("projectDiaries", 0, 0, "9/5/07");
        execute("Collection.edit", "row=0,viewObject=xava_view_section5_projectDiaries");
        execute("Collection.remove", "viewObject=xava_view_section5_projectDiaries");
        assertCollectionRowCount("projectDiaries", 0);
        execute("CRUD.save");
        assertNoErrors();
        execute("CRUD.new");
        setValue("id", projectID);
        execute("CRUD.search");
        execute("Sections.change", "activeSection=6");
        assertCollectionRowCount("documentReview", 0);
        execute("Collection.new", "viewObject=xava_view_section6_documentReview");
        setValue("documentReview.documentTitle", "Software Requirement doc");
        setValue("documentReview.documentVersion", "v1.0");
        setValue("documentReview.documentType", "2");
        setValue("documentReview.documentReviewStatus", "2");
        setValue("documentReview.documentAuthor.id", "1111");
        setValue("documentReview.documentReviewer.id", "1111");
        execute("Collection.save", "viewObject=xava_view_section6_documentReview");
        assertCollectionRowCount("documentReview", 1);
        execute("Collection.edit", "row=0,viewObject=xava_view_section6_documentReview");
        execute("Collection.remove", "viewObject=xava_view_section6_documentReview");
        assertCollectionRowCount("documentReview", 0);
        execute("CRUD.save");
        assertNoErrors();
        execute("CRUD.new");
        setValue("id", projectID);
        execute("CRUD.search");
        execute("Sections.change", "activeSection=7");
        assertCollectionRowCount("projectRisks", 0);
        execute("Collection.new", "viewObject=xava_view_section7_projectRisks");
        setValue("projectRisks.riskName", "First Risk");
        setValue("projectRisks.riskOwner.id", "1111");
        setValue("projectRisks.riskReporter.id", "1111");
        setValue("projectRisks.riskImpact", "2");
        setValue("projectRisks.riskLikelihood", "2");
        setValue("projectRisks.riskStatus", "2");
        setValue("projectRisks.riskReported", "01/10/07");
        setValue("projectRisks.riskScheduledClose", "01/11/07");
        setValue("projectRisks.riskActualClose", "01/12/07");
        execute("Collection.save", "viewObject=xava_view_section7_projectRisks");
        assertCollectionRowCount("projectRisks", 1);
        execute("Collection.edit", "row=0,viewObject=xava_view_section7_projectRisks");
        execute("Collection.remove", "viewObject=xava_view_section7_projectRisks");
        assertCollectionRowCount("projectRisks", 0);
        execute("CRUD.save");
        assertNoErrors();
        execute("CRUD.new");
        setValue("id", projectID);
        execute("CRUD.search");
        execute("Sections.change", "activeSection=8");
        assertCollectionRowCount("testCases", 0);
        execute("Collection.new", "viewObject=xava_view_section8_testCases");
        setValue("testCases.testCaseID", "1234");
        setValue("testCases.testCaseAuthor.id", "1111");
        setValue("testCases.testCaseVersion", "v1.0");
        execute("Collection.save", "viewObject=xava_view_section8_testCases");
        assertCollectionRowCount("testCases", 1);
        execute("Collection.edit", "row=0,viewObject=xava_view_section8_testCases");
        execute("Collection.remove", "viewObject=xava_view_section8_testCases");
        assertCollectionRowCount("testCases", 0);
        assertNoErrors();
    }

    public void testProjectScheduleDateValidations() throws Exception {
        execute("CRUD.new");
        setValue("id", projectID);
        setValue("name", projectName);
        execute("Sections.change", "activeSection=0");
        setValue("scheduledStart", "09/02/08");
        setValue("scheduledEnd", "09/06/07");
        execute("CRUD.save");
        assertErrorsCount(1);
        assertError("Scheduled end date cannot be before Scheduled start date");
        execute("CRUD.new");
        setValue("id", projectID);
        setValue("name", projectName);
        execute("Sections.change", "activeSection=0");
        setValue("actualStart", "09/09/07");
        setValue("actualEnd", "09/06/07");
        execute("CRUD.save");
        assertErrorsCount(1);
        assertError("Actual end date cannot be before Actual start date");
        execute("Sections.change", "activeSection=0");
        setValue("actualStart", "09/09/09");
        setValue("actualEnd", "09/10/09");
        execute("CRUD.save");
        assertErrorsCount(2);
        assertError("Actual end date cannot be a future date");
        assertError("Actual start date cannot be a future date");
    }

    public void testProjectDiaryValidations() throws Exception {
        execute("CRUD.new");
        setValue("id", projectID);
        setValue("name", projectName);
        execute("CRUD.save");
        assertNoErrors();
        setValue("id", projectID);
        execute("CRUD.search");
        execute("Sections.change", "activeSection=5");
        assertCollectionRowCount("projectDiaries", 0);
        execute("Collection.new", "viewObject=xava_view_section5_projectDiaries");
        execute("Collection.save", "viewObject=xava_view_section5_projectDiaries");
        assertErrorsCount(1);
        assertError("Value for Title in Project Diary is required");
    }

    public void testAddingMultipleDiaryRecords() throws Exception {
        execute("CRUD.new");
        setValue("id", projectID);
        setValue("name", projectName);
        execute("CRUD.save");
        assertNoErrors();
        setValue("id", projectID);
        execute("CRUD.search");
        execute("Sections.change", "activeSection=5");
        assertCollectionRowCount("projectDiaries", 0);
        execute("Collection.new", "viewObject=xava_view_section5_projectDiaries");
        setValue("projectDiaries.date", "09/05/07");
        setValue("projectDiaries.title", "Test title 1");
        setValue("projectDiaries.remarks", "Test Remark 1");
        execute("Collection.save", "viewObject=xava_view_section5_projectDiaries");
        assertNoErrors();
        execute("Sections.change", "activeSection=5");
        assertCollectionRowCount("projectDiaries", 1);
        setValue("projectDiaries.date", "09/06/07");
        setValue("projectDiaries.title", "Test title 2");
        setValue("projectDiaries.remarks", "Test Remark  2");
        execute("Collection.save", "viewObject=xava_view_section5_projectDiaries");
        execute("Sections.change", "activeSection=5");
        assertCollectionRowCount("projectDiaries", 2);
    }

    public void testProjectContactPersonValidations() throws Exception {
        execute("CRUD.new");
        setValue("id", projectID);
        setValue("name", projectName);
        execute("CRUD.save");
        assertNoErrors();
        setValue("id", projectID);
        execute("CRUD.search");
        execute("Sections.change", "activeSection=4");
        assertCollectionRowCount("contacts", 0);
        execute("Collection.new", "viewObject=xava_view_section4_contacts");
        execute("Collection.save", "viewObject=xava_view_section4_contacts");
        assertErrorsCount(1);
        assertError("Value for Name in Project Contacts is required");
    }

    public void tearDown() throws Exception {
        execute("CRUD.new");
        setValue("id", projectID);
        execute("CRUD.search");
        execute("CRUD.delete");
    }
}
