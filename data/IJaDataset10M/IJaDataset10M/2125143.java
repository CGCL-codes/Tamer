package cn.vlabs.duckling.vwb.services.auth;

import java.util.List;
import cn.vlabs.duckling.vwb.services.auth.provider.data.ResourceMetaPriv;
import cn.vlabs.duckling.vwb.services.auth.provider.data.ResourcePermission;
import cn.vlabs.duckling.vwb.services.auth.provider.data.ResourcePrincipal;

/**
 * Introduction Here.
 * @date 2010-2-25
 * @author Fred Zhang (fred@cnic.cn)
 */
public interface AccessControlServiceInterface {

    public boolean hasAccess(ResourcePrincipal principal, ResourcePermission permission);

    public boolean hasAccess(ResourcePrincipal[] principals, ResourcePermission permission);

    public void setResourcePrivs(List<ResourceMetaPriv> privs);

    public List<ResourceMetaPriv> getResourcePriv(int resourceId);
}
