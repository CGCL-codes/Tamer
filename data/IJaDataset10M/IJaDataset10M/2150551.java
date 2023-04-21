package org.openuss.documents;

import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @see org.openuss.documents.FolderEntry
 */
public class FolderEntryImpl extends org.openuss.documents.FolderEntryBase implements org.openuss.documents.FolderEntry {

    /**
	 * The serial version UID of this class. Needed for serialization.
	 */
    private static final long serialVersionUID = 9108164927807060019L;

    @Override
    public String getExtension() {
        return null;
    }

    @Override
    public Date getModified() {
        return getCreated();
    }

    @Override
    public Integer getFileSize() {
        return 0;
    }

    @Override
    public String getPath() {
        if (getParent() == null) {
            return "";
        } else {
            String path = getParent().getPath();
            if (StringUtils.isNotBlank(path)) {
                return path + "/" + getParent().getName();
            }
            return getParent().getName();
        }
    }

    @Override
    public String getAbsoluteName() {
        String path = getPath();
        return StringUtils.isBlank(path) ? getFileName() : getPath() + "/" + getFileName();
    }

    @Override
    public String getSizeAsString() {
        Integer size = getFileSize();
        if (size != null) {
            return FileUtils.byteCountToDisplaySize(size);
        } else {
            return "";
        }
    }

    @Override
    public String getFileName() {
        return getName();
    }

    public boolean isReleased() {
        return new Date().after(getCreated());
    }

    public Date releaseDate() {
        return getCreated();
    }
}
