package com.nullfish.lib.plugin;

import com.nullfish.app.jfd2.JFD;
import com.nullfish.app.jfd2.ui.container2.JFDOwner;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;

/**
 * @author shunji
 *
 */
public abstract class AbstractPlugin implements Plugin {

    private VFile baseFile;

    private PluginInformation info;

    /**
	 * 定義ファイル名
	 */
    public static final String DEFINITION = "plugin.xml";

    /**
	 * ルートノード名
	 */
    public static final String NODE_NAME = "plugin";

    /**
	 * 名称属性
	 */
    public static final String ATTR_NAME = "name";

    /**
	 * クラス属性
	 */
    public static final String ATTR_CLASS = "class";

    /**
	 * 説明ノード
	 */
    public static final String NODE_DESCRIPTION = "description";

    public VFile getBaseDir() {
        return baseFile;
    }

    /**
	 * システム開始時に呼び出される。
	 * 
	 * @throws VFSException
	 */
    public void systemStarted() {
    }

    /**
	 * システム終了時に呼び出される。
	 * 
	 * @throws VFSException
	 */
    public void systemExited() {
    }

    /**
	 * 設定変更時に呼び出される。
	 * 
	 * @throws VFSException
	 */
    public void configurationChanged() {
    }

    /**
	 * JFD2インスタンスが生成された際に呼び出される。
	 * 
	 * @param jfd
	 */
    public void jfdCreated(JFD jfd) {
    }

    /**
	 * JFD2インスタンスが初期化された際に呼び出される。
	 * 
	 * @param jfd
	 */
    public void jfdInited(JFD jfd, VFile baseDir) {
    }

    /**
	 * JFD2インスタンスが廃棄された際に呼び出される。
	 * 
	 * @param jfd
	 */
    public void jfdDisposed(JFD jfd) {
    }

    /**
	 * JFDオーナーインスタンスが生成された際に呼び出される。
	 * 
	 * @param jfd
	 */
    public void jfdOwnerCreated(JFDOwner owner) {
    }

    /**
	 * JFD2オーナーインスタンスが廃棄された際に呼び出される。
	 * 
	 * @param jfd
	 */
    public void jfdOwnerDisposed(JFDOwner owner) {
    }

    public Object getParam(String key) {
        return info.getParameter(key);
    }

    public String getName() {
        return info.getName();
    }

    /**
	 * プラグインバージョンを取得する。
	 * @return
	 */
    public double getVersion() {
        return info.getVersion();
    }

    public String getDescription() {
        return info.getDescription();
    }

    void init(VFile baseFile, PluginInformation info) {
        this.baseFile = baseFile;
        this.info = info;
    }

    public VFile getResource(String path) {
        try {
            return baseFile.getRelativeFile(path);
        } catch (VFSException e) {
            return null;
        }
    }

    /**
	 * プラグインを停止する。
	 *
	 */
    public void close() {
    }
}
