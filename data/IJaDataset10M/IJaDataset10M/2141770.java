package org.photovault.imginfo.indexer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.photovault.common.PVDatabase;
import org.photovault.common.PhotovaultException;
import org.photovault.common.PhotovaultSettings;
import org.photovault.dbhelper.ODMG;
import org.photovault.folder.PhotoFolder;
import org.photovault.imginfo.ExternalVolume;
import org.photovault.imginfo.FileUtils;
import org.photovault.imginfo.ImageInstance;
import org.photovault.imginfo.PhotoInfo;
import org.photovault.test.PhotovaultTestCase;

/**
 *
 * @author harri
 */
public class Test_ExtVolIndexer extends PhotovaultTestCase {

    /** Creates a new instance of Test_ExtVolIndexer */
    public Test_ExtVolIndexer() {
    }

    /**
     Sets up the directory hiearchy used in test cases. Also deletes all th photos
     used in test from database so that hash lookups give expected results
     */
    public void setUp() {
        File testfile1 = null;
        File testfile2 = null;
        File testfile3 = null;
        try {
            extVolDir = File.createTempFile("pv_indexer_test_", "");
            extVolDir.delete();
            extVolDir.mkdir();
            File extVolSubdir = new File(extVolDir, "test");
            extVolSubdir.mkdir();
            testfile1 = new File("testfiles", "test1.jpg");
            testfile2 = new File("testfiles", "test2.jpg");
            testfile3 = new File("testfiles", "test3.jpg");
            photo1 = new File(extVolDir, "test1.jpg");
            FileUtils.copyFile(testfile1, photo1);
            photo2inst1 = new File(extVolDir, "test2.jpg");
            FileUtils.copyFile(testfile2, photo2inst1);
            photo2inst2 = new File(extVolSubdir, "test2.jpg");
            FileUtils.copyFile(testfile2, photo2inst2);
            File txtFile = new File(extVolDir, "test.txt");
            FileWriter writer = new FileWriter(txtFile);
            writer.write("Not an image");
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        topFolder = PhotoFolder.create("ExtVolTest", PhotoFolder.getRoot());
        hash1 = ImageInstance.calcHash(testfile1);
        PhotoInfo photos1[] = PhotoInfo.retrieveByOrigHash(hash1);
        if (photos1 != null) {
            for (int n = 0; n < photos1.length; n++) {
                photos1[n].delete();
            }
        }
        hash2 = ImageInstance.calcHash(testfile2);
        PhotoInfo photos2[] = PhotoInfo.retrieveByOrigHash(hash2);
        if (photos2 != null) {
            for (int n = 0; n < photos2.length; n++) {
                photos2[n].delete();
            }
        }
        hash3 = ImageInstance.calcHash(testfile3);
        PhotoInfo photos3[] = PhotoInfo.retrieveByOrigHash(hash3);
        if (photos3 != null) {
            for (int n = 0; n < photos3.length; n++) {
                photos3[n].delete();
            }
        }
    }

    public void tearDown() {
        FileUtils.deleteTree(extVolDir);
        topFolder.delete();
        PhotoInfo photos1[] = PhotoInfo.retrieveByOrigHash(hash1);
        if (photos1 != null) {
            for (int n = 0; n < photos1.length; n++) {
                photos1[n].delete();
            }
        }
        PhotoInfo photos2[] = PhotoInfo.retrieveByOrigHash(hash2);
        if (photos2 != null) {
            for (int n = 0; n < photos2.length; n++) {
                photos2[n].delete();
            }
        }
        PhotoInfo photos3[] = PhotoInfo.retrieveByOrigHash(hash3);
        if (photos3 != null) {
            for (int n = 0; n < photos3.length; n++) {
                photos3[n].delete();
            }
        }
    }

    File extVolDir = null;

    byte[] hash1;

    byte[] hash2;

    byte[] hash3;

    File photo1 = null;

    File photo2inst1 = null;

    File photo2inst2 = null;

    private PhotoFolder topFolder = null;

    private class TestListener implements ExtVolIndexerListener {

        public int instanceCount = 0;

        public int photoCount = 0;

        boolean complete = false;

        public void fileIndexed(ExtVolIndexerEvent e) {
            switch(e.getResult()) {
                case ExtVolIndexerEvent.RESULT_NEW_PHOTO:
                    instanceCount++;
                    photoCount++;
                    break;
                case ExtVolIndexerEvent.RESULT_NEW_INSTANCE:
                    instanceCount++;
                    break;
            }
        }

        public void indexingComplete(ExtVolIndexer indexer) {
            complete = true;
        }

        public void indexingError(String message) {
        }
    }

    public void testIndexing() {
        int n;
        ExternalVolume v = new ExternalVolume("extVol", extVolDir.getAbsolutePath());
        PhotovaultSettings settings = PhotovaultSettings.getSettings();
        PVDatabase db = settings.getDatabase("pv_junit");
        try {
            db.addVolume(v);
        } catch (PhotovaultException ex) {
            fail(ex.getMessage());
        }
        ExtVolIndexer indexer = new ExtVolIndexer(v);
        indexer.setTopFolder(topFolder);
        TestListener l = new TestListener();
        indexer.addIndexerListener(l);
        assertEquals("Indexing not started -> completeness must be 0", 0, indexer.getPercentComplete());
        assertNull("StartTime must be null before starting", indexer.getStartTime());
        indexer.run();
        if (ODMG.getODMGImplementation().currentTransaction() != null) {
            fail("Still in transaction");
        }
        PhotoInfo[] photos1 = PhotoInfo.retrieveByOrigHash(hash1);
        if (ODMG.getODMGImplementation().currentTransaction() != null) {
            fail("Still in transaction");
        }
        assertNotNull("photos1 = null", photos1);
        assertEquals("Only 1 photo per picture should be found", 1, photos1.length);
        PhotoInfo p1 = photos1[0];
        assertEquals("2 instances should be found in photo 1", 2, p1.getNumInstances());
        PhotoInfo[] photos2 = PhotoInfo.retrieveByOrigHash(hash2);
        if (ODMG.getODMGImplementation().currentTransaction() != null) {
            fail("Still in transaction");
        }
        assertEquals("1 photo per picture should be found", 1, photos2.length);
        PhotoInfo p2 = photos2[0];
        assertEquals("3 instances should be found in photo 2", 3, p2.getNumInstances());
        boolean found[] = { false, false };
        File files[] = { photo2inst1, photo2inst2 };
        for (n = 0; n < p2.getNumInstances(); n++) {
            ImageInstance i = p2.getInstance(n);
            for (int m = 0; m < found.length; m++) {
                if (files[m].equals(i.getImageFile())) {
                    found[m] = true;
                }
            }
        }
        for (n = 0; n < found.length; n++) {
            assertTrue("Photo " + n + " not found", found[n]);
        }
        if (ODMG.getODMGImplementation().currentTransaction() != null) {
            fail("Still in transaction");
        }
        PhotoInfo[] photosInTopFolder = { p1, p2 };
        assertFolderHasPhotos(topFolder, photosInTopFolder);
        PhotoFolder subFolder = topFolder.getSubfolder(0);
        assertEquals("Subfolder name not correct", "test", subFolder.getName());
        PhotoInfo[] photosInSubFolder = { p2 };
        assertFolderHasPhotos(subFolder, photosInSubFolder);
        assertEquals("Wrong photo count in listener", 2, l.photoCount);
        assertEquals("Wrong photo count in indexer statistics", 2, indexer.getNewPhotoCount());
        assertEquals("Wrong instance count in listener", 3, l.instanceCount);
        assertEquals("Wrong instance count in indexer statistics", 3, indexer.getNewInstanceCount());
        assertEquals("Indexing complete 100%", 100, indexer.getPercentComplete());
        assertNotNull("StartTime still null", indexer.getStartTime());
        if (ODMG.getODMGImplementation().currentTransaction() != null) {
            fail("Still in transaction");
        }
        try {
            File testfile3 = new File("testfiles", "test3.jpg");
            File f3 = new File(extVolDir, "test3.jpg");
            FileUtils.copyFile(testfile3, f3);
            File f1 = new File(extVolDir, "test1.jpg");
            FileUtils.copyFile(testfile3, f1);
            File f2 = new File(extVolDir, "test2.jpg");
            f2.delete();
        } catch (IOException ex) {
            fail("IOException while altering external volume: " + ex.getMessage());
        }
        indexer = new ExtVolIndexer(v);
        indexer.setTopFolder(topFolder);
        l = new TestListener();
        indexer.addIndexerListener(l);
        assertEquals("Indexing not started -> completeness must be 0", 0, indexer.getPercentComplete());
        assertNull("StartTime must be null before starting", indexer.getStartTime());
        indexer.run();
        PhotoInfo[] photos3 = PhotoInfo.retrieveByOrigHash(hash3);
        assertEquals("1 photo per picture should be found", 1, photos3.length);
        PhotoInfo p3 = photos3[0];
        PhotoInfo photosInTopFolder2[] = { p3 };
        assertFolderHasPhotos(topFolder, photosInTopFolder2);
        assertEquals("More than 1 subfolder in topFolder", 1, topFolder.getSubfolderCount());
        subFolder = topFolder.getSubfolder(0);
        assertEquals("Subfolder name not correct", "test", subFolder.getName());
        PhotoInfo[] photosInSubFolder2 = { p2 };
        assertFolderHasPhotos(subFolder, photosInSubFolder2);
        Collection p2folders = p2.getFolders();
        assertFalse("p2 must not be in topFolder", p2folders.contains(topFolder));
    }

    void assertFolderHasPhotos(PhotoFolder folder, PhotoInfo photos[]) {
        boolean found[] = new boolean[photos.length];
        for (int n = 0; n < photos.length; n++) {
            found[n] = false;
        }
        int numPhotosInFolder = folder.getPhotoCount();
        for (int i = 0; i < numPhotosInFolder; i++) {
            PhotoInfo p = folder.getPhoto(i);
            for (int n = 0; n < photos.length; n++) {
                if (p == photos[n]) {
                    found[n] = true;
                }
            }
        }
        for (int n = 0; n < photos.length; n++) {
            if (!found[n]) {
                fail("Photo " + n + ": " + photos[n].getUid() + " not found in folder " + folder.getName());
            }
        }
    }

    public static Test suite() {
        return new TestSuite(Test_ExtVolIndexer.class);
    }

    public static void main(String[] args) {
        URL log4jPropertyURL = Test_ExtVolIndexer.class.getClassLoader().getResource("photovault_log4j.properties");
        org.apache.log4j.PropertyConfigurator.configure(log4jPropertyURL);
        org.apache.log4j.Logger instLog = org.apache.log4j.Logger.getLogger(ExtVolIndexer.class.getName());
        instLog.setLevel(org.apache.log4j.Level.DEBUG);
        junit.textui.TestRunner.run(suite());
    }
}
