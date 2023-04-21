package net.sf.refactorit.ui.options;

/**
 * Type safe wrapper for sourcepath/classpath string.
 *
 * @author Igor Malinin
 */
public class Path {

    public String path;

    public Path(String path) {
        this.path = path == null ? "" : path;
    }

    public String toString() {
        return path;
    }
}
