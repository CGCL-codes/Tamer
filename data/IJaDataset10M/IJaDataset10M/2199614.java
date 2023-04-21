package net.sourceforge.antme.wtk;

import java.io.File;
import java.io.FileInputStream;
import net.sourceforge.antme.tasks.MachineProperties;
import junit.framework.TestCase;

public class ToolkitConfigurationTest extends TestCase {

    private File _sunWtkBase;

    private File _midp2Jar;

    private File _cldcJar;

    public ToolkitConfigurationTest() {
    }

    public void setUp() throws Exception {
        MachineProperties props = new MachineProperties();
        props.load(new FileInputStream(new File("test.properties")));
        _sunWtkBase = new File(props.getProperty("wtk.sun22"));
        _midp2Jar = new File(_sunWtkBase, "lib/midpapi20.jar");
        _cldcJar = new File(_sunWtkBase, "lib/cldcapi11.jar");
    }

    public void testEmulator() {
        ToolkitConfiguration configuration = new ToolkitConfiguration();
        assertNull(configuration.getEmulator());
        configuration.setEmulator(new File("foo"));
        assertNotNull(configuration.getEmulator());
    }

    public void testJars() {
        ToolkitConfiguration configuration = new ToolkitConfiguration();
        assertNotNull(configuration.getJars());
        configuration.setJars(null);
        assertNotNull(configuration.getJars());
        ToolkitJarSet jars = new ToolkitJarSet();
        configuration.setJars(jars);
        assertEquals(jars, configuration.getJars());
    }

    public void testAdd() throws Exception {
        ToolkitConfiguration configuration = new ToolkitConfiguration();
        assertNotNull(configuration.getJars());
        assertEquals(0, configuration.getJars().getSet().size());
        configuration.addJar(new ToolkitJar(_midp2Jar));
        assertEquals(1, configuration.getJars().getSet().size());
        configuration.addJar(new ToolkitJar(_midp2Jar));
        assertEquals(1, configuration.getJars().getSet().size());
        configuration.addJar(new ToolkitJar(_cldcJar));
        assertEquals(2, configuration.getJars().getSet().size());
    }
}
