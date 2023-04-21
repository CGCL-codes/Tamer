package com.nhn.ssi.bo;

import java.sql.SQLException;
import java.util.List;
import com.nhn.ssi.bean.Order;
import com.nhn.ssi.bean.UserBean;
import com.nhn.ssi.dao.TestDao;
import com.nhn.ssi.util.Pager;

public class TestBoImpl implements TestBo {

    TestDao testDao;

    public TestDao getTestDao() {
        return testDao;
    }

    public void setTestDao(TestDao testDao) {
        this.testDao = testDao;
    }

    public List getAllUserList() throws Exception {
        return testDao.findAllUserList();
    }

    public List getUserList(Pager pager) throws Exception {
        return testDao.findUserList(pager);
    }

    public int getUserListSize() throws Exception {
        return testDao.getUserListSize();
    }

    public UserBean getUserById(String id) throws Exception {
        return testDao.findUserById(id);
    }

    public void addUser(UserBean userBean) throws Exception {
        testDao.addUser(userBean);
        if ("123".equals(userBean.getPassword())) throw new SQLException();
        UserBean ub = new UserBean();
        ub.setId("11111");
        ub.setUser_name("11111");
        ub.setEmail("11111");
        ub.setPassword("11111");
        testDao.addUser(ub);
    }

    public void modifyUser(UserBean userBean) throws Exception {
        testDao.modifyUser(userBean);
    }

    public void deleteUser(String data) throws Exception {
        testDao.deleteUser(data);
    }

    public boolean login(String username, String password) throws Exception {
        UserBean userBean = testDao.login(username, password);
        if (userBean == null) return false; else return true;
    }

    @Override
    public List<Order> getOrderList() throws SQLException {
        return testDao.getOrderList(null);
    }
}
