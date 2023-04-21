package net.sourceforge.processdash.tool.diff.impl.file;

import java.io.File;

public class FileSystemDiffException extends RuntimeException {

    public FileSystemDiffException() {
    }

    public FileSystemDiffException(String message) {
        super(message);
    }

    public FileSystemDiffException(Throwable cause) {
        super(cause);
    }

    public FileSystemDiffException(String message, Throwable cause) {
        super(message, cause);
    }

    public static class TypeMismatch extends FileSystemDiffException {
    }

    public static class FileNotFound extends FileSystemDiffException {

        private File file;

        public FileNotFound(File file) {
            this.file = file;
        }

        public File getMissingFile() {
            return file;
        }
    }

    public static class NoFilesListed extends FileSystemDiffException {
    }
}
