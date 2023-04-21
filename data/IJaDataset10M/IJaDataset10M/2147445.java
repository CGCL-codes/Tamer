package com.ncs.server.service.impl;

import com.ncs.common.service.impl.BaseServiceImpl;
import com.ncs.order.vo.RoleVo;
import com.ncs.order.vo.UserVo;
import com.ncs.server.dao.SubjectDao;
import com.ncs.server.service.RoleService;
import com.ncs.server.service.SubjectService;
import com.ncs.server.to.ResourceTo;
import com.ncs.server.to.RoleTo;
import com.ncs.server.to.SubjectTo;
import flex.messaging.FlexContext;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

@Service("subjectService")
public class SubjectServiceImpl extends BaseServiceImpl<SubjectTo, String> implements SubjectService {

    @Resource
    public void setBaseDao(SubjectDao subjectDao) {
        super.setBaseDao(subjectDao);
    }

    @Resource
    private SubjectDao subjectDao;

    @Resource
    private RoleService roleService;

    private static Logger log = Logger.getLogger(SubjectServiceImpl.class);

    public String create(UserVo vo) {
        SubjectTo loginUser = (SubjectTo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String projId = loginUser.getProjId();
        String newUsername = vo.getUsername();
        Set set = subjectDao.getByUsernameByProjId(newUsername, projId);
        if (set.size() != 0) {
            return "已存在相同登录名";
        }
        SubjectTo to = new SubjectTo();
        to.setUsername(vo.getUsername());
        to.setEmail(vo.getEmail());
        to.setName(vo.getName());
        to.setIsAccountEnabled(vo.getIsAccountEnabled());
        to.setIsAccountExpired(false);
        to.setIsAccountLocked(false);
        to.setLoginFailureCount(0);
        to.setIsCredentialsExpired(false);
        to.setPassword(vo.getUsername());
        to.setProjId(vo.getProjId());
        RoleTo roleTo = new RoleTo();
        roleTo.setId(vo.getRoleList().get(0));
        Set roleSet = new HashSet();
        roleSet.add(roleTo);
        to.setRoleSet(roleSet);
        subjectDao.save(to);
        return "OK";
    }

    public SubjectTo findByUsername(String username) {
        return subjectDao.findByUsername(username);
    }

    public String changePassword(String oldPassword, String newPassword) {
        String username = "unknown";
        String flg = "OK";
        username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!username.equals("unknown")) {
            try {
                SubjectTo subjectTO = subjectDao.findByUsername(username);
                if (subjectTO != null && subjectTO.getPassword().equals(oldPassword)) {
                    subjectTO.setPassword(newPassword);
                    subjectTO.setLastUpdateDate(new Timestamp(new Date().getTime()));
                    subjectDao.update(subjectTO);
                } else {
                    flg = "旧密码不正确";
                }
            } catch (Exception e) {
                flg = "密码修改失败，请与管理员联系!";
            }
        }
        return flg;
    }

    public void logoutPrincipal() {
        log.info("CustomJdbcDaoImpl logout run...");
        String username = "unknown";
        try {
            username = SecurityContextHolder.getContext().getAuthentication().getName();
            FlexContext.setUserPrincipal(null);
            FlexContext.getHttpRequest().getSession().invalidate();
            FlexContext.getFlexSession().invalidate();
            SecurityContextHolder.clearContext();
        } catch (Exception e) {
            log.warn("Problem while logging out the current user : " + username);
        }
        log.info("CustomJdbcDaoImpl logout end...");
    }

    public List<RoleVo> queryRoleList(String projId) {
        Set<RoleTo> roleSet = roleService.getList("projectId", projId);
        List<RoleTo> list = new ArrayList();
        for (RoleTo roleTo : roleSet) {
            list.add(roleTo);
        }
        List<RoleVo> result = new ArrayList<RoleVo>();
        for (RoleTo to : list) {
            RoleVo vo = new RoleVo();
            vo.setRoleId(to.getId());
            vo.setName(to.getName());
            vo.setValue(to.getValue());
            result.add(vo);
        }
        return result;
    }

    public UserVo getSubjectAtPortal() {
        log.info("getSubjectTOAtPortal run...");
        SubjectTo to = (SubjectTo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserVo vo = new UserVo();
        vo.setSubjectId(to.getId());
        vo.setUsername(to.getUsername());
        vo.setEmail(to.getEmail());
        vo.setName(to.getName());
        vo.setEmail(to.getEmail());
        vo.setProjId(to.getProjId());
        vo.setDepartment(to.getDepartment());
        vo.setIsAccountEnabled(to.getIsAccountEnabled());
        List<String> roleList = new ArrayList<String>();
        List<String> resourceList = new ArrayList<String>();
        for (RoleTo role : to.getRoleSet()) {
            roleList.add(role.getValue());
            for (ResourceTo r : role.getResourceSet()) {
                resourceList.add(r.getValue());
            }
        }
        vo.setRoleList(roleList);
        vo.setResource(resourceList.toString());
        log.info("getSubjectTOAtPortal end...");
        return vo;
    }

    public void remove(UserVo userVo) {
        SubjectTo subjectTo = subjectDao.get(userVo.getSubjectId());
        subjectDao.delete(subjectTo);
    }

    public List findByProjId(String projId) {
        Set<SubjectTo> subjectSet = subjectDao.getList("projId", projId);
        List<SubjectTo> list = new ArrayList<SubjectTo>();
        for (SubjectTo subjectTo : subjectSet) {
            list.add(subjectTo);
        }
        List<UserVo> result = new ArrayList<UserVo>();
        for (SubjectTo to : list) {
            UserVo vo = new UserVo();
            vo.setSubjectId(to.getId());
            vo.setUsername(to.getUsername());
            vo.setEmail(to.getEmail());
            vo.setName(to.getName());
            vo.setProjId(to.getProjId());
            vo.setDepartment(to.getDepartment());
            vo.setIsAccountEnabled(to.getIsAccountEnabled());
            List<String> roleList = new ArrayList<String>();
            for (RoleTo role : to.getRoleSet()) {
                roleList.add(role.getName());
            }
            vo.setRoleList(roleList);
            result.add(vo);
        }
        return result;
    }

    public String update(UserVo vo) {
        SubjectTo to = subjectDao.get(vo.getSubjectId());
        to.setEmail(vo.getEmail());
        to.setName(vo.getName());
        to.setIsAccountEnabled(vo.getIsAccountEnabled());
        RoleTo roleTO = new RoleTo();
        roleTO.setId(vo.getRoleList().get(0));
        Set set = new HashSet();
        set.add(roleTO);
        to.setRoleSet(set);
        subjectDao.update(to);
        return "OK";
    }

    public String resetPass(UserVo vo) {
        String result = "OK";
        try {
            SubjectTo to = subjectDao.get(vo.getSubjectId());
            to.setPassword(to.getUsername());
            subjectDao.update(to);
        } catch (Exception e) {
            e.printStackTrace();
            result = "operation failed!";
        }
        return result;
    }
}
