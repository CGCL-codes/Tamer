package com.techstar.dmis.service.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.lang.StringUtils;
import com.techstar.framework.dao.IBaseJdbcDao;
import com.techstar.framework.dao.model.QueryListObj;
import com.techstar.framework.utils.BeanHelper;
import com.techstar.framework.utils.SequenceCreator;
import com.techstar.dmis.dao.*;
import com.techstar.dmis.dto.DdWoutageplanDto;
import com.techstar.dmis.entity.DdWoutageplan;
import com.techstar.dmis.service.IDdWoutageplanService;
import org.springframework.dao.DataAccessException;
import com.techstar.dmis.dto.DdRepairdetailDto;
import com.techstar.dmis.entity.DdRepairdetail;
import com.techstar.dmis.dto.DdMoutageplanDto;
import com.techstar.dmis.entity.DdMoutageplan;

/** 
 * 业务对象服务接口实现类
 * @author 
 * @date
 */
public class DdWoutageplanServiceImpl implements IDdWoutageplanService {

    private IDdWoutageplanDao ddWoutageplanDao;

    private IBaseJdbcDao baseJdbcDao;

    private IDdRepairdetailDao ddRepairdetailDao;

    public void setDdRepairdetailDao(IDdRepairdetailDao ddRepairdetailDao) {
        this.ddRepairdetailDao = ddRepairdetailDao;
    }

    private IDdMoutageplanDao ddMoutageplanDao;

    public void setDdMoutageplanDao(IDdMoutageplanDao ddMoutageplanDao) {
        this.ddMoutageplanDao = ddMoutageplanDao;
    }

    public DdWoutageplanServiceImpl() {
    }

    public void addDdWoutageplan(DdWoutageplanDto dto) {
        if (StringUtils.isEmpty(dto.getFwplanno())) {
            if (StringUtils.isEmpty(dto.getFwplanno())) {
                dto.setFwplanno(new SequenceCreator().getUID());
            }
        }
        DdWoutageplan ddWoutageplan = (DdWoutageplan) BeanHelper.buildBean(DdWoutageplan.class, dto);
        ddWoutageplanDao.saveOrUpdate(ddWoutageplan);
    }

    public void saveOrUpdateDdWoutageplan(DdWoutageplanDto dto) {
        if (StringUtils.isEmpty(dto.getFwplanno())) {
            if (StringUtils.isEmpty(dto.getFwplanno())) {
                dto.setFwplanno(new SequenceCreator().getUID());
            }
        }
        DdWoutageplan ddWoutageplan = (DdWoutageplan) BeanHelper.buildBean(DdWoutageplan.class, dto);
        ddWoutageplanDao.saveOrUpdate(ddWoutageplan);
    }

    public void deleteDdWoutageplan(String ddWoutageplanId) {
        DdWoutageplan ddWoutageplan = new DdWoutageplan();
        ddWoutageplan.setFwplanno(ddWoutageplanId);
        ddWoutageplanDao.delete(ddWoutageplan);
    }

    public void deleteDdWoutageplan(List dtos) {
        List pos = new ArrayList();
        pos = (List) BeanHelper.buildBeans(DdWoutageplan.class, dtos);
        ddWoutageplanDao.deleteAll(pos);
    }

    public DdWoutageplanDto loadDdWoutageplan(String fwplanno) {
        DdWoutageplan ddWoutageplan = (DdWoutageplan) ddWoutageplanDao.findByPk(fwplanno);
        DdWoutageplanDto dto = (DdWoutageplanDto) BeanHelper.buildBean(DdWoutageplanDto.class, ddWoutageplan);
        return dto;
    }

    public QueryListObj listDdWoutageplan() {
        QueryListObj obj = ddWoutageplanDao.getQueryList();
        if (obj.getElemList() != null) {
            List dtos = (List) BeanHelper.buildBeans(DdWoutageplanDto.class, obj.getElemList());
            obj.setElemList(dtos);
        }
        return obj;
    }

    public QueryListObj listDdWoutageplanByHql(String hql) {
        QueryListObj obj = ddWoutageplanDao.getQueryListByHql(hql);
        if (obj.getElemList() != null) {
            List dtos = (List) BeanHelper.buildBeans(DdWoutageplanDto.class, obj.getElemList());
            obj.setElemList(dtos);
        }
        return obj;
    }

    public QueryListObj getDdWoutageplanByHql(String hql, int beginPage, int pageSize, String sql) {
        QueryListObj obj = ddWoutageplanDao.getQueryListByHql(hql, beginPage, pageSize);
        if (obj.getElemList() != null) {
            List dtos = (List) BeanHelper.buildBeans(DdWoutageplanDto.class, obj.getElemList());
            obj.setElemList(dtos);
        }
        List sumList = ddWoutageplanDao.getObjPropertySums(sql);
        obj.setPropertySum(sumList);
        return obj;
    }

    public IDdWoutageplanDao getDdWoutageplanDao() {
        return ddWoutageplanDao;
    }

    public void setDdWoutageplanDao(IDdWoutageplanDao ddWoutageplanDao) {
        this.ddWoutageplanDao = ddWoutageplanDao;
    }

    public void setBaseJdbcDao(IBaseJdbcDao baseJdbcDao) {
        this.baseJdbcDao = baseJdbcDao;
    }

    public void saveOrUpdateDdWoutageplan(List dtos) {
        List pos = new ArrayList();
        for (int i = 0; i < dtos.size(); i++) {
            DdWoutageplanDto ddWoutageplanDto = (DdWoutageplanDto) dtos.get(i);
            DdWoutageplan ddWoutageplan = (DdWoutageplan) BeanHelper.buildBean(DdWoutageplan.class, ddWoutageplanDto);
            pos.add(ddWoutageplan);
        }
        ddWoutageplanDao.saveOrUpdateAll(pos);
    }

    public QueryListObj listDdRepairdetail() {
        QueryListObj obj = ddRepairdetailDao.getQueryList();
        if (obj.getElemList() != null) {
            List dtos = (List) BeanHelper.buildBeans(DdRepairdetailDto.class, obj.getElemList());
            obj.setElemList(dtos);
        }
        return obj;
    }

    public void saveOrUpdateDdRepairdetail(DdRepairdetailDto dto) {
        if (StringUtils.isEmpty(dto.getFid())) {
            if (StringUtils.isEmpty(dto.getFid())) {
                dto.setFid(new SequenceCreator().getUID());
            }
        }
        DdRepairdetail ddRepairdetail = (DdRepairdetail) BeanHelper.buildBean(DdRepairdetail.class, dto);
        ddRepairdetailDao.saveOrUpdate(ddRepairdetail);
    }

    public void deleteDdRepairdetail(String fid) {
        DdRepairdetail ddRepairdetail = new DdRepairdetail();
        ddRepairdetail.setFid(fid);
        ddRepairdetailDao.delete(ddRepairdetail);
    }

    public DdRepairdetailDto loadDdRepairdetail(String fid) {
        DdRepairdetail ddRepairdetail = (DdRepairdetail) ddRepairdetailDao.findByPk(fid);
        DdRepairdetailDto dto = (DdRepairdetailDto) BeanHelper.buildBean(DdRepairdetailDto.class, ddRepairdetail);
        return dto;
    }

    public QueryListObj listDdRepairdetailByHql(String hql) {
        QueryListObj obj = ddRepairdetailDao.getQueryListByHql(hql);
        if (obj.getElemList() != null) {
            List dtos = (List) BeanHelper.buildBeans(DdRepairdetailDto.class, obj.getElemList());
            obj.setElemList(dtos);
        }
        return obj;
    }

    public void saveOrUpdateDdRepairdetail(List dtos) {
        List pos = new ArrayList();
        for (int i = 0; i < dtos.size(); i++) {
            DdRepairdetailDto ddRepairdetailDto = (DdRepairdetailDto) dtos.get(i);
            DdRepairdetail ddRepairdetail = (DdRepairdetail) BeanHelper.buildBean(DdRepairdetail.class, ddRepairdetailDto);
            pos.add(ddRepairdetail);
        }
        ddRepairdetailDao.saveOrUpdateAll(pos);
    }

    public void deleteDdRepairdetail(List dtos) {
        List pos = new ArrayList();
        pos = (List) BeanHelper.buildBeans(DdRepairdetail.class, dtos);
        ddRepairdetailDao.deleteAll(pos);
    }

    public QueryListObj listDdMoutageplan() {
        QueryListObj obj = ddMoutageplanDao.getQueryList();
        if (obj.getElemList() != null) {
            List dtos = (List) BeanHelper.buildBeans(DdMoutageplanDto.class, obj.getElemList());
            obj.setElemList(dtos);
        }
        return obj;
    }

    public void saveOrUpdateDdMoutageplan(DdMoutageplanDto dto) {
        if (StringUtils.isEmpty(dto.getFmonthplanno())) {
            if (StringUtils.isEmpty(dto.getFmonthplanno())) {
                dto.setFmonthplanno(new SequenceCreator().getUID());
            }
        }
        DdMoutageplan ddMoutageplan = (DdMoutageplan) BeanHelper.buildBean(DdMoutageplan.class, dto);
        ddMoutageplanDao.saveOrUpdate(ddMoutageplan);
    }

    public void deleteDdMoutageplan(String fmonthplanno) {
        DdMoutageplan ddMoutageplan = new DdMoutageplan();
        ddMoutageplan.setFmonthplanno(fmonthplanno);
        ddMoutageplanDao.delete(ddMoutageplan);
    }

    public DdMoutageplanDto loadDdMoutageplan(String fmonthplanno) {
        DdMoutageplan ddMoutageplan = (DdMoutageplan) ddMoutageplanDao.findByPk(fmonthplanno);
        DdMoutageplanDto dto = (DdMoutageplanDto) BeanHelper.buildBean(DdMoutageplanDto.class, ddMoutageplan);
        return dto;
    }

    public QueryListObj listDdMoutageplanByHql(String hql) {
        QueryListObj obj = ddMoutageplanDao.getQueryListByHql(hql);
        if (obj.getElemList() != null) {
            List dtos = (List) BeanHelper.buildBeans(DdMoutageplanDto.class, obj.getElemList());
            obj.setElemList(dtos);
        }
        return obj;
    }

    public void saveOrUpdateDdMoutageplan(List dtos) {
        List pos = new ArrayList();
        for (int i = 0; i < dtos.size(); i++) {
            DdMoutageplanDto ddMoutageplanDto = (DdMoutageplanDto) dtos.get(i);
            DdMoutageplan ddMoutageplan = (DdMoutageplan) BeanHelper.buildBean(DdMoutageplan.class, ddMoutageplanDto);
            pos.add(ddMoutageplan);
        }
        ddMoutageplanDao.saveOrUpdateAll(pos);
    }

    public void deleteDdMoutageplan(List dtos) {
        List pos = new ArrayList();
        pos = (List) BeanHelper.buildBeans(DdMoutageplan.class, dtos);
        ddMoutageplanDao.deleteAll(pos);
    }
}
