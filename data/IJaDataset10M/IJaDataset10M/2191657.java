package net.community.chest.apache.ant.mvnsync.helpers;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Jan 30, 2008 2:38:50 PM
 */
public class RemoteRepository extends LocalRepository {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4447433232941937497L;

    public RemoteRepository() {
        super();
    }

    public String getUrl() {
        return getPath();
    }

    @Override
    public void setPath(String path) {
        if (haveRefId()) throw new IllegalStateException("setPath(" + path + ") already using ref-id");
        super.setPath(path);
    }

    public void setUrl(String url) {
        final int uLen = (null == url) ? 0 : url.length();
        if ((uLen > 0) && (url.charAt(uLen - 1) != '/')) throw new IllegalArgumentException("URL does not end in '/'");
        setPath(url);
    }

    private String _id;

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        if (haveRefId()) throw new IllegalStateException("setId(" + id + ") already using ref-id");
        _id = id;
    }

    private String _refId;

    public String getRefId() {
        return _refId;
    }

    protected boolean haveRefId() {
        final String rid = getRefId();
        return (rid != null) && (rid.length() > 0);
    }

    public void setRefId(String refId) {
        final String[] vals = { getUrl(), getId() };
        for (final String v : vals) {
            if ((v != null) && (v.length() > 0)) throw new IllegalStateException("setRefId(" + refId + ") other attributes already set");
        }
        _refId = refId;
    }

    public String getDependencyFilePath(final Dependency d, final String tgtFileName) {
        final String dGroup = d.getGroupId(), grpSubPath = dGroup.replace('.', '/');
        return getUrl() + grpSubPath + "/" + d.getArtifactId() + "/" + d.getVersion() + "/" + tgtFileName;
    }

    public String getDependencyJarFilePath(final Dependency d) {
        return getDependencyFilePath(d, d.getTargetJarFileName());
    }

    public String getDependencyPomFilePath(final Dependency d) {
        return getDependencyFilePath(d, d.getTargetPomFileName());
    }

    public String getDependencySourcesFilePath(final Dependency d) {
        return getDependencyFilePath(d, d.getTargetSourcesFileName());
    }
}
