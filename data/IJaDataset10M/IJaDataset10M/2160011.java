package com.yict.csms.resourceplan.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yict.common.Constants;
import com.yict.common.service.impl.CommonService;
import com.yict.csms.resourceplan.dao.impl.RtgcFlPlanAllDaoImpl;
import com.yict.csms.resourceplan.dao.impl.RtgcFlTimeDaoImpl;
import com.yict.csms.resourceplan.entity.RtgcFlContractor;
import com.yict.csms.resourceplan.entity.RtgcFlPlan;
import com.yict.csms.resourceplan.entity.RtgcFlPlanAll;
import com.yict.csms.resourceplan.entity.RtgcFlTime;
import com.yict.csms.system.entity.DataDict;
import com.yict.csms.system.service.impl.DataDictServiceImpl;

@Service
public class RtgcFlPlanAllServiceImpl extends CommonService<RtgcFlPlanAll, Long> {

    @Autowired
    private RtgcFlPlanAllDaoImpl rtgcFlPlanAllDaoImpl;

    @Autowired
    private DataDictServiceImpl dataDictService;

    @Autowired
    private RtgcFlTimeDaoImpl rtgcFlTimeDaoImpl;

    public List<RtgcFlPlanAll> search(Map<String, Object> queryMap) throws Exception {
        StringBuilder queryString = new StringBuilder();
        List<RtgcFlPlanAll> list = new ArrayList<RtgcFlPlanAll>();
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            queryString.append("from RtgcFlPlanAll as c left join fetch c.rtgcFlContractorSet as s where 1=1 ");
            if (queryMap != null && queryMap.size() > 0) {
                if (queryMap.get("rtgcFlTimeId") != null && !"".equals(queryMap.get("rtgcFlTimeId"))) {
                    queryString.append(" and c.planTime.dictid = :rtgcFlTimeId");
                    map.put("rtgcFlTimeId", queryMap.get("rtgcFlTimeId"));
                }
                if (queryMap.get("rtgcfldate") != null && !"".equals(queryMap.get("rtgcfldate"))) {
                    queryString.append(" and c.planDate like to_date(:rtgcfldate,'yyyy-MM-dd')");
                    map.put("rtgcfldate", queryMap.get("rtgcfldate"));
                }
                if (queryMap.get("opTypeId") != null && !"".equals(queryMap.get("opTypeId"))) {
                    queryString.append(" and c.machine.dictid = :opTypeId");
                    map.put("opTypeId", queryMap.get("opTypeId"));
                }
                if (queryMap.get("companyId") != null && !"".equals(queryMap.get("companyId"))) {
                    queryString.append(" and s.company.companyId = :companyId");
                    map.put("companyId", queryMap.get("companyId"));
                }
                if (queryMap.get("statusId") != null && !"".equals(queryMap.get("statusId"))) {
                    queryString.append(" and c.status.dictid = :statusId");
                    map.put("statusId", queryMap.get("statusId"));
                }
            }
            list = rtgcFlPlanAllDaoImpl.search(queryString.toString(), map, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Transactional(readOnly = false, rollbackFor = { Exception.class })
    public boolean saveOrUpdate(RtgcFlPlanAll all, List<RtgcFlPlan> planList, List<RtgcFlContractor> contractorList) throws Exception {
        boolean bool = false;
        try {
            bool = rtgcFlPlanAllDaoImpl.saveOrUpdate(all);
            bool = rtgcFlPlanAllDaoImpl.saveOrUpdateRtgcFlPlan(planList);
            bool = rtgcFlPlanAllDaoImpl.saveOrUpdateRtgcFlContractor(contractorList);
        } catch (Exception e) {
            bool = false;
            e.printStackTrace();
            throw new RuntimeException();
        }
        return bool;
    }

    @Transactional(readOnly = false, rollbackFor = { Exception.class })
    public boolean delPlan(String[] ids) throws Exception {
        boolean bool = false;
        Set<RtgcFlPlan> list = new HashSet<RtgcFlPlan>();
        try {
            for (String string : ids) {
                RtgcFlPlan vo;
                if (string != null) {
                    vo = rtgcFlPlanAllDaoImpl.findRtgcFlPlanById(Long.valueOf(string));
                    list.add(vo);
                }
            }
            bool = rtgcFlPlanAllDaoImpl.delRtgcFlPlan(list);
        } catch (Exception e) {
            bool = false;
            e.printStackTrace();
            throw new RuntimeException();
        }
        return bool;
    }

    @Transactional(readOnly = false, rollbackFor = { Exception.class })
    public boolean delContractor(String[] ids) throws Exception {
        boolean bool = false;
        Set<RtgcFlContractor> set = new HashSet<RtgcFlContractor>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        String queryString = "from RtgcFlContractor as c where c.company.companyId = :companyId";
        try {
            for (String string : ids) {
                map.put("companyId", Long.valueOf(string));
                List<RtgcFlContractor> vos = rtgcFlPlanAllDaoImpl.findByCompanyId(queryString, map);
                set.addAll(vos);
                bool = rtgcFlPlanAllDaoImpl.delRtgcFlContractor(set);
            }
        } catch (Exception e) {
            bool = false;
            e.printStackTrace();
            throw new RuntimeException();
        }
        return bool;
    }

    @Transactional(readOnly = false, rollbackFor = { Exception.class })
    public boolean back(String allId) throws Exception {
        boolean bool = false;
        try {
            RtgcFlPlanAll all = rtgcFlPlanAllDaoImpl.findRtgcFlPlanAllById(Long.valueOf(allId));
            RtgcFlTime rtgcFlTime = all.getRtgcFlTime();
            DataDict status = dataDictService.findByCode(Constants.PLANSTATUS1);
            rtgcFlTime.setStatus(status);
            bool = rtgcFlTimeDaoImpl.update(rtgcFlTime);
            bool = rtgcFlPlanAllDaoImpl.delRtgcFlPlan(all.getRtgcFlPlanSet());
            bool = rtgcFlPlanAllDaoImpl.delRtgcFlContractor(all.getRtgcFlContractorSet());
            bool = rtgcFlPlanAllDaoImpl.remove(all);
        } catch (Exception e) {
            bool = false;
            e.printStackTrace();
            throw new RuntimeException();
        }
        return bool;
    }

    @Transactional(readOnly = false, rollbackFor = { Exception.class })
    public boolean commitStatus(String allId) throws Exception {
        boolean bool = false;
        try {
            RtgcFlPlanAll all = rtgcFlPlanAllDaoImpl.findRtgcFlPlanAllById(Long.valueOf(allId));
            DataDict vo = dataDictService.findByCode(Constants.PLANSTATUS2);
            all.setStatus(vo);
            all.setRtgcFlPlanSet(null);
            all.setRtgcFlContractorSet(null);
            bool = rtgcFlPlanAllDaoImpl.update(all);
        } catch (Exception e) {
            bool = false;
            e.printStackTrace();
            throw new RuntimeException();
        }
        return bool;
    }
}
