package org.databene.benerator;

import static org.junit.Assert.*;
import java.util.Map;
import org.databene.commons.version.VersionInfo;
import org.junit.Test;

/**
 * Tests the {@link VersionInfo} class.<br/><br/>
 * Created: 23.03.2011 11:34:32
 * @since 0.6.6
 * @author Volker Bergmann
 */
public class VersionInfoTest {

    @Test
    public void testVersion() {
        VersionInfo version = getVersionInfo();
        checkVersionNumber(version.getVersion());
        System.out.println(version);
    }

    @Test
    public void testVerifyDependencies() {
        VersionInfo version = getVersionInfo();
        version.verifyDependencies();
    }

    @Test
    public void testDependencies() {
        VersionInfo version = getVersionInfo();
        Map<String, String> dependencies = version.getDependencies();
        assertEquals(5, dependencies.size());
        checkDependency("jdbacl", dependencies);
        checkDependency("webdecs", dependencies);
        checkDependency("script", dependencies);
        checkDependency("contiperf", dependencies);
        checkDependency("commons", dependencies);
    }

    private void checkDependency(String name, Map<String, String> dependencies) {
        String dependencyVersion = dependencies.get(name);
        checkVersionNumber(dependencyVersion);
        System.out.println("using " + name + ' ' + dependencyVersion);
    }

    @SuppressWarnings("null")
    private void checkVersionNumber(String versionNumber) {
        assertFalse("version number is empty", versionNumber == null || versionNumber.length() == 0);
        assertFalse("version number was not substituted", versionNumber.startsWith("${"));
    }

    private VersionInfo getVersionInfo() {
        return VersionInfo.getInfo("benerator");
    }
}
