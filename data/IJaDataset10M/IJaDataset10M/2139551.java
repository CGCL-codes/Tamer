package org.asoft.magnus.i18n;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.asoft.sapiente.i18n.MessageSource;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Jul 17, 2009
 * 
 * @author Alex Silva
 */
public class ClassPathMessageSource extends ResourceBundleMessageSource implements MessageSource, ApplicationListener {

    private Set<String> locations = new HashSet<String>();

    @Override
    public void addBundleLocation(String location) {
        if (location != null) locations.add(location);
    }

    @Override
    public String getMessage(String msg, Locale locale, Object... args) {
        try {
            return super.getMessage(msg, args, locale);
        } catch (NoSuchMessageException e) {
            return null;
        }
    }

    @Override
    public String getMessage(String msg, String defaultMessage, Locale locale, Object... args) {
        try {
            return super.getMessage(msg, args, defaultMessage, locale);
        } catch (NoSuchMessageException e) {
            return null;
        }
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        setBasenames(locations.toArray(new String[locations.size()]));
    }
}
