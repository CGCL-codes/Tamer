package com.ideo.sweetdevria.gettingstarted.i18n;

import java.util.Locale;
import javax.servlet.http.HttpSession;
import com.ideo.sweetdevria.i18n.impl.DefaultResourcesManagerImpl;

public class EnglishResourcesManager extends DefaultResourcesManagerImpl {

    public Locale getLocale(HttpSession session) {
        return Locale.ENGLISH;
    }
}
