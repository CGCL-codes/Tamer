package ch.sahits.codegen.java.input;

import org.eclipse.jdt.core.IJavaProject;
import ch.sahits.codegen.input.IXMLInputFileParser;
import ch.sahits.codegen.generator.IBaseGenerator;
import ch.sahits.codegen.extensions.XMLInputParser;
import ch.sahits.codegen.core.java.ProjectClassLoaderV2;

/**
 * This class loader parses an XML input file for
 * the model generator class that must be specified in
 * a &lt;modelgenerator&gt; tag. The class is tried to load
 * over the extension point ch.sahits.codegen.java.inputxmlparser
 * or the {@link ProjectClassLoaderV2}.
 * @author Andi Hotz
 * @since 0.9.3
 */
public final class XMLInputFileGeneratorClassLoader extends ProjectClassLoaderV2 {

    /**
	 * Initialize the current active project
	 * @param project active project
	 */
    public XMLInputFileGeneratorClassLoader(IJavaProject project) {
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
        IXMLInputFileParser parser = XMLInputParser.getParser(name);
        if (parser != null) {
            return parser.getClass();
        }
        return super.findClass(name);
    }
}
