package com.techstar.dmis.service;

import java.util.List;
import java.util.Collection;
import com.techstar.framework.dao.model.QueryListObj;
import com.techstar.dmis.dto.ZdhEligiblermrateDto;

/** 
 * 业务对象服务接口类
 * @author 
 * @date
 */
public interface IZdhEligiblermrateService {

    public void addZdhEligiblermrate(ZdhEligiblermrateDto dto);

    /**
	 * 进行增加或修改操作业务处理
	 * 
	 * @param dto
	 *ZdhEligiblermrateDto 业务传输对象
	 * 
	 */
    public void saveOrUpdateZdhEligiblermrate(ZdhEligiblermrateDto dto);

    public void saveOrUpdateZdhEligiblermrate(List dtos);

    /**
	 * 进行加载的业务操作
	 * @param zdhEligiblermrateId
	 *            Integer 主键值ֵ
	 */
    public ZdhEligiblermrateDto loadZdhEligiblermrate(String zdhEligiblermrateId);

    /**
	 * 删除指定的业务
	 * @param zdhEligiblermrateId
	 *            Integer 主键值ֵ
	 */
    public void deleteZdhEligiblermrate(String zdhEligiblermrateId);

    /**
	 * 进行申请查询操作业务处理(分页)
	 * 
	 * @return QueryListObj 封装了结果集及记录总数的对象
	 */
    public QueryListObj listZdhEligiblermrate();

    public QueryListObj listZdhEligiblermrateByHql(String hql);

    public QueryListObj getZdhEligiblermrateByHql(String hql, int beginPage, int pageSize, String sql);

    public void deleteZdhEligiblermrate(String zdhEligiblermrateId, int version);

    /**
 *  交接班使用
 */
    public QueryListObj getZdhEligiblermrateByHql(String hql);
}
