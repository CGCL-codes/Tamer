package net.sf.refactorit.ui.options;

/** @author Igor Malinin */
public final class ClassPath extends Path {

    public static final ClassPath EMPTY = new ClassPath();

    private ClassPath() {
        super("");
    }

    public ClassPath(String path) {
        super(path);
    }
}
