package com.techstar.dmis.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import com.techstar.dmis.common.DispatchConstants;
import com.techstar.dmis.dao.IDdEnsuringpsmanageDao;
import com.techstar.dmis.dto.DdEnsuringpsmanageDto;
import com.techstar.dmis.entity.DdEnsuringpsmanage;
import com.techstar.dmis.entity.HandleHistory;
import com.techstar.dmis.helper.dto.WorkflowHandleDto;
import com.techstar.dmis.service.IDdEnsuringpsmanageService;
import com.techstar.dmis.service.workflow.IDdEnsuringpsmanageWFService;
import com.techstar.dmis.util.DateUtil;
import com.techstar.framework.dao.IBaseJdbcDao;
import com.techstar.framework.dao.model.QueryListObj;
import com.techstar.framework.utils.BeanHelper;
import com.techstar.framework.utils.SequenceCreator;

/**
 * 业务对象服务接口实现类
 * 
 * @author
 * @date
 */
public class DdEnsuringpsmanageServiceImpl implements IDdEnsuringpsmanageService {

    private IDdEnsuringpsmanageDao ddEnsuringpsmanageDao;

    private IBaseJdbcDao baseJdbcDao;

    private IDdEnsuringpsmanageWFService ddEnsuringpsmanageWFService;

    public DdEnsuringpsmanageServiceImpl() {
    }

    public void addDdEnsuringpsmanage(DdEnsuringpsmanageDto dto) {
        if (StringUtils.isEmpty(dto.getFid())) {
            if (StringUtils.isEmpty(dto.getFid())) {
                dto.setFid(new SequenceCreator().getUID());
            }
        }
        DdEnsuringpsmanage ddEnsuringpsmanage = (DdEnsuringpsmanage) BeanHelper.buildBean(DdEnsuringpsmanage.class, dto);
        ddEnsuringpsmanageDao.saveOrUpdate(ddEnsuringpsmanage);
    }

    public void saveOrUpdateDdEnsuringpsmanage(DdEnsuringpsmanageDto dto) {
        if (StringUtils.isEmpty(dto.getFid())) {
            if (StringUtils.isEmpty(dto.getFid())) {
                dto.setFid(new SequenceCreator().getUID());
            }
        }
        DdEnsuringpsmanage ddEnsuringpsmanage = (DdEnsuringpsmanage) BeanHelper.buildBean(DdEnsuringpsmanage.class, dto);
        ddEnsuringpsmanageDao.saveOrUpdate(ddEnsuringpsmanage);
    }

    public void deleteDdEnsuringpsmanage(String ddEnsuringpsmanageId, int version) {
        DdEnsuringpsmanage ddEnsuringpsmanage = new DdEnsuringpsmanage();
        ddEnsuringpsmanage.setFid(ddEnsuringpsmanageId);
        ddEnsuringpsmanage.setVersion(version);
        ddEnsuringpsmanageDao.delete(ddEnsuringpsmanage);
    }

    public DdEnsuringpsmanageDto loadDdEnsuringpsmanage(String fid) {
        DdEnsuringpsmanage ddEnsuringpsmanage = (DdEnsuringpsmanage) ddEnsuringpsmanageDao.findByPk(fid);
        DdEnsuringpsmanageDto dto = (DdEnsuringpsmanageDto) BeanHelper.buildBean(DdEnsuringpsmanageDto.class, ddEnsuringpsmanage);
        return dto;
    }

    public QueryListObj listDdEnsuringpsmanage() {
        QueryListObj obj = ddEnsuringpsmanageDao.getQueryList();
        if (obj.getElemList() != null) {
            List dtos = (List) BeanHelper.buildBeans(DdEnsuringpsmanageDto.class, obj.getElemList());
            obj.setElemList(dtos);
        }
        return obj;
    }

    public QueryListObj listDdEnsuringpsmanageByHql(String hql) {
        QueryListObj obj = ddEnsuringpsmanageDao.getQueryListByHql(hql);
        if (obj.getElemList() != null) {
            List dtos = (List) BeanHelper.buildBeans(DdEnsuringpsmanage.class, obj.getElemList());
            obj.setElemList(dtos);
        }
        return obj;
    }

    public QueryListObj getDdEnsuringpsmanageByHql(String hql, int beginPage, int pageSize, String sql) {
        QueryListObj obj = ddEnsuringpsmanageDao.getQueryListByHql(hql, beginPage, pageSize);
        if (obj.getElemList() != null) {
            List dtos = (List) BeanHelper.buildBeans(DdEnsuringpsmanage.class, obj.getElemList());
            obj.setElemList(dtos);
        }
        List sumList = ddEnsuringpsmanageDao.getObjPropertySums(sql);
        obj.setPropertySum(sumList);
        return obj;
    }

    public IDdEnsuringpsmanageDao getDdEnsuringpsmanageDao() {
        return ddEnsuringpsmanageDao;
    }

    public void setDdEnsuringpsmanageDao(IDdEnsuringpsmanageDao ddEnsuringpsmanageDao) {
        this.ddEnsuringpsmanageDao = ddEnsuringpsmanageDao;
    }

    public void setBaseJdbcDao(IBaseJdbcDao baseJdbcDao) {
        this.baseJdbcDao = baseJdbcDao;
    }

    public void saveOrUpdateDdEnsuringpsmanage(List dtos) {
        List pos = new ArrayList();
        for (int i = 0; i < dtos.size(); i++) {
            DdEnsuringpsmanageDto ddEnsuringpsmanageDto = (DdEnsuringpsmanageDto) dtos.get(i);
            DdEnsuringpsmanage ddEnsuringpsmanage = (DdEnsuringpsmanage) BeanHelper.buildBean(DdEnsuringpsmanage.class, ddEnsuringpsmanageDto);
            pos.add(ddEnsuringpsmanage);
        }
        ddEnsuringpsmanageDao.saveOrUpdateAll(pos);
    }

    /** ***************************工作流服务************************** */
    public void appReback(WorkflowHandleDto dto) throws DataAccessException {
        int ireturn = ddEnsuringpsmanageWFService.appReback(dto);
    }

    public void approve(WorkflowHandleDto dto) throws DataAccessException {
        int ireturn = ddEnsuringpsmanageWFService.approve(dto);
        DdEnsuringpsmanage ddEnsuringpsmanage = ddEnsuringpsmanageDao.findByPk(dto.getBusId());
        ddEnsuringpsmanage.setFstatus(DispatchConstants.DdEnsuringpsmanage_WORKFLOW_CITY_STATUS_SENT);
        ddEnsuringpsmanage.setFexcutestatus(DispatchConstants.DdEnsuringpsmanage_BUSINESS_CITY_APPROVED);
        ddEnsuringpsmanageDao.saveOrUpdate(ddEnsuringpsmanage);
    }

    public void countersign(WorkflowHandleDto dto) throws DataAccessException {
        int ireturn = ddEnsuringpsmanageWFService.countersign(dto);
        if (ireturn == 1) {
            DdEnsuringpsmanage ddEnsuringpsmanage = ddEnsuringpsmanageDao.findByPk(dto.getBusId());
            ddEnsuringpsmanage.setFstatus(DispatchConstants.DdEnsuringpsmanage_WORKFLOW_CITY_STATUS_APPROVE);
            ddEnsuringpsmanage.setFexcutestatus(DispatchConstants.DdEnsuringpsmanage_BUSINESS_CITY_APPROVING);
            ddEnsuringpsmanageDao.saveOrUpdate(ddEnsuringpsmanage);
        }
    }

    public void execute(WorkflowHandleDto dto) throws DataAccessException {
        int ireturn = ddEnsuringpsmanageWFService.execute(dto);
        DdEnsuringpsmanage ddEnsuringpsmanage = ddEnsuringpsmanageDao.findByPk(dto.getBusId());
        ddEnsuringpsmanage.setFstatus(DispatchConstants.DdEnsuringpsmanage_WORKFLOW_CITY_STATUS_KEEPON);
        ddEnsuringpsmanage.setFexcutestatus(DispatchConstants.DdEnsuringpsmanage_BUSINESS_CITY_EXECUTED);
        ddEnsuringpsmanageDao.saveOrUpdate(ddEnsuringpsmanage);
    }

    public void keepon(WorkflowHandleDto dto) throws DataAccessException {
        int ireturn = ddEnsuringpsmanageWFService.keepon(dto);
        DdEnsuringpsmanage ddEnsuringpsmanage = ddEnsuringpsmanageDao.findByPk(dto.getBusId());
        ddEnsuringpsmanage.setFstatus(DispatchConstants.DdEnsuringpsmanage_WORKFLOW_CITY_STATUS_OVER);
        ddEnsuringpsmanage.setFexcutestatus(DispatchConstants.DdEnsuringpsmanage_BUSINESS_CITY_KEEPONED);
        ddEnsuringpsmanageDao.saveOrUpdate(ddEnsuringpsmanage);
    }

    public void modify(WorkflowHandleDto dto) throws DataAccessException {
        int ireturn = ddEnsuringpsmanageWFService.modify(dto);
        DdEnsuringpsmanage ddEnsuringpsmanage = ddEnsuringpsmanageDao.findByPk(dto.getBusId());
        ddEnsuringpsmanage.setFstatus(DispatchConstants.DdEnsuringpsmanage_WORKFLOW_CITY_STATUS_COURSIGN);
        ddEnsuringpsmanage.setFexcutestatus(DispatchConstants.DdEnsuringpsmanage_BUSINESS_CITY_COURSIGNING);
        ddEnsuringpsmanageDao.saveOrUpdate(ddEnsuringpsmanage);
    }

    public void sent(WorkflowHandleDto dto) throws DataAccessException {
        int ireturn = ddEnsuringpsmanageWFService.sent(dto);
        DdEnsuringpsmanage ddEnsuringpsmanage = ddEnsuringpsmanageDao.findByPk(dto.getBusId());
        ddEnsuringpsmanage.setFstatus(DispatchConstants.DdEnsuringpsmanage_WORKFLOW_CITY_STATUS_EXEC);
        ddEnsuringpsmanage.setFexcutestatus(DispatchConstants.DdEnsuringpsmanage_BUSINESS_CITY_SENTED);
        ddEnsuringpsmanageDao.saveOrUpdate(ddEnsuringpsmanage);
    }

    public void start(WorkflowHandleDto dto) throws DataAccessException {
        int ireturn = ddEnsuringpsmanageWFService.start(dto);
        DdEnsuringpsmanage ddEnsuringpsmanage = ddEnsuringpsmanageDao.findByPk(dto.getBusId());
        ddEnsuringpsmanage.setFstatus(DispatchConstants.DdEnsuringpsmanage_WORKFLOW_CITY_STATUS_COURSIGN);
        ddEnsuringpsmanage.setFexcutestatus(DispatchConstants.DdEnsuringpsmanage_BUSINESS_CITY_COURSIGNING);
        ddEnsuringpsmanageDao.saveOrUpdate(ddEnsuringpsmanage);
    }

    public IDdEnsuringpsmanageWFService getDdEnsuringpsmanageWFService() {
        return ddEnsuringpsmanageWFService;
    }

    public void setDdEnsuringpsmanageWFService(IDdEnsuringpsmanageWFService ddEnsuringpsmanageWFService) {
        this.ddEnsuringpsmanageWFService = ddEnsuringpsmanageWFService;
    }
}
