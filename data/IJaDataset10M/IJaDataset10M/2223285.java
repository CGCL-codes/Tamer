package cn.csust.net2.manager.server.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.csust.net2.manager.server.dao.RoleDAO;
import cn.csust.net2.manager.server.dao.StudentDAO;
import cn.csust.net2.manager.server.security.AuthCurrentUser;
import cn.csust.net2.manager.server.util.Util;
import cn.csust.net2.manager.shared.po.Authority;
import cn.csust.net2.manager.shared.po.Student;
import cn.csust.net2.manager.shared.po.User;
import cn.csust.net2.manager.shared.service.LoginService;
import cn.csust.net2.manager.shared.util.Constant;
import cn.csust.net2.manager.shared.util.ServiceConstant;
import cn.csust.net2.manager.shared.vo.LoginVo;

@Service(value = ServiceConstant.SERVICE_NAME_LOGIN)
public class LoginServiceImpl extends BaseServiceImpl implements LoginService {

    @Resource(name = Constant.DAO_NAME_STUDENT)
    private StudentDAO studentDAO;

    @Resource(name = Constant.DAO_NAME_ROLE)
    private RoleDAO roleDAO;

    @Override
    @Transactional
    public LoginVo login(String username, String password, String type) {
        LoginVo l = new LoginVo();
        String queryStr = " from User c where c.userID = :username and c.password = :password";
        User user = (User) studentDAO.getCurrentSession().createQuery(queryStr).setString("username", username).setString("password", password).uniqueResult();
        if (user != null) {
            l.setUser(user);
            List<Authority> authorities = this.getSqlMapper().findAuthorities(user.getUserID());
            l.setAuthorities(authorities);
            this.request.getSession().setAttribute(USER_SYS, user);
            AuthCurrentUser.set((User) this.request.getSession().getAttribute(USER_SYS));
            if (user instanceof Student) {
                l.setNowTerm(Util.getNowTerm(((Student) user).getEntryDate()));
            }
            this.testLogin();
        } else {
            l.setUser(null);
        }
        return l;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public LoginVo testLogin() {
        System.out.println("登陆测试");
        LoginVo l = new LoginVo();
        User user = (User) this.request.getSession().getAttribute(USER_SYS);
        if (user != null) {
            l.setUser(user);
            List<Authority> authorities = studentDAO.getCurrentSession().createSQLQuery(AUTHORITY_QUERY).addEntity(Authority.class).setLong("ID", user.getUserID()).list();
            l.setAuthorities(authorities);
        } else {
            l.setUser(null);
        }
        return l;
    }

    @Override
    public boolean testSecurity(String authority) {
        User user = (User) this.request.getSession().getAttribute(USER_SYS);
        int count = this.getSqlMapper().judgeAuthority(user.getUserID(), authority);
        System.out.println("查询到" + count);
        if (count > 0) return true; else return false;
    }

    @Transactional
    @Override
    public void LoginOut() {
        this.request.getSession().removeAttribute(USER_SYS);
    }

    public StudentDAO getStudentDAO() {
        return studentDAO;
    }

    public void setStudentDAO(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    public RoleDAO getRoleDAO() {
        return roleDAO;
    }

    public void setRoleDAO(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }
}
