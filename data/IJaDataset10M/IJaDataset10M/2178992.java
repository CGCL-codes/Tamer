package traitmap.filter;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.apache.oro.text.perl.Perl5Util;
import org.apache.torque.Torque;
import org.apache.torque.TorqueException;
import sun.misc.BASE64Decoder;
import traitmap.login.LoginServlet;
import traitmap.login.UserInformation;

/**
 * �A�N�Z�X����N���X�B
 * 
 * @author Konta
 * @version 1.0
 */
public final class AuthFilter implements Filter {

    static Logger log = Logger.getLogger(AuthFilter.class);

    private static final String privateKey = "bs3ZBD6l";

    Perl5Util pUtil = new Perl5Util();

    String m_redirect;

    String m_forward;

    String m_message;

    String m_mask;

    String m_loginKey;

    String m_encoding;

    /**
	 * �ŏ��ɌĂ΂��B
	 * @param config
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
    public void init(FilterConfig config) throws ServletException {
        String properties = config.getInitParameter("torque");
        m_encoding = config.getInitParameter("encoding");
        m_redirect = config.getInitParameter("redirect");
        m_forward = config.getInitParameter("forward");
        m_message = config.getInitParameter("message");
        m_mask = config.getInitParameter("mask");
        m_loginKey = config.getServletContext().getInitParameter("LOGIN");
        try {
            if (!Torque.isInit()) {
                Class theClass = getClass();
                ClassLoader loader = theClass.getClassLoader();
                PropertiesConfiguration pConfig = new PropertiesConfiguration();
                pConfig.load(loader.getResourceAsStream(properties));
                Torque.init(pConfig);
            }
        } catch (TorqueException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.debug("init");
    }

    /**
	 * �t�B���^�[�������s���܂��B
	 * 
	 * @param request ServletRequest
	 * @param response ServletResponse
	 * @param chain FilterChain
	 * @exception IOException
	 * @exception ServletException
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        req.setCharacterEncoding(m_encoding);
        HttpSession session = req.getSession();
        UserInformation userInfo = (UserInformation) session.getAttribute(m_loginKey);
        String requestUrl = req.getRequestURI();
        log.debug(requestUrl);
        if (m_mask != null || !m_mask.equals("")) {
            if (pUtil.match("/" + m_mask + "/i", requestUrl)) {
                chain.doFilter(req, res);
                return;
            }
        }
        if (req.getHeader("Authorization") != null && !req.getHeader("Authorization").equals("") && req.getRemoteUser() != null && !req.getRemoteUser().equals("") && userInfo == null) {
            userInfo = new UserInformation();
            userInfo.setUserId(req.getRemoteUser());
            session = req.getSession(false);
            userInfo.setEncryptKey(privateKey + session.getId());
            BASE64Decoder decoder = new BASE64Decoder();
            String auth = req.getHeader("Authorization").substring(6);
            String dec = new String(decoder.decodeBuffer(auth));
            String password = dec.substring(dec.indexOf(":") + 1);
            log.debug("password: " + password);
            try {
                if (userInfo.login(password, false)) {
                    session.setAttribute(m_loginKey, userInfo);
                    session.setAttribute("NCS_USERNAME", userInfo.getUserId());
                    session.setAttribute("NCS_WORKGROUP", userInfo.getProjectName());
                    LoginServlet.stampLoginDate(userInfo);
                } else {
                    session.invalidate();
                }
            } catch (TorqueException e) {
                log.error(e);
                e.printStackTrace();
                session.invalidate();
                userInfo = null;
            }
            if (req.getRequestURI().indexOf("/Logout") > -1) return;
        }
        try {
            AccessControl control = new AccessControl(userInfo, requestUrl);
            if (control.isControl()) {
                res.setHeader("Pragma", "no-cache");
                res.setHeader("Cache-Control", "no-cache");
                res.setHeader("Cache-Control", "no-store");
                write(res);
                return;
            }
        } catch (TorqueException e) {
            log.debug(e.fillInStackTrace());
            throw new ServletException(e);
        }
        chain.doFilter(req, res);
    }

    /**
	 * ���b�Z�[�W���o�͂��܂��B
	 * @param res
	 * @param message
	 * @throws IOException
	 */
    public void write(HttpServletResponse res) throws IOException {
        res.setContentType("text/html");
        PrintWriter out = new PrintWriter(res.getOutputStream());
        out.println(m_message);
        out.flush();
        out.close();
    }

    /**
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
    public void destroy() {
        Torque.shutdown();
        log.debug("destroy");
    }
}
