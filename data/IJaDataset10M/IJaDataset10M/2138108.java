package net.sf.rcer.conn.locales;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import net.sf.rcer.conn.Messages;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IRegistryEventListener;
import org.eclipse.core.runtime.Platform;

/**
 * The central registry for SAP Locales. Use this singleton class to obtain the locales 
 * defined using the extension point <tt>net.sf.rcer.conn.saplocales</tt>.
 * @author vwegert
 *
 */
public class LocaleRegistry implements IRegistryEventListener {

    /**
	 * The name of the extension point used to define the locales.
	 */
    public static final String SAPLOCALES_EXTENSION_POINT = "net.sf.rcer.conn.saplocales";

    /**
	 * The singleton instance.
	 */
    private static volatile LocaleRegistry instance;

    /**
	 * The main container for the locale objects.
	 */
    private Set<Locale> locales = new HashSet<Locale>();

    /**
	 * A sorted map that contains the locale objects, ordered by ISO codes.
	 */
    private SortedMap<String, Locale> localesByISO = new TreeMap<String, Locale>();

    /**
	 * A map to retrieve the locale objects by their SAP code.
	 */
    private Map<String, Locale> localesByID = new HashMap<String, Locale>();

    /**
	 * Private constructor to prevent secondary instantiation.
	 */
    private LocaleRegistry() {
        addLocales(Platform.getExtensionRegistry().getExtensionPoint(SAPLOCALES_EXTENSION_POINT).getExtensions());
        Platform.getExtensionRegistry().addListener(this, SAPLOCALES_EXTENSION_POINT);
    }

    /**
	 * @return the singleton instance.
	 */
    public static LocaleRegistry getInstance() {
        if (instance == null) {
            synchronized (LocaleRegistry.class) {
                if (instance == null) {
                    instance = new LocaleRegistry();
                }
            }
        }
        return instance;
    }

    /**
	 * @return the list of locales that are currently registered
	 */
    public Collection<Locale> getLocales() {
        return locales;
    }

    /**
	 * @return the list of ISO codes that are currently registered
	 */
    public Collection<String> getISOCodes() {
        return localesByISO.keySet();
    }

    /**
	 * @return the list of SAP codes that are currently registered
	 */
    public Collection<String> getIDs() {
        return localesByID.keySet();
    }

    /**
	 * @param isoCode the ISO code of the locale to retrieve
	 * @return the {@link Locale} object
	 * @throws LocaleNotFoundException
	 */
    public Locale getLocaleByISO(String isoCode) throws LocaleNotFoundException {
        if (localesByISO.containsKey(isoCode)) {
            return localesByISO.get(isoCode);
        }
        throw new LocaleNotFoundException(MessageFormat.format(Messages.LocaleRegistry_NoLocaleForISOCode, isoCode));
    }

    /**
	 * @param id the SAP ID code of the locale to retrieve
	 * @return the {@link Locale} object
	 * @throws LocaleNotFoundException
	 */
    public Locale getLocaleByID(String id) throws LocaleNotFoundException {
        if (localesByID.containsKey(id)) {
            return localesByID.get(id);
        }
        throw new LocaleNotFoundException(MessageFormat.format(Messages.LocaleRegistry_NoLocaleForInternalID, id));
    }

    public void added(IExtension[] extensions) {
        addLocales(extensions);
    }

    public void removed(IExtension[] extensions) {
        removeLocales(extensions);
    }

    public void added(IExtensionPoint[] extensionPoints) {
    }

    public void removed(IExtensionPoint[] extensionPoints) {
    }

    /**
	 * Examine the extensions provided and either insert new locale objects or update the existing ones.
	 * @param extensions
	 */
    private void addLocales(IExtension[] extensions) {
        for (final IExtension extension : extensions) {
            assert extension.getExtensionPointUniqueIdentifier().equals(SAPLOCALES_EXTENSION_POINT);
            final IConfigurationElement[] elements = extension.getConfigurationElements();
            for (final IConfigurationElement element : elements) {
                if (element.getName().equals("locale")) {
                    final String id = element.getAttribute("id");
                    final String isoCode = element.getAttribute("iso");
                    final String description = element.getAttribute("description");
                    if (localesByID.containsKey(id)) {
                        locales.remove(localesByID.get(id));
                        localesByID.remove(id);
                    }
                    if (localesByISO.containsKey(isoCode)) {
                        locales.remove(localesByISO.get(isoCode));
                        localesByISO.remove(isoCode);
                    }
                    Locale locale = new Locale(id, isoCode, description);
                    locales.add(locale);
                    localesByID.put(id, locale);
                    localesByISO.put(isoCode, locale);
                }
            }
        }
    }

    /**
	 * Examine the extensions provided and remove the locale objects.
	 * @param extensions
	 */
    private void removeLocales(IExtension[] extensions) {
        for (final IExtension extension : extensions) {
            assert extension.getExtensionPointUniqueIdentifier().equals(SAPLOCALES_EXTENSION_POINT);
            final IConfigurationElement[] elements = extension.getConfigurationElements();
            for (final IConfigurationElement element : elements) {
                if (element.getName().equals("locale")) {
                    final String id = element.getAttribute("id");
                    final String isoCode = element.getAttribute("iso");
                    if (localesByID.containsKey(id)) {
                        locales.remove(localesByID.get(id));
                        localesByID.remove(id);
                    }
                    if (localesByISO.containsKey(isoCode)) {
                        locales.remove(localesByISO.get(isoCode));
                        localesByISO.remove(isoCode);
                    }
                }
            }
        }
    }
}
