package com.scholardesk.jcaptcha;

import com.scholardesk.jcaptcha.ScholarDeskDefaultImageEngine;
import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;

public class CaptchaServiceSingleton {

    private static ImageCaptchaService m_instance = new DefaultManageableImageCaptchaService(new FastHashMapCaptchaStore(), new ScholarDeskDefaultImageEngine(), 180, 100000, 75000);

    public static ImageCaptchaService getInstance() {
        return m_instance;
    }
}
