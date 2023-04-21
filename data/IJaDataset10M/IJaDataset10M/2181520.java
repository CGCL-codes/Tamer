package org.tranche.project;

import org.tranche.hash.BigHash;
import org.tranche.meta.MetaData;

/**
 * This is a helper class to represent summaries of known project files. This way the tool can keep a lot of projects in memory without having to keep the entire project file.
 * @author James "Augie" Hill - augman85@gmail.com
 * @author Bryan E. Smith - bryanesmith@gmail.com
 */
public class ProjectSummary {

    /**
     * Need a consistent format so can easily test whether data was found. Keeps code cleaner and more consistent behavior.
     */
    public static final String DEFAULT_STRING_VALUE = "";

    /**
     * Need a consistent format so can easily test whether data was found. Keeps code cleaner and more consistent behavior.
     */
    public static final long DEFAULT_NUMERIC_VALUE = -1;

    public String title = DEFAULT_STRING_VALUE, description = DEFAULT_STRING_VALUE, uploader = DEFAULT_STRING_VALUE, type = DEFAULT_STRING_VALUE;

    public long size = DEFAULT_NUMERIC_VALUE, files = DEFAULT_NUMERIC_VALUE, uploadTimestamp = DEFAULT_NUMERIC_VALUE;

    public BigHash hash = null, oldVersion = null, newVersion = null;

    public boolean isEncrypted = false, isPublished = false, isHidden = false, shareMetaDataIfEncrypted = false, isProject = true;

    private boolean isMetaDataParsed = false, isProjectFileParsed = false;

    public ProjectSummary(BigHash hash) {
        this.hash = hash;
    }

    public ProjectSummary(MetaData md, BigHash hash) {
        this(hash);
        parseMetaData(md, hash);
    }

    public ProjectSummary(ProjectFile pf, MetaData md, BigHash hash) {
        this(md, hash);
        parseProjectFile(pf);
    }

    public ProjectSummary(BigHash hash, String title, String description, long size, long files, long uploadTimestamp, String uploader, boolean shareMetaDataIfEncrypted) {
        isMetaDataParsed = true;
        if (hash == null) {
            throw new RuntimeException("Hash is null.");
        }
        this.hash = hash;
        if (title == null) {
            this.title = "";
        } else {
            this.title = title;
        }
        if (description == null) {
            this.description = "";
        } else {
            this.description = description;
        }
        this.size = size;
        this.files = files;
        this.uploadTimestamp = uploadTimestamp;
        this.uploader = uploader;
        this.shareMetaDataIfEncrypted = shareMetaDataIfEncrypted;
    }

    /**
     * <p>Parses out the meta data info into this project summary</p>
     */
    public void parseMetaData(MetaData md, BigHash hash) {
        isMetaDataParsed = true;
        uploadTimestamp = md.getTimestampUploaded();
        isEncrypted = md.isEncrypted();
        isPublished = md.isPublicPassphraseSet();
        isHidden = md.isHidden();
        isProject = md.isProjectFile();
        shareMetaDataIfEncrypted = md.shareMetaDataIfEncrypted();
        uploader = md.getSignature().getUserName();
        if (isProject) {
            if (md.getDataSetName() != null) {
                title = md.getDataSetName();
            }
            if (md.getDataSetDescription() != null) {
                description = md.getDataSetDescription();
            }
            size = md.getDataSetSize();
            files = md.getDataSetFiles();
        } else {
            title = md.getName();
            files = 1;
            size = hash.getLength();
        }
    }

    /**
     * <p>Parses out the project file info into this project summary</p>
     */
    public void parseProjectFile(ProjectFile pf) {
        if (pf == null) {
            return;
        }
        isProjectFileParsed = true;
        files = pf.getParts().size();
        size = pf.getSize().longValue();
        if (title == null || title.equals("")) {
            title = pf.getName();
        }
        if (description == null || description.equals("")) {
            description = pf.getDescription();
        }
    }

    /**
     * Returns true if this has an old version in MetaData annotation
     */
    public boolean hasOldVersion() {
        return oldVersion != null;
    }

    /**
     * Returns true if this has a new version in MetaData annotation
     */
    public boolean hasNewVersion() {
        return newVersion != null;
    }

    /**
     * <p>Returns true if the project information is available. Otherwise, must download.</p>
     */
    public boolean isProjectInformationAvailable() {
        return !title.equals(DEFAULT_STRING_VALUE) && size != DEFAULT_NUMERIC_VALUE && files != DEFAULT_NUMERIC_VALUE && uploadTimestamp != DEFAULT_NUMERIC_VALUE;
    }

    /**
     * 
     * @return
     */
    public boolean isMetaDataParsed() {
        return isMetaDataParsed;
    }

    /**
     *
     * @return
     */
    public boolean isProjectFileParsed() {
        return isProjectFileParsed;
    }
}
