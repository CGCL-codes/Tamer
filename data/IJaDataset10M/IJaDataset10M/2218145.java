package cz.kibo.photoGalleryCreator.workers.tests;

import junit.framework.TestCase;
import cz.kibo.photoGalleryCreator.workers.images.CheckImageName;

public class CheckImageNameTest extends TestCase {

    String INPUT_NAME = "žlutý";

    String EXPECTED_NAME = "XlutX";

    public void testCheckImageName() {
        String testName = new CheckImageName().checkName(INPUT_NAME);
        assertEquals(EXPECTED_NAME, testName);
    }
}
