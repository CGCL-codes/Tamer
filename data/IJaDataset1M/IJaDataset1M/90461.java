package cmsc128.proj.iterc.dao;

import java.util.ArrayList;
import java.util.List;
import org.apache.torque.util.Criteria;

public class UserPeer extends cmsc128.proj.iterc.dao.BaseUserPeer {

    public static boolean createUser(String username, String password, String firstname, String middleinitial, String lastname, String contact, String address, String type) {
        Criteria criteria = new Criteria();
        criteria.add(UserPeer.USERNAME, username);
        criteria.add(UserPeer.PASSWORD, password);
        criteria.add(UserPeer.FIRSTNAME, firstname);
        criteria.add(UserPeer.MIDDLEINITIAL, middleinitial);
        criteria.add(UserPeer.LASTNAME, lastname);
        criteria.add(UserPeer.CONTACT, contact);
        criteria.add(UserPeer.ADDRESS, address);
        criteria.add(UserPeer.TYPE, type);
        try {
            UserPeer.doInsert(criteria);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteUser(String username) {
        Criteria criteria = new Criteria();
        criteria.add(UserPeer.USERNAME, username);
        try {
            UserPeer.doDelete(criteria);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<User> retrieveUsers() {
        Criteria criteria = new Criteria();
        try {
            return (List<User>) UserPeer.doSelect(criteria);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<User>();
        }
    }

    public static User retrieveUser(String username) {
        Criteria criteria = new Criteria();
        criteria.add(UserPeer.USERNAME, username);
        try {
            return (User) UserPeer.doSelect(criteria).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static User loginUser(String username, String password) {
        Criteria criteria = new Criteria();
        criteria.add(UserPeer.USERNAME, username);
        criteria.add(UserPeer.PASSWORD, password);
        try {
            System.out.println((User) UserPeer.doSelect(criteria).get(0));
            return (User) UserPeer.doSelect(criteria).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
