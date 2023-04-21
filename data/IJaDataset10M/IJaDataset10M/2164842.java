package org.kwantu.m2.model;

import java.io.BufferedReader;
import org.kwantu.m2.KwantuContingencyException;
import static org.testng.Assert.*;
import org.testng.annotations.Test;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.kwantu.m2.model.ui.KwantuPanel;
import org.kwantu.m2.model.ui.KwantuField;
import org.kwantu.m2.model.ui.KwantuButton;
import org.kwantu.m2.model.ui.MethodArg;
import org.kwantu.m2.model.ui.KwantuTable;
import org.kwantu.m2.model.ui.KwantuColumn;
import org.kwantu.m2.model.ui.KwantuComponent;
import org.kwantu.m2.model.ui.KwantuUploader;
import org.kwantu.m2.model.ui.KwantuDownloader;
import org.kwantu.m2.model.ui.KwantuText;
import org.kwantu.m2.model.ui.KwantuMenu;
import org.kwantu.m2.model.ui.KwantuLabel;
import org.kwantu.m2.model.ui.GridRowGap;
import org.kwantu.m2.KwantuFaultException;
import java.util.Set;
import java.io.StringWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import org.kwantu.m2.KwantuItemNotFoundException;
import org.kwantu.m2.model.ui.KwantuPanelReference;
import org.kwantu.m2.test.ConfigurationModelModelGenerator;
import org.kwantu.m2.test.M2TestDatabase;

/**
 *   Test basic creation and persistence of Model Data
 */
public class KwantuModelTest {

    private static final Log LOG = LogFactory.getLog(KwantuModelTest.class);

    @Test
    public void kwantuClassCollection() {
        LOG.info(">> KwantuModelTest - Collection");
        KwantuBusinessObjectModel bom = new KwantuBusinessObjectModel();
        assertEquals(bom.getKwantuClasses().size(), 0, "Set of classes should be empty");
        KwantuClass project = bom.createKwantuClass("Project");
        assertEquals(bom.getKwantuClasses().size(), 1, "Set of classes should contain one element");
        try {
            bom.createKwantuClass("Project");
            fail("Create of a class with a duplicate name succeeded.");
        } catch (KwantuFaultException e) {
        }
        Set<KwantuClass> classes = bom.getKwantuClasses();
        assertTrue(classes.contains(project), "Set of classes should contain the Project class.");
        bom.deleteKwantuClass("Project");
        assertEquals(bom.getKwantuClasses().size(), 0, "Set of classes should be empty");
        project = bom.createKwantuClass("Project2");
        KwantuClass deliverable = bom.createKwantuClass("Deliverable");
        assertEquals(bom.getKwantuClasses().size(), 2, "Set of classes should be of size 2.");
        assertEquals(bom.findKwantuClass("Deliverable"), deliverable, "Find KwantuClass failed.");
        assertEquals(bom.findKwantuClass("xyz"), null, "Find of non-existing KwantuClass succeeded.");
        try {
            bom.deleteKwantuClass("Project");
            fail("Delete of non-existing class 'Project' succeeded.");
        } catch (Exception e) {
        }
        LOG.info("<< KwantuModelTest - Collection");
    }

    @Test(groups = { "PersistModel" }, dependsOnGroups = { "SetupPersistModel" })
    public void persistModel() {
        LOG.info(">> KwantuModelTest - PersistModel");
        Session session = SetupPersistModelTest.getSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        KwantuBusinessObjectModel bom = new KwantuBusinessObjectModel();
        KwantuModel model = new KwantuModel(bom);
        KwantuDependency dependency1 = bom.createKwantuDependency("org.apache.maven", "maven-core", "2.0.7");
        KwantuClass project = bom.createKwantuClass("Project");
        KwantuClass deliverable = bom.createKwantuClass("Deliverable");
        KwantuRelationship rel1 = project.createKwantuRelationship("hasDeliverables", deliverable, KwantuRelationship.Cardinality.MANY, "ofProject", true);
        KwantuAttribute nameAttribute = deliverable.createKwantuAttribute("name", KwantuAttribute.Type.STRING);
        KwantuMethod method1 = deliverable.createKwantuMethod("methodSig()", "MethodBody");
        KwantuImport import1 = deliverable.createKwantuImport("org.apache.maven.maven-core");
        KwantuClass report = bom.createKwantuClass("Report");
        KwantuClass schoolReport = bom.createKwantuClass("SchoolReport");
        KwantuClass clinicReport = bom.createKwantuClass("ClinicReport");
        report.updateKwantuSuperClass(deliverable);
        schoolReport.updateKwantuSuperClass(report);
        clinicReport.updateKwantuSuperClass(report);
        LOG.info("Kwantu Class " + bom.getKwantuClass("Deliverable").getName() + ", Id: " + bom.getKwantuClass("Deliverable").getIdentityId());
        session.save(model);
        transaction.commit();
        Query q = session.createQuery("from KwantuModel");
        assertEquals(1, q.list().size());
        KwantuModel model2 = (KwantuModel) q.list().get(0);
        KwantuBusinessObjectModel bom2 = model2.getKwantuBusinessObjectModel();
        assertEquals(bom2, bom, "Saved and retrieved Model are not equal.");
        assertEquals(bom2.getKwantuDependencies().toArray()[0], dependency1, "Dependency not persisted correctly.");
        KwantuClass deliverable2 = null;
        try {
            deliverable2 = bom2.getKwantuClass("Deliverable");
        } catch (KwantuFaultException e) {
            fail("Model not persisted correctly.");
        }
        assertEquals(deliverable2.getKwantuSubclasses().size(), 3, "Saving and Retrieving subclasses failed");
        assertEquals(deliverable2.getKwantuRelationship("ofProject"), rel1.getInverseKwantuRelationship(), "Relationship not persisted correctly.");
        assertEquals(deliverable2.getDeclaredKwantuAttribute("name"), nameAttribute, "Attribute not persisted correctly.");
        assertEquals(deliverable2.getKwantuMethod("methodSig()"), method1, "Method not persisted correctly.");
        assertEquals(deliverable.getKwantuImports().toArray()[0], import1, "Import not persisted correctly.");
        SetupPersistModelTest.clearHibernateData();
        assertEquals(session.createQuery("from KwantuBusinessObjectModel").list().size(), 0, "ClearAllData failed.");
        LOG.info("<< KwantuModelTest - PersistModel");
    }

    @Test
    public void serialize() {
        LOG.info(">> KwantuModelTest - Serialize");
        StringWriter writer = new StringWriter();
        KwantuBusinessObjectModel bom1 = new KwantuBusinessObjectModel();
        KwantuClass class1 = bom1.createKwantuClass("Deliverable");
        KwantuModel model = new KwantuModel("model01", bom1);
        try {
            model.serializeToXML(writer);
        } catch (IOException e) {
            fail("Unable to serialize the model.", e);
        }
        String xml = writer.toString();
        KwantuModel model2 = new KwantuModel();
        try {
            model2.deserializeFromXML(new StringReader(xml));
        } catch (IOException e) {
            fail("Unable to serialize the model.", e);
        }
        KwantuBusinessObjectModel bom2 = model2.composeEffectiveModel().getKwantuBusinessObjectModel();
        assertEquals(class1, bom2.getKwantuClass("Deliverable"), "Deserialized model is not consistent with the original.");
        LOG.info("<< KwantuModelTest - Serialize");
    }

    @Test
    public void importTest() throws KwantuContingencyException {
        String importText = "org.kwantu.m2.model.KwantuModel model = new org.kwantu.m2.model.KwantuModel();\n" + "model.setName(\"TestModel\");\n" + "org.kwantu.m2.model.KwantuBusinessObjectModel bom = new org.kwantu.m2.model.KwantuBusinessObjectModel();\n" + "model.setKwantuBusinessObjectModel(bom);\n" + "bom.setOwningKwantuModel(model);\n" + "bom.addKwantuClassesAsArray(new org.kwantu.m2.model.KwantuClass[] {\n" + "    new org.kwantu.m2.model.KwantuClass(\"Class1\"),\n" + "    new org.kwantu.m2.model.KwantuClass(\"Class2\")\n" + "});\n" + "return model;";
        KwantuModel importedModel = KwantuModel.importKwantuModel(new BufferedReader(new StringReader(importText)), null, null);
        assertNotNull(importedModel);
        assertTrue(importedModel.getKwantuBusinessObjectModel().getKwantuClasses().size() == 2);
        try {
            KwantuModel.importKwantuModel(new BufferedReader(new StringReader("invalid import text")), null, null);
            fail("trying to import invalid text, should have received an exception");
        } catch (KwantuContingencyException ex) {
        }
        try {
            KwantuModel.importKwantuModel(new BufferedReader(new Reader() {

                @Override
                public int read(char[] cbuf, int off, int len) throws IOException {
                    throw new IOException("not supposed to read from here.");
                }

                @Override
                public void close() throws IOException {
                    throw new IOException("not supposed to close this.");
                }
            }), null, null);
            fail("reader should have thrown IOException, which should be mapped " + "to KwantuContingencyException.");
        } catch (KwantuFaultException ex) {
        }
    }

    @Test(groups = { "PersistModel" }, dependsOnGroups = { "SetupPersistModel" })
    public void exportTest() throws IOException, KwantuContingencyException {
        org.kwantu.m2.model.KwantuModel model = new org.kwantu.m2.model.KwantuModel();
        model.setName("TestExportModel");
        org.kwantu.m2.model.KwantuBusinessObjectModel bom = new org.kwantu.m2.model.KwantuBusinessObjectModel();
        model.setKwantuBusinessObjectModel(bom);
        bom.setOwningKwantuModel(model);
        bom.addKwantuClassesAsArray(new org.kwantu.m2.model.KwantuClass[] { new org.kwantu.m2.model.KwantuClass("Organisation2").addAttribute("orgName", org.kwantu.m2.model.KwantuAttribute.Type.STRING, null).addAttribute("backupPath", org.kwantu.m2.model.KwantuAttribute.Type.STRING, "my label").addAttribute("aboutUs", org.kwantu.m2.model.KwantuAttribute.Type.STRING, null).addMethod("public void serializeAll(String path)", "        getController().logInfo(this, \"backupPath is \" + backupPath); \n" + "        getController().logInfo(this, \"serializing to \" + path); \n" + "        getController().serializeAll(path);\n").addMethod("public void deleteTopProject()", "        getController().delete(topProject);\n").addValidation("orgName != null", "Org Name may not be null"), new org.kwantu.m2.model.KwantuClass("Project2").addAttribute("name", org.kwantu.m2.model.KwantuAttribute.Type.STRING, null).addAttribute("backupPath", org.kwantu.m2.model.KwantuAttribute.Type.STRING, null).addMethod("public void setNameFoo()", "        name = \"Foo\";\n").addMethod("public void setNameBar()", "        name = \"Bar\";\n"), new org.kwantu.m2.model.KwantuClass("LongProject2") });
        bom.addSuperclass("LongProject2", "Project2");
        bom.addKwantuRelationship("Organisation2", "projects", "Project2", "MANY", "organisation", "Owned");
        bom.addKwantuRelationship("Organisation2", "topProject", "Project2", "ONE", "topProjectOfOrganisation", "");
        bom.addKwantuRelationship("Organisation2", "interestingProjects", "Project2", "MANY", "organisationIfInterestingProject", "");
        bom.createKwantuDependency("org.apache.maven", "maven-core", "2.0.7");
        model.createUiPage("Relative Project2 Page", "Project2", ".", new KwantuComponent[] { new KwantuField("name") });
        model.createUiPage("Page01", "Organisation", "/Organisation", new KwantuComponent[] { new KwantuLabel("Organisation").fontSizePercent(200).fontWeight(700).paddingTopPixels(10).alignRight(true).columnSpan(1), new KwantuMenu(new KwantuComponent[] { new KwantuLabel("Menu"), new KwantuMenu(new KwantuComponent[] { new KwantuLabel("in Submenu"), new KwantuButton(false, "Delete Top Project", null, null, "deleteTopProject", null), new KwantuButton(false, "Create Project", "Project01", "/ReturnValue/Project", "createProject", null) }) }), new GridRowGap(10), new KwantuPanel(".", 3, new KwantuComponent[] { new KwantuField("orgName", true, 20), new KwantuField("backupPath", true, 30).columnSpan(2), new KwantuButton(true, "Apply", null, null, null, null), new KwantuButton(true, "Generate Data", null, null, "setup", null), new KwantuButton(true, "Serialize Data", null, null, "serializeAll", new MethodArg[] { new MethodArg("Backup Path", "backupPath") }), new KwantuField("topProject/name") }), new KwantuTable("projects", new KwantuColumn[] { new KwantuColumn(".", "name", new KwantuComponent[] { new KwantuField("name") }), new KwantuColumn(".", "action", new KwantuComponent[] { new KwantuButton(false, "Edit", "Project01", ".", null, null) }) }), new KwantuPanel(".", 1, new KwantuComponent[] { new KwantuButton(false, "Delete Top Project", null, null, "deleteTopProject", null), new KwantuButton(false, "Create Project", "Project01", "/ReturnValue/Project", "createProject", null) }), new KwantuUploader("Upload file", "upload"), new KwantuDownloader("Download file", "download"), new KwantuText("aboutUs").setHeightAndWidth(10, 60), new KwantuPanelReference("Relative Project2 Page", false) }).backgroundColor(0xFFFF99);
        model.createUiPage("Page02", "Organisation", "/Organisation", null);
        model.createUiPage("Page03", "Organisation", "/Organisation", new KwantuComponent[] { new KwantuTable("projects", new KwantuColumn[] { new KwantuColumn(".", "name", new KwantuComponent[] { new KwantuField("name") }), new KwantuColumn(".", "action", new KwantuComponent[] {}) }), new KwantuTable("projects2", new KwantuColumn[] {}), new KwantuPanel(".", 1, new KwantuComponent[] {}) });
        File tempFile = File.createTempFile("exportTestA", ".java");
        FileWriter fileWriter = new FileWriter(tempFile);
        ModelExporter.export(fileWriter, model);
        fileWriter.close();
        StringWriter writer = new StringWriter();
        ModelExporter.export(writer, model);
        writer.flush();
        KwantuModel importedModel = KwantuModel.importKwantuModel(new BufferedReader(new StringReader(writer.toString())), null, null);
        assertNotNull(importedModel);
        KwantuBusinessObjectModel newBom = importedModel.getKwantuBusinessObjectModel();
        assertTrue(newBom.getKwantuClasses().size() == 3);
        assertTrue(newBom.findKwantuClass("Organisation2").getKwantuRelationships().size() == 3);
        assertTrue(newBom.findKwantuClass("LongProject2").getKwantuSuperClass().equals(newBom.findKwantuClass("Project2")));
        assertTrue(newBom.findKwantuClass("Organisation2").findKwantuMethodByName("serializeAll") != null);
        assertTrue(newBom.getKwantuDependencies().size() == 1);
        StringWriter writer2 = new StringWriter();
        ModelExporter.export(writer2, importedModel);
        writer2.close();
        File tempFile2 = File.createTempFile("exportTestB", ".java");
        FileWriter fileWriter2 = new FileWriter(tempFile2);
        ModelExporter.export(fileWriter2, importedModel);
        fileWriter2.close();
        assertEquals(writer2.getBuffer().toString(), writer.getBuffer().toString());
        Session session = SetupPersistModelTest.getSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        session.save(importedModel);
        transaction.commit();
        session.beginTransaction();
        Query query = session.createQuery("from KwantuModel where name = :name");
        query.setString("name", importedModel.getName());
        KwantuModel queriedModel = (KwantuModel) query.uniqueResult();
        StringWriter writer3 = new StringWriter();
        ModelExporter.export(writer3, queriedModel);
        writer3.close();
        assertNotSame(queriedModel, model);
        assertEquals(writer3.getBuffer().toString(), writer.getBuffer().toString());
        session.delete(queriedModel);
        session.getTransaction().commit();
    }

    @Test
    public void exportWithKwantuDependencies() throws IOException, KwantuContingencyException {
        M2TestDatabase m2db = M2TestDatabase.acquire();
        Session session = m2db.openSession();
        final KwantuModel modelA = new KwantuModel("A");
        KwantuModel modelB = new KwantuModel("B").addKwantuModelDependencies(modelA);
        assertEquals(modelA.getDependencies().size(), 0);
        assertEquals(modelB.getDependencies().size(), 1);
        assertEquals(modelA.getDependents().size(), 1);
        assertEquals(modelB.getDependents().size(), 0);
        StringWriter writer = new StringWriter();
        ModelExporter.export(writer, modelB);
        assertEquals(modelA.getDependencies().size(), 0);
        assertEquals(modelB.getDependents().size(), 0);
        ArrayList<KwantuModel> importedDependencies = new ArrayList<KwantuModel>();
        KwantuModel importedModelB = KwantuModel.importKwantuModel(new BufferedReader(new StringReader(writer.toString())), new KwantuModelResolver() {

            @Override
            public KwantuModel resolve(String name) throws KwantuItemNotFoundException {
                if (name.equals("A")) {
                    return modelA;
                }
                throw new KwantuItemNotFoundException("not 'A'???");
            }
        }, importedDependencies);
        assertEquals(importedDependencies.size(), 1);
        m2db.release();
    }

    @Test
    public void exportExampleModels() throws IOException {
        ModelExporter.export(new File("./target/ConfigurationModel1.model"), ConfigurationModelModelGenerator.createModel());
    }
}
