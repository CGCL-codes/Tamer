package com.misyshealthcare.connect.doc.ccd;

import hl7OrgV3.ClinicalDocumentDocument1;
import hl7OrgV3.POCDMT000040Component2;
import hl7OrgV3.POCDMT000040StructuredBody;
import java.util.Calendar;
import java.util.GregorianCalendar;
import junit.framework.TestCase;
import com.misyshealthcare.connect.base.SharedEnums;
import com.misyshealthcare.connect.base.clinicaldata.AdvanceDirective;
import com.misyshealthcare.connect.base.clinicaldata.Allergy;
import com.misyshealthcare.connect.base.clinicaldata.Code;
import com.misyshealthcare.connect.base.clinicaldata.DoseQuantity;
import com.misyshealthcare.connect.base.clinicaldata.Immunization;
import com.misyshealthcare.connect.base.clinicaldata.LabResult;
import com.misyshealthcare.connect.base.clinicaldata.Measurements;
import com.misyshealthcare.connect.base.clinicaldata.Medication;
import com.misyshealthcare.connect.base.clinicaldata.Order;
import com.misyshealthcare.connect.base.clinicaldata.Procedure;
import com.misyshealthcare.connect.base.clinicaldata.Quantity;
import com.misyshealthcare.connect.base.clinicaldata.Result;
import com.misyshealthcare.connect.base.clinicaldata.SimpleProblem;
import com.misyshealthcare.connect.base.clinicaldata.Test;
import com.misyshealthcare.connect.doc.ccd.BaseBuildingComponent;
import com.misyshealthcare.connect.doc.ccd.EffectiveTime;
import com.misyshealthcare.connect.doc.ccd.EstimatedTime;
import com.misyshealthcare.connect.doc.ccd.ProposedDisposition;
import com.misyshealthcare.connect.doc.ccd.TransportMode;
import com.misyshealthcare.connect.doc.ccd.XmlBeanUtil;
import com.misyshealthcare.connect.util.LibraryConfig;

/**
 *  
 *
 * @author Wenzhi Li
 * @version 3.0, Dec 3, 2007
 */
public class BaseBuildingComponentTest extends TestCase {

    BaseBuildingComponent builder;

    public void setUp() throws Exception {
        super.setUp();
        LibraryConfig.getInstance().setOidSource(MemoryOidSource.getInstance());
        LibraryConfig.getInstance().setOidRoot("2.16.840.1.113883.3.28.1");
        builder = new BaseBuildingComponent();
    }

    public void testBuildResonForReferral() {
        ClinicalDocumentDocument1 doc = ClinicalDocumentDocument1.Factory.newInstance();
        POCDMT000040Component2 component = doc.addNewClinicalDocument().addNewComponent();
        POCDMT000040StructuredBody body = component.addNewStructuredBody();
        boolean required = true;
        builder.buildReasonForReferral(body, "He is sick", required);
        String xml = XmlBeanUtil.toXml(doc);
        System.out.println("XML:");
        System.out.println(xml);
        builder.buildReasonForReferral(body, "", required);
        xml = XmlBeanUtil.toXml(doc);
        System.out.println("XML:");
        System.out.println(xml);
    }

    public void testBuildActiveProblem() {
        ClinicalDocumentDocument1 doc = ClinicalDocumentDocument1.Factory.newInstance(null);
        POCDMT000040Component2 component = doc.addNewClinicalDocument().addNewComponent();
        POCDMT000040StructuredBody body = component.addNewStructuredBody();
        SimpleProblem[] problems = new SimpleProblem[2];
        problems[0] = new SimpleProblem("CHRONIC PHARYNGITIS AND NASOPHARYNGITIS", new Code("472.0", null, "ICD9", null), new GregorianCalendar(), null, SharedEnums.ClinicalStatusCode.RESOLVED, "Misys CPR", "This is comment1");
        problems[1] = new SimpleProblem("Problem2", new Code("222.2", null, "ICD9", null), new GregorianCalendar(), null, SharedEnums.ClinicalStatusCode.ACTIVE, "Misys CPR", "This is comment2");
        boolean required = true;
        builder.buildActiveProblemComponent(body, problems, required);
        String xml = XmlBeanUtil.toXml(doc);
        System.out.println("XML:");
        System.out.println(xml);
    }

    public void testBuildResolvedProblem() {
        ClinicalDocumentDocument1 doc = ClinicalDocumentDocument1.Factory.newInstance();
        POCDMT000040Component2 component = doc.addNewClinicalDocument().addNewComponent();
        POCDMT000040StructuredBody body = component.addNewStructuredBody();
        SimpleProblem[] problems = new SimpleProblem[2];
        problems[0] = new SimpleProblem("CHRONIC PHARYNGITIS AND NASOPHARYNGITIS", new Code("472.0", null, "ICD9", null), new GregorianCalendar(), null, SharedEnums.ClinicalStatusCode.RESOLVED, "Misys CPR", "This is comment1");
        problems[1] = new SimpleProblem("Problem2", new Code("222.2", null, "ICD9", null), new GregorianCalendar(), null, SharedEnums.ClinicalStatusCode.ACTIVE, "Misys CPR", "This is comment2");
        boolean required = true;
        builder.buildResolvedProblemComponent(body, problems, required);
        String xml = XmlBeanUtil.toXml(doc);
        System.out.println("XML:");
        System.out.println(xml);
    }

    public void testEmptyProblem() {
        ClinicalDocumentDocument1 doc = ClinicalDocumentDocument1.Factory.newInstance();
        POCDMT000040Component2 component = doc.addNewClinicalDocument().addNewComponent();
        POCDMT000040StructuredBody body = component.addNewStructuredBody();
        SimpleProblem[] problems = new SimpleProblem[0];
        boolean required = true;
        builder.buildActiveProblemComponent(body, problems, required);
        String xml = XmlBeanUtil.toXml(doc);
        System.out.println("XML:");
        System.out.println(xml);
        builder.buildResolvedProblemComponent(body, problems, required);
        xml = XmlBeanUtil.toXml(doc);
        System.out.println("XML:");
        System.out.println(xml);
    }

    public void testBuildMedication() {
        ClinicalDocumentDocument1 doc = ClinicalDocumentDocument1.Factory.newInstance();
        POCDMT000040Component2 component = doc.addNewClinicalDocument().addNewComponent();
        POCDMT000040StructuredBody body = component.addNewStructuredBody();
        Medication[] medications = new Medication[1];
        medications[0] = new Medication("Amoxicillin (250 mg tabs)", new GregorianCalendar(), null, new Code("472.0", null, "ICD9", null), "3 times per day  ora", new Code("14735", null, "RouteOfAdministration", null), new DoseQuantity(new Quantity("1", "tablet"), null), "t.i.d", null, "", null, "active", null);
        boolean required = true;
        builder.buildMedicationComponent(body, medications, required);
        String xml = XmlBeanUtil.toXml(doc);
        System.out.println("XML:");
        System.out.println(xml);
    }

    public void testBuildAllergy() {
        ClinicalDocumentDocument1 doc = ClinicalDocumentDocument1.Factory.newInstance();
        POCDMT000040Component2 component = doc.addNewClinicalDocument().addNewComponent();
        POCDMT000040StructuredBody body = component.addNewStructuredBody();
        Allergy[] allergies = new Allergy[1];
        allergies[0] = new Allergy("Iodinated contrast", "laryngeal oedema", null, null, new GregorianCalendar(), null);
        boolean required = true;
        builder.buildAllergyComponent(body, allergies, required);
        String xml = XmlBeanUtil.toXml(doc);
        System.out.println("XML:");
        System.out.println(xml);
    }

    public void testBuildSurgicalHistory() {
        ClinicalDocumentDocument1 doc = ClinicalDocumentDocument1.Factory.newInstance();
        POCDMT000040Component2 component = doc.addNewClinicalDocument().addNewComponent();
        POCDMT000040StructuredBody body = component.addNewStructuredBody();
        String[] surgeries = new String[2];
        surgeries[0] = "Surgery history 1";
        surgeries[1] = "Surgery history 2";
        boolean required = true;
        builder.buildSurgeryComponent(body, surgeries, required);
        String xml = XmlBeanUtil.toXml(doc);
        System.out.println("XML:");
        System.out.println(xml);
    }

    public void testBuildImmunization() {
        ClinicalDocumentDocument1 doc = ClinicalDocumentDocument1.Factory.newInstance();
        POCDMT000040Component2 component = doc.addNewClinicalDocument().addNewComponent();
        POCDMT000040StructuredBody body = component.addNewStructuredBody();
        Immunization[] immunizations = new Immunization[1];
        immunizations[0] = new Immunization(GregorianCalendar.getInstance(), "Influenza virus vaccine, IM", new Code("88", null, "LOINC", null), new Code("14628", "Oral", "RouteOfAdministration", null), new DoseQuantity(new Quantity("1", null), null), null, "comment", "reference", null);
        boolean required = true;
        builder.buildImmunizationComponent(body, immunizations, required);
        String xml = XmlBeanUtil.toXml(doc);
        System.out.println("XML:");
        System.out.println(xml);
    }

    public void testBuildFamilyHistory() {
        ClinicalDocumentDocument1 doc = ClinicalDocumentDocument1.Factory.newInstance();
        POCDMT000040Component2 component = doc.addNewClinicalDocument().addNewComponent();
        POCDMT000040StructuredBody body = component.addNewStructuredBody();
        String[] histories = new String[2];
        histories[0] = "Family history 1";
        histories[1] = "Family history 2";
        boolean required = true;
        builder.buildFamilyHistoryComponent(body, histories, required);
        String xml = XmlBeanUtil.toXml(doc);
        System.out.println("XML:");
        System.out.println(xml);
    }

    public void testBuildSocialHistory() {
        ClinicalDocumentDocument1 doc = ClinicalDocumentDocument1.Factory.newInstance();
        POCDMT000040Component2 component = doc.addNewClinicalDocument().addNewComponent();
        POCDMT000040StructuredBody body = component.addNewStructuredBody();
        String[] histories = new String[2];
        histories[0] = "Social history 1";
        histories[1] = "Social history 2";
        boolean required = true;
        builder.buildSocialHistoryComponent(body, histories, required);
        String xml = XmlBeanUtil.toXml(doc);
        System.out.println("XML:");
        System.out.println(xml);
    }

    public void testBuildReviewOfSystems() {
        ClinicalDocumentDocument1 doc = ClinicalDocumentDocument1.Factory.newInstance();
        POCDMT000040Component2 component = doc.addNewClinicalDocument().addNewComponent();
        POCDMT000040StructuredBody body = component.addNewStructuredBody();
        String[] reviews = new String[2];
        reviews[0] = "System Review 1";
        reviews[1] = "System Review 2";
        boolean required = true;
        builder.buildReviewOfSystemComponent(body, reviews, required);
        String xml = XmlBeanUtil.toXml(doc);
        System.out.println("XML:");
        System.out.println(xml);
    }

    public void testBuildVitalSign() {
        ClinicalDocumentDocument1 doc = ClinicalDocumentDocument1.Factory.newInstance();
        POCDMT000040Component2 component = doc.addNewClinicalDocument().addNewComponent();
        POCDMT000040StructuredBody body = component.addNewStructuredBody();
        Measurements[] measurements = new Measurements[1];
        measurements[0] = new Measurements(GregorianCalendar.getInstance(), "Temperature", "Head", "Home", "97", "F", null, "fever", null);
        boolean required = true;
        builder.buildVitalSignComponent(body, measurements, required);
        String xml = XmlBeanUtil.toXml(doc);
        System.out.println("XML:");
        System.out.println(xml);
    }

    public void testBuildPhysicalExam() {
        ClinicalDocumentDocument1 doc = ClinicalDocumentDocument1.Factory.newInstance();
        POCDMT000040Component2 component = doc.addNewClinicalDocument().addNewComponent();
        POCDMT000040StructuredBody body = component.addNewStructuredBody();
        String[] exams = new String[2];
        exams[0] = "PhysicalExam 1";
        exams[1] = "PhysicalExam 2";
        boolean required = true;
        builder.buildPhysicalExamComponent(body, exams, required);
        String xml = XmlBeanUtil.toXml(doc);
        System.out.println("XML:");
        System.out.println(xml);
    }

    public void testBuildStudiesSummary() {
        ClinicalDocumentDocument1 doc = ClinicalDocumentDocument1.Factory.newInstance();
        POCDMT000040Component2 component = doc.addNewClinicalDocument().addNewComponent();
        POCDMT000040StructuredBody body = component.addNewStructuredBody();
        Procedure[] procedures = new Procedure[1];
        procedures[0] = new Procedure(GregorianCalendar.getInstance(), "Total hip replacement, left", new Code("52734007", null, "LOINC", null), SharedEnums.StatusCode.COMPLETED, null, null);
        Test test = new Test();
        test.setCodes(new Code[] { new Code("52734007", null, "LOINC", null) });
        test.setName("testName1");
        Order order = new Order();
        order.setStatus("Completed");
        Result result = new Result();
        result.setValue("10");
        result.setUom("cm");
        result.setStatus("completed");
        result.setDate(Calendar.getInstance());
        result.setRange("10-12");
        test.setResult(new Result[] { result });
        test.setOrder(order);
        LabResult[] lResults = new LabResult[1];
        lResults[0] = new LabResult(new Test[] { test }, null);
        boolean required = true;
        builder.buildStudiesSummaryComponent(body, procedures, lResults, required);
        String xml = XmlBeanUtil.toXml(doc);
        System.out.println("XML:");
        System.out.println(xml);
    }

    public void testBuildPlanOfCare() {
        ClinicalDocumentDocument1 doc = ClinicalDocumentDocument1.Factory.newInstance();
        POCDMT000040Component2 component = doc.addNewClinicalDocument().addNewComponent();
        POCDMT000040StructuredBody body = component.addNewStructuredBody();
        String[] cares = new String[2];
        cares[0] = "PlanOfCare 1";
        cares[1] = "PlanOfCare 2";
        boolean required = true;
        builder.buildPlanOfCareComponent(body, cares, required);
        String xml = XmlBeanUtil.toXml(doc);
        System.out.println("XML:");
        System.out.println(xml);
    }

    public void testBuildAdvanceDirective() {
        ClinicalDocumentDocument1 doc = ClinicalDocumentDocument1.Factory.newInstance();
        POCDMT000040Component2 component = doc.addNewClinicalDocument().addNewComponent();
        POCDMT000040StructuredBody body = component.addNewStructuredBody();
        AdvanceDirective[] ads = new AdvanceDirective[1];
        ads[0] = new AdvanceDirective("Do not resuscitate", new Code("304251008", null, "LOINC", null), GregorianCalendar.getInstance(), "completed", "No comment", null);
        boolean required = true;
        builder.buildAdvancedDirectiveComponent(body, ads, required);
        String xml = XmlBeanUtil.toXml(doc);
        System.out.println("XML:");
        System.out.println(xml);
    }

    public void testBuildTransportMode() {
        ClinicalDocumentDocument1 doc = ClinicalDocumentDocument1.Factory.newInstance();
        POCDMT000040Component2 component = doc.addNewClinicalDocument().addNewComponent();
        POCDMT000040StructuredBody body = component.addNewStructuredBody();
        TransportMode mode = new TransportMode();
        mode.setTransportModeCode(SharedEnums.TransportModeCode.GROUND_AMBULANCE);
        mode.setEstimatedTimeOfArrival(GregorianCalendar.getInstance());
        boolean required = true;
        builder.buildModeOfTransport(body, mode, required);
        String xml = XmlBeanUtil.toXml(doc);
        System.out.println("XML:");
        System.out.println(xml);
    }

    public void testBuildProposedEDDisposition() {
        ClinicalDocumentDocument1 doc = ClinicalDocumentDocument1.Factory.newInstance();
        POCDMT000040Component2 component = doc.addNewClinicalDocument().addNewComponent();
        POCDMT000040StructuredBody body = component.addNewStructuredBody();
        ProposedDisposition pd = new ProposedDisposition();
        pd.setEncounterDisposition("This is encounter disposition");
        pd.setEffectiveTime(new EffectiveTime(GregorianCalendar.getInstance(), null));
        pd.setDischargeDispositionCode(SharedEnums.DischargeDispositionCode.DISCHARGE_TO_HOME_OR_SELFCARE);
        boolean required = true;
        builder.buildProposedEDDispositionComponent(body, pd, required);
        String xml = XmlBeanUtil.toXml(doc);
        System.out.println("XML:");
        System.out.println(xml);
    }
}
