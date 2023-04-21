package org.zkoss.eclipse.setting.zklib.archive;

import java.util.LinkedList;

/**
 * @author Ian Tsai
 *
 */
public class ZkJavaEEWebArchive extends AbstractInputFileNameArchive {

    /**
	 * 
	 * @param file
	 */
    public ZkJavaEEWebArchive(IArchiveEntry file) {
        super(file);
    }

    public void browseGeneralContent(IContentVisitor visitor) {
        LinkedList<IArchiveEntry> list = new LinkedList<IArchiveEntry>(node.getChildren());
        list.remove(node.getChild("WEB-INF"));
        recursiveVisiting("", list, visitor);
    }

    public void browseJavaLibrarys(IContentVisitor visitor) {
        IArchiveEntry libs = ArchiveUtil.getNodeByPath("WEB-INF/lib/", node);
        recursiveVisiting("WEB-INF/lib/", libs.getChildren(), visitor);
    }

    public void browseJavaSources(IContentVisitor visitor) {
    }

    public void browseProtectedContent(IContentVisitor visitor) {
        recursiveVisiting("WEB-INF/", node.getChild("WEB-INF").getChildren(), visitor);
    }

    public IArchiveEntry getJavaEEWebSetting() {
        return ArchiveUtil.getNodeByPath("WEB-INF/web.xml", node);
    }

    public IArchiveEntry getZkSetting() {
        return ArchiveUtil.getNodeByPath("WEB-INF/zk.xml", node);
    }

    public ArchiveType getType() {
        return ArchiveType.getInstance(ArchiveType.ZK_WEB_ARCHIVE);
    }
}
