package net.sourceforge.customercare.client.server.entities.person;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import net.sourceforge.customercare.server.entities.EntityLogic;
import net.sourceforge.customercare.server.entities.Entry;
import net.sourceforge.customercare.server.entities.SearchEntry;
import net.sourceforge.customercare.server.entities.Searchable;
import net.sourceforge.customercare.server.exceptions.CustomerCareException;
import net.sourceforge.customercare.server.helpers.Calculator;

public class PersonLogic extends UnicastRemoteObject implements EntityLogic, Searchable {

    private static final long serialVersionUID = 1L;

    private PersonCore core;

    public PersonLogic(PersonCore core) throws RemoteException {
        this.core = core;
    }

    public Entry create() throws CustomerCareException {
        Person per = new Person();
        per.setId(Calculator.getNext(core.getConnection(), "tbl_person", "per_id"));
        core.insert(per);
        return per;
    }

    public void save(Entry entry) throws CustomerCareException {
        Person per = (Person) entry;
        if (hasChanged(per)) throw new CustomerCareException(CustomerCareException.ENTRY_CHANGED); else {
            per.setChgctr(per.getChgctr() + 1);
            core.update(entry);
        }
    }

    public void remove(Integer id) throws CustomerCareException {
        core.delete(id);
    }

    public Entry get(Integer id) throws CustomerCareException {
        Person per = new Person();
        per.setId(id);
        ResultSet rs = core.select(per);
        try {
            if (rs.next()) per = map(rs); else throw new CustomerCareException(CustomerCareException.NO_ENTRY_FOUND);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            throw new CustomerCareException(CustomerCareException.DATABASE_ERROR);
        }
        return per;
    }

    public Iterator<Entry> getAll() throws CustomerCareException {
        Person per = new Person();
        ResultSet rs = core.select(per);
        ArrayList<Entry> arl = new ArrayList<Entry>(0);
        try {
            while (rs.next()) {
                Person perTemp = map(rs);
                arl.add(perTemp);
            }
        } catch (SQLException sqlEx) {
            throw new CustomerCareException(CustomerCareException.DATABASE_ERROR);
        }
        return arl.iterator();
    }

    public Iterator<Entry> search(SearchEntry searchEntry) throws RemoteException {
        ResultSet rs = core.search(searchEntry);
        ArrayList<Entry> arl = new ArrayList<Entry>(0);
        try {
            while (rs.next()) {
                Person perTemp = map(rs);
                arl.add(perTemp);
            }
        } catch (SQLException sqlEx) {
            throw new CustomerCareException(CustomerCareException.DATABASE_ERROR);
        }
        return arl.iterator();
    }

    private boolean hasChanged(Person per) throws CustomerCareException {
        boolean changed = true;
        Person current = (Person) get(per.getId());
        if (current.getChgctr().equals(per.getChgctr())) changed = false;
        return changed;
    }

    private Person map(ResultSet rs) throws SQLException {
        Person per = new Person();
        per.setId(rs.getInt(1));
        per.setEmployeeNumber(rs.getInt(2));
        per.setTitle(rs.getString(3));
        per.setFirstname(rs.getString(4));
        per.setLastname(rs.getString(5));
        per.setRole(rs.getString(6));
        per.setEmailaddress(rs.getString(7));
        per.setGrpId(rs.getInt(8));
        per.setAdrId(rs.getInt(9));
        per.setPnbId(rs.getInt(10));
        per.setChgctr(rs.getInt(11));
        return per;
    }
}
