package net.sf.javascribe.generator;

/**
 * @author User
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CodeSnippet {

    private StringBuffer source = null;

    public CodeSnippet() {
        source = new StringBuffer();
    }

    public StringBuffer getSource() {
        return source;
    }

    public void merge(CodeSnippet other) {
        if (other != null) {
            source.append(other.getSource());
        }
    }
}
