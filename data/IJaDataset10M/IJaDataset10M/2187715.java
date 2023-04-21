package ru.ecom.jbossinstaller.service.impl.file;

import java.io.File;

/**
 *
 */
public class AbstractFile implements IFile {

    public AbstractFile(String aName, IFile aParent) {
        theName = aName;
        theParent = aParent;
    }

    public String getName() {
        return theName;
    }

    public File getFile(IFile aFile) {
        if (theParent != null) {
            return new File(theParent.getFile(aFile), getName());
        } else {
            File file = aFile.getFile(null);
            return new File(file, getName());
        }
    }

    private final String theName;

    private final IFile theParent;
}
