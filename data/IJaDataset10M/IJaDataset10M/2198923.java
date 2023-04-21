package ch.sahits.codegen.java.extensions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import ch.sahits.codegen.java.input.IInputParser;
import ch.sahits.codegen.core.Logging;

/**
 * This class encapsules the access to the extension point
 * ch.sahits.codegen.java.inputparser
 * @author Andi Hotz
 * @since 0.9.3
 */
public class InputParser {

    /**
	 * Name of the extension point
	 */
    private static final String EXTENSION_POINT = "ch.sahits.codegen.java.inputparser";

    /** Name of the attribute that holds the class name with the implementation */
    private static final String ATTR_GENERATOR_CLASS = "class";

    /**
	 * Retrieve the {@link IInputParser} from the
	 * extension point for a specific file extension. If there is more than one implementation
	 * for one and the same file extension registered, the one with the
	 * lowest ranking is choosen.
	 * @param fileExtension file extension of the input file to be parsed
	 * @return Instance of an parser or null if for the specified
	 * product no implementation is found.
	 */
    public static IInputParser getGenerator(String fileExtension) {
        IExtensionRegistry reg = Platform.getExtensionRegistry();
        IConfigurationElement[] extensions = reg.getConfigurationElementsFor(EXTENSION_POINT);
        IInputParser parser = null;
        int curRanking = 0;
        for (int i = 0; i < extensions.length; i++) {
            IConfigurationElement element = extensions[i];
            try {
                IInputParser tempParser = (IInputParser) element.createExecutableExtension(ATTR_GENERATOR_CLASS);
                String parserFileExtension = tempParser.getFileExtension();
                if (parserFileExtension.equals(fileExtension)) {
                    if (tempParser.getRanking() > curRanking) {
                        parser = tempParser;
                        curRanking = parser.getRanking();
                    }
                }
            } catch (CoreException e) {
                Logging.logWarning(e);
            }
        }
        return parser;
    }
}
