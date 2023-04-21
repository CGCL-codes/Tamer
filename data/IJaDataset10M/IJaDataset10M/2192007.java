package com.air.admin.service.imp;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.air.admin.dao.AdminPrivilegeDao;
import com.air.admin.dao.AdminRoleDao;
import com.air.admin.mo.AdminPrivilege;
import com.air.admin.mo.AdminRole;
import com.air.admin.service.IAdminRoleService;
import com.air.admin.vo.RoleQueryRequestVO;
import com.air.common.service.imp.BaseServiceImp;
import com.air.common.util.QueryCondition;
import com.air.common.util.QueryExpression;
import com.air.common.vo.PageResultListVO;

@Service("adminRoleService")
public class AdminRoleServiceImp extends BaseServiceImp implements IAdminRoleService, InitializingBean {

    private final String simpleModeQueryStringTemplate = "CODE like '%{queryString}%' " + "or PRIVILEGES like '%{queryString}%' or DESCRIPTION like '%{queryString}%'";

    @Autowired
    AdminPrivilegeDao adminPrivilegeMapper;

    @Autowired
    AdminRoleDao adminRoleMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.baseDao = adminRoleMapper;
    }

    public List<AdminPrivilege> getPrivilege(AdminRole role) {
        List<AdminPrivilege> privilegeList = new ArrayList<AdminPrivilege>();
        if (role.getPrivileges() == null || role.getPrivileges().trim().length() == 0) {
            return privilegeList;
        }
        for (String privilegeCode : role.getPrivileges().split(",")) {
            AdminPrivilege privilege = adminPrivilegeMapper.selectByCode(privilegeCode);
            privilegeList.add(privilege);
        }
        return privilegeList;
    }

    @Override
    public boolean hasRelativeRole(List<String> privilegeCodeList) {
        for (String privilegeCode : privilegeCodeList) {
            QueryCondition condition = new QueryCondition();
            condition.addQueryCondition("privileges", privilegeCode, QueryExpression.LIKE);
            List<AdminRole> roles = this.queryByCondition(condition);
            if (roles != null && roles.size() > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public PageResultListVO queryInAdvanceMode(QueryCondition condition, Integer pageNum, Integer pageLimit, RoleQueryRequestVO queryRequestVO) throws Exception {
        if (queryRequestVO.getCode().length() > 0) {
            condition.addQueryCondition("code", queryRequestVO.getCode(), QueryExpression.LIKE);
        }
        if (queryRequestVO.getPrivileges().length() > 0) {
            condition.addQueryCondition("privileges", queryRequestVO.getPrivileges(), QueryExpression.LIKE);
        }
        if (queryRequestVO.getDescription().length() > 0) {
            condition.addQueryCondition("description", queryRequestVO.getDescription(), QueryExpression.LIKE);
        }
        return queryByCondition(condition, pageNum, pageLimit);
    }

    @Override
    public PageResultListVO queryInSimpleMode(QueryCondition condition, Integer pageNum, Integer pageLimit, String queryString) throws Exception {
        if (queryString != null && queryString.trim().length() > 0) {
            queryString = queryString.toUpperCase();
            String simpleModeQueryCondition = simpleModeQueryStringTemplate.replace("{queryString}", queryString);
            condition.addManualCondition(simpleModeQueryCondition);
        }
        return this.queryByCondition(condition, pageNum, pageLimit);
    }
}
