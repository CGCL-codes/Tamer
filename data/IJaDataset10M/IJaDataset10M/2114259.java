package com.fundsmart.workplan.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.fundsmart.workplan.dao.PlanDao;
import com.fundsmart.workplan.enumeration.PlanStatus;
import com.fundsmart.workplan.pojo.Plan;
import com.fundsmart.workplan.pojo.User;
import com.fundsmart.workplan.service.PlanService;

public class PlanServiceImpl implements PlanService {

    private PlanDao planDao;

    public void setPlanDao(PlanDao planDao) {
        this.planDao = planDao;
    }

    @Transactional
    public void addPlan(User user, Plan plan) {
        plan.setOwner(user);
        plan.setCreateTime(new Date());
        plan.setUpdateTime(new Date());
        plan.setPlanStatus(PlanStatus.INITIAL);
        planDao.addPlan(plan);
    }

    @Transactional
    public void updatePlan(Plan plan) {
        plan.setUpdateTime(new Date());
        planDao.updatePlan(plan);
    }

    @Transactional(readOnly = true)
    public List<Plan> searchPlan(String username, String planName, PlanStatus planStatus, Date startTime, Date endTime) {
        return planDao.searchPlan(username, planName, planStatus, startTime, endTime);
    }
}
