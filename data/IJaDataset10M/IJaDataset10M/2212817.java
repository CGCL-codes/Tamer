package com.yict.csms.resourceplan.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yict.common.entity.PageEntity;
import com.yict.common.service.impl.BaseServiceImpl;
import com.yict.csms.resourceplan.dao.IBoxContractorDao;
import com.yict.csms.resourceplan.dao.IBoxContractorLineDao;
import com.yict.csms.resourceplan.dao.IBoxDao;
import com.yict.csms.resourceplan.dao.IBoxTempDao;
import com.yict.csms.resourceplan.entity.Box;
import com.yict.csms.resourceplan.entity.BoxContractor;
import com.yict.csms.resourceplan.entity.BoxContractorLine;
import com.yict.csms.resourceplan.entity.BoxTemp;
import com.yict.csms.resourceplan.service.IBoxService;
import com.yict.csms.system.util.DictionaryUtils;

@Service("boxService")
public class BoxServiceImpl extends BaseServiceImpl<Box, Long, IBoxDao> implements IBoxService {

    @Resource(name = "boxContractorDao")
    private IBoxContractorDao boxContractorDao;

    @Resource(name = "boxContractorLineDao")
    private IBoxContractorLineDao boxContractorLineDao;

    @Resource(name = "boxTempDao")
    private IBoxTempDao boxTempDao;

    public List<Box> search(Map<String, Object> queryMap, PageEntity page) {
        List<Box> list = new ArrayList<Box>();
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder("from Box t where 1=1 ");
        String boxNum = (String) queryMap.get("boxNum");
        if (boxNum != null && boxNum.length() > 0) {
            map.put("boxNum", boxNum.toLowerCase());
            hql.append(" and lower(t.boxNum) like :boxNum");
        }
        String shipCompanyCode = (String) queryMap.get("shipCompanyCode");
        if (shipCompanyCode != null && shipCompanyCode.length() > 0) {
            map.put("shipCompanyCode", shipCompanyCode);
            hql.append(" and t.shipCompanyCode.dictid = :shipCompanyCode");
        }
        String yardPosition = (String) queryMap.get("yardPosition");
        if (yardPosition != null && yardPosition.length() > 0) {
            map.put("yardPosition", yardPosition.toLowerCase());
            hql.append(" and lower(t.yardPosition) like :yardPosition");
        }
        Long profeId = (Long) queryMap.get("profeId");
        if (profeId != null) {
            map.put("profeId", profeId);
            hql.append(" and t.profeId.profeid in (:profeId)");
        }
        String boxShape = (String) queryMap.get("boxShape");
        if (boxShape != null && boxShape.length() > 0) {
            map.put("boxShape", boxShape);
            hql.append(" and t.boxShape like :boxShape");
        }
        String getOrderNum = (String) queryMap.get("getOrderNum");
        if (getOrderNum != null && getOrderNum.length() > 0) {
            map.put("getOrderNum", getOrderNum.toLowerCase());
            hql.append(" and lower(t.getOrderNum) like :getOrderNum");
        }
        String importId = (String) queryMap.get("importData");
        if (importId != null && importId.length() > 0) {
            map.put("importId", "%" + importId + "%");
            hql.append(" and t.importId like :importId");
        }
        int count = this.getBaseDao().queryCount("select count(*) " + hql.toString(), map);
        page.setTotalRecord(count);
        if (count > 0) {
            StringBuilder orderString = new StringBuilder("");
            if (page.getField() != null && !"".equals(page.getField()) && page.getOrder() != null && !"".equals(page.getOrder())) {
                if (page.getField().equals("shipCompanyName")) {
                    orderString.append("shipCompanyCode " + page.getOrder());
                } else if (page.getField().equals("profeName")) {
                    orderString.append("profeId " + page.getOrder());
                } else {
                    orderString.append(page.getField() + page.getOrder());
                }
                hql.append(" order by ").append(orderString);
            } else {
                hql.append(" order by t.boxNum");
            }
            list = this.getBaseDao().list(hql.toString(), map, (page.getToPage() - 1) * page.getPageSize(), page.getPageSize());
        }
        return list;
    }

    @Resource(name = "boxDao")
    public void setBaseDao(IBoxDao baseDao) {
        super.setBaseDao(baseDao);
    }

    public String getMaxWorkNo(Date date) {
        return boxContractorDao.getMaxWorkNo(date);
    }

    @Override
    public String getMaxImportId(String date) {
        return this.getBaseDao().getMaxImportId(date);
    }

    @Transactional(readOnly = false)
    @Override
    public boolean save(BoxContractor boxContractor, List<BoxContractorLine> boxContractorLines, List<Long> ids) throws Exception {
        boolean bool = false;
        bool = boxContractorDao.save(boxContractor);
        Long bcId = boxContractorDao.fingByWorkNo(boxContractor.getWorkNo()).getBcId();
        List<BoxContractorLine> boxContractorLiness = new ArrayList<BoxContractorLine>();
        for (BoxContractorLine boxContractorLine : boxContractorLines) {
            boxContractorLine.setBcId(bcId);
            boxContractorLiness.add(boxContractorLine);
        }
        bool = boxContractorLineDao.save(boxContractorLiness);
        bool = this.getBaseDao().remove(ids);
        return bool;
    }

    @Transactional(readOnly = false)
    @Override
    public boolean save(List<BoxTemp> boxTemps) throws Exception {
        this.getBaseDao().deleteTemp();
        return boxTempDao.save(boxTemps);
    }

    @Override
    public boolean findBoxByBoxNum(String boxNum) {
        return this.getBaseDao().findBoxByBoxNum(boxNum);
    }

    @Override
    public boolean findBoxCLineByBoxNum(String boxNum) {
        BoxContractorLine boxContractorLine = boxContractorLineDao.findByBoxNum(boxNum);
        if (boxContractorLine == null) {
            return false;
        }
        return true;
    }

    @Transactional(readOnly = false)
    @Override
    public boolean trans() {
        this.getBaseDao().trans();
        return false;
    }

    @Override
    public String getWorkNoByBoxNum(String boxNum) {
        return boxContractorDao.fingWorkNoByBoxNum(boxNum).getWorkNo();
    }

    @Transactional(readOnly = false)
    @Override
    public void deleteTemp() {
        this.getBaseDao().deleteTemp();
    }

    @Override
    public String findWorkNoByBoxNum(String boxNum) {
        String result = "";
        BoxContractorLine boxContractorLine = new BoxContractorLine();
        boxContractorLine = boxContractorLineDao.findByBoxNum(boxNum);
        if (boxContractorLine == null) {
            return result;
        }
        BoxContractor boxContractor = new BoxContractor();
        boxContractor = boxContractorDao.findbyId(boxContractorLine.getBcId());
        if (boxContractor == null) {
            return result;
        }
        if (boxContractor.getStatus() == null) {
            return result;
        }
        if (boxContractor.getStatus().getDatacode() != null && !boxContractor.getStatus().getDatacode().equals(DictionaryUtils.BOX_STATUS_3)) {
            return boxContractor.getWorkNo();
        }
        return result;
    }
}
