package com.nullfish.lib.vfs.impl.zipcloak.manipulation;

import java.io.InputStream;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.zipcloak.ZIPFile;
import com.nullfish.lib.vfs.impl.zipcloak.ZIPFileSystem;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetInputStreamManipulation;

/**
 * @author shunji
 *
 */
public class ZIPGetInputStreamManipulation extends AbstractGetInputStreamManipulation {

    public ZIPGetInputStreamManipulation(VFile file) {
        super(file);
    }

    public InputStream doGetInputStream(VFile file) throws VFSException {
        ZIPFileSystem fileSystem = (ZIPFileSystem) file.getFileSystem();
        return fileSystem.getInputStream((ZIPFile) file);
    }
}
