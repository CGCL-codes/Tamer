package ru.adv.web.captcha;

import ru.adv.xml.newt.NewtContext;

public interface CaptchaService {

    public static String CAPTCHA_NAME = "mcaptcha";

    public abstract boolean isCorrect(NewtContext context);
}
