package com.luxoft.fitpro.core.codegenerator.importer;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;
import com.luxoft.fitpro.codegenerator.importer.FitImporter;
import com.luxoft.fitpro.codegenerator.model.ClassModel;
import com.luxoft.fitpro.codegenerator.model.ClassModelRegistry;
import com.luxoft.fitpro.codegenerator.model.ClassOperation;
import com.luxoft.fitpro.core.parser.ParseException;

public class FitImporterTest {

    @Test
    public void testReadFile() throws IOException {
        FitImporter importer = new FitImporter("unitsrc/unitTestFiles/columnFixture.fit", new ClassModelRegistry());
        Assert.assertNotNull(importer.getFitFileContent());
        Assert.assertFalse(importer.getFitFileContent().equals(""));
    }

    @Test
    public void testGetClasses() {
        FitImporter importer = new FitImporter("unitsrc/unitTestFiles/columnFixture.fit", new ClassModelRegistry());
        try {
            importer.createClasses();
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("Could not read the file");
        }
    }

    @Test
    public void testGetClassStereoTypeForActionFixture() {
        Iterator<ClassModel> iter = getClasses("unitsrc/unitTestFiles/actionFixture.fit");
        while (iter.hasNext()) {
            ClassModel fixtureClass = (ClassModel) iter.next();
            Assert.assertEquals("ActionFixture", fixtureClass.getStereotype());
        }
    }

    @Test
    public void testGetClassStereoTypeForColumnFixture() {
        Iterator<ClassModel> iter = getClasses("unitsrc/unitTestFiles/columnFixture.fit");
        while (iter.hasNext()) {
            ClassModel fixtureClass = (ClassModel) iter.next();
            Assert.assertEquals("ColumnFixture", fixtureClass.getStereotype());
        }
    }

    @Test
    public void testGetClassStereoTypeForRowFixture() {
        Iterator<ClassModel> iter = getClasses("unitsrc/unitTestFiles/rowFixture.fit");
        while (iter.hasNext()) {
            ClassModel fixtureClass = (ClassModel) iter.next();
            Assert.assertEquals("RowFixture", fixtureClass.getStereotype());
        }
    }

    @Test
    public void testGetClassName() {
        Iterator<ClassModel> iter = getClasses("unitsrc/unitTestFiles/columnFixture.fit");
        while (iter.hasNext()) {
            ClassModel fixtureClass = (ClassModel) iter.next();
            Assert.assertEquals("ColumnFixtureAlot", fixtureClass.getName());
        }
    }

    @Test
    public void testGetMethodsForColumnFixture() {
        Iterator<ClassModel> iter = getClasses("unitsrc/unitTestFiles/columnFixture.fit");
        while (iter.hasNext()) {
            ClassModel fixtureClass = (ClassModel) iter.next();
            Assert.assertEquals(2, fixtureClass.getOperations().size());
        }
    }

    @Test
    public void testGetMethodsForRowFixture() {
        Iterator<ClassModel> iter = getClasses("unitsrc/unitTestFiles/rowFixture.fit");
        while (iter.hasNext()) {
            ClassModel fixtureClass = (ClassModel) iter.next();
            Assert.assertEquals(2, fixtureClass.getOperations().size());
        }
    }

    @Test
    public void testGetMethodsForActionFixture() {
        Iterator<ClassModel> iter = getClasses("unitsrc/unitTestFiles/actionFixture.fit");
        while (iter.hasNext()) {
            ClassModel fixtureClass = (ClassModel) iter.next();
            Assert.assertEquals(9, fixtureClass.getOperations().size());
        }
    }

    @Test
    public void testGetAttribsForColumnFixture() {
        Iterator<ClassModel> iter = getClasses("unitsrc/unitTestFiles/columnFixture.fit");
        while (iter.hasNext()) {
            ClassModel fixtureClass = (ClassModel) iter.next();
            Assert.assertEquals(3, fixtureClass.getAttributes().size());
        }
    }

    @Test
    public void testGetAttribsForRowFixture() {
        Iterator<ClassModel> iter = getClasses("unitsrc/unitTestFiles/rowFixture.fit");
        while (iter.hasNext()) {
            ClassModel fixtureClass = (ClassModel) iter.next();
            Assert.assertEquals(2, fixtureClass.getAttributes().size());
        }
    }

    @Test
    public void testGetAttribsForActionFixture() {
        Iterator<ClassModel> iter = getClasses("unitsrc/unitTestFiles/actionFixture.fit");
        while (iter.hasNext()) {
            ClassModel fixtureClass = (ClassModel) iter.next();
            Assert.assertEquals(0, fixtureClass.getAttributes().size());
        }
    }

    @Test
    public void testGetCorrectMethodName() {
        Iterator<ClassModel> iter = getClasses("unitsrc/unitTestFiles/actionFixture.fit");
        while (iter.hasNext()) {
            ClassModel fixtureClass = (ClassModel) iter.next();
            Iterator<ClassOperation> operations = fixtureClass.getOperations().iterator();
            while (operations.hasNext()) {
                String methodName = operations.next().getName();
                if (methodName.equals("newCity")) {
                    return;
                }
            }
        }
        Assert.fail("The method newCity should exist.");
    }

    @Test
    public void testGetCorrectClassName() {
        Iterator<ClassModel> iter = getClasses("unitsrc/unitTestFiles/actionFixture.fit");
        while (iter.hasNext()) {
            ClassModel fixtureClass = (ClassModel) iter.next();
            Assert.assertEquals("Simulator", fixtureClass.getName());
        }
    }

    @Test
    public void testGetMethodsForFixtureWithComments() {
        Iterator<ClassModel> iter = getClasses("unitsrc/unitTestFiles/columnFixtureWithComments.fit");
        while (iter.hasNext()) {
            ClassModel fixtureClass = (ClassModel) iter.next();
            Assert.assertEquals(2, fixtureClass.getOperations().size());
        }
    }

    @Test
    public void testGetAttribsForFixtureWithComments() {
        Iterator<ClassModel> iter = getClasses("unitsrc/unitTestFiles/columnFixtureWithComments.fit");
        while (iter.hasNext()) {
            ClassModel fixtureClass = (ClassModel) iter.next();
            Assert.assertEquals(3, fixtureClass.getAttributes().size());
        }
    }

    private Iterator<ClassModel> getClasses(String fitFileName) {
        FitImporter importer = new FitImporter(fitFileName, new ClassModelRegistry());
        Set<ClassModel> classes = null;
        try {
            classes = importer.createClasses();
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("Could not read the file");
        }
        Assert.assertEquals(1, classes.size());
        return classes.iterator();
    }
}
