package traitmap.login;

import java.sql.Connection;
import java.util.Date;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.torque.TorqueException;
import traitmap.Action;
import traitmap.om.UserInfo;
import traitmap.om.UserInfoPeer;

/**
 * ���O�C���������s���A�Z�b�V�����ɓo�^���܂��B
 * 
 * @author Konta
 * @version 1.0
 */
public final class LoginServlet extends Action {

    static Logger log = Logger.getLogger(LoginServlet.class);

    private static final String privateKey = "bs3ZBD6l";

    String success;

    String failure;

    /**
	 * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
	 */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        success = config.getInitParameter("success");
        failure = config.getInitParameter("failure");
    }

    /**
	 * ���O�C���������s���܂��B
	 * @param req
	 * @param res
	 * @exception ServletException
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    protected String execute(HttpServletRequest req, HttpServletResponse res, Connection con) throws ServletException {
        String key = getServletContext().getInitParameter("LOGIN");
        UserInformation info = new UserInformation();
        String userId = getParameterEx(req, "userId");
        String password = getParameterEx(req, "password");
        if (userId.equals("") || password.equals("")) {
            write(res, failure);
            return null;
        }
        try {
            HttpSession session = req.getSession(false);
            String sessionId = "";
            if (session != null) {
                sessionId = session.getId();
                session.invalidate();
                session = null;
            }
            session = req.getSession(true);
            log.debug("UserID=" + userId + ",Password=" + password + ", SeesionID=" + sessionId);
            info.setUserId(userId);
            info.setEncryptKey(privateKey + sessionId);
            if (info.login(password)) {
                session.setAttribute(key, info);
                session.setAttribute("NCS_USERNAME", info.getUserId());
                session.setAttribute("NCS_WORKGROUP", info.getProjectName());
                stampLoginDate(info);
                log.debug(success);
                write(res, success);
            } else {
                session.invalidate();
                log.debug(failure);
                write(res, failure);
            }
        } catch (TorqueException e) {
            throw new ServletException(e);
        } catch (IllegalStateException e) {
        }
        return null;
    }

    public static void stampLoginDate(UserInformation info) throws TorqueException {
        UserInfo userInfo = UserInfoPeer.doSelectByUserId(info.getUserId());
        if (userInfo == null) return;
        userInfo.setLastLogin(new Date());
        UserInfoPeer.doUpdate(userInfo);
    }
}
