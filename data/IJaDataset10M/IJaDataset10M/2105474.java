package openfarmtools.repository.filehandling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import junit.framework.Assert;
import openfarmtools.interpreter.exceptions.VideoFileNotFoundException;
import org.junit.Test;

public class MaterialOsTest {

    private MaterialOs mat;

    private String rootRepository = "utresources\\media";

    /**
	 * Test method for {@link openfarmtools.repository.filehandling.MaterialOs#MaterialOs(java.lang.String, java.lang.String, openfarmtools.repository.filehandling.EOperatingSystemProperties)}.
	 */
    @Test
    public void testMaterialOs() {
        mat = new WindowsMaterial(rootRepository);
        Assert.assertEquals(EOperatingSystemProperties.WINDOWS, mat.materialProps);
        mat = new LinuxMaterial(rootRepository);
        Assert.assertEquals(EOperatingSystemProperties.LINUX, mat.materialProps);
    }

    /**
	 * Test method for {@link openfarmtools.repository.filehandling.MaterialOs#validateVideos(java.lang.String[])}.
	 * @throws VideoFileNotFoundException 
	 */
    @Test
    public void testValidateVideos() {
        String[] videos = { "mock1.mp4", "mock2.mp4", "mock3.mp4" };
        mat = new WindowsMaterial(rootRepository);
        try {
            mat.validateVideos(videos);
        } catch (VideoFileNotFoundException e) {
            fail(e.getMessage());
        }
        videos[0] = "inexistent.mp4";
        boolean passed = false;
        try {
            mat.validateVideos(videos);
        } catch (VideoFileNotFoundException e) {
            passed = true;
        }
        assertTrue(passed);
    }

    /**
	 * Test method for {@link openfarmtools.repository.filehandling.MaterialOs#formatMaterial(java.lang.String)}.
	 */
    @Test
    public void testFormatMaterialString() {
        mat = new WindowsMaterial(rootRepository);
        String[] videos = { "cilps|mock1.mp4", "mock2.mp4", "clips\\mock3.mp4" };
        assertEquals("utresources\\media\\cilps\\mock1.mp4", mat.formatMaterial(videos[0]));
        assertEquals("utresources\\media\\mock2.mp4", mat.formatMaterial(videos[1]));
        assertEquals("utresources\\media\\clips\\mock3.mp4", mat.formatMaterial(videos[2]));
        mat = new LinuxMaterial(rootRepository);
        String[] videosLinux = { "cilps|mock1.mp4", "mock2.mp4", "clips/mock3.mp4" };
        assertEquals("utresources\\media/cilps/mock1.mp4", mat.formatMaterial(videosLinux[0]));
        assertEquals("utresources\\media/mock2.mp4", mat.formatMaterial(videosLinux[1]));
        assertEquals("utresources\\media/clips/mock3.mp4", mat.formatMaterial(videosLinux[2]));
    }

    /**
	 * Test method for {@link openfarmtools.repository.filehandling.MaterialOs#formatMaterial(java.lang.String[])}.
	 */
    @Test
    public void testFormatMaterialStringArray() {
        mat = new WindowsMaterial(rootRepository);
        String[] videos = { "cilps|mock1.mp4", "mock2.mp4", "clips\\mock3.mp4" };
        mat.formatMaterial(videos);
        assertEquals("utresources\\media\\cilps\\mock1.mp4", videos[0]);
        assertEquals("utresources\\media\\mock2.mp4", videos[1]);
        assertEquals("utresources\\media\\clips\\mock3.mp4", videos[2]);
        mat = new LinuxMaterial(rootRepository);
        String[] videosLinux = { "cilps|mock1.mp4", "mock2.mp4", "clips/mock3.mp4" };
        mat.formatMaterial(videosLinux);
        assertEquals("utresources\\media/cilps/mock1.mp4", videosLinux[0]);
        assertEquals("utresources\\media/mock2.mp4", videosLinux[1]);
        assertEquals("utresources\\media/clips/mock3.mp4", videosLinux[2]);
    }

    /**
	 * Test method for {@link openfarmtools.repository.filehandling.MaterialOs#toUniversalFormat(java.lang.String[])}.
	 */
    @Test
    public void testToUniversalFormat() {
        mat = new WindowsMaterial(rootRepository);
        String[] videos = { "cilps|mock1.mp4", "mock2.mp4", "clips\\mock3.mp4" };
        mat.toUniversalFormat(videos);
        assertEquals("cilps|mock1.mp4", videos[0]);
        assertEquals("mock2.mp4", videos[1]);
        assertEquals("clips|mock3.mp4", videos[2]);
        mat = new LinuxMaterial(rootRepository);
        String[] videosLinux = { "cilps|mock1.mp4", "mock2.mp4", "clips/mock3.mp4" };
        mat.toUniversalFormat(videosLinux);
        assertEquals("cilps|mock1.mp4", videosLinux[0]);
        assertEquals("mock2.mp4", videosLinux[1]);
        assertEquals("clips|mock3.mp4", videosLinux[2]);
    }

    /**
	 * Test method for {@link openfarmtools.repository.filehandling.MaterialOs#toTemplateFormat(java.lang.String)}.
	 */
    @Test
    public void testToTemplateFormat() {
        mat = new WindowsMaterial(rootRepository);
        assertEquals("mock1_avi", mat.toTemplateFormat("mock1.avi"));
        assertEquals("c_mock1_avi", mat.toTemplateFormat("c:\\mock1.avi"));
        mat = new LinuxMaterial(rootRepository);
        assertEquals("mock1_avi", mat.toTemplateFormat("mock1.avi"));
        assertEquals("_home_filipe_media_mock1_avi", mat.toTemplateFormat("/home/filipe/media/mock1.avi"));
    }
}
