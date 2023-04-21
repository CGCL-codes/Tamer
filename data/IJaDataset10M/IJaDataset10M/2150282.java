package DAO;

import Types.AccountType;

/**
 * The interface for the Account Table contains the methods for the sqls that interact with Account
 * @author Jason and Sam
 */
public interface AccountDAO {

    /**
     * Checkes the database for the Account identified by uname and pass, all of info from that row
     * is returned as AccountType
     * @param uname
     * @param pass
     * @return AccountType
     */
    public AccountType validAccount(String uname, String pass);

    /**
     * Added the user to the database
     * @param uname
     * @param pass
     * @param fname
     * @param sname
     * @param address
     */
    public void registerUser(String uname, String pass, String fname, String sname, String address);

    /**
     * Class to delete the account specified by uname
     * @param uname
     * @return
     */
    public boolean deleteAccount(String uname);

    /**
     * Will check if there is already and account identified by uname in the account table
     * @param uname
     * @return
     */
    public boolean checkAvailability(String uname);
}
