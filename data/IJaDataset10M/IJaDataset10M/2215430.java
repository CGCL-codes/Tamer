package tests;

import java.net.URI;
import javax.tools.SimpleJavaFileObject;

/** This class makes a mock JavaFileObject.  It holds a String as its content
 * and is given a pseudo-filename to use, but does not represent an actual file in 
 * the file system. 
 * <P>
 * Note that we use Kind.OTHER to designate specification (non-.java) files.
 * 
 * @author David Cok
 */
public class TestJavaFileObject extends SimpleJavaFileObject {

    protected String content;

    protected static final URI uritest = makeURI();

    /** A utility method to make the URI, so it can handle the exceptions. 
     * We don't try to recover gracefully if the exception occurs - this is
     * just used in testing anyway. */
    private static URI makeURI() {
        try {
            return new URI("file:///TEST.java");
        } catch (Exception e) {
            System.err.println("CATASTROPHIC EXIT - FAILED TO CONSTRUCT A MOCK URI");
            System.exit(3);
            return null;
        }
    }

    /** A constructor of a JavaFileObject of kind SOURCE,
     * with the given content and a made-up file name.
     * @param s The content of the file
     */
    public TestJavaFileObject(String s) {
        super(uritest, Kind.SOURCE);
        content = s;
    }

    /** Constructs a new JavaFileObject of kind SOURCE or OTHER depending on the
     * filename extension
     * @param filename the filename to use (no leading slash) (null indicates to
     *          use the internal fabricated filename)
     * @param content the content of the pseudo file
     * @throws Exception if a URI cannot be created
     */
    public TestJavaFileObject(String filename, String content) throws Exception {
        super(filename == null ? uritest : new URI("file:///" + filename), filename == null || filename.endsWith(".java") ? Kind.SOURCE : Kind.OTHER);
        this.content = content;
    }

    /** Constructs a new JavaFileObject
     * @param uri the URI to use
     * @param content the content of the pseudo file
     */
    public TestJavaFileObject(URI uri, String content) {
        super(uri, uri.getPath().endsWith(".java") ? Kind.SOURCE : Kind.OTHER);
        this.content = content;
    }

    /** Overrides the parent to provide the content directly from the String
     * supplied at construction, rather than reading the file.  This is called
     * by the system.
     */
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return content;
    }

    @Override
    public boolean isNameCompatible(String simpleName, Kind kind) {
        String s = uri.getPath();
        if (kind == Kind.OTHER) {
            int i = s.lastIndexOf('/');
            s = s.substring(i + 1);
            return s.startsWith(simpleName);
        } else {
            String baseName = simpleName + kind.extension;
            return s.endsWith("/" + baseName);
        }
    }

    /** Returns true if the receiver and argument represent the same file */
    public boolean equals(Object o) {
        if (!(o instanceof TestJavaFileObject)) return false;
        String s = uri.normalize().getPath();
        String ss = ((TestJavaFileObject) o).uri.normalize().getPath();
        return s.equals(ss) || s.endsWith(ss) || ss.endsWith(s);
    }

    /** A definition of hashCode, since we have a definition of equals */
    public int hashCode() {
        return uri.normalize().getPath().hashCode();
    }

    public String toString() {
        String s = getName();
        return s;
    }
}
