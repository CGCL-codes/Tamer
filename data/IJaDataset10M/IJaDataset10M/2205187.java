package ru.ecom.jbossinstaller.service.impl.file;

import java.io.File;

/**
 *
 */
public class JBossDir implements IFile {

    public JBossDir(File aDir) {
        theDir = aDir;
    }

    public File getFile(IFile aFile) {
        return theDir;
    }

    public String getName() {
        throw new IllegalStateException("Не использовать!");
    }

    private final File theDir;
}
