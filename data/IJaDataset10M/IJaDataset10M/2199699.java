package au.com.gworks.jump.app.wiki.server;

import au.com.gworks.jump.app.wiki.client.service.WikiRpc;
import au.com.gworks.jump.io.ResourceAttributes;

public class AbstractBeanMapper {

    public static WikiRpc.ResourceAttrInfo map(ResourceAttributes attr) {
        WikiRpc.ResourceAttrInfo ret = new WikiRpc.ResourceAttrInfo();
        ret.name = attr.getName();
        ret.parentPath = attr.getParentPath();
        ret.revision = attr.getRevision();
        ret.lastModified = attr.getLastModified();
        ret.lastUpdater = attr.getLastUpdater();
        ret.lastChangedRevision = attr.getLastChangedRevision();
        ret.size = attr.getSize();
        ret.uuid = attr.getUuid();
        ret.properties.putAll(attr.getProperties());
        return ret;
    }
}
