package protoj.lang.ant;

import org.apache.tools.ant.taskdefs.Jar;
import org.apache.tools.ant.taskdefs.Manifest;
import org.apache.tools.ant.taskdefs.Manifest.Attribute;

/**
 * A convenience class for creating a jar file. Use the constructors to specify
 * the minimal and most widely anticipated configuration and the
 * <code>initXXX</code> methods for the less common configuration options.
 * 
 * @author Ashley Williams
 * 
 */
public final class JarWrapper {

    /**
	 * See {@link #getJar()}.
	 */
    private final Jar jar;

    private Manifest manifest;

    public JarWrapper(Jar jar) {
        this.jar = jar;
    }

    /**
	 * Call repeatedly in order to add a top level attribute to the manifest.
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
    public Attribute initManifest(String name, String value) {
        manifest = createManifest();
        Attribute attribute = new Manifest.Attribute(name, value);
        manifest.addConfiguredAttribute(attribute);
        return attribute;
    }

    /**
	 * The underlying ant task.
	 * 
	 * @return
	 */
    public Jar getJar() {
        return jar;
    }

    /**
	 * Lazily creates a manifest instance and adds to the jar task.
	 * 
	 * @return
	 */
    private Manifest createManifest() {
        if (manifest == null) {
            manifest = new Manifest();
            jar.addConfiguredManifest(manifest);
        }
        return manifest;
    }
}
