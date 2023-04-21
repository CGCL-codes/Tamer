package com.nullfish.lib.vfs.impl.classpath;

import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.Manipulation;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.impl.AbstractFileSystem;

/**
 * 
 * @author Shunji Yamaura
 */
public class ClassPathFileSystem extends AbstractFileSystem {

    private static ClassPathFileSystem instance;

    /**
	 * コンストラクタ
	 * @param drive
	 */
    private ClassPathFileSystem(VFS vfs) {
        super(vfs);
        rootName = new ClassPathFileName(new String[0]);
    }

    /**
	 * ファイルシステムのインスタンスを取得する
	 * @param drive
	 * @return
	 */
    public static ClassPathFileSystem getFileSystem(VFS vfs) throws IllegalArgumentException {
        if (instance == null) {
            instance = new ClassPathFileSystem(vfs);
        }
        return instance;
    }

    public void doOpen(Manipulation manipulation) {
    }

    public void doClose(Manipulation manipulation) {
    }

    /**
	 * 
	 */
    public boolean isOpened() {
        return true;
    }

    public void createFileSystem() {
    }

    public VFile getFile(FileName fileName) {
        return new ClassPathFile(this, fileName);
    }

    public boolean isLocal() {
        return true;
    }

    public boolean isShellCompatible() {
        return false;
    }
}
