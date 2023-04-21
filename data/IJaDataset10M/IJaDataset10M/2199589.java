package com.yict.csms.system.service;

import java.util.List;
import java.util.Map;
import com.yict.common.entity.PageEntity;
import com.yict.common.service.ICommonService;
import com.yict.csms.system.entity.Permission;
import com.yict.csms.system.entity.UserGroupPermission;

/**
 * 
 * 
 * @author Patrick.Deng
 * 
 */
public interface IPermissionService extends ICommonService<UserGroupPermission, Long> {

    public List<UserGroupPermission> search(Map<String, Object> queryMap, PageEntity page);

    public List<Permission> findPermission();
}
