package com.nullfish.lib.vfs.impl.smbwin;

import java.io.File;
import jp.ne.anet.kentkt.fastfile.FastFile;
import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileSystem;
import com.nullfish.lib.vfs.ManipulationFactory;
import com.nullfish.lib.vfs.VFile;

/**
 * SMBファイルクラス
 * 
 * @author Shunji Yamaura
 */
public class SMBFile extends VFile {

    private File file;

    private static SMBFileManipulationFactory provider = new SMBFileManipulationFactory();

    /**
	 * コンストラクタ
	 * @param fileSystem
	 * @param fileName
	 */
    public SMBFile(FileSystem fileSystem, FileName fileName) {
        this(fileSystem, fileName, null);
    }

    /**
	 * コンストラクタ
	 * @param fileSystem
	 * @param fileName
	 * @param file
	 */
    public SMBFile(FileSystem fileSystem, FileName fileName, File file) {
        super(fileSystem, fileName);
        this.file = file;
        setAttributeCache(new SMBFileAttribute(this));
        permission = new SMBPermission(this);
    }

    /**
	 * ローカルファイル操作クラス提供クラスを取得する。
	 * @see com.nullfish.lib.vfs.VFile#getManipulationFactory()
	 */
    public ManipulationFactory getManipulationFactory() {
        return provider;
    }

    /**
	 * Javaのファイルオブジェクトを取得する。
	 * @return
	 */
    public File getFile() {
        if (file == null) {
            file = new FastFile(getFileName().getAbsolutePath());
        }
        return file;
    }

    /**
	 * ファイルが等しいか判定する。
	 * 他のVFile継承クラスと異なり、java.io.Fileのequalsで判定している。
	 * これはWindowsの場合、大文字と小文字を区別しないため。
	 */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!getClass().equals(o.getClass())) {
            return false;
        }
        SMBFile otherFile = (SMBFile) o;
        return this.getFile().equals(otherFile.getFile());
    }
}
