package ch.sahits.codegen.java.util;

import org.eclipse.jdt.core.IJavaProject;
import ch.sahits.codegen.core.java.ProjectClassLoaderV2;
import ch.sahits.codegen.generator.IBaseGenerator;
import ch.sahits.codegen.java.extensions.Generator;

/**
 * This is a special class loader for the generator classes.
 * There are three levels of trying to load the class:
 * <ol>
 * <li>The class is on the class path of the class loader of the plugin</li>
 * <li>The class is supplied through an extension point</li>
 * <li>The class can be found anywhere on the project</li>
 * </ol>
 * @author Andi Hotz
 * @since 0.9.2
 */
public final class ProjectGeneratorClassLoader extends ProjectClassLoaderV2 {

    /**
	 * Initialize the current active project
	 * @param project active project
	 */
    public ProjectGeneratorClassLoader(IJavaProject project) {
        super(project);
    }

    /**
	 * This method is called when the class is not found on the classpath.
	 * Check out all extensions for the extension point
	 * <code>ch.sahits.codegen.java.generator</code> and look there for the
	 * class that implements the interface {@link IBaseGenerator}.
	 * @param name of the class to be found
	 * @see ch.sahits.codegen.core.java.ProjectClassLoaderV2#findClass(java.lang.String)
	 */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        IBaseGenerator gen = Generator.getGenerator(name);
        if (gen != null) {
            return gen.getClass();
        }
        return super.findClass(name);
    }
}
