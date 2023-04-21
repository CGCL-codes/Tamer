package cn.jsprun.service.logs;

import java.util.List;
import cn.jsprun.dao.RewardlogDao;
import cn.jsprun.domain.Rewardlog;
import cn.jsprun.utils.BeanFactory;

public class RewardlogService {

    public boolean deleteRewardlogByTid(int tid) {
        RewardlogDao rewardDao = (RewardlogDao) BeanFactory.getBean("rewardlogDao");
        return rewardDao.deleteRewardlogByTid(tid);
    }

    public boolean insertRewardlog(Rewardlog rewardlog) {
        RewardlogDao rewardDao = (RewardlogDao) BeanFactory.getBean("rewardlogDao");
        return rewardDao.insertRewardlog(rewardlog);
    }

    public List<Rewardlog> findRewardlogByHql(String hql, int start, int maxrow) {
        RewardlogDao rewardDao = (RewardlogDao) BeanFactory.getBean("rewardlogDao");
        return rewardDao.findRewardlogByHql(hql, start, maxrow);
    }

    public int findRewardlogCountByHql(String hql) {
        RewardlogDao rewardDao = (RewardlogDao) BeanFactory.getBean("rewardlogDao");
        return rewardDao.findRewardlogCountByHql(hql);
    }
}
