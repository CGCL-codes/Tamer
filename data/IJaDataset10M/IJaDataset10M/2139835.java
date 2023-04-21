package up5.mi.visio.sipdb.sip;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import org.apache.log4j.Logger;
import up5.mi.visio.sipdb.AuthModule;
import up5.mi.visio.sipdb.DBFactory;
import up5.mi.visio.sipdb.UnknowDBException;
import up5.mi.visio.sipdb.UserDoesNotExistException;
import up5.mi.visio.sipdb.accounting.UserCredential;

/**
 * This class provides default implementation of the Authentication API.
 */
public class AuthModuleImpl implements AuthModule {

    private final Logger logger = Logger.getLogger(AuthModuleImpl.class.getName());

    @Override
    public ChallengeBean getChallenge(String callId) throws UnknowDBException, ChallengeNotFoundException {
        String request = "SELECT * FROM challenge WHERE sipcallid='" + callId + "'";
        Connection connection = null;
        Statement stmt = null;
        ChallengeBean challenge = null;
        try {
            connection = DBFactory.getConnection();
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(request);
            if (rs.next()) {
                challenge = new ChallengeBean();
                challenge.setNonce(rs.getString(4));
                challenge.setOpaque(rs.getString(5));
                challenge.setScheme(rs.getString(6));
                challenge.setAlgorythm(rs.getString(7));
                challenge.setQop(rs.getString(8));
                challenge.setNc(rs.getString(9));
                challenge.setStale(rs.getString(10));
            } else {
                this.logger.error("challenge not found for call Id " + callId);
                throw new ChallengeNotFoundException();
            }
        } catch (SQLException e) {
            this.logger.error("in getChallenge : " + e);
            throw new UnknowDBException();
        } finally {
            try {
                stmt.close();
            } catch (Exception e) {
                this.logger.error("unknow error : " + e);
            }
            try {
                connection.close();
            } catch (Exception e) {
                this.logger.error("unknow error : " + e);
            }
        }
        return challenge;
    }

    @Override
    public void registerChallenge(String callId, ChallengeBean challenge, int expires) throws UnknowDBException {
        final String calledProcedure = "{call challenge_create(?,?,?,?,?,?,?,?,?)}";
        Connection connection = null;
        CallableStatement cs = null;
        try {
            connection = DBFactory.getConnection();
            cs = connection.prepareCall(calledProcedure);
            cs.setString(1, callId);
            cs.setString(2, challenge.getNonce());
            cs.setString(3, challenge.getOpaque());
            cs.setString(4, challenge.getScheme());
            cs.setString(5, challenge.getAlgorythm());
            cs.setString(6, challenge.getQop());
            cs.setString(7, challenge.getNc());
            cs.setString(8, challenge.getStale());
            cs.setInt(9, expires);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.executeUpdate();
            int res = cs.getInt(1);
            if (res != 1) {
                throw new UnknowDBException();
            }
            cs.close();
            connection.close();
        } catch (SQLException e) {
            this.logger.debug("SQLException", e);
            throw new UnknowDBException();
        } finally {
            try {
                cs.close();
            } catch (Exception e) {
                this.logger.error("unknow error : " + e);
            }
            try {
                connection.close();
            } catch (Exception e) {
                this.logger.error("unknow error : " + e);
            }
        }
    }

    @Override
    public UserCredential getUserCredential(String sipuri) throws UnknowDBException, UserDoesNotExistException {
        String calledProcedure = "{call get_user_credentials(?)}";
        UserCredential credential = new UserCredential();
        Connection connection = null;
        CallableStatement cs = null;
        try {
            connection = DBFactory.getConnection();
            cs = connection.prepareCall(calledProcedure);
            ResultSet r = null;
            cs.setString(1, sipuri);
            cs.execute();
            r = cs.getResultSet();
            if (r.next()) {
                credential.setSipuri(sipuri);
                credential.setPasswd(r.getString(2));
                credential.setRealm(r.getString(3));
            } else {
                throw new UserDoesNotExistException();
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("unknow user")) {
                throw new UserDoesNotExistException();
            } else {
                this.logger.debug("SQLException", e);
                throw new UnknowDBException();
            }
        } finally {
            try {
                cs.close();
            } catch (Exception e) {
                this.logger.error("unknow error : " + e);
            }
            try {
                connection.close();
            } catch (Exception e) {
                this.logger.error("unknow error : " + e);
            }
        }
        return credential;
    }
}
