package net.sourceforge.jrrd;

import java.io.File;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestPlateformSupport {

    public static final String RRD_SAMPLE_PATH_KEY = "rrd.sample.path";

    public static final String VERSION_1_RRD_FILE_NAME = "cpu-load.rrd";

    public static final String VERSION_3_LINUX_X86_FILE_NAME = "pidle.linux.x86.rrd";

    public static final String VERSION_3_LINUX_X86_64_FILE_NAME = "pidle.linux.x86_64.rrd";

    public static final String VERSION_1_SOLARIS_FILE_NAME = "piddle.solaris.rrd";

    private String rrdSamplePath = null;

    private String version1RrdFileName = null;

    private String version3LinuxX86FileName = null;

    private String version3LinuxX86_64FileName = null;

    private String version1SolarisFileName = null;

    @Before
    public void setUp() {
        rrdSamplePath = System.getProperty(RRD_SAMPLE_PATH_KEY);
        version1RrdFileName = rrdSamplePath + "/" + VERSION_1_RRD_FILE_NAME;
        version3LinuxX86FileName = rrdSamplePath + "/" + VERSION_3_LINUX_X86_FILE_NAME;
        version3LinuxX86_64FileName = rrdSamplePath + "/" + VERSION_3_LINUX_X86_64_FILE_NAME;
        version1SolarisFileName = rrdSamplePath + "/" + VERSION_1_SOLARIS_FILE_NAME;
    }

    @Test
    public void testSetUp() {
        Assert.assertTrue(rrdSamplePath != null);
        Assert.assertTrue(new File(rrdSamplePath).exists());
        Assert.assertTrue(new File(version1RrdFileName).exists());
        Assert.assertTrue(new File(version3LinuxX86FileName).exists());
        Assert.assertTrue(new File(version3LinuxX86_64FileName).exists());
        Assert.assertTrue(new File(version1SolarisFileName).exists());
    }

    @Test
    public void testGetVersionForAVersion1File() throws IOException {
        RRDatabase rrdb = new RRDatabase(version1RrdFileName);
        Assert.assertEquals(rrdb.getVersion(), 1);
        rrdb.close();
    }

    @Test
    public void testGetVersionForAVersion3FileOnLinuxX86() throws IOException {
        RRDatabase rrdb = new RRDatabase(version3LinuxX86FileName);
        Assert.assertEquals(rrdb.getVersion(), 3);
        rrdb.close();
    }

    @Test
    public void testGetVersionForAVersion3FileOnLinuxX86_64() throws IOException {
        RRDatabase rrdb = new RRDatabase(version3LinuxX86_64FileName);
        Assert.assertEquals(rrdb.getVersion(), 3);
        rrdb.close();
    }

    @Test
    public void testGetVersionForAVersion1FileOnSolaris() throws IOException {
        RRDatabase rrdb = new RRDatabase(version1SolarisFileName);
        Assert.assertEquals(rrdb.getVersion(), 1);
        rrdb.close();
    }

    @Test
    public void testEndianStuffForFileOnSolaris() throws IOException {
        RRDatabase rrdb = new RRDatabase(version1SolarisFileName);
        Assert.assertTrue(rrdb.isBigEndian());
        rrdb.close();
    }

    @Test
    public void testEndianStuffForFileOnLinuxX86() throws IOException {
        RRDatabase rrdb = new RRDatabase(version3LinuxX86FileName);
        Assert.assertFalse(rrdb.isBigEndian());
        rrdb.close();
    }

    @Test
    public void testEndianStuffForFileOnLinuxX86_64() throws IOException {
        RRDatabase rrdb = new RRDatabase(version3LinuxX86_64FileName);
        Assert.assertFalse(rrdb.isBigEndian());
        rrdb.close();
    }

    @Test
    public void test64bitsStuffForFileOnLinuxX86_64() throws IOException {
        RRDatabase rrdb = new RRDatabase(version3LinuxX86_64FileName);
        Assert.assertTrue(rrdb.is64bitsRRDDatabase());
        rrdb.close();
    }

    @Test
    public void test64bitsStuffForFileOnLinuxX86() throws IOException {
        RRDatabase rrdb = new RRDatabase(version3LinuxX86FileName);
        Assert.assertFalse(rrdb.is64bitsRRDDatabase());
        rrdb.close();
    }

    @Test
    public void test64bitsStuffForFileOnSolaris() throws IOException {
        RRDatabase rrdb = new RRDatabase(version1SolarisFileName);
        Assert.assertFalse(rrdb.is64bitsRRDDatabase());
        rrdb.close();
    }
}
