package org.cmsuite2.web.locale;

import java.util.Iterator;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.views.jsp.TagUtils;
import org.displaytag.Messages;
import org.displaytag.localization.I18nResourceProvider;
import org.displaytag.localization.LocaleResolver;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.LocaleProvider;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.util.ValueStack;

public class I18nWebworkAdapter implements LocaleResolver, I18nResourceProvider {

    public static final String UNDEFINED_KEY = "???";

    private static Log log;

    static {
        log = LogFactory.getLog(org.displaytag.localization.I18nWebworkAdapter.class);
    }

    @SuppressWarnings("rawtypes")
    public Locale resolveLocale(HttpServletRequest request) {
        Locale result = null;
        ValueStack stack = ActionContext.getContext().getValueStack();
        Iterator iterator = stack.getRoot().iterator();
        do {
            if (!iterator.hasNext()) break;
            Object o = iterator.next();
            if (!(o instanceof LocaleProvider)) continue;
            LocaleProvider lp = (LocaleProvider) o;
            result = lp.getLocale();
            break;
        } while (true);
        if (result == null) {
            log.debug("Missing LocalProvider actions, init locale to default");
            result = Locale.getDefault();
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    public String getResource(String resourceKey, String defaultValue, Tag tag, PageContext pageContext) {
        String key = resourceKey == null ? defaultValue : resourceKey;
        String message = null;
        ValueStack stack = TagUtils.getStack(pageContext);
        Iterator iterator = stack.getRoot().iterator();
        do {
            if (!iterator.hasNext()) break;
            Object o = iterator.next();
            if (!(o instanceof TextProvider)) continue;
            TextProvider tp = (TextProvider) o;
            message = tp.getText(key);
            break;
        } while (true);
        if (message == null && resourceKey != null) {
            log.debug(Messages.getString("Localization.missingkey", resourceKey));
            message = "???" + resourceKey + "???";
        }
        return message;
    }
}
