package ise.plugin.svn.data;

import java.util.List;
import org.tmatesoft.svn.core.wc.SVNRevision;

public class UpdateData extends SVNData {

    private List<String> conflictedFiles = null;

    private List<String> addedFiles = null;

    private List<String> deletedFiles = null;

    private List<String> updatedFiles = null;

    private SVNRevision revision = SVNRevision.HEAD;

    /**
     * Returns the value of revision.
     */
    public long getRevision() {
        return revision.getNumber();
    }

    /**
     * Sets the value of revision.
     * @param revision The value to assign revision.
     */
    public void setRevision(long number) {
        this.revision = SVNRevision.create(number);
    }

    /**
     * Returns the value of revision.
     */
    public SVNRevision getSVNRevision() {
        return revision;
    }

    /**
     * Sets the value of revision.
     * @param revision The value to assign revision.
     */
    public void setSVNRevision(SVNRevision revision) {
        this.revision = revision;
    }

    /**
     * Returns the value of conflictedFiles.
     */
    public List<String> getConflictedFiles() {
        return conflictedFiles;
    }

    /**
     * Sets the value of conflictedFiles.
     * @param conflictedFiles The value to assign conflictedFiles.
     */
    public void setConflictedFiles(List<String> conflictedFiles) {
        this.conflictedFiles = conflictedFiles;
    }

    /**
     * Returns the value of addedFiles.
     */
    public List<String> getAddedFiles() {
        return addedFiles;
    }

    /**
     * Sets the value of addedFiles.
     * @param addedFiles The value to assign addedFiles.
     */
    public void setAddedFiles(List<String> addedFiles) {
        this.addedFiles = addedFiles;
    }

    /**
     * Returns the value of deletedFiles.
     */
    public List<String> getDeletedFiles() {
        return deletedFiles;
    }

    /**
     * Sets the value of deletedFiles.
     * @param deletedFiles The value to assign deletedFiles.
     */
    public void setDeletedFiles(List<String> deletedFiles) {
        this.deletedFiles = deletedFiles;
    }

    /**
     * Returns the value of updatedFiles.
     */
    public List<String> getUpdatedFiles() {
        return updatedFiles;
    }

    /**
     * Sets the value of updatedFiles.
     * @param updatedFiles The value to assign updatedFiles.
     */
    public void setUpdatedFiles(List<String> updatedFiles) {
        this.updatedFiles = updatedFiles;
    }
}
