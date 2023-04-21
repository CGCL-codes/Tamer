package org.taffe.pms.service.system;

import org.taffe.pms.model.system.SysUser;
import org.taffe.pms.service.BaseManager;

/**
 * @author Shane
 * 
 */
public interface SysUserManager extends BaseManager<SysUser, Long> {

    public SysUser signup(Long userNO, String password, String token);
}
