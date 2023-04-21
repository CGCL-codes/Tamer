package fr.univartois.cril.alloyplugin.core;

import org.eclipse.core.resources.IFile;

/**
 * 
 * @author desruelles lionel
 * 
 */
public class RenameAlsInfo {

    private int offset;

    private String newName;

    private String oldName;

    private IFile sourceFile;

    private boolean updateBundle;

    private boolean allProjects;

    public int getOffset() {
        return offset;
    }

    public void setOffset(final int offset) {
        this.offset = offset;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(final String newName) {
        this.newName = newName;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(final String oldName) {
        this.oldName = oldName;
    }

    public IFile getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(final IFile sourceFile) {
        this.sourceFile = sourceFile;
    }

    public boolean isAllProjects() {
        return allProjects;
    }

    public void setAllProjects(final boolean allProjects) {
        this.allProjects = allProjects;
    }

    public boolean isUpdateBundle() {
        return updateBundle;
    }

    public void setUpdateBundle(final boolean updateBundle) {
        this.updateBundle = updateBundle;
    }
}
