package whf.framework.security.service;

import java.util.List;
import java.util.Set;
import whf.framework.exception.FindException;
import whf.framework.security.entity.Permission;
import whf.framework.security.entity.Role;

/**
 * @author wanghaifeng
 * @email king@126.com
 * @modify 2006-05-31
 */
public interface PermissionService extends whf.framework.service.Service<Permission> {

    /**
	 * 通过对象和操作类型查找
	 * @modify wanghaifeng Aug 28, 2006 11:03:03 PM
	 * @param target
	 * @param operation
	 * @return
	 * @throws FindException
	 */
    public Permission findByTargetOperation(String target, String operation) throws FindException;

    /**
	 * @modify wanghaifeng Sep 2, 2006 10:28:54 PM
	 * @param target
	 * @return
	 * @throws FindException
	 */
    public List<Permission> findByTarget(String target) throws FindException;

    /**
	 * 
	 * @modify wanghaifeng Mar 10, 2007 6:43:44 PM
	 * @param role
	 * @return
	 * @throws FindException
	 */
    public Set<Permission> findByRole(Role role) throws FindException;
}
