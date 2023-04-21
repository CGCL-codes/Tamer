package com.techstar.dmis.web.facade;

import java.util.List;
import org.springframework.dao.DataAccessException;
import com.techstar.framework.dao.model.QueryListObj;
import com.techstar.dmis.service.IRepRepresultService;
import com.techstar.dmis.dto.RepRepresultDto;
import com.techstar.dmis.dto.RepTree2resultDto;
import com.techstar.dmis.dto.RepReptemplateDto;
import com.techstar.dmis.dto.RepResdataDto;

/**
 * facade类
 * @author 
 * @date
 */
public class RepRepresultFacade {

    private IRepRepresultService repRepresultService;

    /**
	 * 得到列表数据
	 * 
	 * @return List
	 */
    public List listRepRepresult() {
        return repRepresultService.listRepRepresult().getElemList();
    }

    /**
	 * 增加DTO
	 * 
	 * @param dto
	 */
    public void addRepRepresult(RepRepresultDto dto) {
        repRepresultService.saveOrUpdateRepRepresult(dto);
    }

    /**
	 * 得到指定DTO
	 * 
	 * @param RepRepresultId
	 *           ֵ

	 */
    public RepRepresultDto getRepRepresultById(String repRepresultId) {
        RepRepresultDto dto = repRepresultService.loadRepRepresult(repRepresultId);
        return dto;
    }

    /**
	 * 删除指定DTO
	 * 
	 * @param RepRepresultId
	 *         ֵ

	 */
    public List deleteRepRepresult(String repRepresultId) {
        repRepresultService.deleteRepRepresult(repRepresultId);
        return repRepresultService.listRepRepresult().getElemList();
    }

    /**
	 * 删除指定DTO集合
	 */
    public List deleteRepRepresult(List dtos) {
        repRepresultService.deleteRepRepresult(dtos);
        return repRepresultService.listRepRepresult().getElemList();
    }

    /**
	 * 得到业务对象列表
	 * @return List
	 */
    public List listRepRepresult4dwr() {
        return repRepresultService.listRepRepresult().getElemList();
    }

    public void setRepRepresultService(IRepRepresultService repRepresultService) {
        this.repRepresultService = repRepresultService;
    }

    public QueryListObj getRepRepresultByHql(String hql, int beginPage, int pageSize, String sql) {
        return this.repRepresultService.getRepRepresultByHql(hql, beginPage, pageSize, sql);
    }

    public RepRepresultDto loadRepRepresult(String repRepresultId) {
        return this.repRepresultService.loadRepRepresult(repRepresultId);
    }

    public RepTree2resultDto loadRepTree2result(String f_relationid) {
        return this.repRepresultService.loadRepTree2result(f_relationid);
    }

    public List deleteRepTree2result(String f_relationid) {
        repRepresultService.deleteRepTree2result(f_relationid);
        return repRepresultService.listRepTree2result().getElemList();
    }

    public List deleteRepTree2result(List dtos) {
        repRepresultService.deleteRepTree2result(dtos);
        return repRepresultService.listRepTree2result().getElemList();
    }

    public void addRepTree2result(RepTree2resultDto dto) {
        repRepresultService.saveOrUpdateRepTree2result(dto);
    }

    public RepReptemplateDto loadRepReptemplate(String f_reptmplid) {
        return this.repRepresultService.loadRepReptemplate(f_reptmplid);
    }

    public List deleteRepReptemplate(String f_reptmplid) {
        repRepresultService.deleteRepReptemplate(f_reptmplid);
        return repRepresultService.listRepReptemplate().getElemList();
    }

    public List deleteRepReptemplate(List dtos) {
        repRepresultService.deleteRepReptemplate(dtos);
        return repRepresultService.listRepReptemplate().getElemList();
    }

    public void addRepReptemplate(RepReptemplateDto dto) {
        repRepresultService.saveOrUpdateRepReptemplate(dto);
    }

    public RepResdataDto loadRepResdata(String res_id) {
        return this.repRepresultService.loadRepResdata(res_id);
    }

    public List deleteRepResdata(String res_id) {
        repRepresultService.deleteRepResdata(res_id);
        return repRepresultService.listRepResdata().getElemList();
    }

    public List deleteRepResdata(List dtos) {
        repRepresultService.deleteRepResdata(dtos);
        return repRepresultService.listRepResdata().getElemList();
    }

    public void addRepResdata(RepResdataDto dto) {
        repRepresultService.saveOrUpdateRepResdata(dto);
    }
}
