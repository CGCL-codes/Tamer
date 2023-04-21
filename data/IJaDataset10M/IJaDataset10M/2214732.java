package cn.csust.net2.manager.server.dao.impl;

import org.springframework.stereotype.Service;
import cn.csust.net2.manager.server.dao.ParameterDAO;
import cn.csust.net2.manager.shared.po.Parameter;
import cn.csust.net2.manager.shared.util.Constant;

@Service(value = Constant.DAO_NAME_PARAMETER)
public class ParameterDAOImpl extends BaseDAOImpl<Parameter> implements ParameterDAO {
}
