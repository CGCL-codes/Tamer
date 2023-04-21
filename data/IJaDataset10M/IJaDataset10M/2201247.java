package cn.myapps.core.logger.dao;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.base.dao.IDesignTimeDAO;
import cn.myapps.core.logger.ejb.LogVO;
import cn.myapps.core.user.action.WebUser;

public interface LogDAO extends IDesignTimeDAO<LogVO> {

    public DataPackage<LogVO> queryLog(ParamsTable params, WebUser user) throws Exception;

    public DataPackage<LogVO> queryLog(ParamsTable params, WebUser user, String domain) throws Exception;
}
