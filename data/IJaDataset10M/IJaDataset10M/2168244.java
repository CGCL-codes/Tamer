package org.scribble.type.java.parser;

import org.scribble.extensions.*;
import org.scribble.model.ModelReference;

/**
 * This class registers the Java type notation.
 */
@RegistryInfo(extension = org.scribble.parser.Notation.class)
public class JavaTypeNotation implements org.scribble.parser.Notation {

    private static final String JAVA_NAME = "Java";

    private static final String JAVA_CODE = "java";

    /**
	 * This method returns the code for the notation.
	 * 
	 * @return The notation code
	 */
    public String getCode() {
        return (JAVA_CODE);
    }

    /**
	 * This method returns the name of the notation.
	 * 
	 * @return The name
	 */
    public String getName() {
        return (JAVA_NAME);
    }

    /**
	 * This method returns the initial description associated
	 * with the supplied reference.
	 * 
	 * @param ref The reference
	 * @return The initial description, or null if no description
	 */
    public String getInitialDescription(ModelReference ref) {
        StringBuffer buf = new StringBuffer();
        return (buf.toString());
    }

    /**
	 * This method determines whether the Scribble editor should
	 * be used for editing the notation.
	 * 
	 * @return Whether the Scribble editor should be used
	 */
    public boolean useScribbleEditor() {
        return (false);
    }
}
