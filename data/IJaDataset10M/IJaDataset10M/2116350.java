package net.sf.tacos.contrib.captcha;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;
import java.awt.image.BufferedImage;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

/**
 * A {@link CaptchaService} implelemtation using jcaptcha.
 *
 * @see http://forge.octo.com/jcaptcha/confluence/display/general/Home
 * @author andyhot
 */
public class CaptchaServiceImpl implements CaptchaService {

    private ImageCaptchaService jcaptcha;

    private HttpServletRequest servletRequest;

    public HttpServletRequest getServletRequest() {
        return servletRequest;
    }

    public void setServletRequest(HttpServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    public void initializeService() {
        jcaptcha = new DefaultManageableImageCaptchaService();
    }

    public ImageCaptchaService getService() {
        return jcaptcha;
    }

    public BufferedImage getImage(String id, Locale locale) {
        return jcaptcha.getImageChallengeForID(id, locale);
    }

    public boolean validate(String parameter) {
        Boolean isResponseCorrect = Boolean.FALSE;
        String captchaId = getServletRequest().getSession().getId();
        String word = parameter;
        try {
            isResponseCorrect = jcaptcha.validateResponseForID(captchaId, word);
        } catch (CaptchaServiceException e) {
        }
        return isResponseCorrect != null && isResponseCorrect.booleanValue();
    }
}
