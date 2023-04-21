package org.unitmetrics.flavors;

import org.unitmetrics.IFlavor;
import org.unitmetrics.junit.ProjectTestCase;

/**
 * @author Martin Kersten
 */
public class FileFlavorTest extends ProjectTestCase {

    public void testFlavor() {
        IFlavor flavor = new FileFlavor();
        assertFalse("Project should not be of file-flavor", flavor.isOfFlavor(projectUnit));
        assertFalse("Folder should not be of file-flavor", flavor.isOfFlavor(folderUnit));
        assertTrue("File should be of file-flavor", flavor.isOfFlavor(fileUnit));
    }
}
