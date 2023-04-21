package cn.feigme.identity.service;

import java.util.List;
import org.apache.log4j.Logger;
import org.apache.lucene.queryParser.ParseException;
import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;
import cn.feigme.identity.dao.LoginHistoryDao;
import cn.feigme.identity.dao.UserDao;
import cn.feigme.identity.model.LoginHistory;
import cn.feigme.identity.model.User;

public class UserServiceImpl implements UserService {

    private static Logger logger = Logger.getLogger(UserServiceImpl.class);

    private UserDao userDao;

    private LoginHistoryDao loginHistoryDao;

    /**
	 * @return the userDao
	 */
    public UserDao getUserDao() {
        return userDao;
    }

    /**
	 * @param userDao the userDao to set
	 */
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
	 * @return the loginHistoryDao
	 */
    public LoginHistoryDao getLoginHistoryDao() {
        return loginHistoryDao;
    }

    /**
	 * @param loginHistoryDao the loginHistoryDao to set
	 */
    public void setLoginHistoryDao(LoginHistoryDao loginHistoryDao) {
        this.loginHistoryDao = loginHistoryDao;
    }

    @Transactional
    public User getUser(String id) {
        return getUserDao().find(id);
    }

    @Transactional
    public void saveHistory(LoginHistory loginHistory) {
        long startTime = System.currentTimeMillis();
        getLoginHistoryDao().save(loginHistory);
        logger.info("process time : " + (System.currentTimeMillis() - startTime) / 1000 + "s");
    }

    @Transactional
    public String register(User user) {
        long startTime = System.currentTimeMillis();
        this.getUserDao().save(user);
        logger.info("process time : " + (System.currentTimeMillis() - startTime) / 1000 + "s");
        return user.getId();
    }

    @Transactional
    public void modifyRegisterInfo(User user) {
        long startTime = System.currentTimeMillis();
        this.getUserDao().update(user);
        logger.info("process time : " + (System.currentTimeMillis() - startTime) / 1000 + "s");
    }

    @Transactional
    public List<User> login(String email, String password) {
        long startTime = System.currentTimeMillis();
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        List<User> users = this.getUserDao().getUsersByExample(user);
        logger.info("process time : " + (System.currentTimeMillis() - startTime) / 1000 + "s");
        return users;
    }

    @Transactional
    public User getLoginHistroy(User user) {
        long startTime = System.currentTimeMillis();
        User back = this.userDao.find(user.getId());
        Hibernate.initialize(back.getLoginHistorys());
        logger.info("process time : " + (System.currentTimeMillis() - startTime) / 1000 + "s");
        return back;
    }

    public boolean checkUserExist(String email) {
        long startTime = System.currentTimeMillis();
        User user = new User();
        user.setEmail(email);
        List<User> users = this.userDao.getUsersByExample(user);
        logger.info("process time : " + (System.currentTimeMillis() - startTime) / 1000 + "s");
        if (users.size() > 0) return true;
        return false;
    }

    public List testLucene(String str) throws ParseException {
        return this.getUserDao().testLucene(str);
    }
}
