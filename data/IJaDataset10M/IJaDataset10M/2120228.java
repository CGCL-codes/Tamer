package com.hk.svr;

import java.util.List;
import java.util.Map;
import com.hk.bean.CmpActor;
import com.hk.bean.CmpActorPink;
import com.hk.bean.CmpActorRole;
import com.hk.svr.processor.CmpActorProcessor;

/**
 * 企业人物以及角色的相关逻辑
 * 
 * @author akwei
 */
public interface CmpActorService {

    void createCmpActorRole(CmpActorRole cmpActorRole);

    void updateCmpActorRole(CmpActorRole cmpActorRole);

    /**
	 * 删除角色时，需要把相关角色的人员数据中角色id置为0
	 * 
	 * @param companyId
	 * @param actorRoleId
	 *            2010-7-21
	 */
    void deleteCmpActorRole(long roleId);

    CmpActorRole getCmpActorRole(long roleId);

    List<CmpActorRole> getCmpActorRoleListByCompanyId(long companyId);

    /**
	 * 请使用 {@link CmpActorProcessor#createCmpActor(CmpActor)}
	 * 
	 * @param cmpActor
	 *            2010-7-21
	 */
    void createCmpActor(CmpActor cmpActor);

    /**
	 * 请使用 {@link CmpActorProcessor#updateCmpActor(CmpActor)}
	 * 
	 * @param cmpActor
	 *            2010-7-21
	 */
    void updateCmpActor(CmpActor cmpActor);

    /**
	 * 请使用 {@link CmpActorProcessor#deleteCmpActor(long, long)}
	 * 
	 * @param companyId
	 * @param actorId
	 *            2010-7-21
	 */
    void deleteCmpActor(long actorId);

    CmpActor getCmpActor(long actorId);

    CmpActor getCmpActorByName(long companyId, String name);

    /**
	 * @param companyId
	 * @param name 为空时，不参与查询
	 * @param begin
	 * @param size
	 * @return
	 *         2010-7-21
	 */
    List<CmpActor> getCmpActorListByCompanyId(long companyId, long roleId, String name, int begin, int size);

    Map<Long, CmpActor> getCmpActorMapInId(List<Long> idList);

    Map<Long, CmpActorRole> getCmpActorRoleMapByCompanyIdAndInId(List<Long> idList);

    /**
	 * @param companyId
	 * @param begin 如果<0时忽略begin,size参数，获取所有数据
	 * @param size 如果<0时忽略begin,size参数，获取所有数据
	 * @return
	 *         2010-8-20
	 */
    List<CmpActor> getCmpActorListByCompanyIdForCanReserve(long companyId, int begin, int size);

    void updateWorkCountByActorId(long actorId, int workCount);

    /**
	 * 如果已经存在actorid，则不创建数据
	 * 
	 * @param cmpActorPink
	 *            2010-8-22
	 */
    void createCmpActorPink(CmpActorPink cmpActorPink);

    void deleteCmpActorPink(long oid);

    List<CmpActorPink> getCmpActorPinkList(int begin, int size);

    CmpActorPink getCmpActorPinkByActorId(long actorId);

    List<CmpActor> getCmpActorListForWorkCount(int begin, int size);

    int sumCmpActorScoreByCompanyId(long companyId);

    int sumCmpActorScoreUserNumByCompanyId(long companyId);
}
