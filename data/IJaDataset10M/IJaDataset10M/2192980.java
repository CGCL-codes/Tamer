package com.foursoft.fourever.xmlfileio;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.custommonkey.xmlunit.XMLTestCase;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import com.foursoft.component.util.FileUtilities;
import com.foursoft.fourever.objectmodel.ComplexInstance;
import com.foursoft.fourever.objectmodel.Instance;
import com.foursoft.fourever.objectmodel.Link;
import com.foursoft.fourever.objectmodel.ObjectModel;
import com.foursoft.fourever.objectmodel.SimpleInstance;

/**
 * @version $Revision: 1.9 $
 */
@SuppressWarnings("all")
public class ReadSaveTest extends XMLTestCase {

    private ClassPathXmlApplicationContext ctx;

    private XMLFileIOManager fiom;

    /**
	 * @see junit.framework.TestCase#setUp()
	 */
    @Override
    protected void setUp() throws Exception {
        ctx = new ClassPathXmlApplicationContext("xmlfileio/beans.xml");
        fiom = (XMLFileIOManager) ctx.getBean("xmlfileiomanager");
    }

    /**
	 * @see junit.framework.TestCase#tearDown()
	 */
    @Override
    protected void tearDown() throws Exception {
        ctx.close();
    }

    private File copyAllFiles(File directory) {
        assertTrue(directory.isDirectory());
        File tmpDir = new File(System.getProperty("java.io.tmpdir"), "4EverTest");
        if (tmpDir.exists()) {
            FileUtilities.deleteFileRecursively(tmpDir);
        }
        tmpDir.mkdirs();
        tmpDir.deleteOnExit();
        try {
            FileUtilities.copyDirectoryRecursively(directory, tmpDir, false);
        } catch (IOException ex) {
            fail("Could not create temporary copy of test data.");
        }
        return tmpDir;
    }

    /**
	 * test loading a very simple XML file (with schema) and test the expected
	 * results
	 * 
	 * See hund.xsd and bello.xml for the files
	 * 
	 * Expected model: Types: Instances:
	 * 
	 * * (root) * (root) | | [ComplexBinding] name="hund" [Link] | |
	 * (ComplexType) name="hund" (ComplexInstance) | | [SimpleBinding]
	 * name="name" [Link] | | (StringType) (StringInstance) value="Bello"
	 * 
	 * @throws Exception
	 *             if test fails
	 */
    public void testReadSaveSimpleDocument() throws Exception {
        Document d;
        ObjectModel om;
        ClassPathResource testdir = new ClassPathResource("xmlfileio/save");
        File tmpDir = copyAllFiles(testdir.getFile());
        File original = new File(tmpDir, "bello.xml");
        long originalTimeStamp = original.lastModified();
        d = fiom.openDocument(original);
        om = d.getObjectModel();
        Iterator fragments = d.getFragments();
        Fragment f = null;
        if (fragments.hasNext()) {
            f = (Fragment) fragments.next();
        }
        assertNotNull(f);
        Link rootLink = f.getRootLink();
        rootLink = om.getRootLinks().next();
        Instance hundInstance = rootLink.getTarget(0);
        assertTrue(hundInstance instanceof ComplexInstance);
        Link nameLink = ((ComplexInstance) hundInstance).getOutgoingLink("name");
        SimpleInstance nameInstance = (SimpleInstance) nameLink.getTarget(0);
        String oldName = nameInstance.getValueAsString();
        assertNotNull(oldName);
        nameInstance.setValueFromString("jess");
        nameInstance.setValueFromString(oldName);
        f.save();
        assertTrue(original.exists());
        assertTrue(originalTimeStamp < original.lastModified());
    }

    /**
	 * @throws Exception
	 *             if test fails
	 */
    public void testReadComplexReferenceDocument() throws Exception {
        Document d;
        ObjectModel om;
        ClassPathResource testdata = new ClassPathResource("xmlfileio/references/foursoft.xml");
        File original = testdata.getFile();
        File copy = new File(original.getAbsoluteFile().getParentFile(), "copy_" + original.getName());
        if (copy.exists()) {
            copy.delete();
        }
        assertTrue(!copy.exists());
        FileUtilities.copyBinaryFile(original, copy);
        d = fiom.openDocument(copy);
        om = d.getObjectModel();
        Iterator fragments = d.getFragments();
        Fragment f = null;
        while (fragments.hasNext()) {
            f = (Fragment) fragments.next();
            break;
        }
        Link companyL = om.getRootLinks().next();
        assertTrue(companyL.getBinding().getBindingName().equals("company"));
        ComplexInstance companyObj = (ComplexInstance) companyL.getTarget(0);
        Iterator companyLinks = companyObj.getOutgoingLinks();
        Link departmentManagement = null;
        Link departmentTI = null;
        String departmentManagementName = "Management";
        String departmentTIName = "Technical Infrastructure";
        Link departmentManagementStaffLink = null;
        Link departmentTIStaffLink = null;
        while (companyLinks.hasNext()) {
            Link link = (Link) companyLinks.next();
            if (link.getBinding().getBindingName().equals("department")) {
                Iterator departments = link.getTargets();
                while (departments.hasNext()) {
                    ComplexInstance departmentObject = (ComplexInstance) departments.next();
                    assertNotNull(departmentObject);
                    Link nameLink = departmentObject.getOutgoingLink("name");
                    Link refLink = departmentObject.getOutgoingLink("staffRef");
                    String departmentName = ((SimpleInstance) nameLink.getTarget(0)).getValueAsString();
                    assertNotNull(nameLink);
                    assertNotNull(refLink);
                    if (departmentName.equals(departmentManagementName)) {
                        departmentManagement = link;
                        departmentManagementStaffLink = refLink;
                    } else if (departmentName.equals(departmentTIName)) {
                        departmentTI = link;
                        departmentTIStaffLink = refLink;
                    }
                }
            }
        }
        assertNotNull(departmentManagement);
        assertNotNull(departmentTI);
        assertNotNull(departmentManagementStaffLink);
        assertNotNull(departmentTIStaffLink);
        assertEquals(departmentTIStaffLink.getNumberOfTargets(), 1);
        assertEquals(departmentManagementStaffLink.getNumberOfTargets(), 2);
        boolean tobiFound = false;
        boolean marcFound = false;
        boolean klausFound = false;
        Link tiNameLink = ((ComplexInstance) departmentTIStaffLink.getTarget(0)).getOutgoingLink("name");
        assertNotNull(tiNameLink);
        Instance tiPersonName = tiNameLink.getTarget(0);
        assertNotNull(tiPersonName);
        if (((SimpleInstance) tiPersonName).getValueAsString().equals("Tobi")) {
            tobiFound = true;
        }
        ((SimpleInstance) tiPersonName).setValueFromString("Roland");
        Iterator managementPeople = departmentManagementStaffLink.getTargets();
        while (managementPeople.hasNext()) {
            Instance managementPerson = (Instance) managementPeople.next();
            assertNotNull(managementPerson);
            Instance managementPersonName = ((ComplexInstance) managementPerson).getOutgoingLink("name").getTarget(0);
            assertNotNull(managementPersonName);
            if (((SimpleInstance) managementPersonName).getValueAsString().equals("Marc")) {
                marcFound = true;
            } else if (((SimpleInstance) managementPersonName).getValueAsString().equals("Klaus")) {
                klausFound = true;
            }
        }
        assertTrue(tobiFound);
        assertTrue(marcFound);
        assertTrue(klausFound);
        f.save();
        assertTrue(copy.exists());
        assertTrue(copy.length() > 0);
        assertTrue(original.length() > 0);
        copy.delete();
        assertTrue(!copy.exists());
    }

    /**
	 * Test Multifile References
	 * 
	 * @throws Exception
	 */
    public void testReadMultiFileComplexReferenceDocument() throws Exception {
        Document d;
        ObjectModel om;
        ClassPathResource testdir = new ClassPathResource("xmlfileio/multifile");
        File tmpDir = copyAllFiles(testdir.getFile());
        File testFile = new File(tmpDir, "foursoft.xml");
        assertTrue(testFile.exists());
        d = fiom.openDocument(testFile);
        om = d.getObjectModel();
        Iterator roots = om.getRootLinks();
        Link companyL = null;
        while (roots.hasNext()) {
            Link rootLink = (Link) roots.next();
            if (rootLink.getBinding().getBindingName().equals("company")) {
                companyL = rootLink;
            }
        }
        assertNotNull(companyL);
        ComplexInstance companyObj = (ComplexInstance) companyL.getTarget(0);
        Iterator companyLinks = companyObj.getOutgoingLinks();
        Link departmentManagement = null;
        Link departmentTI = null;
        String departmentManagementName = "Management";
        String departmentTIName = "Technical Infrastructure";
        Link departmentManagementStaffLink = null;
        Link departmentTIStaffLink = null;
        while (companyLinks.hasNext()) {
            Link link = (Link) companyLinks.next();
            if (link.getBinding().getBindingName().equals("department")) {
                Iterator departments = link.getTargets();
                while (departments.hasNext()) {
                    ComplexInstance departmentObject = (ComplexInstance) departments.next();
                    assertNotNull(departmentObject);
                    Link nameLink = departmentObject.getOutgoingLink("name");
                    Link refLink = departmentObject.getOutgoingLink("staffRef");
                    String departmentName = ((SimpleInstance) nameLink.getTarget(0)).getValueAsString();
                    assertNotNull(nameLink);
                    assertNotNull(refLink);
                    if (departmentName.equals(departmentManagementName)) {
                        departmentManagement = link;
                        departmentManagementStaffLink = refLink;
                    } else if (departmentName.equals(departmentTIName)) {
                        departmentTI = link;
                        departmentTIStaffLink = refLink;
                    }
                }
            }
        }
        assertNotNull(departmentManagement);
        assertNotNull(departmentTI);
        assertNotNull(departmentManagementStaffLink);
        assertNotNull(departmentTIStaffLink);
        assertEquals(departmentTIStaffLink.getNumberOfTargets(), 1);
        assertEquals(departmentManagementStaffLink.getNumberOfTargets(), 2);
        boolean tobiFound = false;
        boolean marcFound = false;
        boolean klausFound = false;
        Link tiNameLink = ((ComplexInstance) departmentTIStaffLink.getTarget(0)).getOutgoingLink("name");
        assertNotNull(tiNameLink);
        Instance tiPersonName = tiNameLink.getTarget(0);
        assertNotNull(tiPersonName);
        if (((SimpleInstance) tiPersonName).getValueAsString().equals("Tobi")) {
            tobiFound = true;
        }
        ((SimpleInstance) tiPersonName).setValueFromString("Roland");
        ((SimpleInstance) tiPersonName).setValueFromString("Tobi");
        Iterator managementPeople = departmentManagementStaffLink.getTargets();
        while (managementPeople.hasNext()) {
            Instance managementPerson = (Instance) managementPeople.next();
            assertNotNull(managementPerson);
            Instance managementPersonName = ((ComplexInstance) managementPerson).getOutgoingLink("name").getTarget(0);
            assertNotNull(managementPersonName);
            if (((SimpleInstance) managementPersonName).getValueAsString().equals("Marc")) {
                marcFound = true;
            } else if (((SimpleInstance) managementPersonName).getValueAsString().equals("Klaus")) {
                klausFound = true;
            }
        }
        assertTrue(tobiFound);
        assertTrue(marcFound);
        assertTrue(klausFound);
        Fragment companyFrag = d.getFragment(companyObj);
        assertNotNull(companyFrag);
        Fragment staffFragment = d.getFragment(departmentTIStaffLink.getTarget(0));
        assertNotNull(staffFragment);
        assertTrue(staffFragment != companyFrag);
        assertEquals(false, companyFrag.isDirty());
        assertTrue(staffFragment.isDirty());
        d.saveFragments();
        assertTrue(!companyFrag.isDirty());
        assertTrue(!staffFragment.isDirty());
    }
}
