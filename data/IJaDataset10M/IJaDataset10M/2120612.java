package com.techstar.dmis.web.facade;

import java.util.List;
import org.springframework.dao.DataAccessException;
import com.techstar.framework.dao.model.QueryListObj;
import com.techstar.dmis.service.IZdhRmrecService;
import com.techstar.dmis.dto.ZdhRmrecDto;

/**
 * facade类
 * @author 
 * @date
 */
public class ZdhRmrecFacade {

    private IZdhRmrecService zdhRmrecService;

    /**
	 * 得到列表数据
	 * 
	 * @return List
	 */
    public List listZdhRmrec() {
        return zdhRmrecService.listZdhRmrec().getElemList();
    }

    /**
	 * 增加DTO
	 * 
	 * @param dto
	 */
    public void addZdhRmrec(ZdhRmrecDto dto) {
        zdhRmrecService.saveOrUpdateZdhRmrec(dto);
    }

    /**
	 * 得到指定DTO
	 * 
	 * @param ZdhRmrecId
	 *           ֵ

	 */
    public ZdhRmrecDto getZdhRmrecById(String zdhRmrecId) {
        ZdhRmrecDto dto = zdhRmrecService.loadZdhRmrec(zdhRmrecId);
        return dto;
    }

    /**
	 * 删除指定DTO
	 * 
	 * @param ZdhRmrecId
	 *         ֵ

	 */
    public List deleteZdhRmrec(String zdhRmrecId) {
        zdhRmrecService.deleteZdhRmrec(zdhRmrecId);
        return zdhRmrecService.listZdhRmrec().getElemList();
    }

    /**
	 * 得到业务对象列表
	 * @return List
	 */
    public List listZdhRmrec4dwr() {
        return zdhRmrecService.listZdhRmrec().getElemList();
    }

    public void setZdhRmrecService(IZdhRmrecService zdhRmrecService) {
        this.zdhRmrecService = zdhRmrecService;
    }

    public QueryListObj getZdhRmrecByHql(String hql, int beginPage, int pageSize, String sql) {
        return this.zdhRmrecService.getZdhRmrecByHql(hql, beginPage, pageSize, sql);
    }

    public ZdhRmrecDto loadZdhRmrec(String zdhRmrecId) {
        return this.zdhRmrecService.loadZdhRmrec(zdhRmrecId);
    }

    public void deleteZdhRmrec(String zdhRmrecId, int version) {
        zdhRmrecService.deleteZdhRmrec(zdhRmrecId, version);
    }

    /**
	 * 根椐ZdhRmrec对象外键系统图ID取数据集
	 * @param foreignId
	 * @return
	 */
    public ZdhRmrecDto getZdhRmrecByForeign(String foreignId) {
        ZdhRmrecDto zdhRmrecDto = zdhRmrecService.getZdhRmrecByForeign(foreignId);
        return zdhRmrecDto;
    }
}
