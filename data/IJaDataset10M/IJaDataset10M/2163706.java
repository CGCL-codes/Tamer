package net.sourceforge.customercare.client.server.entities.phonenumber;

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
 * phonenumver logic-class
 */
public class PhonenumberLogic extends UnicastRemoteObject implements EntityLogic {

    private static final long serialVersionUID = 1L;

    private PhonenumberCore core;

    public PhonenumberLogic(PhonenumberCore core) throws RemoteException {
        this.core = core;
    }

    public Entry create() throws CustomerCareException {
        Phonenumber pnb = new Phonenumber();
        pnb.setId(Calculator.getNext(core.getConnection(), "tbl_phonenumber", "pnb_id"));
        core.insert(pnb);
        return pnb;
    }

    public void save(Entry entry) throws CustomerCareException {
        Phonenumber pnb = (Phonenumber) entry;
        if (hasChanged(pnb)) throw new CustomerCareException(CustomerCareException.ENTRY_CHANGED); else {
            pnb.setChgctr(pnb.getChgctr() + 1);
            core.update(entry);
        }
    }

    public void remove(Integer id) throws CustomerCareException {
        core.delete(id);
    }

    public Entry get(Integer id) throws CustomerCareException {
        Phonenumber pnb = new Phonenumber();
        pnb.setId(id);
        ResultSet rs = core.select(pnb);
        try {
            if (rs.next()) {
                pnb.setPhonenumber(rs.getString(2));
                pnb.setPntId(rs.getInt(3));
                pnb.setChgctr(rs.getInt(4));
            } else {
                throw new CustomerCareException(CustomerCareException.NO_ENTRY_FOUND);
            }
        } catch (SQLException sqlEx) {
            throw new CustomerCareException(CustomerCareException.DATABASE_ERROR);
        }
        return pnb;
    }

    public Iterator<Entry> getAll() throws CustomerCareException {
        Phonenumber pnb = new Phonenumber();
        ResultSet rs = core.select(pnb);
        ArrayList<Entry> arl = new ArrayList<Entry>(0);
        try {
            while (rs.next()) {
                Phonenumber pnbTemp = new Phonenumber();
                pnbTemp.setId(rs.getInt(1));
                pnbTemp.setPhonenumber(rs.getString(2));
                pnbTemp.setPntId(rs.getInt(3));
                pnbTemp.setChgctr(rs.getInt(4));
                arl.add(pnbTemp);
            }
        } catch (SQLException sqlEx) {
            throw new CustomerCareException(CustomerCareException.DATABASE_ERROR);
        }
        return arl.iterator();
    }

    private boolean hasChanged(Phonenumber pnb) throws CustomerCareException {
        boolean changed = true;
        Phonenumber current = (Phonenumber) get(pnb.getId());
        if (current.getChgctr().equals(pnb.getChgctr())) changed = false;
        return changed;
    }
}
