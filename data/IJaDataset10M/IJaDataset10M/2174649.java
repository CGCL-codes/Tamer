package org.cilogon.d2.storage.impl.postgres;

import edu.uiuc.ncsa.myproxy.oa4mp.server.DSClient;
import edu.uiuc.ncsa.myproxy.oa4mp.server.DSClientApproval;
import edu.uiuc.ncsa.myproxy.oa4mp.server.storage.sql.ClientApprovalTable;
import edu.uiuc.ncsa.myproxy.oa4mp.server.storage.sql.ClientStoreTable;
import edu.uiuc.ncsa.myproxy.oa4mp.server.storage.sql.SQLClientApprovalStore;
import edu.uiuc.ncsa.myproxy.oa4mp.server.storage.sql.SQLClientStore;
import edu.uiuc.ncsa.security.delegation.server.storage.ClientApprovalStore;
import edu.uiuc.ncsa.security.delegation.server.storage.ClientStore;
import edu.uiuc.ncsa.security.delegation.storage.TransactionStore;
import edu.uiuc.ncsa.security.storage.sql.ConnectionPool;
import org.cilogon.d2.TestStoreProvider;
import org.cilogon.d2.storage.ArchivedUserStore;
import org.cilogon.d2.storage.IdentityProviderStore;
import org.cilogon.d2.storage.UserStore;
import org.cilogon.d2.storage.impl.sql.CILSQLTransactionStore;
import org.cilogon.d2.storage.impl.sql.CILSQLArchivedUserStore;
import org.cilogon.d2.storage.impl.sql.CILSQLIdentityProviderStore;
import org.cilogon.d2.storage.impl.sql.CILSQLUserStore;
import org.cilogon.d2.util.CILogonServiceTransaction;
import org.cilogon.d2.util.Incrementable;
import org.cilogon.d2.util.SerialStrings;

/**
 * <p>Created by Jeff Gaynor<br>
 * on 3/14/12 at  5:20 PM
 */
public class PostgresStoreProvider extends TestStoreProvider {

    ClientApprovalStore<DSClientApproval> clientApprovalStore;

    @Override
    public ClientApprovalStore<DSClientApproval> getClientApprovalStore() throws Exception {
        if (clientApprovalStore == null) {
            clientApprovalStore = new SQLClientApprovalStore(connectionPool, new ClientApprovalTable("cilogon", "a", "client_approvals"));
        }
        return clientApprovalStore;
    }

    ClientStore<DSClient> clientStore;

    @Override
    public ClientStore<DSClient> getClientStore() throws Exception {
        if (clientStore == null) {
            clientStore = new SQLClientStore(connectionPool, new ClientStoreTable("cilogon", "a", "clients"));
        }
        return clientStore;
    }

    public PostgresStoreProvider(SerialStrings serialStrings, ConnectionPool connectionPool) {
        super(serialStrings);
        this.connectionPool = connectionPool;
    }

    TransactionStore<CILogonServiceTransaction> transactionStore;

    @Override
    public TransactionStore<CILogonServiceTransaction> getTransactionStore() throws Exception {
        if (transactionStore == null) {
            CILogonServiceTransactionTable t = new CILogonServiceTransactionTable("cilogon", "a", "transactions");
            transactionStore = new CILSQLTransactionStore(getServiceTokenFactory(), getClientStore(), connectionPool, t);
        }
        return transactionStore;
    }

    ArchivedUserStore archivedUserStore;

    UserStore userStore;

    Incrementable sequence;

    IdentityProviderStore identityProviderStore;

    @Override
    public ArchivedUserStore getArchivedUserStore() throws Exception {
        if (archivedUserStore == null) {
            ArchivedUsersTable aut = new ArchivedUsersTable("cilogon", "a", "old_user");
            aut.setUsersTable(new UsersTable("cilogon", "a", "user"));
            archivedUserStore = new CILSQLArchivedUserStore(connectionPool, aut, serialStrings, getServiceTokenFactory());
        }
        return archivedUserStore;
    }

    @Override
    public UserStore getUserStore() throws Exception {
        if (userStore == null) {
            userStore = new CILSQLUserStore(connectionPool, new UsersTable("cilogon", "a", "user"), getSequence(), serialStrings, getServiceTokenFactory());
        }
        return userStore;
    }

    @Override
    public IdentityProviderStore getIDP() throws Exception {
        if (identityProviderStore == null) {
            identityProviderStore = new CILSQLIdentityProviderStore(connectionPool, new IdentityProvidersTable("cilogon", "a", "identity_provider"));
        }
        return identityProviderStore;
    }

    @Override
    public Incrementable getSequence() throws Exception {
        if (sequence == null) {
            sequence = new PostgresSequence(connectionPool, new SequenceTable("cilogon", "a", "uid_seq"));
        }
        return sequence;
    }

    ConnectionPool connectionPool;
}
