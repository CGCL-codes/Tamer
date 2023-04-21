package cn.csust.net2.manager.server.dao.impl;

import org.springframework.stereotype.Service;
import cn.csust.net2.manager.server.dao.RewardDAO;
import cn.csust.net2.manager.shared.po.Reward;
import cn.csust.net2.manager.shared.util.Constant;

@Service(value = Constant.DAO_NAME_REWARD)
public class RewardDAOImpl extends BaseDAOImpl<Reward> implements RewardDAO {
}
