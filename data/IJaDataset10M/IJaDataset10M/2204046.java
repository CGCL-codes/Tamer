package com.lus.sso.servlet;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.json.JSONObject;
import com.lus.sso.handlers.UserDataResultSetHandler;
import com.lus.sso.security.SecureTokenService;

/**
* LoginServlet
*
* <p>
* This servlet check the db for user existence.
* If the parameters username and password are valid :
* 
* 	- Create the secure cookie (with encrypted token)
* 	- Return the Json response with 200 ok if successfully logged, 404 if user not found. 
* 
* <p>
* This is "Open Source" software and released under the <a href="http://www.gnu.org/licenses/gpl.html">GNU/GPL</a> license.<br>
* It is provided "as is" without warranty of any kind.<br>
* Copyright 2009: Luca Sepe, luca.sepe@gmail.com<br>
* Home page: <a href="http://sourceforge.net/projects/tinysso/">http://sourceforge.net/projects/tinysso/</a><br>
*
* 
* @author Luca Sepe
* 
* @email luca.sepe@gmail.com
* 
*/
public class LoginServlet extends TinySsoServlet {

    private static final long serialVersionUID = -7168330494781310551L;

    private static final String PARAM_USERNAME = "username", PARAM_PASSWORD = "password";

    @SuppressWarnings({ "unchecked" })
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (isRequestParameterNullAndEmpty(request, PARAM_USERNAME)) {
            m_logger.warn(MessageFormat.format("Manca il parametro della richiesta <{0}>", PARAM_USERNAME));
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (isRequestParameterNullAndEmpty(request, PARAM_PASSWORD)) {
            m_logger.warn(MessageFormat.format("Manca il parametro della richiesta <{0}>", PARAM_PASSWORD));
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        JSONObject json_response = null;
        Connection con = null;
        QueryRunner qr = null;
        String password_from_db = null;
        boolean user_found = false;
        try {
            con = getConnection();
            qr = new QueryRunner();
            password_from_db = (String) qr.query(con, getSql("user.login").toString(), new ScalarHandler(), new Object[] { request.getParameter(PARAM_USERNAME) });
            if (password_from_db == null) user_found = false; else if (password_from_db.equals(request.getParameter(PARAM_PASSWORD))) user_found = true;
        } catch (SQLException sqle) {
            m_logger.error(sqle);
            user_found = false;
            json_response = getJsonResponse(404);
        } finally {
            if (con != null) DbUtils.closeQuietly(con);
            con = null;
            qr = null;
            password_from_db = null;
        }
        String token = null;
        if (user_found) {
            SecureTokenService ts = null;
            Map map = null;
            try {
                ts = getSecureTokenService();
                con = getConnection();
                qr = new QueryRunner();
                map = (Map) qr.query(con, getSql("user.get").toString(), new UserDataResultSetHandler(), new Object[] { request.getParameter(PARAM_USERNAME) });
                if (map != null) {
                    token = ts.createToken(request.getParameter(PARAM_USERNAME), map);
                    json_response = getJsonResponse(200);
                    if (m_logger.isDebugEnabled()) m_logger.debug(MessageFormat.format("Token created : {0}", new Object[] { token }));
                }
            } catch (SQLException sqle) {
                m_logger.error(sqle);
                map = null;
                token = null;
                json_response = getJsonResponse(new Exception("token creation FAILED"));
            } catch (InvalidKeyException ike) {
                m_logger.error(ike);
                token = null;
                json_response = getJsonResponse(new Exception("token creation FAILED"));
            } catch (NoSuchAlgorithmException nsae) {
                m_logger.error(nsae);
                token = null;
                json_response = getJsonResponse(new Exception("token creation FAILED"));
            } finally {
                if (con != null) DbUtils.closeQuietly(con);
                qr = null;
                con = null;
                ts = null;
                map = null;
            }
        }
        sendJsonRpcResponse(json_response, response, token);
    }
}

;
