package edu.uiuc.ncsa.security.oauth_1_0a.sql;

import edu.uiuc.ncsa.security.delegation.storage.TransactionStore;
import edu.uiuc.ncsa.security.delegation.storage.impl.BasicTransaction;
import edu.uiuc.ncsa.security.delegation.token.AccessToken;
import edu.uiuc.ncsa.security.delegation.token.AuthorizationGrant;
import edu.uiuc.ncsa.security.delegation.token.Token;
import edu.uiuc.ncsa.security.delegation.token.Verifier;
import edu.uiuc.ncsa.security.core.exceptions.GeneralException;
import edu.uiuc.ncsa.security.core.exceptions.TransactionNotFoundException;
import edu.uiuc.ncsa.security.oauth_1_0a.AccessTokenImpl;
import edu.uiuc.ncsa.security.oauth_1_0a.AuthorizationGrantImpl;
import edu.uiuc.ncsa.security.oauth_1_0a.VerifierImpl;
import edu.uiuc.ncsa.security.storage.sql.ColumnMap;
import edu.uiuc.ncsa.security.storage.sql.ConnectionPool;
import edu.uiuc.ncsa.security.storage.sql.SQLStore;
import edu.uiuc.ncsa.security.storage.sql.TransactionTable;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>Created by Jeff Gaynor<br>
 * on May 10, 2010 at  3:45:05 PM
 */
public abstract class SQLBaseTransactionStore<V extends BasicTransaction> extends SQLStore<String, V> implements TransactionStore<V> {

    @Override
    public String toK(String x) {
        return x;
    }

    @Override
    public void setConnectionPool(ConnectionPool connectionPool) {
        super.setConnectionPool(connectionPool);
    }

    public SQLBaseTransactionStore() {
        super();
    }

    public TransactionTable getTransactionTable() {
        return (TransactionTable) getTable();
    }

    /**
     * Since there are several possible statements (by temp cred, access token, verifier) that
     * will return a transaction, this method will handle them all.
     *
     * @param identifier
     * @param statement
     * @return
     */
    protected V getTransaction(String identifier, String statement) {
        if (identifier == null) {
            throw new IllegalStateException("Error: a null identifier was supplied");
        }
        Connection c = getConnection();
        V t = null;
        try {
            PreparedStatement stmt = c.prepareStatement(statement);
            stmt.setString(1, identifier);
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            if (!rs.next()) {
                rs.close();
                stmt.close();
                throw new TransactionNotFoundException("No transaction found for identifier \"" + identifier + "\"");
            }
            ColumnMap map = rsToMap(rs);
            rs.close();
            stmt.close();
            t = create();
            populate(map, t);
        } catch (SQLException e) {
            throw new GeneralException("Error getting transaction with identifier \"" + identifier + "\"", e);
        } finally {
            releaseConnection(c);
        }
        return t;
    }

    public V get(AuthorizationGrant tempCred) {
        try {
            V t = getTransaction(tempCred.getToken(), getTransactionTable().getByTempCredStatement());
            return t;
        } catch (TransactionNotFoundException x) {
            return null;
        }
    }

    public V get(AccessToken accessToken) {
        try {
            V t = getTransaction(accessToken.getToken(), getTransactionTable().getByAccessTokenStatement());
            return t;
        } catch (TransactionNotFoundException x) {
            return null;
        }
    }

    public V get(Verifier verifier) {
        try {
            V t = getTransaction(verifier.getToken(), getTransactionTable().getByVerifierStatement());
            return t;
        } catch (TransactionNotFoundException x) {
            return null;
        }
    }

    @Override
    public void populate(ColumnMap map, V value) {
        URI tokenUri = null;
        String token;
        TransactionTable tt = getTransactionTable();
        token = map.getString(tt.getAccessToken());
        if (token != null) tokenUri = URI.create(token);
        AccessTokenImpl at = new AccessTokenImpl(tokenUri);
        value.setAccessToken(at);
        token = map.getString(tt.getTempCred());
        if (token != null) {
            tokenUri = URI.create(token);
        } else {
            tokenUri = null;
        }
        AuthorizationGrantImpl tc = new AuthorizationGrantImpl(tokenUri);
        value.setAuthorizationGrant(tc);
        token = map.getString(tt.getVerifier());
        if (token != null) {
            VerifierImpl verifier = new VerifierImpl(URI.create(token));
            value.setVerifier(verifier);
        }
    }

    protected String getToken(Token at) throws SQLException {
        return at == null ? null : at.getToken();
    }
}
