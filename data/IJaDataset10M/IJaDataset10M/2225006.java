package au.com.gworks.jump.app.util;

import org.javaongems.runtime.io.PathUtils;
import au.com.gworks.jump.io.PathStatus;
import au.com.gworks.jump.io.ResourceAttributes;

public final class RequestPathInfo {

    public final String revSpecifier;

    public final String namespace, context;

    public final String path;

    public final Integer revision, headRevision;

    public final int pathStatus;

    public final ResourceAttributes attributes;

    public final boolean wasRevisionSpecified;

    public final boolean isNamespaceAdmin;

    public RequestPathInfo(String revId, String ctx, String nmspace, boolean admin, String pth, boolean wasRevSpecified, Integer rev, Integer head, ResourceAttributes ra, int status) {
        revSpecifier = revId;
        namespace = nmspace;
        context = ctx;
        path = pth;
        revision = rev;
        headRevision = head;
        attributes = ra;
        pathStatus = status;
        wasRevisionSpecified = wasRevSpecified;
        isNamespaceAdmin = admin;
    }

    public long getRevisionAsAt() {
        if (attributes == null) return 0;
        return attributes.getLastModified();
    }

    public boolean isFolderPath() {
        return PathStatus.IS_FOLDER == pathStatus;
    }

    public boolean isDocumentPath() {
        return PathStatus.IS_DOCUMENT == pathStatus;
    }

    public boolean isHeadRevision() {
        return revision.equals(headRevision);
    }

    public boolean isEditableContext() {
        return isHeadRevision();
    }

    public String getExtendedContext(boolean inclRevInfo) {
        StringBuffer buff = new StringBuffer(PathUtils.FORWARD_SLASH);
        buff.append(context).append(PathUtils.FORWARD_SLASH).append(namespace);
        if (inclRevInfo && wasRevisionSpecified) appendRevToContext(buff, revision);
        return buff.toString();
    }

    public String getOrigRevisionContext(boolean inclRevInfo) {
        StringBuffer buff = new StringBuffer(PathUtils.FORWARD_SLASH);
        buff.append(context).append(PathUtils.FORWARD_SLASH).append(namespace);
        if (inclRevInfo && wasRevisionSpecified) wrapRevisionTags(buff, revSpecifier);
        return buff.toString();
    }

    private void appendRevToContext(StringBuffer buff, Integer rev) {
        wrapRevisionTags(buff, EnvironmentUtils.ENV_REV_SPECIFIER_REVISION + rev.toString());
    }

    private void wrapRevisionTags(StringBuffer buff, String spec) {
        buff.append(PathUtils.FORWARD_SLASH).append(EnvironmentUtils.ENV_REV_SPECIFIER_BEGIN).append(spec).append(EnvironmentUtils.ENV_REV_SPECIFIER_END);
    }

    public String makeBaseRevisionContext() {
        StringBuffer buff = new StringBuffer(getExtendedContext(false));
        appendRevToContext(buff, headRevision);
        buff.append(path);
        return buff.toString();
    }
}
