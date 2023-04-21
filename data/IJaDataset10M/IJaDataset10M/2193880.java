package cn.csust.net2.manager.server.dao.impl;

import org.springframework.stereotype.Service;
import cn.csust.net2.manager.server.dao.ModuleDAO;
import cn.csust.net2.manager.shared.po.Module;
import cn.csust.net2.manager.shared.util.Constant;

@Service(value = Constant.DAO_NAME_MODULE)
public class ModuleDAOImpl extends BaseDAOImpl<Module> implements ModuleDAO {
}
