package whf.framework.security.helper;

import java.util.List;
import whf.framework.entity.decorator.EntityWrapper;
import whf.framework.log.Log;
import whf.framework.log.LogFactory;
import whf.framework.security.entity.Permission;
import whf.framework.security.service.PermissionService;
import whf.framework.security.service.PermissionServiceImp;
import whf.framework.util.Utils;

/**
 * 作为Permission粉状Object对象用
 * @author wanghaifeng
 * @create Sep 3, 2006 10:23:57 PM
 * 
 */
public class PermissionWrapper extends EntityWrapper {

    private static Log log = LogFactory.getLog(PermissionWrapper.class);

    private PermissionService service;

    public PermissionWrapper(whf.framework.meta.entity.Object input) {
        super(input);
    }

    /**
	 * @return Returns the meta.
	 */
    public final whf.framework.meta.entity.Object getMeta() {
        return (whf.framework.meta.entity.Object) bo;
    }

    public List<Permission> getPermissions() {
        try {
            if (service == null) {
                service = PermissionServiceImp.getPermissionService();
            }
            return service.findByTarget(getMeta().getBoClassName());
        } catch (Exception e) {
            log.error(this, e);
            return Utils.newArrayList();
        }
    }
}
