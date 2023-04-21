package com.nullfish.lib.vfs.impl.smbwin.manipulation;

import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.impl.smbwin.SMBFile;
import com.nullfish.lib.vfs.impl.smbwin.SMBFileAttribute;
import com.nullfish.lib.vfs.manipulation.abst.AbstractGetAttributesManipulation;

/**
 * SMBファイルの属性初期化、取得操作クラス
 * @author Shunji Yamaura
 */
public class SMBGetAttributesManipulation extends AbstractGetAttributesManipulation {

    public SMBGetAttributesManipulation(VFile file) {
        super(file);
    }

    public FileAttribute doGetAttribute(VFile file) throws VFSException {
        return new SMBFileAttribute((SMBFile) file);
    }
}
