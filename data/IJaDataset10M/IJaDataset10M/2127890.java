package org.vardb.util.services;

import java.util.Properties;
import javax.annotation.Resource;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

public class CRecaptchaServiceImpl implements IRecaptchaService {

    @Resource(name = "recaptcha")
    private ReCaptchaImpl recaptcha;

    public ReCaptchaResponse checkAnswer(String ipaddress, String challenge, String answer) {
        return this.recaptcha.checkAnswer(ipaddress, challenge, answer);
    }

    public String createRecaptchaHtml(String errMsg) {
        if (errMsg != null) System.out.println("reCaptcha error message: " + errMsg);
        Properties properties = new Properties();
        properties.put("theme", IRecaptchaService.Theme.white.name());
        return this.recaptcha.createRecaptchaHtml(errMsg, properties);
    }
}
