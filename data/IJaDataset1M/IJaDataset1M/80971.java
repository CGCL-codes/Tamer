package com.systop.common.modules.dept.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import com.systop.common.modules.dept.model.Dept;
import com.systop.core.service.BaseGenericsManager;

/**
 * 部门管理Manager
 * 
 * @author Sam Lee
 * 
 */
@Service
public class DeptManager extends BaseGenericsManager<Dept> {

    /**
	 * 用于计算部门编号
	 */
    private DeptSerialNoManager serialNoManager;

    @Autowired(required = true)
    public void setSerialNoManager(DeptSerialNoManager serialNoManager) {
        this.serialNoManager = serialNoManager;
    }

    /**
	 * 保存部门信息
	 * 
	 * @see BaseGenericsManager#save(java.lang.Object)
	 */
    @Override
    @Transactional
    public void save(Dept dept) {
        Assert.notNull(dept);
        logger.debug("Parent dept {}", dept.getParentDept());
        Dept parent = dept.getParentDept();
        getDao().evict(parent);
        if (parent != null && parent.getId() != null) {
            logger.debug("Parent dept Id {}", dept.getParentDept().getId());
            parent = get(dept.getParentDept().getId());
            if (parent != null) {
                parent.getChildDepts().add(dept);
                dept.setParentDept(parent);
            }
        } else {
            dept.setParentDept(null);
        }
        if (dept.getId() == null) {
            dept.setSerialNo(serialNoManager.getSerialNo(dept));
        }
        getDao().getHibernateTemplate().clear();
        super.save(dept);
    }

    /**
	 * 删除部门，解除关联关系
	 */
    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public void remove(Dept dept) {
        Assert.notNull(dept);
        dept.setParentDept(null);
        Set<Dept> children = dept.getChildDepts();
        for (Dept child : children) {
            child.setParentDept(null);
        }
        dept.setChildDepts(Collections.EMPTY_SET);
        super.remove(dept);
    }

    /**
	 * 根据部门名称得到部门对应的实体
	 * 
	 * @return
	 */
    public Dept getDeptByName(String name) {
        String hql = "from Dept d where d.name = ? ";
        Dept dept = (Dept) getDao().findObject(hql, name);
        return dept;
    }

    /**
	 * 根据区县ID获得该区县的所有执法部门
	 * 
	 * @param countyId
	 * @return
	 */
    public List<Dept> getEnforcementByCounty(Integer countyId) {
        String hql = "from Dept d where d.parentDept.id = ?";
        List<Dept> depts = query(hql, countyId);
        return depts;
    }
}
