package cn.vlabs.duckling.vwb.services.auth.impl;

import java.util.List;
import cn.vlabs.duckling.vwb.services.auth.AccessControlServiceInterface;
import cn.vlabs.duckling.vwb.services.auth.provider.data.ResourceMetaPriv;
import cn.vlabs.duckling.vwb.services.auth.provider.data.ResourcePermission;
import cn.vlabs.duckling.vwb.services.auth.provider.data.ResourcePrincipal;

/**
 * Introduction Here.
 * @date 2010-2-25
 * @author Fred Zhang (fred@cnic.cn)
 */
public class AccessControlServiceImpl implements AccessControlServiceInterface {

    public List<ResourceMetaPriv> getResourcePriv(int resourceId) {
        return null;
    }

    public boolean hasAccess(ResourcePrincipal principal, ResourcePermission permission) {
        return false;
    }

    public boolean hasAccess(ResourcePrincipal[] principals, ResourcePermission permission) {
        return false;
    }

    public void setResourcePrivs(List<ResourceMetaPriv> privs) {
    }
}
