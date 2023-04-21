package org.netbeans.module.flexbean.modules.as;

import junit.framework.TestCase;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.Repository;
import org.openide.loaders.DataObject;

public class ActionScriptDataObjectTest extends TestCase {

    public ActionScriptDataObjectTest(String testName) {
        super(testName);
    }

    public void testDataObject() throws Exception {
        FileObject root = Repository.getDefault().getDefaultFileSystem().getRoot();
        FileObject template = root.getFileObject("Templates/Other/ActionScriptTemplate.as");
        assertNotNull("Template file shall be found", template);
        DataObject obj = DataObject.find(template);
        assertEquals("It is our data object", ActionScriptDataObject.class, obj.getClass());
    }
}
