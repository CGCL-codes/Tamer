package net.sourceforge.customercare.client.server.entities.user;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import net.sourceforge.customercare.client.server.entities.EntityLogic;
import net.sourceforge.customercare.client.server.entities.Entry;
import net.sourceforge.customercare.client.server.exceptions.CustomerCareException;
import net.sourceforge.customercare.client.server.helpers.Calculator;

/**
 * user logic-class
 */
public class UserLogic extends UnicastRemoteObject implements EntityLogic {

    private static final long serialVersionUID = 1L;

    private UserCore core;

    public UserLogic(UserCore core) throws RemoteException {
        this.core = core;
    }

    public Entry create() throws CustomerCareException {
        User usr = new User();
        usr.setId(Calculator.getNext(core.getConnection(), "tbl_user", "usr_id"));
        core.insert(usr);
        return usr;
    }

    public void save(Entry entry) throws CustomerCareException {
        User usr = (User) entry;
        if (hasChanged(usr)) throw new CustomerCareException(CustomerCareException.ENTRY_CHANGED); else {
            usr.setChgctr(usr.getChgctr() + 1);
            core.update(entry);
        }
    }

    public void remove(Integer id) throws CustomerCareException {
        core.delete(id);
    }

    public Entry get(Integer id) throws CustomerCareException {
        User usr = new User();
        usr.setId(id);
        ResultSet rs = core.select(usr);
        try {
            if (rs.next()) {
                usr.setUsername(rs.getString(2));
                usr.setPassword(rs.getString(3), false);
                usr.setRolId(rs.getInt(4));
                usr.setSplId(rs.getInt(5));
                usr.setPerId(rs.getInt(6));
                usr.setChgctr(rs.getInt(7));
            } else {
                throw new CustomerCareException(CustomerCareException.NO_ENTRY_FOUND);
            }
        } catch (SQLException sqlEx) {
            throw new CustomerCareException(CustomerCareException.DATABASE_ERROR);
        }
        return usr;
    }

    public Iterator<Entry> getAll() throws CustomerCareException {
        User usr = new User();
        ResultSet rs = core.select(usr);
        ArrayList<Entry> arl = new ArrayList<Entry>(0);
        try {
            while (rs.next()) {
                User usrTemp = new User();
                usrTemp.setId(rs.getInt(1));
                usrTemp.setUsername(rs.getString(2));
                usrTemp.setPassword(rs.getString(3), false);
                usrTemp.setRolId(rs.getInt(4));
                usrTemp.setSplId(rs.getInt(5));
                usrTemp.setPerId(rs.getInt(6));
                usrTemp.setChgctr(rs.getInt(7));
                arl.add(usrTemp);
            }
        } catch (SQLException sqlEx) {
            throw new CustomerCareException(CustomerCareException.DATABASE_ERROR);
        }
        return arl.iterator();
    }

    private boolean hasChanged(User usr) throws CustomerCareException {
        boolean changed = true;
        User current = (User) get(usr.getId());
        if (current.getChgctr().equals(usr.getChgctr())) changed = false;
        return changed;
    }
}
