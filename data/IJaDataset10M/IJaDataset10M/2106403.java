package org.eclipse.jdt.internal.formatter;

/**
 * Represents a reference in a javadoc comment block (see 
 * {@link FormatJavadocBlock}.
 * <p>
 * A specific class is used as intermediate positions need to be stored for further
 * formatting improvements (typically for qualified references).
 * </p>
 */
public class FormatJavadocReference extends FormatJavadocNode {

    public FormatJavadocReference(int start, int end, int line) {
        super(start, end, line);
    }

    public FormatJavadocReference(long position, int line) {
        super((int) (position >>> 32), (int) position, line);
    }

    void clean() {
    }

    protected void toString(StringBuffer buffer) {
        buffer.append("ref");
        super.toString(buffer);
    }
}
