package com.yict.csms.adminManage.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.yict.common.entity.PageEntity;
import com.yict.common.service.impl.BaseServiceImpl;
import com.yict.csms.adminManage.dao.IContractorRankingDao;
import com.yict.csms.adminManage.entity.ContractorRanking;
import com.yict.csms.adminManage.service.IContractorRankingService;

/**
 * 
 * 
 * @author ryan.wang
 * 
 */
@Service("contractorRankingService")
public class ContractorRankingServiceImpl extends BaseServiceImpl<ContractorRanking, Long, IContractorRankingDao> implements IContractorRankingService {

    public List<ContractorRanking> search(Map<String, Object> queryMap, PageEntity page) {
        List<ContractorRanking> list = new ArrayList<ContractorRanking>();
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder("from ContractorRanking t where 1=1 ");
        Long[] companyIdList = (Long[]) queryMap.get("companyIdList");
        if (companyIdList != null && companyIdList.length > 0) {
            map.put("companyIdList", companyIdList);
            hql.append(" and t.companyId.companyId in (:companyIdList)");
        }
        String companyName = (String) queryMap.get("companyName");
        if (companyName != null && companyName.length() > 0) {
            map.put("companyName", "%" + companyName.toLowerCase() + "%");
            hql.append(" and lower(t.companyId.companyName) like :companyName");
        }
        Long[] contractTypeIdList = (Long[]) queryMap.get("contractTypeIdList");
        if (contractTypeIdList != null && contractTypeIdList.length > 0) {
            map.put("contractTypeIdList", contractTypeIdList);
            hql.append(" and t.typeId.profeid  in (:contractTypeIdList)");
        }
        Long contractNo = (Long) queryMap.get("contractId");
        if (contractNo != null) {
            map.put("contractNo", contractNo);
            hql.append(" and t.typeId in (select c.contractType.typeId from Contract c where c.contractId = :contractNo)");
        }
        String startDate1 = (String) queryMap.get("startDate1");
        String startDate2 = (String) queryMap.get("startDate2");
        if (startDate1 != null && startDate1.length() > 0) {
            map.put("startDate1", startDate1);
            hql.append(" and t.startDate >= to_date(:startDate1,'yyyy-mm-dd')");
        }
        if (startDate2 != null && startDate2.length() > 0) {
            map.put("startDate2", startDate2);
            hql.append(" and t.startDate <= to_date(:startDate2,'yyyy-mm-dd')");
        }
        String endDate1 = (String) queryMap.get("endDate1");
        String endDate2 = (String) queryMap.get("endDate2");
        if (endDate1 != null && endDate1.length() > 0) {
            map.put("endDate1", endDate1);
            hql.append(" and t.endDate >= to_date(:endDate1,'yyyy-mm-dd')");
        }
        if (endDate2 != null && endDate2.length() > 0) {
            map.put("endDate2", endDate2);
            hql.append(" and t.endDate <= to_date(:endDate2,'yyyy-mm-dd')");
        }
        int count = this.getBaseDao().queryCount("select count(*) " + hql.toString(), map);
        page.setTotalRecord(count);
        if (count > 0) {
            StringBuilder orderString = new StringBuilder("");
            if (page.getField() != null && !"".equals(page.getField()) && page.getOrder() != null && !"".equals(page.getOrder())) {
                if (page.getField().equals("companySName")) {
                    orderString.append("t.companyId.companySName " + page.getOrder());
                } else if (page.getField().equals("contractType")) {
                    orderString.append("t.typeId.profename " + page.getOrder());
                } else {
                    orderString.append(page.getField() + page.getOrder());
                }
                hql.append(" order by ").append(orderString);
            } else {
                hql.append(" order by t.ranking");
            }
            list = this.getBaseDao().list(hql.toString(), map, (page.getToPage() - 1) * page.getPageSize(), page.getPageSize());
        }
        return list;
    }

    public List<ContractorRanking> search(Map<String, Object> queryMap) {
        List<ContractorRanking> list = new ArrayList<ContractorRanking>();
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder("from ContractorRanking t where 1=1 ");
        Long[] companyIdList = (Long[]) queryMap.get("companyIdList");
        if (companyIdList != null && companyIdList.length > 0) {
            map.put("companyIdList", companyIdList);
            hql.append(" and t.companyId.companyId in (:companyIdList)");
        }
        Date date = (Date) queryMap.get("date");
        if (date != null) {
            map.put("date", date);
            hql.append(" and t.companyId.companyName like :companyName");
        }
        Date cuDate = (Date) queryMap.get("cuDate");
        if (cuDate != null) {
            map.put("cuDate", cuDate);
            hql.append(" and t.startDate <= :cuDate and t.endDate >= :cuDate");
        }
        Long proId = (Long) queryMap.get("proId");
        if (cuDate != null) {
            map.put("proId", proId);
            hql.append(" and t.typeId.profeid = :proId");
        }
        hql.append(" order by t.fixedPercent desc");
        list = this.getBaseDao().list(hql.toString(), map, 0, 0);
        return list;
    }

    @Resource(name = "contractorRankingDao")
    public void setBaseDao(IContractorRankingDao baseDao) {
        super.setBaseDao(baseDao);
    }

    @Override
    public boolean check(String flag, ContractorRanking contractorRanking, boolean saveOrUpdate) {
        return this.getBaseDao().check(flag, contractorRanking, saveOrUpdate);
    }

    @Override
    public Long findMaxRanking(Long typeId) {
        return this.getBaseDao().findMaxRanking(typeId);
    }
}
