package net.joindesk.i18n;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

public interface LocaleResolver {

    public Locale resolveLocale(HttpServletRequest request);
}
