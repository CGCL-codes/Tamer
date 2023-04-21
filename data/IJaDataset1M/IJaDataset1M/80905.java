package com.nullfish.lib.vfs.impl.antzip.manipulation;

import java.util.Date;
import org.apache.tools.zip.ZipEntry;
import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.FileType;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.DefaultFileAttribute;
import com.nullfish.lib.vfs.impl.antzip.ZIPFile;
import com.nullfish.lib.vfs.impl.antzip.ZIPFileSystem;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetAttributesManipulation;

/**
 * @author shunji
 *
 */
public class ZIPGetAttributesManipulation extends AbstractGetAttributesManipulation {

    public ZIPGetAttributesManipulation(VFile file) {
        super(file);
    }

    public FileAttribute doGetAttribute(VFile file) throws VFSException {
        ZIPFile zfile = (ZIPFile) file;
        ZipEntry entry = zfile.getZipEntry();
        if (file.isRoot()) {
            return new DefaultFileAttribute(true, -1, null, FileType.DIRECTORY, ((ZIPFileSystem) file.getFileSystem()).getTotalSpace(), 0);
        }
        if (entry == null) {
            return new DefaultFileAttribute(false, -1, null, FileType.NOT_EXISTS);
        }
        FileType type;
        if (entry.isDirectory()) {
            type = FileType.DIRECTORY;
        } else {
            type = FileType.FILE;
        }
        return new DefaultFileAttribute(true, entry.getSize(), new Date(entry.getTime()), type, ((ZIPFileSystem) file.getFileSystem()).getTotalSpace(), 0);
    }
}
