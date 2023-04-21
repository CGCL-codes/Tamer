package net.sf.ahtutils.controller.factory.java.acl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import net.sf.ahtutils.controller.exception.AhtUtilsConfigurationException;
import net.sf.ahtutils.test.AhtUtilsTstBootstrap;
import net.sf.ahtutils.xml.access.Category;
import net.sf.ahtutils.xml.access.View;
import net.sf.ahtutils.xml.access.Views;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import freemarker.template.TemplateException;

public class TestJavaAclIdentifierFactory extends AbstractJavaAclFactoryTest {

    static final Logger logger = LoggerFactory.getLogger(TestJavaAclIdentifierFactory.class);

    private JavaAclIdentifierFactory idFactory;

    private Category c1;

    private View v1;

    private File fPackage;

    private String classPrefix;

    @Before
    public void init() {
        classPrefix = "Utils";
        fPackage = new File(fTarget, "idFactory");
        fPackage.mkdir();
        idFactory = new JavaAclIdentifierFactory(fPackage, "net.sf.ahtutils", classPrefix);
        v1 = new View();
        v1.setCode("myCode");
        c1 = new Category();
        c1.setCode("xx");
        c1.setViews(new Views());
        c1.getViews().getView().add(v1);
    }

    @After
    public void clean() throws IOException {
        if (fPackage.isDirectory()) {
            FileUtils.deleteDirectory(fPackage);
        } else if (fPackage.isFile()) {
            fPackage.delete();
        }
        idFactory = null;
    }

    @Test(expected = AhtUtilsConfigurationException.class)
    public void noDir() throws AhtUtilsConfigurationException {
        fPackage.delete();
        idFactory.checkBaseDir();
    }

    @Test(expected = AhtUtilsConfigurationException.class)
    public void isFile() throws AhtUtilsConfigurationException, IOException {
        FileUtils.deleteDirectory(fPackage);
        fPackage.createNewFile();
        idFactory.checkBaseDir();
    }

    @Test(expected = AhtUtilsConfigurationException.class)
    public void categoryDirIsFile() throws AhtUtilsConfigurationException, IOException, TemplateException {
        File actual = new File(fPackage, c1.getCode());
        actual.createNewFile();
        idFactory.create(c1);
    }

    @Test
    public void categoryDir() throws AhtUtilsConfigurationException, IOException, TemplateException {
        idFactory.create(c1);
        File actual = new File(fPackage, c1.getCode());
        Assert.assertTrue(actual.exists());
        Assert.assertTrue(actual.isDirectory());
    }

    @Test
    public void createIdentifier() throws AhtUtilsConfigurationException, IOException, TemplateException {
        idFactory.create(c1);
        File fSub = new File(fPackage, c1.getCode());
        for (View v : c1.getViews().getView()) {
            File actual = new File(fSub, idFactory.createFileName(v.getCode()));
            Assert.assertTrue("File should exist: " + actual.getAbsolutePath(), actual.exists());
            Assert.assertTrue(actual.isFile());
            File expected = new File(rootDir, "aclIdentifier-" + v.getCode() + ".java");
            assertText(expected, actual);
        }
    }

    @Test
    public void deleteExisting() throws AhtUtilsConfigurationException, IOException, TemplateException {
        idFactory.create(c1);
        File fSub = new File(fPackage, c1.getCode());
        idFactory.cleanCategoryDir(fSub);
        for (File f : fSub.listFiles()) {
            System.out.println("f: " + f);
            Assert.assertFalse("File exists: " + f.getName(), f.getName().endsWith(".java"));
        }
    }

    @Test
    public void deleteExistingButIgnoreOther() throws AhtUtilsConfigurationException, IOException, TemplateException {
        String testName = "test.txt";
        idFactory.create(c1);
        File fSub = new File(fPackage, c1.getCode());
        File fTest = new File(fSub, testName);
        fTest.createNewFile();
        idFactory.cleanCategoryDir(fSub);
        boolean fTestFound = false;
        for (File f : fSub.listFiles()) {
            if (f.getName().equals(testName)) {
                fTestFound = true;
            }
        }
        Assert.assertTrue(fTestFound);
    }

    @Test
    public void testClassNames() {
        List<String[]> tests = new ArrayList<String[]>();
        tests.add(new String[] { "test", classPrefix + "Test" });
        tests.add(new String[] { "Test", classPrefix + "Test" });
        tests.add(new String[] { "testMe", classPrefix + "TestMe" });
        for (String[] test : tests) {
            Assert.assertEquals(test[1], idFactory.createClassName(test[0]));
        }
    }

    public static void main(String[] args) throws Exception {
        AhtUtilsTstBootstrap.init();
        TestJavaAclIdentifierFactory test = new TestJavaAclIdentifierFactory();
        test.setSaveReference(true);
        test.init();
        test.createIdentifier();
    }
}
