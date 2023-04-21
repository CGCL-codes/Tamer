package com.jdesk.dao;

import com.jdesk.model.Address;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author pedro
 */
public class AddressDAO extends BaseDAOFactory<Address> implements Serializable {

    private Session session;

    public AddressDAO(Session ses) {
        this.session = ses;
    }

    public AddressDAO() {
        this.session = getSession();
    }

    public int addAddress(Address addrs) {
        saveOrUpdatePojo(addrs);
        return addrs.getID();
    }

    public Object getAddressById(String addrsId) {
        Address addrs = getPojo(Address.class, new Integer(addrsId));
        return addrs;
    }

    public void removeAddress(Address addrs) {
        removePojo(addrs);
    }

    public void updateAddress(Address addrs) {
        updateAddress(addrs);
    }

    public Address updateAddress(int addrsId) {
        Address addrs = getPojo(Address.class, addrsId);
        return addrs;
    }

    public List<Address> getAddress() {
        return getPurePojo(Address.class, "from Address addrs");
    }

    public List<Address> getAddressByUserID(String userID) {
        System.out.println("[AddressDAO] - userID " + userID);
        return getPojoList("from Address addrs where addrs.userID = ?", userID);
    }
}
