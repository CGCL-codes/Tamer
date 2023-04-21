package com.kongur.network.erp.manager.uc;

import java.util.Date;
import java.util.List;
import com.kongur.network.erp.common.Paginable;
import com.kongur.network.erp.domain.uc.CaringTactics;

/**
 * @author gaojf
 * @version $Id: CaringTacticsManager.java,v 0.1 2011-12-19 ����04:31:46 gaojf Exp $
 */
public interface CaringTacticsManager {

    /**
     * ���id��ùػ�����
     * @param id
     * @return
     */
    public CaringTactics getCaringTacticsById(Long id);

    /**
     * �����ػ�����
     * @param caringTactics
     * @return
     */
    public Long insertCaringTactics(CaringTactics caringTactics);

    /**
     * �޸Ĺػ�����
     * @param caringTactics
     * @return
     */
    public int updateCaringTactics(CaringTactics caringTactics);

    /**
     * ɾ��ػ�����
     * @param id
     */
    public void deleteCaringTactics(Long id);

    /**
     * ���������ùػ������б�
     * @param sId
     * @return
     */
    public List<CaringTactics> getListByCondition(Long sId);

    /**
     * ���������ùػ������б�
     * @param sId
     * @param name
     * @return
     */
    public List<CaringTactics> getListByCondition(Long sId, String name);

    /**
     * ���������ùػ������б�
     * @param sId
     * @param name
     * @param dealTime
     * @return
     */
    public List<CaringTactics> getListByCondition(Long sId, String name, Date dealTime);

    /**
     * ���������ùػ������б�
     * @param sId
     * @param name
     * @param dealTime
     * @param sendType
     * @return
     */
    public List<CaringTactics> getListByCondition(Long sId, String name, Date dealTime, int sendType);

    /**
     * ���������ùػ������б�
     * @param sId
     * @param name
     * @param dealTime
     * @param sendType
     * @param status
     * @return
     */
    public List<CaringTactics> getListByCondition(Long sId, String name, Date dealTime, int sendType, int status);

    /**
     * ���������ùػ�����
     * @param sId
     * @param name
     * @return
     */
    public CaringTactics getObjByCondition(Long sId, String name);

    /**
     * ��ѯ�ػ����� - ��ҳ
     * @param page
     * @return
     */
    public Paginable<CaringTactics> getPaginatedCaringTactics(Paginable<CaringTactics> page);
}
