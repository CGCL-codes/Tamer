package tms.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import nl.captcha.Captcha;
import tms.client.services.CaptchaService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Service to compare the image text and the user entered text.
 * @author Ismail Lavangee
 *
 */
public class CaptchaServiceImpl extends RemoteServiceServlet implements CaptchaService {

    private static final long serialVersionUID = -589202680422127806L;

    @Override
    public boolean validateCaptcha(String user_captcha) {
        HttpServletRequest request = getThreadLocalRequest();
        HttpSession session = request.getSession();
        Captcha captcha = (Captcha) session.getAttribute(Captcha.NAME);
        return captcha.isCorrect(user_captcha);
    }
}
