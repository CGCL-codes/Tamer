package com.nullfish.lib.vfs.impl.tar.manipulation;

import java.util.List;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.FileNotExistsException;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.tar.TARFile;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetChildrenManipulation;

/**
 * @author shunji
 *
 */
public class TARGetChildrenManipulation extends AbstractGetChildrenManipulation {

    public TARGetChildrenManipulation(VFile file) {
        super(file);
    }

    public VFile[] doGetChildren(VFile file) throws VFSException {
        if (!file.exists(this)) {
            throw new FileNotExistsException(file);
        }
        List filesList = ((TARFile) file).getChildFiles();
        VFile[] rtn = new VFile[filesList.size()];
        return (VFile[]) filesList.toArray(rtn);
    }
}
