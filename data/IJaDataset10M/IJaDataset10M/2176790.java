package com.nokia.ats4.appmodel.util;

import com.nokia.ats4.appmodel.util.Settings;
import com.nokia.ats4.appmodel.util.image.ImageData;
import com.nokia.ats4.appmodel.util.image.ImageGallery;
import java.io.File;
import javax.swing.ImageIcon;
import junit.framework.*;

/**
 * ImageDataTest
 *
 * @author Kimmo Tuukkanen
 * @version $Revision: 2 $
 */
public class ImageDataTest extends TestCase {

    public ImageDataTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        Settings.setProperty("language.variant", "English");
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of getBytes method, of class com.nokia.kendo.util.ImageData.
     */
    public void testGetImageIcon() {
        System.out.println("getImageIcon");
        ImageData instance;
        String variant = Settings.getProperty("language.variant");
        ImageGallery ig = new ImageGallery(new File("test/temp/ImageDataTest/" + System.currentTimeMillis()));
        File imageFile = new File("test/data/screenshot.png");
        assertTrue(imageFile.exists());
        File imported = ig.importImage(imageFile);
        instance = new ImageData(ig);
        instance.setImageFilename(variant, imported.getName());
        ImageIcon image = instance.getImageIcon();
        assertNotNull(image);
    }
}
