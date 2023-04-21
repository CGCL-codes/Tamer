package net.laubenberger.bogatyr.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.File;
import java.util.TimeZone;
import net.laubenberger.bogatyr.misc.exception.RuntimeExceptionIsNull;
import net.laubenberger.bogatyr.model.unit.Bit;
import org.junit.Test;

/**
 * JUnit test for {@link HelperEnvironment}
 *
 * @author Stefan Laubenberger
 * @version 20110124
 */
public class HelperEnvironmentTest {

    @Test
    public void testGetMemoryUsed() {
        assertTrue(HelperEnvironment.getMemoryUsed() > Bit.MEGABYTE.convertTo(Bit.BYTE, 1).longValue());
        assertEquals(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(), HelperEnvironment.getMemoryUsed());
        assertTrue(HelperEnvironment.getMemoryMax() > HelperEnvironment.getMemoryUsed());
    }

    @Test
    public void testGetMemoryFree() {
        assertTrue(HelperEnvironment.getMemoryFree() > Bit.MEGABYTE.convertTo(Bit.BYTE, 1).longValue());
        assertEquals(Runtime.getRuntime().maxMemory() - (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()), HelperEnvironment.getMemoryFree());
        assertTrue(HelperEnvironment.getMemoryMax() > HelperEnvironment.getMemoryFree());
    }

    @Test
    public void testGetMemoryTotal() {
        assertTrue(HelperEnvironment.getMemoryTotal() > Bit.MEGABYTE.convertTo(Bit.BYTE, 2).longValue());
        assertEquals(Runtime.getRuntime().totalMemory(), HelperEnvironment.getMemoryTotal());
        assertTrue(HelperEnvironment.getMemoryMax() > HelperEnvironment.getMemoryTotal());
    }

    @Test
    public void testGetMemoryMax() {
        assertTrue(HelperEnvironment.getMemoryMax() > Bit.MEGABYTE.convertTo(Bit.BYTE, 4).longValue());
        assertEquals(Runtime.getRuntime().maxMemory(), HelperEnvironment.getMemoryMax());
        assertTrue(HelperEnvironment.getMemoryMax() > HelperEnvironment.getMemoryTotal());
    }

    @Test
    public void testGetJavaVersion() {
        assertNotNull(HelperEnvironment.getJavaVersion());
        assertEquals(System.getProperties().getProperty("java.version"), HelperEnvironment.getJavaVersion());
    }

    @Test
    public void testGetJavaVendor() {
        assertNotNull(HelperEnvironment.getJavaVendor());
        assertEquals(System.getProperties().getProperty("java.vendor"), HelperEnvironment.getJavaVendor());
    }

    @Test
    public void testGetJavaVmName() {
        assertNotNull(HelperEnvironment.getJavaVmName());
        assertEquals(System.getProperties().getProperty("java.vm.name"), HelperEnvironment.getJavaVmName());
    }

    @Test
    public void testGetJavaVmVersion() {
        assertNotNull(HelperEnvironment.getJavaVmVersion());
        assertEquals(System.getProperties().getProperty("java.vm.version"), HelperEnvironment.getJavaVmVersion());
    }

    @Test
    public void testGetJavaProperties() {
        assertTrue(50 < HelperEnvironment.getJavaProperties().size());
        assertEquals(System.getProperties(), HelperEnvironment.getJavaProperties());
    }

    @Test
    public void testGetClassPath() {
        assertNotNull(HelperEnvironment.getClassPath());
        assertEquals(System.getProperties().getProperty("java.class.path"), HelperEnvironment.getClassPath());
    }

    @Test
    public void testGetLibraryPath() {
        assertNotNull(HelperEnvironment.getLibraryPath());
        assertEquals(System.getProperties().getProperty("java.library.path"), HelperEnvironment.getLibraryPath());
    }

    @Test
    public void testGetAvailableProcessors() {
        assertTrue(0 < HelperEnvironment.getAvailableProcessors());
        assertEquals(Runtime.getRuntime().availableProcessors(), HelperEnvironment.getAvailableProcessors());
    }

    @Test
    public void testGetOsArch() {
        assertNotNull(HelperEnvironment.getOsArch());
        assertEquals(System.getProperties().getProperty("os.arch"), HelperEnvironment.getOsArch());
    }

    @Test
    public void testGetOsName() {
        assertNotNull(HelperEnvironment.getOsName());
        assertEquals(System.getProperties().getProperty("os.name"), HelperEnvironment.getOsName());
    }

    @Test
    public void testGetOsVersion() {
        assertNotNull(HelperEnvironment.getOsVersion());
        assertEquals(System.getProperties().getProperty("os.version"), HelperEnvironment.getOsVersion());
    }

    @Test
    public void testGetOsEnvironmentVariables() {
        assertTrue(5 < HelperEnvironment.getOsEnvironmentVariables().size());
        assertEquals(System.getenv(), HelperEnvironment.getOsEnvironmentVariables());
    }

    @Test
    public void testGetOsEnvironmentVariable() {
        final String variable = "PATH";
        assertNotNull(HelperEnvironment.getOsEnvironmentVariable(variable));
        assertNull(HelperEnvironment.getOsEnvironmentVariable(HelperString.EMPTY_STRING));
        assertEquals(System.getenv(variable), HelperEnvironment.getOsEnvironmentVariable(variable));
        try {
            HelperEnvironment.getOsEnvironmentVariable(null);
            fail("variable is null");
        } catch (RuntimeExceptionIsNull ex) {
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testGetOsTempDirectory() {
        assertNotNull(HelperEnvironment.getOsTempDirectory());
        assertEquals(new File(System.getProperty("java.io.tmpdir")), HelperEnvironment.getOsTempDirectory());
    }

    @Test
    public void testGetUserHomeDirectory() {
        assertNotNull(HelperEnvironment.getUserHomeDirectory());
        assertEquals(new File(System.getProperty("user.home")), HelperEnvironment.getUserHomeDirectory());
    }

    @Test
    public void testGetUserDirectory() {
        assertNotNull(HelperEnvironment.getUserDirectory());
        assertEquals(new File(System.getProperty("user.dir")), HelperEnvironment.getUserDirectory());
    }

    @Test
    public void testGetUserName() {
        assertNotNull(HelperEnvironment.getUserName());
        assertEquals(System.getProperty("user.name"), HelperEnvironment.getUserName());
    }

    @Test
    public void testGetUserCountry() {
        assertNotNull(HelperEnvironment.getUserCountry());
        assertEquals(System.getProperty("user.country"), HelperEnvironment.getUserCountry().getCode());
    }

    @Test
    public void testGetUserLanguage() {
        assertNotNull(HelperEnvironment.getUserLanguage());
        assertEquals(System.getProperty("user.language"), HelperEnvironment.getUserLanguage().getCode());
    }

    @Test
    public void testGetUserTimezone() {
        assertNotNull(HelperEnvironment.getUserTimezone());
        assertEquals(TimeZone.getDefault(), HelperEnvironment.getUserTimezone());
    }

    @Test
    public void testGetPlatform() {
        assertNotNull(HelperEnvironment.getPlatform());
    }

    @Test
    public void testGetReportJava() {
        assertEquals(7, HelperEnvironment.getReportJava().size());
    }

    @Test
    public void testGetReportOS() {
        assertEquals(6, HelperEnvironment.getReportOS().size());
    }

    @Test
    public void testGetReportUser() {
        assertEquals(6, HelperEnvironment.getReportUser().size());
    }

    @Test
    public void testGetReportSystem() {
        assertTrue(6 < HelperEnvironment.getReportSystem().size());
    }
}
