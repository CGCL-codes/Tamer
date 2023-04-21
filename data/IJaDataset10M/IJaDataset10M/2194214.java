package com.gorillalogic.accounts.base;

import com.gorillalogic.accounts.*;
import com.gorillalogic.accounts.authentication.GXETicket;
import com.gorillalogic.dal.*;
import com.gorillalogic.dal.model.Entity;
import com.gorillalogic.dal.model.Principal;
import com.gorillalogic.dal.common.*;

public class AccountsFactory implements GXEAccount.Factory, GXERole.Factory, GXESession.Factory {

    public static final AccountsFactory singleton = new AccountsFactory();

    private boolean inited = false;

    private BaseSession mainSession = null;

    private AccountsFactory() {
    }

    /**
	 * Startup initialization, called once from the dal.
	 *
	 * @param pkg the /accounts <code>PkgTable</code> 
	 * @return the session <code>Table</code> that will then
	 * be added to pkg
	 * @exception AccessException if an error occurs
	 */
    public Table initAccounts(CommonPkgTable pkg, CommonDomain.DomainStrategy ds) throws AccessException {
        if (!inited) {
            inited = true;
            doInitAccounts(pkg, ds);
        }
        return SessionTable.current();
    }

    private void doInitAccounts(CommonPkgTable pkg, CommonDomain.DomainStrategy ds) throws AccessException {
        SessionTable.initSessionTable(pkg, ds);
        SessionTable.current().initAssociations(pkg);
        Table accountsTable = pkg.getExtent(GXEAccount.TYPE_NAME);
        Table.Row admin = accountsTable.extend(true).addRow();
        admin.setString(GXEAccount.USERID_COLUMN_NAME, GXEAccount.ADMIN_KEY);
        Table roleTable = pkg.getExtent(GXERole.TYPE_NAME);
        Table.Row sysad = roleTable.extend(true).addRow();
        sysad.setString(GXERole.NAME_COLUMN_NAME, GXERole.ADMIN_KEY);
        admin.getTable(GXEAccount.ROLE_COLUMN_NAME).extend(true).addRef(sysad);
        admin.getTable(GXEAccount.DEFAULT_ROLE_COLUMN_NAME).extend(true).addRef(sysad);
        GXEAccount account = findAccountByName(accountsTable, GXEAccount.ADMIN_KEY);
        account.setMaxSessions(999);
        GXEAuthenticationStrategy strategy = GXEAuthenticationStrategy.factory.passwordStrategy();
        account.setAuthenticationStrategy(strategy);
        {
            InnerColumn ic = new InnerColumn.BooleanType() {

                protected boolean readTypedBoolean(CommonScope scope) throws AccessException {
                    CommonTable table = scope.commonData();
                    if (table.rowCount() != 1) return false;
                    CommonRow row = table.asCommonRow();
                    GXESession current = GXESession.factory.currentSession();
                    if (current.getAccount() == null) {
                        throw new AccessException("Session is expired.") {
                        };
                    }
                    return current.getAccount().srcRow().getRowId() == row.getRowId();
                }
            };
            TypeBuilder builder = accountsTable.builder(true);
            builder.addColumn(GXEAccount.CURRENT_COLUMN_NAME, ic);
        }
        {
            InnerColumn ic = new InnerColumn.BooleanType() {

                protected boolean readTypedBoolean(CommonScope scope) throws AccessException {
                    CommonRow row = scope.commonData().asCommonRow();
                    GXESession current = GXESession.factory.currentSession();
                    return current.getRole().srcRow().getRowId() == row.getRowId();
                }
            };
            TypeBuilder builder = roleTable.builder(true);
            builder.addColumn(GXERole.CURRENT_COLUMN_NAME, ic);
        }
        try {
            String password = "abc";
            GXERole principal = account.getDefaultRole();
            GXETicket ticket = GXETicket.factory.byPassword(account, password);
            mainSession = new BaseSession(ticket, principal) {

                public String getKind() {
                    return "main";
                }
            };
        } catch (InvalidPasswordException e) {
            throw new InternalException(e);
        }
    }

    public GXEAccount sysAdminAccount() throws AccessException {
        return findAccountByName(GXEAccount.ADMIN_KEY);
    }

    public GXEAccount findAccountByName(String userId) throws AccessException {
        Table table = getAccountTable();
        return findAccountByName(table, userId);
    }

    private static GXEAccount findAccountByName(Table table, String userId) throws AccessException {
        Table.Row row = table.row(userId);
        if (row == null || row.rowCount() != 1) {
            throw new AccessException("\"" + userId + "\" is not a valid userId") {
            };
        }
        return new AccountImpl(row);
    }

    public GXEAccount findAccountFromAccountRow(Table.Row row) throws AccessException {
        return new AccountImpl(row);
    }

    public GXEAccount findAccountFromPrincipalRow(Table.Row row) throws AccessException {
        Table.Row accountRow = row.getTable(Principal.INSTANCE_ELEMENT_ACCOUNT).asRow();
        return findAccountFromAccountRow(accountRow);
    }

    public Table getAccountTable() throws AccessException {
        PkgTable pkg = Universe.factory.getSysAccountsWorld().getRootPkg();
        return pkg.getExtent(GXEAccount.TYPE_NAME);
    }

    public GXERole findRoleByName(String userId) throws AccessException {
        Table table = getRoleTable();
        Table.Row row = table.row(userId);
        return new RoleImpl(row);
    }

    public GXERole findRoleFromRoleRow(Table.Row row) throws AccessException {
        return new RoleImpl(row);
    }

    public GXERole findRoleFromPrincipalRow(Table.Row row) throws AccessException {
        Entity.Row erow = row.metaRow();
        Principal.Row prow = erow.asPrincipalRow();
        return prow.getRole();
    }

    public Table getRoleTable() throws AccessException {
        PkgTable pkg = Universe.factory.getSysAccountsWorld().getRootPkg();
        return pkg.getExtent(GXERole.TYPE_NAME);
    }

    public Table getSessionTable() throws AccessException {
        PkgTable pkg = Universe.factory.getSysAccountsWorld().getRootPkg();
        return pkg.getExtent(GXESession.TYPE_NAME);
    }

    public GXESession currentSession() {
        return CommonSession.currentSession();
    }

    public GXESession mainSession() {
        return mainSession;
    }
}
