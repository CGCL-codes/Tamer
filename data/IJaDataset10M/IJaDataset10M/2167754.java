package com.techstar.dmis.web.facade;

import java.util.List;
import org.springframework.dao.DataAccessException;
import com.techstar.framework.dao.model.QueryListObj;
import com.techstar.dmis.service.IStdManufacturerService;
import com.techstar.dmis.dto.StdManufacturerDto;
import com.techstar.dmis.dto.EtsEquipmentDto;

/**
 * facade类
 * @author 
 * @date
 */
public class StdManufacturerFacade {

    private IStdManufacturerService stdManufacturerService;

    /**
	 * 得到列表数据
	 * 
	 * @return List
	 */
    public List listStdManufacturer() {
        return stdManufacturerService.listStdManufacturer().getElemList();
    }

    /**
	 * 增加DTO
	 * 
	 * @param dto
	 */
    public void addStdManufacturer(StdManufacturerDto dto) {
        stdManufacturerService.saveOrUpdateStdManufacturer(dto);
    }

    /**
	 * 得到指定DTO
	 * 
	 * @param StdManufacturerId
	 *           ֵ

	 */
    public StdManufacturerDto getStdManufacturerById(String stdManufacturerId) {
        StdManufacturerDto dto = stdManufacturerService.loadStdManufacturer(stdManufacturerId);
        return dto;
    }

    /**
	 * 删除指定DTO
	 * 
	 * @param StdManufacturerId
	 *         ֵ

	 */
    public List deleteStdManufacturer(String stdManufacturerId) {
        stdManufacturerService.deleteStdManufacturer(stdManufacturerId);
        return stdManufacturerService.listStdManufacturer().getElemList();
    }

    /**
	 * 删除指定DTO集合
	 */
    public List deleteStdManufacturer(List dtos) {
        stdManufacturerService.deleteStdManufacturer(dtos);
        return stdManufacturerService.listStdManufacturer().getElemList();
    }

    /**
	 * 得到业务对象列表
	 * @return List
	 */
    public List listStdManufacturer4dwr() {
        return stdManufacturerService.listStdManufacturer().getElemList();
    }

    public void setStdManufacturerService(IStdManufacturerService stdManufacturerService) {
        this.stdManufacturerService = stdManufacturerService;
    }

    public QueryListObj getStdManufacturerByHql(String hql, int beginPage, int pageSize, String sql) {
        return this.stdManufacturerService.getStdManufacturerByHql(hql, beginPage, pageSize, sql);
    }

    public StdManufacturerDto loadStdManufacturer(String stdManufacturerId) {
        return this.stdManufacturerService.loadStdManufacturer(stdManufacturerId);
    }

    public EtsEquipmentDto loadEtsEquipment(String feqpid) {
        return this.stdManufacturerService.loadEtsEquipment(feqpid);
    }

    public List deleteEtsEquipment(String feqpid) {
        stdManufacturerService.deleteEtsEquipment(feqpid);
        return stdManufacturerService.listEtsEquipment().getElemList();
    }

    public List deleteEtsEquipment(List dtos) {
        stdManufacturerService.deleteEtsEquipment(dtos);
        return stdManufacturerService.listEtsEquipment().getElemList();
    }

    public void addEtsEquipment(EtsEquipmentDto dto) {
        stdManufacturerService.saveOrUpdateEtsEquipment(dto);
    }
}
