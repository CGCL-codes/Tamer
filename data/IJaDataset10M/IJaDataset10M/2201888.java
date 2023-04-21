package uk.ac.shef.wit.saxon.compiler.jdk6;

import java.io.IOException;
import java.net.URI;
import javax.tools.SimpleJavaFileObject;

/**
* A JavaFileObject class for source code, that just uses a String for
* the source code.
* @author Mark A. Greenwood
*/
public class SourceJavaFileObject extends SimpleJavaFileObject {

    /**
	 * Stores the actual source code
	 */
    private final String source;

    /**
	 * Creates a source file object for the given classname and source code.
	 * @param className the name of the class.
	 * @param sourceCode the classes source code.
	 */
    SourceJavaFileObject(String className, final String sourceCode) {
        super(URI.create(className + ".java"), Kind.SOURCE);
        this.source = sourceCode;
    }

    /**
	 * Provides access to the source code of a Rule instance
	 * @param ignoreEncodingErrors this parameter is currently ignored by this implementation
	 * @return the source code for the Rule instance this class represents.
	 */
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException, IllegalStateException, UnsupportedOperationException {
        return source;
    }

    /**
	 * Provides a useful human readable version of the source code in which all the lines have
	 * been numbered to make debugging much easier.
	 * @return the line numbered source code held by this instance.
	 */
    @Override
    public String toString() {
        String[] lines = source.split("\n");
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < lines.length; ++i) {
            formatted.append(i + 1).append(":\t").append(lines[i]).append("\n");
        }
        return formatted.toString();
    }
}
