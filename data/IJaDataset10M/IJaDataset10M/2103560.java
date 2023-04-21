package com.nullfish.lib.vfs.impl.lha;

import java.util.ArrayList;
import java.util.List;
import jp.gr.java_conf.dangan.util.lha.LhaHeader;
import com.nullfish.lib.vfs.FileAttribute;
import com.nullfish.lib.vfs.FileName;
import com.nullfish.lib.vfs.FileSystem;
import com.nullfish.lib.vfs.ManipulationFactory;
import com.nullfish.lib.vfs.VFile;

/**
 * @author shunji
 *
 */
public class LHAFile extends VFile {

    private static final ManipulationFactory provider = new LHAFileManipulationFactory();

    List children = new ArrayList();

    LhaHeader entry;

    /**
	 * コンストラクタ
	 * ファイルインスタンスの生成を制御するため、プライベートになっている。
	 * インスタンス生成にはgetInstanceメソッドを使用する。
	 * @param fileSystem
	 * @param fileName
	 */
    private LHAFile(FileSystem fileSystem, FileName fileName) {
        super(fileSystem, fileName);
    }

    /**
	 * ファイルインスタンスを取得する。
	 * 
	 * @param fileSystem
	 * @param fileName
	 * @param header
	 * @return
	 */
    public static LHAFile getInstance(LHAFileSystem fileSystem, LHAFileName fileName) {
        LHAFile rtn = fileSystem.getFileCache(fileName);
        if (rtn == null) {
            rtn = new LHAFile(fileSystem, fileName);
            fileSystem.addFileCache(fileName, rtn);
        }
        return rtn;
    }

    public ManipulationFactory getManipulationFactory() {
        return provider;
    }

    /**
	 * 子ファイルを追加する。
	 * @param file
	 */
    public void addChildFile(VFile file) {
        if (!children.contains(file)) {
            children.add(file);
        }
    }

    /**
	 * 子ファイルを取得する。
	 */
    public List getChildFiles() {
        return children;
    }

    public void setInitialAttribute(FileAttribute attributes) {
        this.attributes = attributes;
    }

    public FileAttribute getInitialAttribute() {
        return attributes;
    }

    public LhaHeader getLhaHeader() {
        return entry;
    }

    public void setLhaHeader(LhaHeader entry) {
        this.entry = entry;
    }
}
