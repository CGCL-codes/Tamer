package com.javaeye.common.service;

import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.javaeye.common.dao.ModuleDAO;
import com.javaeye.common.dao.UseLogDAO;
import com.javaeye.common.dto.Module;
import com.javaeye.common.dto.UseLog;
import com.javaeye.common.dto.User;
import com.javaeye.common.util.PermissionsHelper;

public class CheckRescService implements ICheckRescService {

    protected Log log = LogFactory.getLog(CheckRescService.class);

    private ModuleDAO moduleDao;

    private UseLogDAO useLogDao;

    public void setModuleDao(ModuleDAO moduleDao) {
        this.moduleDao = moduleDao;
    }

    public void setUseLogDao(UseLogDAO useLogDao) {
        this.useLogDao = useLogDao;
    }

    public boolean checkResc(User user, String actionName) {
        Module module = moduleDao.getModuleCountByAction(actionName);
        if (module == null) {
            UseLog log = new UseLog();
            log.setUserId(user.getId());
            log.setUserName(user.getUserName());
            log.setDateTime(new Date());
            log.setAction(actionName);
            log.setDetail(actionName);
            useLogDao.saveUseLog(log);
            return true;
        }
        boolean hasPermission = PermissionsHelper.checkHasPermissions(user, module);
        if (hasPermission) {
            UseLog log = new UseLog();
            log.setUserId(user.getId());
            log.setUserName(user.getUserName());
            log.setDateTime(new Date());
            log.setAction(module.getUrl());
            log.setDetail(module.getName());
            useLogDao.saveUseLog(log);
        }
        return hasPermission;
    }
}
