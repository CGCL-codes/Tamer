package cn.csust.net2.manager.server.dao.impl;

import org.springframework.stereotype.Service;
import cn.csust.net2.manager.server.dao.InnerMailDAO;
import cn.csust.net2.manager.shared.po.InnerMail;
import cn.csust.net2.manager.shared.util.Constant;

@Service(value = Constant.DAO_NAME_INNERMAIL)
public class InnerMailDAOImpl extends BaseDAOImpl<InnerMail> implements InnerMailDAO {
}
