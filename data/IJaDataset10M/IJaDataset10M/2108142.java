package jp.co.chojo.db.server.impl;

import java.util.List;
import jp.co.chojo.db.ado.HumanDao;
import jp.co.chojo.db.bean.HumanBaseInfo;
import jp.co.chojo.db.server.HumanManager;

public class HumanManagerImpl implements HumanManager {

    private HumanDao humanDao;

    public HumanDao getHumanDao() {
        return humanDao;
    }

    public void setHumanDao(HumanDao humanDao) {
        this.humanDao = humanDao;
    }

    public HumanBaseInfo getHumanBaseInfo(int humanId) {
        return humanDao.getHumanBaseInfo(humanId);
    }

    public int insertHumanBaseInfo(HumanBaseInfo human) {
        return humanDao.insertHumanBaseInfo(human);
    }

    public void updateHumanBaseInfo(HumanBaseInfo human) {
        humanDao.updateHumanBaseInfo(human);
    }

    public List<HumanBaseInfo> getAllHumanBaseInfo() {
        return humanDao.getAllHumanBaseInfo();
    }

    public int checkHumanBaseInfo(HumanBaseInfo human) {
        return humanDao.checkHumanBaseInfo(human);
    }
}
