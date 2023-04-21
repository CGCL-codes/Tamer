package org.zkoss.eclipse.setting.zklib.archive;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Ian Tsai
 *
 */
public class ZkDemoDistributuionArchive extends AbstractInputFileNameArchive {

    private ZkJavaEEWebArchive webArchive;

    /**
	 * 
	 * @param file
	 */
    public ZkDemoDistributuionArchive(IArchiveEntry file) {
        super(file);
        IArchiveEntry zkdemo = ArchiveUtil.bfsSearchFirstNodeByName("zkdemo.war", node);
        if (zkdemo != null && ArchiveUtil.isZkWebArchive(zkdemo)) webArchive = new ZkJavaEEWebArchive(zkdemo);
        zkdemo = ArchiveUtil.bfsSearchFirstNodeByName("zkdemo-all.war", node);
        if (zkdemo != null && ArchiveUtil.isZkWebArchive(zkdemo)) webArchive = new ZkJavaEEWebArchive(zkdemo);
        zkdemo = ArchiveUtil.bfsSearchFirstNodeByName("zkdemo", node);
        if (zkdemo != null && ArchiveUtil.isZkWebArchive(zkdemo)) webArchive = new ZkJavaEEWebArchive(zkdemo);
    }

    public void browseJavaSources(IContentVisitor visitor) {
        IArchiveEntry demoFolder = ArchiveUtil.bfsSearchFirstNodeByName("zkdemo", node);
        IArchiveEntry src = demoFolder.getChild("src");
        recursiveVisiting("", src.getChildren(), visitor);
    }

    public ArchiveType getType() {
        return ArchiveType.getInstance(ArchiveType.ZK_DEMO_DISTRIBUTION);
    }

    public void browseGeneralContent(IContentVisitor visitor) {
        if (webArchive == null) return;
        webArchive.browseGeneralContent(visitor);
    }

    public void browseJavaLibrarys(IContentVisitor visitor) {
        if (webArchive == null) return;
        webArchive.browseJavaLibrarys(visitor);
    }

    public void browseProtectedContent(IContentVisitor visitor) {
        if (webArchive == null) return;
        webArchive.browseProtectedContent(visitor);
    }

    public IArchiveEntry getJavaEEWebSetting() {
        if (webArchive == null) return null;
        return webArchive.getJavaEEWebSetting();
    }

    public IArchiveEntry getZkSetting() {
        if (webArchive == null) return null;
        return webArchive.getZkSetting();
    }
}
