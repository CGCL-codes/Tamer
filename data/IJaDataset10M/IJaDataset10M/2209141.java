package sywico.core.change.model;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import org.apache.log4j.Logger;
import sywico.core.Util;
import sywico.core.message.Message;

/**
 * 
 * This class contains most of the functionality required
 * for new and fullfile changes 
 * 
 */
public abstract class AbstractNewOrFullChange extends FileChange {

    static final long serialVersionUID = Message.serialVersionUID;

    public static Logger logger = Logger.getLogger(DeleteChange.class.getName());

    byte[] contents;

    public AbstractNewOrFullChange(String fileName, byte[] contents) {
        super(fileName);
        this.contents = contents;
    }

    public String toString() {
        return "update " + getFileName();
    }

    public byte[] getContents() {
        return contents;
    }

    public void apply(String from, String useFileName) {
        String fullName = from + File.separator + useFileName;
        if (logger.isDebugEnabled()) logger.debug("create/replace file " + fullName);
        Util.createParentDirectoryIfNeeded(fullName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fullName);
            fileOutputStream.write(getContents());
            Util.closeAndWaitFileOutputStream(fileOutputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int hashCode() {
        return getFileName().hashCode();
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof AbstractNewOrFullChange)) return false;
        AbstractNewOrFullChange fullFileChange = (AbstractNewOrFullChange) o;
        return getFileName().equals(fullFileChange.getFileName()) && Arrays.equals(getContents(), fullFileChange.getContents());
    }
}
