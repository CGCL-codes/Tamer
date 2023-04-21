package org.springframework.ui.context.support;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.ui.context.HierarchicalThemeSource;
import org.springframework.ui.context.Theme;
import org.springframework.ui.context.ThemeSource;

/**
 * ThemeSource implementation that looks up an individual ResourceBundle
 * per theme. The theme name gets interpreted as ResourceBundle basename,
 * supporting a common basename prefix for all themes.
 *
 * @author Jean-Pierre Pawlak
 * @author Juergen Hoeller
 * @see #setBasenamePrefix
 * @see java.util.ResourceBundle
 * @see org.springframework.context.support.ResourceBundleMessageSource
 */
public class ResourceBundleThemeSource implements HierarchicalThemeSource {

    protected final Log logger = LogFactory.getLog(getClass());

    private ThemeSource parentThemeSource;

    private String basenamePrefix = "";

    /** Map from theme name to Theme instance */
    private Map themeMap = Collections.synchronizedMap(new HashMap());

    public void setParentThemeSource(ThemeSource parent) {
        this.parentThemeSource = parent;
        Iterator it = this.themeMap.values().iterator();
        while (it.hasNext()) {
            initParent((Theme) it.next());
        }
    }

    public ThemeSource getParentThemeSource() {
        return parentThemeSource;
    }

    /**
	 * Set the prefix that gets applied to the ResourceBundle basenames,
	 * i.e. the theme names.
	 * E.g.: basenamePrefix="test.", themeName="theme" -> basename="test.theme".
	 * @param basenamePrefix prefix for ResourceBundle basenames
	 * @see java.util.ResourceBundle
	 */
    public void setBasenamePrefix(String basenamePrefix) {
        this.basenamePrefix = (basenamePrefix != null ? basenamePrefix : "");
    }

    /**
	 * This implementation returns a SimpleTheme instance, holding a
	 * ResourceBundle-based MessageSource whose basename corresponds to
	 * the given theme name (prefixed by the configured "basenamePrefix").
	 * <p>SimpleTheme instances are cached per theme name. Use a reloadable
	 * MessageSource if themes should reflect changes to the underlying files.
	 * @see #setBasenamePrefix
	 * @see #createMessageSource
	 */
    public Theme getTheme(String themeName) {
        if (themeName == null) {
            return null;
        }
        Theme theme = (Theme) this.themeMap.get(themeName);
        if (theme == null) {
            String basename = this.basenamePrefix + themeName;
            MessageSource messageSource = createMessageSource(basename);
            theme = new SimpleTheme(themeName, messageSource);
            initParent(theme);
            this.themeMap.put(themeName, theme);
            if (logger.isInfoEnabled()) {
                logger.info("Theme created: name '" + themeName + "', basename [" + basename + "]");
            }
        }
        return theme;
    }

    /**
	 * Create a MessageSource for the given basename,
	 * to be used as MessageSource for the corresponding theme.
	 * <p>Default implementation creates a ResourceBundleMessageSource.
	 * for the given basename. A subclass could create a specifically
	 * configured ReloadableResourceBundleMessageSource, for example.
	 * @param basename the basename to create a MessageSource for
	 * @return the MessageSource
	 * @see org.springframework.context.support.ResourceBundleMessageSource
	 * @see org.springframework.context.support.ReloadableResourceBundleMessageSource
	 */
    protected MessageSource createMessageSource(String basename) {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(basename);
        return messageSource;
    }

    /**
	 * Initialize the MessageSource of the given theme with the
	 * one from the corresponding parent of this ThemeSource.
	 */
    protected void initParent(Theme theme) {
        if (theme.getMessageSource() instanceof HierarchicalMessageSource) {
            HierarchicalMessageSource messageSource = (HierarchicalMessageSource) theme.getMessageSource();
            if (getParentThemeSource() != null && messageSource.getParentMessageSource() == null) {
                Theme parentTheme = getParentThemeSource().getTheme(theme.getName());
                if (parentTheme != null) {
                    messageSource.setParentMessageSource(parentTheme.getMessageSource());
                }
            }
        }
    }
}
