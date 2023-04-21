package com.protest.service.userlogin;

import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;

public class CaptchaServiceSingleton {

    private static ImageCaptchaService instance = new DefaultManageableImageCaptchaService(new FastHashMapCaptchaStore(), new MyImageCaptchaEngine(), 180, 100000, 75000);

    public static ImageCaptchaService getInstance() {
        return instance;
    }
}
