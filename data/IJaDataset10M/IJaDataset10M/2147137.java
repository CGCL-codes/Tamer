package org.mandiwala.selenium.filter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.plexus.util.StringUtils;
import org.mandiwala.InconsistentConfigurationException;
import org.mandiwala.PrefixedPropertyReader;
import org.mandiwala.selenium.SeleniumConfiguration;

/**
 * Use this type to filter out #bundle_name:entry# tokens and replace them by
 * entries in bundle files.
 */
public class BundleFilter extends AbstractSkippingFilter {

    private static final Pattern BUNDLE_REFERENCE_PATTERN = Pattern.compile("#[^:]+:[^#]+#");

    /**
     * Name of the resource bundle factory class name. The factory must
     * implement a static public method {@code getBundle(String, Locale)} that
     * returns a {@link java.util.ResourceBundle ResourceBundle}.
     */
    private static final String RESOURCE_BUNDLE_FACTORY = "resourceBundleFactory";

    /** Language for translation locale. Defaults to {@code en}. */
    private static final String LOCALE_LANGUAGE = "locale.language";

    /** Country for translation locale. Defaults to {@code gb}. */
    private static final String LOCALE_COUNTRY = "locale.country";

    private static final Log LOG = LogFactory.getLog("MANDIWALA");

    private HashMap<String, ResourceBundle> loadedBundles;

    private Locale locale;

    private Method getBundleMethod;

    private String resourceBundleFactory;

    /**
     * Replaces all occurences of {@code #bundle_name:key#} by the value of
     * {@code key} in the resource bundle {@code bundle_name} with the locale
     * specified in configuration.
     * 
     * @param invocations
     *            the invocations
     * 
     * @return the modified invocations
     * 
     * @throws FilterException
     *             On filtering exceptions
     */
    public List<Invocation> doFilter(List<Invocation> invocations) throws FilterException {
        for (Invocation invocation : invocations) {
            String[] params = invocation.getParameters();
            for (int i = 0; i < params.length; ++i) {
                params[i] = replace(params[i]);
            }
        }
        return invocations;
    }

    /**
     * Initializes a BundleFilter instance. For configuration property keys see:
     * 
     * <ul>
     * <li>{@link #LOCALE_COUNTRY}</li>
     * <li>{@link #LOCALE_LANGUAGE}</li>
     * <li>{@link #RESOURCE_BUNDLE_FACTORY}</li>
     * </ul>
     * 
     * @param seleniumConfiguration
     *            the selenium configuration
     */
    public void doInit(SeleniumConfiguration seleniumConfiguration) {
        PrefixedPropertyReader propertyReader = new PrefixedPropertyReader(getClass().getName(), seleniumConfiguration.getProps());
        locale = new Locale(propertyReader.getPrefixedProperty(LOCALE_LANGUAGE, true), propertyReader.getPrefixedProperty(LOCALE_COUNTRY, true));
        resourceBundleFactory = propertyReader.getPrefixedProperty(RESOURCE_BUNDLE_FACTORY, true);
        loadedBundles = new HashMap<String, ResourceBundle>();
        initGetBundleMethod(resourceBundleFactory);
    }

    private void initGetBundleMethod(String resourceBundleFactoryClass) {
        Class<?> factory;
        try {
            factory = Class.forName(resourceBundleFactoryClass);
            getBundleMethod = factory.getMethod("getBundle", String.class, Locale.class);
        } catch (Exception e) {
            throw new InconsistentConfigurationException(String.format("No %s class found, or class contains no public static method" + " getBundle(String, Locale)", resourceBundleFactoryClass), e);
        }
    }

    private String replace(String arg) {
        Matcher matcher = BUNDLE_REFERENCE_PATTERN.matcher(arg);
        while (matcher.find()) {
            String[] match = matcher.group().split(":");
            String bundleName = match[0].substring(1);
            String key = match[1].substring(0, match[1].length() - 1);
            ResourceBundle bundle = loadedBundles.get(bundleName);
            if (bundle == null) {
                try {
                    bundle = (ResourceBundle) getBundleMethod.invoke(null, bundleName, locale);
                } catch (InvocationTargetException e) {
                    if (e.getCause() instanceof MissingResourceException) {
                        LOG.warn("Couldn't find bundle " + bundleName);
                        arg = arg.substring(matcher.start() + 1);
                        matcher = BUNDLE_REFERENCE_PATTERN.matcher(arg);
                        continue;
                    }
                    throw new RuntimeException("getBundle threw exception when trying to load bundle " + bundleName, e);
                } catch (Exception e) {
                    throw new FilterException(String.format("Resource bundle factory must contain a public static method that returns" + " a ResourceBundle instance"), e);
                }
                loadedBundles.put(bundleName, bundle);
            }
            try {
                String replacement = ((ResourceBundle) loadedBundles.get(bundleName)).getString(key);
                arg = StringUtils.replace(arg, matcher.group(), replacement);
                arg = StringUtils.replace(arg, matcher.group(), replacement);
                matcher = BUNDLE_REFERENCE_PATTERN.matcher(arg);
            } catch (MissingResourceException e) {
                LOG.warn("Couldn't find key " + key + " in bundle " + bundleName);
                arg = arg.substring(matcher.start() + 1);
                matcher = BUNDLE_REFERENCE_PATTERN.matcher(arg);
            }
        }
        return arg;
    }
}
