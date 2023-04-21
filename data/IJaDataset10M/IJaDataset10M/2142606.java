package vqwiki.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class PluginClassLoaderTest extends TestCase {

    private PluginClassLoader pcl;

    private File pluginFile;

    private File testOutputDirectory;

    protected void setUp() throws Exception {
        this.testOutputDirectory = new File(getClass().getResource("/").getPath());
        this.pluginFile = new File(this.testOutputDirectory, "/plugin.zip");
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(pluginFile));
        zos.putNextEntry(new ZipEntry("WEB-INF/"));
        zos.putNextEntry(new ZipEntry("WEB-INF/classes/"));
        zos.putNextEntry(new ZipEntry("WEB-INF/classes/system.properties"));
        System.getProperties().store(zos, null);
        zos.closeEntry();
        zos.putNextEntry(new ZipEntry("WEB-INF/lib/"));
        zos.putNextEntry(new ZipEntry("WEB-INF/lib/plugin.jar"));
        File jarFile = new File(this.testOutputDirectory.getPath() + "/plugin.jar");
        JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFile));
        jos.putNextEntry(new ZipEntry("vqwiki/"));
        jos.putNextEntry(new ZipEntry("vqwiki/plugins/"));
        jos.putNextEntry(new ZipEntry("vqwiki/plugins/system.properties"));
        System.getProperties().store(jos, null);
        jos.closeEntry();
        jos.close();
        IOUtils.copy(new FileInputStream(jarFile), zos);
        zos.closeEntry();
        zos.close();
        jarFile.delete();
        pcl = new PluginClassLoader(new File(testOutputDirectory, "/work"));
        pcl.addPlugin(pluginFile);
    }

    protected void tearDown() throws Exception {
        pluginFile.delete();
        FileUtils.deleteDirectory(pcl.getOutputDirectory());
    }

    public final void testShouldLoadPluginResourceFromClasses() throws Throwable {
        URL url = new File(pcl.getOutputDirectory(), "/WEB-INF/classes/").toURI().toURL();
        assertNotNull(new URLClassLoader(new URL[] { url }).getResource("system.properties"));
        assertNotNull(pcl.getResource("system.properties"));
        assertNotNull(pcl.getResourceAsStream("system.properties"));
    }

    public final void testShouldLoadPluginResourceFromJar() {
        assertNotNull(pcl.getResource("vqwiki/plugins/system.properties"));
        assertNotNull(pcl.getResourceAsStream("vqwiki/plugins/system.properties"));
    }
}
