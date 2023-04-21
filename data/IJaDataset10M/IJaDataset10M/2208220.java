package cn.myapps.core.permission.dao;

import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.permission.ejb.PermissionVO;

public interface PermissionDAO extends IDesignTimeDAO {

    public PermissionVO findByResouceAndUser(String resourceId, String userId) throws Exception;
}
