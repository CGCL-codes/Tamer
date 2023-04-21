package com.kongur.network.erp.manager.uc.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eyeieye.melody.util.StringUtil;
import com.kongur.network.erp.common.Paginable;
import com.kongur.network.erp.dao.uc.CaringTacticsDao;
import com.kongur.network.erp.domain.uc.CaringTactics;
import com.kongur.network.erp.manager.uc.CaringTacticsManager;

/**
 * @author gaojf
 * @version $Id: CaringTacticsManagerImpl.java,v 0.1 2011-12-19 ����04:32:29 gaojf Exp $
 */
@Service("caringTacticsManager")
public class CaringTacticsManagerImpl implements CaringTacticsManager {

    @Autowired
    private CaringTacticsDao caringTacticsDao;

    /**
     * ���id��ùػ�����
     */
    public CaringTactics getCaringTacticsById(Long id) {
        return caringTacticsDao.getCaringTacticsById(id);
    }

    /**
     * �����ػ�����
     */
    public Long insertCaringTactics(CaringTactics caringTactics) {
        return caringTacticsDao.insertCaringTactics(caringTactics);
    }

    /**
     * �޸Ĺػ�����
     */
    public int updateCaringTactics(CaringTactics caringTactics) {
        return caringTacticsDao.updateCaringTactics(caringTactics);
    }

    /**
     * ɾ��ػ�����
     */
    public void deleteCaringTactics(Long id) {
        caringTacticsDao.deleteCaringTactics(id);
    }

    /**
     * ���������ùػ������б�
     */
    public List<CaringTactics> getListByCondition(Long sId) {
        return getListByCondition(sId, "");
    }

    /**
     * ���������ùػ������б�
     */
    public List<CaringTactics> getListByCondition(Long sId, String name) {
        return getListByCondition(sId, name, null);
    }

    /**
     * ���������ùػ������б�
     */
    public List<CaringTactics> getListByCondition(Long sId, String name, Date dealTime) {
        return getListByCondition(sId, name, dealTime, 0);
    }

    /**
     * ���������ùػ������б�
     */
    public List<CaringTactics> getListByCondition(Long sId, String name, Date dealTime, int sendType) {
        return getListByCondition(sId, name, dealTime, sendType, 0);
    }

    /**
     * ���������ùػ������б�
     */
    public List<CaringTactics> getListByCondition(Long sId, String name, Date dealTime, int sendType, int status) {
        CaringTactics caringTactics = new CaringTactics();
        caringTactics.setsId(sId);
        caringTactics.setName(name);
        caringTactics.setDealTime(dealTime);
        caringTactics.setSendType(sendType);
        caringTactics.setStatus(status);
        return caringTacticsDao.getListByCondition(caringTactics);
    }

    /**
     * ���������ùػ�����
     */
    public CaringTactics getObjByCondition(Long sId, String name) {
        if (sId == null || StringUtil.isBlank(name)) return null;
        CaringTactics caringTactics = new CaringTactics();
        caringTactics.setsId(sId);
        caringTactics.setName(name);
        return caringTacticsDao.getObjByCondition(caringTactics);
    }

    /**
     * ��ѯ�ػ����� - ��ҳ
     */
    public Paginable<CaringTactics> getPaginatedCaringTactics(Paginable<CaringTactics> page) {
        return caringTacticsDao.getPaginatedCaringTactics(page);
    }
}
