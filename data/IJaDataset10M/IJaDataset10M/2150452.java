package com.fantosoft.admin.userdata;

import java.util.List;
import java.util.Properties;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import com.db4o.query.Query;
import com.fantosoft.admin.common.ICallbackUtility;
import com.fantosoft.admin.userunit.IUser;
import com.fantosoft.admin.userunit.IUserManager;
import com.fantosoft.db4o.Db4oConst;
import com.fantosoft.db4o.Db4oUtil;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UserManager implements IUserManager {

    private ObjectContainer db;

    private ICallbackUtility cu;

    private boolean isConfiged = false;

    private boolean needConfiged = false;

    public UserManager() {
    }

    public UserManager(ObjectContainer db) {
        this.db = db;
        isConfiged = true;
    }

    /**
     * @return Returns the isConfiged.
     */
    public boolean isConfiged() {
        return isConfiged;
    }

    public void configure(final ICallbackUtility callbackUtility) {
        cu = callbackUtility;
        if (needConfiged) {
            Properties callbackProperties = cu.getProperties();
            String filepath = cu.getProperties().getProperty(Db4oConst.dbfile);
            db = Db4oUtil.getObjectServer(filepath).openClient();
            isConfiged = true;
            needConfiged = false;
        }
    }

    public void checkConfig() {
        if (isConfiged) return; else {
            needConfiged = true;
            configure(cu);
        }
    }

    public void addUser(IUser user) {
        checkConfig();
        db.set(user);
        db.commit();
    }

    public void updateUser(IUser user) {
        checkConfig();
        db.set(user);
        db.commit();
    }

    /**
     * ʵ�ֽӿ� UserManager�ж���ķ���������֤�ܹ�ͨ��ͬ�Ļ��Ƶõ��û�
     */
    public IUser findUserByCode(final String userCode) {
        checkConfig();
        Query query = db.query();
        query.constrain(User.class);
        query.descend("userCode").constrain(userCode);
        ObjectSet resultSet = query.execute();
        if (resultSet.hasNext()) {
            return (User) resultSet.next();
        }
        return null;
    }

    public IUser findUser(final Long userId) {
        checkConfig();
        Query query = db.query();
        query.constrain(User.class);
        query.descend("userId").constrain(userId);
        ObjectSet resultSet = query.execute();
        if (resultSet.hasNext()) {
            return (User) resultSet.next();
        }
        return null;
    }

    public List findAllUser() {
        checkConfig();
        List result = null;
        Query query = db.query();
        query.constrain(User.class);
        ObjectSet userList = query.execute();
        if (userList.size() > 0) {
            result = userList.subList(0, userList.size());
        }
        return result;
    }

    public void destoryUser(IUser user) {
        checkConfig();
        db.delete(user);
        db.commit();
    }

    public void finalize() {
        this.db = null;
    }

    public void delAllUser() {
        checkConfig();
        List result = null;
        Query query = db.query();
        query.constrain(User.class);
        ObjectSet userList = query.execute();
        while (userList.hasNext()) {
            destoryUser((User) userList.next());
        }
    }

    public void listUser() {
        checkConfig();
        Query query = db.query();
        query.constrain(User.class);
        ObjectSet userList = query.execute();
        int i = 0;
        while (userList.hasNext()) {
            i++;
            User temp = (User) userList.next();
            System.out.println(temp);
        }
        System.out.println("Now have " + i + " users.");
    }

    public boolean authenticate(String userCode, String pwd) {
        checkConfig();
        IUser user = findUserByCode(userCode);
        if (user.getPassword().equals(pwd)) return true; else return false;
    }

    public synchronized boolean authenticate(final IUser user, final String pwd) {
        if (user.getPassword().equals(pwd)) return true; else return false;
    }
}
