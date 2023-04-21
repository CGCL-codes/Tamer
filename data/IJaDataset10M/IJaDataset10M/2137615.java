package jsr203.nio.file;

import jsr203.nio.file.attribute.BasicFileAttributes;

/**
 * A skeletal visitor of files with default behavior to visit all files.
 *
 * <p> This class trivially implements the {@link #preVisitDirectory preVisitDirectory},
 * {@link #postVisitDirectory postVisitDirectory}, and {@link #visitFile
 * visitFile} methods to return {@link FileVisitResult#CONTINUE CONTINUE} so
 * that all files are visited.
 *
 * @since 1.7
 */
public abstract class AbstractFileVisitor implements FileVisitor {

    /**
     * Initializes a new instance of this class.
     */
    protected AbstractFileVisitor() {
    }

    @Override
    public FileVisitResult preVisitDirectory(FileRef dir, Path relPath) {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(FileRef file, Path relPath, BasicFileAttributes attrs) {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(FileRef dir, Path relPath) {
        return FileVisitResult.CONTINUE;
    }
}
