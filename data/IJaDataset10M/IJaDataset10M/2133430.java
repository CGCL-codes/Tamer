package com.nullfish.lib.vfs.impl.smbwin.manipulation;

import java.io.File;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.smbwin.SMBFile;
import com.nullfish.lib.vfs.manipulation.abst.AbstractMoveManipulation;

/**
 * 
 * @author Shunji Yamaura
 */
public class SMBMoveManipulation extends AbstractMoveManipulation {

    /**
	 * コンストラクタ
	 * @param file
	 */
    public SMBMoveManipulation(VFile file) {
        super(file);
    }

    public boolean doMoveFile(VFile from, VFile dest) throws VFSException {
        File fromFile = ((SMBFile) from).getFile();
        File destFile = ((SMBFile) dest).getFile();
        if (!destFile.getParentFile().exists() && !destFile.getParentFile().mkdirs()) {
            return false;
        }
        return fromFile.renameTo(destFile);
    }
}
