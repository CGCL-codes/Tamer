package fi.hip.gb.gridbank.db.SQL;

import java.util.*;
import fi.hip.gb.gridbank.AccountOwner;
import fi.hip.gb.gridbank.GridBankAccount;
import fi.hip.gb.gridbank.cheques.GridCheque;
import fi.hip.gb.gridbank.db.DBException;
import fi.hip.gb.gridbank.db.OpenChequeStore;
import fi.hip.gb.gridbank.db.SQL.MySqlConnection;
import fi.hip.gb.gridbank.db.inmemory.OpenChequeStoreInMemoryImpl;
import java.sql.*;

/**
 * Database implementation for OpenChequeStore, not fully tested... 
 * 
 * @author Antti.Solonen@cern.ch
 * @version $Id:
 */
public class OpenChequeStoreDBImpl implements OpenChequeStore {

    private static OpenChequeStore store = null;

    MySqlConnection mycon = null;

    /**
	 * Opens a new database connection for the instance
	 */
    public OpenChequeStoreDBImpl() throws DBException {
        MySqlSettings set = MySqlSettings.getSettings();
        try {
            this.mycon = new MySqlConnection(set);
        } catch (SQLException e) {
            throw new DBException("Database connection failed", e);
        }
    }

    /**
	 * Uses an existing database connection instance
	 * @param mycon
	 */
    public OpenChequeStoreDBImpl(MySqlConnection mycon) {
        this.mycon = mycon;
    }

    /**
	 * Returns OpenChequeStore -instance if possible, null otherwise
	 */
    public static synchronized OpenChequeStore getInstance() {
        if (store == null) {
            try {
                store = new OpenChequeStoreDBImpl();
            } catch (DBException e) {
                return null;
            }
        }
        return store;
    }

    /**
	 * Stores a cheque to the database.
	 * The certificate is stored in binary form.
	 * Here accountID is static for testing purposes... NOT TESTED PROPERLY 
	 * (cheque.getAccountId() doesn't work)
	 * @param owner
	 * @throws DBException 		If the query fails. 
	 */
    public void storeOpenCheque(GridCheque cheque) throws DBException {
        try {
            String accountID = cheque.getAccountId();
            String chequeID = cheque.getSerialNumber().toString();
            byte[] encodedCert = cheque.getEncoded();
            String query = "INSERT INTO Cheques SET issuer_serialNumber='" + chequeID + "', chequeAccountID='" + accountID + "'";
            this.mycon.SQLUpdate(query);
            PreparedStatement ps = mycon.con.prepareStatement("UPDATE Cheques SET encodedCert = ? WHERE issuer_serialNumber = '" + chequeID + "'");
            ps.setBytes(1, encodedCert);
            int ret = ps.executeUpdate();
        } catch (Exception e) {
            throw new DBException("Error in storing a cheque: " + e.getMessage(), e);
        }
    }

    /**
	 * Removes a cheque from the database.
	 * @param owner
	 * @throws DBException 		If the query fails. 
	 */
    public void removeOpenCheque(GridCheque cheque) throws DBException {
        try {
            String chequeID = cheque.getSerialNumber().toString();
            String query = "DELETE FROM Cheques WHERE issuer_serialNumber='" + chequeID + "'";
            this.mycon.SQLUpdate(query);
        } catch (Exception e) {
            throw new DBException("Removing the Cheque failed", e);
        }
    }

    /**
	 * Stores all open cheques of the given account to the account.
	 * @throws DBException 
	 */
    public void getOpenCheques(GridBankAccount account) throws DBException {
        GridCheque[] all = this.getOpenCheques();
        try {
            for (GridCheque gc : all) {
                if (gc.getAccountId().equals(account.getAccountId())) {
                    account.addFundsProvision(gc);
                }
            }
        } catch (Exception e) {
            throw new DBException("Could not make funds provisions.", e);
        }
    }

    /**
	 * @return All open cheques of the given <code>AccountOwner</code>,
	 * null if not possible (error(s)) 
	 */
    public GridCheque[] getOpenCheques(AccountOwner owner) throws DBException {
        Vector<GridCheque> returnable = new Vector<GridCheque>();
        GridCheque[] all = this.getOpenCheques();
        for (GridCheque gc : all) {
            if (gc.matchHolder(owner.getUserCert())) {
                returnable.add(gc);
            }
        }
        return returnable.toArray(new GridCheque[0]);
    }

    /**
	 * @return All open cheques or null if error(s) 
	 */
    public GridCheque[] getOpenCheques() throws DBException {
        try {
            Vector<GridCheque> returnable = new Vector<GridCheque>();
            String query = "SELECT * from Cheques";
            ResultSet rs = this.mycon.SQLQuery(query);
            while (rs.next()) {
                GridCheque add = new GridCheque(rs.getBytes("encodedCert"));
                add.setAccountId(rs.getString("chequeAccountID"));
                returnable.add(add);
            }
            return returnable.toArray(new GridCheque[0]);
        } catch (Exception e) {
            return null;
        }
    }

    public GridCheque getOpenCheque(String serial) throws DBException {
        try {
            String query = "SELECT * FROM Cheques WHERE issuer_serialNumber='" + serial + "'";
            ResultSet rs = this.mycon.SQLQuery(query);
            while (rs.next()) {
                GridCheque add = new GridCheque(rs.getBytes("encodedCert"));
                add.setAccountId(rs.getString("chequeAccountID"));
                return add;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
