package com.cube42.echoverse.account;

import java.rmi.RemoteException;
import com.cube42.echoverse.entity.EntityManager;
import com.cube42.echoverse.model.EntityIDCollection;
import com.cube42.util.connection.Connector;
import com.cube42.util.connection.CoreConnectionListener;
import com.cube42.util.exception.Cube42Exception;
import com.cube42.util.exception.Cube42NullParameterException;
import com.cube42.util.gui.SubSystemControlPanel;
import com.cube42.util.logging.LogSeverity;
import com.cube42.util.system.CoreID;
import com.cube42.util.system.RemoteSubSystemCore;
import com.cube42.util.system.SubSystemCoreFoundation;
import com.cube42.util.system.SubSystemID;
import com.cube42.util.system.SubSystemStatus;

/**
 * Implementation of the AccountManager
 *
 * @author Matt Paulin
 * @version $Id: AccountManagerImpl.java,v 1.2 2003/03/12 01:48:34 zer0wing Exp $
 */
public class AccountManagerImpl extends SubSystemCoreFoundation implements AccountManager, CoreConnectionListener {

    /**
     * Reference to the EntityManager
     */
    private EntityManager entityManager;

    /**
     * Account store used to store all the accounts
     */
    private AccountStore accountStore;

    /**
     * Entity shell store used to store all the entityShells
     */
    private EntityShellStore entityShellStore;

    /**
     * Initializes the subsystem core
     */
    public void initSubSystemCore() {
        this.accountStore = new AccountStore();
        this.entityShellStore = new EntityShellStore();
        Connector.registerCoreConnectionListener(this, EntityManager.SUBSYSTEM_ID);
    }

    /**
     * Returns the entity shell with the specified name
     * <p>
     * Returns null if the entity shell doesn't exist
     *
     * @param   name    The name for the entity shell desired
     * @throws  RemoteException if a network error occurs
     */
    public EntityShell getEntityShell(String name) throws RemoteException {
        try {
            return this.entityShellStore.getEntityShell(name);
        } catch (Cube42Exception e) {
            e.log(LogSeverity.ERROR);
            return null;
        }
    }

    /**
     * Returns an EntityShellCollection of all the entityShells available
     *
     * @return  EntityShellCollection of all the entityShells available
     * @throws  Cube42Exception if this is not possible
     * @throws  RemoteException if a network error occurs
     */
    public EntityShellCollection getEntityShells() throws RemoteException, Cube42Exception {
        return this.entityShellStore.getEntityShells();
    }

    /**
     * Creates a new entity shell
     *
     * @param   entityShell     The new entityShell to add to the store
     * @throws  RemoteException if a network error occurs
     * @throws  Cube42Exception if the entity shell already exist
     */
    public void createNewEntityShell(EntityShell entityShell) throws Cube42Exception, RemoteException {
        this.entityShellStore.createNewEntityShell(entityShell);
        this.update();
    }

    /**
     * Deletes the specified entity shell
     *
     * @param   entity shell    The entity shell to delete
     * @throws  RemoteException if a network error occurs
     * @throws  Cube42Exception if no entity shell like this exist
     */
    public void deleteEntityShell(EntityShell entityShell) throws Cube42Exception, RemoteException {
        this.entityShellStore.deleteEntityShell(entityShell);
        this.update();
    }

    /**
     * Deletes all entity shells
     *
     * @throws  RemoteException if a network error occurs
     * @throws  Cube42Exception if this is not possible
     */
    public void deleteAllEntityShells() throws RemoteException, Cube42Exception {
        this.entityShellStore.deleteAllEntityShells();
        this.update();
    }

    /**
     * Updates the specified entityShell with the new information
     *
     * @param   entityShell     The entityShell to update
     * @throws  RemoteException if a network error occurs
     * @throws  Cube42Exception if this entityShell doesn't exist
     */
    public void updateEntityShell(EntityShell entityShell) throws Cube42Exception, RemoteException {
        this.entityShellStore.updateEntityShell(entityShell);
        this.update();
    }

    /**
     * Reserves the specified shell for the stated account
     *
     * @param   shellName   The shell name to reserve
     * @param   account     The account reserving the shell
     * @throws  Cube42Exception if this is not possible
     * @throws  RemoteException if a network error occurs
     */
    public EntityShell reserveShell(String shellName, Account account) throws Cube42Exception, RemoteException {
        Cube42NullParameterException.checkNull(shellName, "shellName", "reserveShell", this);
        Cube42NullParameterException.checkNull(account, "account", "reserveShell", this);
        if (this.accountStore.getAccount(account.getUsername()) == null) {
            throw new Cube42Exception(AccountSystemCodes.ACCOUNT_DOES_NOT_EXIST, new Object[] { account.getUsername() });
        }
        EntityShell tempShell = this.entityShellStore.reserveShell(shellName, account);
        update();
        return tempShell;
    }

    /**
     * Releases the specified shell from the account holding it
     *
     * @param   shellName   The shell the account was using
     * @param   account     The account using the shell
     * @throws  Cube42Exception if this is not possible
     * @throws  RemoteException if a network error occurs
     */
    public void releaseShell(String shellName, Account account) throws Cube42Exception, RemoteException {
        Cube42NullParameterException.checkNull(shellName, "shellName", "releaseShell", this);
        Cube42NullParameterException.checkNull(account, "account", "releaseShell", this);
        if (this.accountStore.getAccount(account.getUsername()) == null) {
            throw new Cube42Exception(AccountSystemCodes.ACCOUNT_DOES_NOT_EXIST, new Object[] { account.getUsername() });
        }
        this.entityShellStore.releaseShell(shellName, account);
        update();
    }

    /**
     * Resets the specified shell
     *
     * @param   shellName   The shell the account was using
     * @throws  Cube42Exception if this is not possible
     * @throws  RemoteException if a network error occurs
     */
    public void resetShell(String shellName) throws Cube42Exception, RemoteException {
        this.entityShellStore.resetShell(shellName);
        update();
    }

    /**
     * Returns a collection of EntityIDs of all the controlled entities
     * in this manager
     *
     * @return  EntityIDCollection representing all the entities contained
     *          in this entity manager
     * @throws  RemoteException if an error occurs
     * @throws  Cube42Exception if the entity manager is unavailable
     */
    public EntityIDCollection getControlledEntities() throws RemoteException, Cube42Exception {
        if (this.entityManager == null) {
            throw new Cube42Exception(AccountSystemCodes.ENTITY_MANAGER_UNAVAILABLE, new Object[] { "getControlledEntities" });
        } else {
            try {
                return this.entityManager.getControlledEntities();
            } catch (RemoteException e) {
                Connector.reportLostCore(EntityManager.SUBSYSTEM_ID);
                throw new Cube42Exception(AccountSystemCodes.ENTITY_MANAGER_UNAVAILABLE, new Object[] { "getControlledEntities" });
            }
        }
    }

    /**
     * Verifies that an account is availible for the specified username and
     * password
     *
     * @param   username    The username to check
     * @param   password    The password to check
     * @returns True if an account is available for the specified username
     *          and password
     * @throws  Cube42Exception if this is not possible
     * @throws  RemoteException if a network error occurs
     */
    public boolean verifyAccount(String username, String password) throws RemoteException, Cube42Exception {
        Cube42NullParameterException.checkNull(username, "username", "verifyAccount", this);
        Cube42NullParameterException.checkNull(password, "password", "verifyAccount", this);
        Account tempAccount = this.accountStore.getAccount(username);
        if (tempAccount == null) return false;
        return tempAccount.getPassword().equals(password);
    }

    /**
     * Returns an AccountCollection of all the accounts available
     *
     * @return  AccountCollection of all the accounts available
     * @throws  Cube42Exception if this is not possible
     * @throws  RemoteException if a network error occurs
     */
    public AccountCollection getAccounts() throws RemoteException, Cube42Exception {
        return this.accountStore.getAccounts();
    }

    /**
     * Returns the account that matches the specified username
     * <p>
     * Null if the account could not be found
     *
     * @param   username    The username for the account desired
     * @throws  RemoteException if a network error occurs
     */
    public Account getAccount(String username) throws RemoteException {
        try {
            return this.accountStore.getAccount(username);
        } catch (Cube42Exception e) {
            e.log(LogSeverity.ERROR);
            return null;
        }
    }

    /**
     * Creates a new account
     *
     * @param   account     The new account to add to the store
     * @throws  RemoteException if a network error occurs
     * @throws  Cube42Exception if the account already exist
     */
    public void createNewAccount(Account account) throws Cube42Exception, RemoteException {
        this.accountStore.createNewAccount(account);
        this.update();
    }

    /**
     * Deletes the specified account
     *
     * @param   account     The account to delete
     * @throws  RemoteException if a network error occurs
     * @throws  Cube42Exception if no account like this exist
     */
    public void deleteAccount(Account account) throws Cube42Exception, RemoteException {
        this.accountStore.deleteAccount(account);
        this.update();
    }

    /**
     * Deletes all accounts
     *
     * @throws  RemoteException if a network error occurs
     * @throws  Cube42Exception if this is not possible
     */
    public void deleteAllAccounts() throws RemoteException, Cube42Exception {
        this.accountStore.deleteAllAccounts();
        this.update();
    }

    /**
     * Updates the specified account with the new information
     *
     * @param   account     The account to update
     * @throws  RemoteException if a network error occurs
     * @throws  Cube42Exception if this account doesn't exist
     */
    public void updateAccount(Account account) throws Cube42Exception, RemoteException {
        this.accountStore.updateAccount(account);
        this.update();
    }

    /**
     * Returns the SubSystemID
     *
     * @return  The SubSystemID of the subsystem.
     */
    public SubSystemID getSubSystemID() {
        return AccountManager.SUBSYSTEM_ID;
    }

    /**
     * Tells the subsystem to shutdown
     *
     * @throws  A remote exception if a network error occurs
     */
    public void shutdownSubSystemCore() {
        this.accountStore.shutdown();
    }

    /**
     * Returns the SubSystemControlPanel used to control the AccountManager
     *
     * @return  The SubSystemControlPanel used to control the AccountManager
     */
    public SubSystemControlPanel getSubSystemControlPanel() {
        return new AccountManagerControlPanel();
    }

    /**
     * Returns the status of the remote subsystem
     *
     * @return  The status of the RemoteSubSystem
     */
    public SubSystemStatus getStatus() {
        SubSystemStatus status = new SubSystemStatus(this.getSubSystemID());
        status.addCoreDependency(EntityManager.SUBSYSTEM_ID);
        status.setDatabaseStatus(this.accountStore.getStatus());
        return status;
    }

    /**
     * Tells the listener that a connection to the specified SubSystem
     * has been made
     *
     * @param   coreID      The SubSystemCore that was connected too
     * @param   core        The RemoteSubSystemCore contacted
     */
    public void connectionMade(CoreID coreID, RemoteSubSystemCore core) {
        if (EntityManager.SUBSYSTEM_ID.equals(coreID.getSubSystemID())) {
            this.entityManager = (EntityManager) core;
            update();
        }
    }

    /**
     * Notifies the listener that the connection to the specified SubSystem
     * core has been lost
     *
     * @param   subSystemID     The SubSystem that lost the core
     */
    public void connectionLost(SubSystemID subSystemID) {
        if (EntityManager.SUBSYSTEM_ID.equals(subSystemID)) {
            this.entityManager = null;
        }
    }
}
