package com.nullfish.app.jfd2.viewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jdom.Element;
import org.jdom.JDOMException;
import com.nullfish.app.jfd2.Initable;
import com.nullfish.app.jfd2.JFD;
import com.nullfish.app.jfd2.util.DomCache;
import com.nullfish.lib.vfs.VFS;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;

/**
 * 
 * @author shunji
 */
public class FileViewerManager implements Initable {

    /**
	 * シングルトンインスタンス
	 */
    private static FileViewerManager instance = new FileViewerManager();

    /**
	 * 設定ディレクトリ
	 */
    private VFile baseDir;

    /**
	 * ビューアのファクトリのリスト
	 */
    private List viewerFactories = new ArrayList();

    /**
	 * 拡張子とビューアファクトリーのマップ
	 */
    private Map extensionViewerFactoryMap = new HashMap();

    /**
	 * キーとビューアのセットのマップ
	 */
    private Map viewerSetMap = new HashMap();

    /**
	 * ビューアのキャッシュのマップ
	 */
    private Map viewerCacheMap = new HashMap();

    /**
	 * デフォルトのビューア
	 */
    private FileViewerFactory defaultViewerFactory;

    public static FileViewerManager getInstance() {
        return instance;
    }

    private FileViewerManager() {
    }

    /**
	 * 初期化する。
	 */
    public void init(VFile baseDir) throws VFSException {
        this.baseDir = baseDir;
        viewerFactories.clear();
        extensionViewerFactoryMap.clear();
        viewerSetMap.clear();
        viewerCacheMap.clear();
        String[] files = { "classpath:///text_viewer.xml", "classpath:///jl_player.xml", "classpath:///graphic_viewer.xml" };
        FileViewerFactory[] factories = new FileViewerFactory[files.length];
        for (int i = 0; i < files.length; i++) {
            try {
                factories[i] = initFactory(VFS.getInstance().getFile(files[i]), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        defaultViewerFactory = factories[0];
    }

    public FileViewerFactory addFileViewer(VFile definition) throws JDOMException, IOException, VFSException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        return initFactory(definition, getClass().getClassLoader());
    }

    public FileViewerFactory addFileViewer(VFile definition, ClassLoader loader) throws JDOMException, IOException, VFSException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        return initFactory(definition, loader);
    }

    /**
	 * ファイルビューアのファクトリーを生成、初期化して返す。
	 * 
	 * @param definition
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 * @throws VFSException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
    private FileViewerFactory initFactory(VFile definition, ClassLoader loader) throws JDOMException, IOException, VFSException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Element root = DomCache.getInstance().getDocument(definition).getRootElement();
        FileViewerFactory factory = (FileViewerFactory) Class.forName(root.getAttributeValue("factory")).newInstance();
        if (loader != null) {
            factory.setLoader(loader);
        }
        factory.init(baseDir, root);
        String[] extensions = factory.getSupportedExtensions();
        for (int i = 0; extensions != null && i < extensions.length; i++) {
            registerFactory(extensions[i], factory);
        }
        return factory;
    }

    public void registerFactory(String extension, FileViewerFactory factory) {
        extensionViewerFactoryMap.put(extension, factory);
    }

    /**
	 * 廃棄
	 * 
	 */
    public void dispose(Object owner) {
        FileViewerCache cache = getFileViewerCache(owner);
        cache.disposeAll();
        viewerCacheMap.remove(owner);
        viewerSetMap.remove(owner);
    }

    /**
	 * ファイルを表示する。
	 * 
	 * @param jfd
	 * @param file
	 * @return ファイル形式に対応するファイルビューアが存在し、ビューアが開かれた場合trueを返す。
	 */
    public boolean openFileWithDefaultViewer(JFD jfd, VFile file) {
        return openFile(jfd, file, defaultViewerFactory);
    }

    private boolean openFile(JFD jfd, VFile file, FileViewerFactory factory) {
        ViewerController controller = factory.getController();
        FileViewer viewer = controller.getViewer(jfd, file, factory);
        if (viewer == null) {
            return false;
        }
        ViewerSet viewerSet = controller.getViewerSet(jfd);
        FileViewer currentViewer = viewerSet.getViewer(viewer.getPosition());
        if (currentViewer != null && currentViewer != viewer) {
            currentViewer.close();
        }
        viewer.open(file, jfd);
        viewerSet.setViewer(viewer.getPosition(), viewer);
        return true;
    }

    /**
	 * ファイルを表示する。
	 * 
	 * @param jfd
	 * @param file
	 * @return ファイル形式に対応するファイルビューアが存在し、ビューアが開かれた場合trueを返す。
	 */
    public boolean openFile(JFD jfd, VFile file) {
        FileViewerFactory factory = (FileViewerFactory) extensionViewerFactoryMap.get(file.getFileName().getExtension().toLowerCase());
        if (factory == null) {
            factory = defaultViewerFactory;
        }
        return openFile(jfd, file, factory);
    }

    public FileViewerCache getFileViewerCache(Object key) {
        FileViewerCache rtn = (FileViewerCache) viewerCacheMap.get(key);
        if (rtn == null) {
            rtn = new FileViewerCache();
            viewerCacheMap.put(key, rtn);
        }
        return rtn;
    }

    /**
	 * 一番上のファイルビューア（エスケープキーでクローズされるビューア）を取得する。
	 * 
	 * @param jfd
	 * @return
	 */
    public FileViewer getTopViewer(JFD jfd) {
        FileViewer jfdViewer = getViewerSet(jfd).getTopViewer();
        if (jfdViewer != null) {
            return jfdViewer;
        }
        FileViewer ownerViewer = getViewerSet(jfd.getJFDOwner()).getTopViewer();
        if (ownerViewer != null) {
            return ownerViewer;
        }
        return getViewerSet(Runtime.getRuntime()).getTopViewer();
    }

    /**
	 * ファイルビューアが閉じたときに呼び出される。
	 * @param viewer
	 */
    public void fileViewerClosed(FileViewer viewer) {
        ViewerSet[] sets = (ViewerSet[]) viewerSetMap.values().toArray(new ViewerSet[0]);
        for (int i = 0; i < sets.length; i++) {
            sets[i].removeViewer(viewer);
        }
    }

    /**
	 * ビューアのセットを取得する。
	 * @param key
	 * @return
	 */
    public ViewerSet getViewerSet(Object key) {
        ViewerSet rtn = (ViewerSet) viewerSetMap.get(key);
        if (rtn == null) {
            rtn = new ViewerSet();
            viewerSetMap.put(key, rtn);
        }
        return rtn;
    }
}
