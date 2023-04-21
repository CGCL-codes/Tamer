package geodress.tests;

import geodress.exceptions.NoMetaDataException;
import geodress.main.FileOperations;
import geodress.main.InfoConstants;
import geodress.model.PictureBox;
import geodress.model.reader.GoogleMapsReader;
import geodress.model.reader.SanselanReader;
import geodress.model.writer.ExifToolWriter;
import java.io.File;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the writing implementation of
 * {@link PictureBox#writeAddresses(geodress.model.writer.MetaDataWriter, int, geodress.ui.ProgressInfo)}
 * (needs Internet connection).<br>
 * Attention: This class uses the {@link SanselanReader} class to assert the
 * results.<br>
 * Note: For accessing ExifTool, it should be installed and registered as an
 * system variable so it can be easy called via entering <tt>exiftool</tt> on
 * the command-line.
 * 
 * @author Stefan T.
 */
public class PictureWritingTest {

    /** the directory with test files */
    private File dir;

    /** test picture with EXIF data */
    private File testFileBeach;

    /** test picture with EXIF data */
    private File testFileParis;

    /** test picture with EXIF data and user comment */
    private File testFileGate;

    /** test picture with EXIF data and image description */
    private File testFileSky;

    /** test picture without EXIF data */
    private File testFileField;

    /** a reader to validate test writings */
    private SanselanReader reader;

    /**
	 * Initializes the objects and makes copies of the test files that could be
	 * modified.
	 * 
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
        dir = new File("testfiles" + File.separator + "PictureWritingTest_" + System.currentTimeMillis());
        dir.mkdir();
        dir.deleteOnExit();
        File testPictureWithExifBeach = new File("testfiles" + File.separator + "beach.jpg");
        testFileBeach = new File(dir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg");
        testFileBeach.deleteOnExit();
        FileOperations.copy(testPictureWithExifBeach, testFileBeach);
        File testPictureWithExifParis = new File("testfiles" + File.separator + "paris.jpg");
        testFileParis = new File(dir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg");
        testFileParis.deleteOnExit();
        FileOperations.copy(testPictureWithExifParis, testFileParis);
        File testPictureWithExifGate = new File("testfiles" + File.separator + "gate.jpg");
        testFileGate = new File(dir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg");
        testFileGate.deleteOnExit();
        FileOperations.copy(testPictureWithExifGate, testFileGate);
        File testPictureWithExifSky = new File("testfiles" + File.separator + "sky.jpg");
        testFileSky = new File(dir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg");
        testFileSky.deleteOnExit();
        FileOperations.copy(testPictureWithExifSky, testFileSky);
        File testPictureWithoutExifSign = new File("testfiles" + File.separator + "field.jpg");
        testFileField = new File(dir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg");
        testFileField.deleteOnExit();
        FileOperations.copy(testPictureWithoutExifSign, testFileField);
        reader = new SanselanReader();
    }

    /**
	 * Makes big test for writing addresses to image description.
	 * 
	 * @throws Exception
	 *             may cause an exception
	 */
    @Test
    public void makeWritingTestDescription() throws Exception {
        PictureBox testBox = new PictureBox(dir);
        testBox.catchAddresses(new GoogleMapsReader(), new EmptyProgress());
        testBox.writeAddresses(new ExifToolWriter(), InfoConstants.IMAGE_DESCRIPTION, new EmptyProgress());
        reader.setFile(testFileBeach);
        String testedAddress = reader.getData(InfoConstants.IMAGE_DESCRIPTION);
        Assert.assertTrue(testedAddress + " does not contain 'Nacional'", testedAddress.contains("Nacional"));
        Assert.assertTrue(testedAddress + " does not contain 'Palma'", testedAddress.contains("Palma"));
        Assert.assertTrue(testedAddress + " does not contain 'Spain'", testedAddress.contains("Spain"));
        reader.setFile(testFileParis);
        testedAddress = reader.getData(InfoConstants.IMAGE_DESCRIPTION);
        Assert.assertTrue(testedAddress + " does not contain 'Paris'", testedAddress.contains("Paris"));
        Assert.assertTrue(testedAddress + " does not contain 'France'", testedAddress.contains("France"));
        reader.setFile(testFileGate);
        testedAddress = reader.getData(InfoConstants.IMAGE_DESCRIPTION);
        Assert.assertTrue(testedAddress + " does not contain 'Portella'", testedAddress.contains("Portella"));
        Assert.assertTrue(testedAddress + " does not contain 'Palma'", testedAddress.contains("Palma"));
        Assert.assertTrue(testedAddress + " does not contain 'Spain'", testedAddress.contains("Spain"));
        reader.setFile(testFileSky);
        testedAddress = reader.getData(InfoConstants.IMAGE_DESCRIPTION);
        Assert.assertTrue(testedAddress + " does not contain 'Nacional'", testedAddress.contains("Nacional"));
        Assert.assertTrue(testedAddress + " does not contain 'Palma'", testedAddress.contains("Palma"));
        Assert.assertTrue(testedAddress + " does not contain 'Spain'", testedAddress.contains("Spain"));
        reader.setFile(testFileField);
        boolean exception = false;
        try {
            testedAddress = reader.getData(InfoConstants.IMAGE_DESCRIPTION);
        } catch (NoMetaDataException nmde) {
            exception = true;
        }
        Assert.assertTrue(exception);
    }

    /**
	 * Makes big test for writing addresses to user comment.
	 * 
	 * @throws Exception
	 *             may cause an exception
	 */
    @Test
    public void makeWritingTestComment() throws Exception {
        ExifToolWriter writer = new ExifToolWriter();
        writer.setExifToolPath("exiftool");
        PictureBox testBox = new PictureBox(dir);
        testBox.catchAddresses(new GoogleMapsReader(), new EmptyProgress());
        testBox.writeAddresses(writer, InfoConstants.USER_COMMENT, new EmptyProgress());
        reader.setFile(testFileBeach);
        String testedAddress = reader.getData(InfoConstants.USER_COMMENT);
        Assert.assertTrue(testedAddress + " does not contain 'Nacional'", testedAddress.contains("Nacional"));
        Assert.assertTrue(testedAddress + " does not contain 'Palma'", testedAddress.contains("Palma"));
        Assert.assertTrue(testedAddress + " does not contain 'Spain'", testedAddress.contains("Spain"));
        reader.setFile(testFileParis);
        testedAddress = reader.getData(InfoConstants.USER_COMMENT);
        Assert.assertTrue(testedAddress + " does not contain 'Paris'", testedAddress.contains("Paris"));
        Assert.assertTrue(testedAddress + " does not contain 'France'", testedAddress.contains("France"));
        reader.setFile(testFileGate);
        testedAddress = reader.getData(InfoConstants.USER_COMMENT);
        Assert.assertTrue(testedAddress + " does not contain 'Portella'", testedAddress.contains("Portella"));
        Assert.assertTrue(testedAddress + " does not contain 'Palma'", testedAddress.contains("Palma"));
        Assert.assertTrue(testedAddress + " does not contain 'Spain'", testedAddress.contains("Spain"));
        reader.setFile(testFileSky);
        testedAddress = reader.getData(InfoConstants.USER_COMMENT);
        Assert.assertTrue(testedAddress + " does not contain 'Nacional'", testedAddress.contains("Nacional"));
        Assert.assertTrue(testedAddress + " does not contain 'Palma'", testedAddress.contains("Palma"));
        Assert.assertTrue(testedAddress + " does not contain 'Spain'", testedAddress.contains("Spain"));
        reader.setFile(testFileField);
        boolean exception = false;
        try {
            testedAddress = reader.getData(InfoConstants.USER_COMMENT);
        } catch (NoMetaDataException nmde) {
            exception = true;
        }
        Assert.assertTrue(exception);
    }
}
