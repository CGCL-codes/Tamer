package org.gvsig.i18n.impl;

import java.io.*;
import java.security.AccessController;
import java.util.*;
import java.util.Map.Entry;
import java.util.zip.*;
import org.gvsig.i18n.*;
import sun.security.action.GetPropertyAction;
import com.iver.andami.Launcher;
import com.iver.andami.PluginServices;
import com.iver.andami.config.generate.AndamiConfig;
import com.iver.utiles.StringUtilities;
import com.iver.utiles.XMLEntity;

/**
 * Implementation of the I18nManager interface.
 *
 * @author <a href="mailto:dcervera@disid.com">David Cervera</a>
 * @author <a href="mailto:cordin@disid.com">C�sar Ordi�ana</a>
 */
public class I18nManagerImpl implements I18nManager {

    private static final String LOCALES_FILE_NAME = "locales.csv";

    private static final String CSV_SEPARATOR = ",";

    private static final String I18N_EXTENSION = "org.gvsig.i18n";

    private static final String VARIANT = "variant";

    private static final String COUNTRY = "country";

    private static final String LANGUAGE = "language";

    private static final String REGISTERED_LOCALES_PERSISTENCE = "RegisteredLocales";

    private static final I18nManager DEFAULT = new I18nManagerImpl();

    private Set registeredLocales;

    /**
     * The list of default reference locales. The last one will be used to get
     * all the keys when translating to a new locale.
     */
    private Locale[] referenceLocales = new Locale[] { ENGLISH, SPANISH };

    private Locale[] defaultLocales = new Locale[] { SPANISH, ENGLISH, new Locale("en", "US"), new Locale("ca"), new Locale("gl"), new Locale("eu"), new Locale("de"), new Locale("cs"), new Locale("fr"), new Locale("it"), new Locale("pl"), new Locale("pt"), new Locale("pt", "BR"), new Locale("ro"), new Locale("zh"), new Locale("ru"), new Locale("el"), new Locale("pl"), new Locale("tr"), new Locale("sr"), new Locale("sw") };

    /**
     * Returns the unique instance of the I18nManager.
     *
     * @return the unique instance
     */
    public static I18nManager getInstance() {
        return DEFAULT;
    }

    public static String capitalize(String text) {
        String capitalLetter = new String(new char[] { Character.toUpperCase(text.charAt(0)) });
        return capitalLetter.concat(text.substring(1));
    }

    /**
     * Empty constructor.
     */
    I18nManagerImpl() {
    }

    public Locale[] getInstalledLocales() {
        if (registeredLocales == null) {
            XMLEntity child = getRegisteredLocalesPersistence();
            if (child == null) {
                Locale[] defaultLocales = getDefaultLocales();
                registeredLocales = new LinkedHashSet(defaultLocales.length);
                for (int i = 0; i < defaultLocales.length; i++) {
                    registeredLocales.add(defaultLocales[i]);
                }
                storeInstalledLocales();
            } else {
                XMLEntity localesEntity = getRegisteredLocalesPersistence();
                registeredLocales = new LinkedHashSet(localesEntity.getChildrenCount());
                for (int i = 0; i < localesEntity.getChildrenCount(); i++) {
                    XMLEntity localeEntity = localesEntity.getChild(i);
                    String language = localeEntity.getStringProperty(LANGUAGE);
                    String country = localeEntity.getStringProperty(COUNTRY);
                    String variant = localeEntity.getStringProperty(VARIANT);
                    Locale locale = new Locale(language, country, variant);
                    registeredLocales.add(locale);
                }
            }
        }
        return (Locale[]) registeredLocales.toArray(new Locale[registeredLocales.size()]);
    }

    public void uninstallLocale(Locale locale) throws I18nException {
        if (getCurrentLocale().equals(locale) || isReferenceLocale(locale)) {
            throw new UninstallLocaleException(locale);
        }
        if (registeredLocales.remove(locale)) {
            storeInstalledLocales();
            File bundleFile = new File(getResourcesFolder(), getResourceFileName(locale));
            if (bundleFile.exists()) {
                bundleFile.delete();
            }
        }
    }

    public String getDisplayName(Locale locale) {
        return getDisplayName(locale, locale);
    }

    public String getDisplayName(Locale locale, Locale displayLocale) {
        StringBuffer name = new StringBuffer(getLanguageDisplayName(locale, displayLocale));
        if (!isEmpty(locale.getCountry())) {
            name.append(" - ");
            name.append(locale.getDisplayCountry(displayLocale));
        }
        if (!isEmpty(locale.getVariant())) {
            name.append(" - ");
            name.append(locale.getDisplayVariant(displayLocale));
        }
        name.append(" (").append(locale.toString()).append(")");
        return name.toString();
    }

    private boolean isEmpty(String text) {
        return text == null || text.trim().length() == 0;
    }

    public String getLanguageDisplayName(Locale locale) {
        return getLanguageDisplayName(locale, locale);
    }

    public String getLanguageDisplayName(Locale locale, Locale displayLocale) {
        String displayName;
        if ("eu".equals(locale.getLanguage()) && "vascuence".equals(locale.getDisplayLanguage())) {
            displayName = "Euskera";
        } else if ("ca".equals(locale.getLanguage())) {
            displayName = "Valenci�";
        } else if ("gl".equals(locale.getLanguage()) && "gallegan".equalsIgnoreCase(locale.getDisplayLanguage(displayLocale))) {
            displayName = "Galego";
        } else {
            displayName = locale.getDisplayLanguage(displayLocale);
        }
        return capitalize(displayName);
    }

    public Locale getCurrentLocale() {
        return Locale.getDefault();
    }

    public Locale getDefaultSystemLocale() {
        String language, region, country, variant;
        language = (String) AccessController.doPrivileged(new GetPropertyAction("user.language", "en"));
        region = (String) AccessController.doPrivileged(new GetPropertyAction("user.region"));
        if (region != null) {
            int i = region.indexOf('_');
            if (i >= 0) {
                country = region.substring(0, i);
                variant = region.substring(i + 1);
            } else {
                country = region;
                variant = "";
            }
        } else {
            country = (String) AccessController.doPrivileged(new GetPropertyAction("user.country", ""));
            variant = (String) AccessController.doPrivileged(new GetPropertyAction("user.variant", ""));
        }
        return new Locale(language, country, variant);
    }

    public void setCurrentLocale(Locale locale) {
        AndamiConfig config = Launcher.getAndamiConfig();
        config.setLocaleLanguage(locale.getLanguage());
        config.setLocaleCountry(locale.getCountry());
        config.setLocaleVariant(locale.getVariant());
    }

    public Locale[] installLocales(File importFile) throws I18nException {
        List importLocales = new ArrayList();
        try {
            ZipFile zipFile = new ZipFile(importFile);
            Map locales = getZipFileNonReferenceLocales(zipFile);
            if (locales == null || locales.size() == 0) {
                return null;
            }
            for (Iterator iterator = locales.entrySet().iterator(); iterator.hasNext(); ) {
                Entry entry = (Entry) iterator.next();
                String fileName = (String) entry.getKey();
                Locale locale = (Locale) entry.getValue();
                importLocales.add(locale);
                if (!registeredLocales.contains(locale)) {
                    registeredLocales.add(locale);
                    storeInstalledLocales();
                }
                ZipEntry zipEntry = zipFile.getEntry(fileName);
                InputStream is = zipFile.getInputStream(zipEntry);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                File bundleFile = getResourceFile(locale);
                FileWriter fileWriter = new FileWriter(bundleFile);
                BufferedWriter writer = new BufferedWriter(fileWriter);
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.write('\n');
                }
                writer.flush();
                writer.close();
                fileWriter.close();
                reader.close();
                is.close();
            }
        } catch (Exception ex) {
            throw new InstallLocalesException(importFile, ex);
        }
        return (Locale[]) importLocales.toArray(new Locale[importLocales.size()]);
    }

    public void exportLocaleForUpdate(Locale locale, Locale referenceLocale, File exportFile) throws I18nException {
        exportLocalesForUpdate(new Locale[] { locale }, referenceLocale, exportFile);
    }

    public void exportLocalesForUpdate(Locale[] locales, Locale referenceLocale, File exportFile) throws I18nException {
        exportLocale(locales, new Locale[] { referenceLocale }, exportFile, true);
    }

    public void exportLocaleForTranslation(Locale locale, Locale referenceLocale, File exportFile) throws I18nException {
        exportLocaleForTranslation(locale, new Locale[] { referenceLocale }, exportFile);
    }

    public void exportLocaleForTranslation(Locale locale, Locale[] referenceLocales, File exportFile) throws I18nException {
        exportLocale(new Locale[] { locale }, referenceLocales, exportFile, false);
    }

    public Locale[] getReferenceLocales() {
        return referenceLocales;
    }

    public void setReferenceLocales(Locale[] referenceLocales) {
        this.referenceLocales = referenceLocales;
    }

    public void setDefaultLocales(Locale[] defaultLocales) {
        this.defaultLocales = defaultLocales;
    }

    private void exportLocale(Locale[] locales, Locale[] referenceLocales, File exportFile, boolean update) throws I18nException {
        Locale[] refArray = getReferenceLocalesToExport(locales, referenceLocales);
        Set allReferenceKeys = new HashSet();
        Map referenceTexts = new HashMap(refArray.length);
        for (int i = 0; i < refArray.length; i++) {
            Map texts = getAllTexts(refArray[i]);
            referenceTexts.put(refArray[i], texts);
            allReferenceKeys.addAll(texts.keySet());
        }
        try {
            FileOutputStream fos = new FileOutputStream(exportFile);
            ZipOutputStream zipos = new ZipOutputStream(fos);
            writeZipFileLocales(zipos, locales, refArray);
            PrintStream ps = new PrintStream(zipos);
            Map texts = null;
            if (update) {
                if (locales != null) {
                    for (int i = 0; i < locales.length; i++) {
                        texts = getAllTexts(locales[i]);
                        addPendingKeys(texts, allReferenceKeys);
                        putResourceInZip(zipos, ps, texts, getResourceFileName(locales[i]));
                    }
                }
            } else {
                if (locales != null) {
                    for (int i = 0; i < locales.length; i++) {
                        putResourceInZip(zipos, ps, allReferenceKeys, getResourceFileName(locales[i]));
                    }
                }
            }
            if (refArray != null) {
                for (int i = 0; i < refArray.length; i++) {
                    texts = getAllTexts(refArray[i]);
                    putResourceInZip(zipos, ps, texts, getResourceFileName(refArray[i]));
                }
            }
            ps.flush();
            ps.close();
            zipos.close();
            fos.close();
        } catch (IOException ex) {
            throw new ExportLocaleException(locales, ex);
        }
    }

    /**
     * Adds the keys of the set to the map that aren't still contained in it
     * with a related empty String.
     * 
     * @param texts
     *            the map to complete with the keys
     * @param allReferenceKeys
     *            the complete key set
     */
    private void addPendingKeys(Map texts, Set allReferenceKeys) {
        for (Iterator iterator = allReferenceKeys.iterator(); iterator.hasNext(); ) {
            Object key = (Object) iterator.next();
            if (!texts.containsKey(key)) {
                texts.put(key, "");
            }
        }
    }

    /**
     * Returns the list of reference locales to export, as the union of the
     * default reference locales list and the one selected as reference. The
     * locales to translate or update are extracted from the list.
     */
    private Locale[] getReferenceLocalesToExport(Locale[] locales, Locale[] referenceLocalesSelected) {
        Set exportRefLocales = new HashSet(referenceLocales.length);
        for (int i = 0; i < referenceLocales.length; i++) {
            exportRefLocales.add(referenceLocales[i]);
        }
        if (referenceLocalesSelected != null) {
            for (int i = 0; i < referenceLocalesSelected.length; i++) {
                exportRefLocales.add(referenceLocalesSelected[i]);
            }
        }
        if (locales != null) {
            for (int i = 0; i < locales.length; i++) {
                exportRefLocales.remove(locales[i]);
            }
        }
        Locale[] refArray = (Locale[]) exportRefLocales.toArray(new Locale[exportRefLocales.size()]);
        return refArray;
    }

    /**
     * Returns all the localized texts and its keys for a locale.
     */
    private Map getAllTexts(Locale locale) {
        return Messages.getAllTexts(locale);
    }

    private Map getZipFileNonReferenceLocales(ZipFile zipFile) throws I18nException {
        ZipEntry zipEntry = zipFile.getEntry(LOCALES_FILE_NAME);
        if (zipEntry == null) {
            return null;
        }
        Map locales;
        try {
            InputStream is = zipFile.getInputStream(zipEntry);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            locales = new HashMap(2);
            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, CSV_SEPARATOR, true);
                String fileName = st.nextToken();
                if (CSV_SEPARATOR.equals(fileName)) {
                    throw new LocaleFileNameRequiredException(line);
                } else {
                    st.nextToken();
                }
                String language = st.nextToken();
                if (CSV_SEPARATOR.equals(language)) {
                    throw new LocaleLanguageRequiredException(line);
                } else {
                    st.nextToken();
                }
                String country = st.nextToken();
                if (CSV_SEPARATOR.equals(country)) {
                    country = null;
                } else {
                    st.nextToken();
                }
                String variant = st.nextToken();
                if (CSV_SEPARATOR.equals(variant)) {
                    variant = null;
                } else {
                    st.nextToken();
                }
                String refStr = st.nextToken();
                if (CSV_SEPARATOR.equals(refStr)) {
                    refStr = null;
                }
                if (refStr != null && !"true".equals(refStr.toLowerCase())) {
                    if (country == null) {
                        variant = null;
                    }
                    country = country == null ? "" : country;
                    variant = variant == null ? "" : variant;
                    Locale locale = new Locale(language, country, variant);
                    locales.put(fileName, locale);
                }
            }
            reader.close();
            is.close();
        } catch (IOException ex) {
            throw new ReadCSVLocalesFileException(ex);
        }
        return locales;
    }

    private void writeZipFileLocales(ZipOutputStream zos, Locale[] locales, Locale[] referenceLocales) throws IOException {
        ZipEntry zipEntry = new ZipEntry(LOCALES_FILE_NAME);
        zos.putNextEntry(zipEntry);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(zos));
        if (locales != null) {
            for (int i = 0; i < locales.length; i++) {
                writeLocaleEntry(locales[i], writer, false);
            }
        }
        if (referenceLocales != null) {
            for (int i = 0; i < referenceLocales.length; i++) {
                writeLocaleEntry(referenceLocales[i], writer, true);
            }
        }
        writer.flush();
        zos.closeEntry();
    }

    /**
     * Writes the locale entry into a writer.
     *
     * @param locale
     *            the locale to create the entry for
     * @param writer
     *            to write to
     * @param reference
     *            is it is a reference locale or not
     * @throws IOException
     *             if there is an error creating the locale entry
     */
    private void writeLocaleEntry(Locale locale, BufferedWriter writer, boolean reference) throws IOException {
        String language = locale.getLanguage();
        String country = locale.getCountry();
        country = country == null ? "" : country;
        String variant = locale.getVariant();
        variant = variant == null ? "" : variant;
        writer.write(getResourceFileName(locale));
        writer.write(',');
        writer.write(language);
        writer.write(',');
        writer.write(country);
        writer.write(',');
        writer.write(variant);
        writer.write(',');
        writer.write(Boolean.toString(reference));
        writer.write('\n');
    }

    /**
     * Returns if a locale is one of the default reference ones.
     */
    private boolean isReferenceLocale(Locale locale) {
        for (int i = 0; i < referenceLocales.length; i++) {
            if (referenceLocales[i].equals(locale)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Puts a new resource file into a Jar file.
     */
    private void putResourceInZip(ZipOutputStream zipos, PrintStream ps, Map texts, String resourceFileName) throws IOException {
        zipos.putNextEntry(new ZipEntry(resourceFileName));
        for (Iterator iterator = texts.entrySet().iterator(); iterator.hasNext(); ) {
            Entry entry = (Entry) iterator.next();
            String keyEncoded = escape((String) entry.getKey(), true);
            ps.print(keyEncoded);
            ps.print("=");
            String valueEncoded = escape((String) entry.getValue(), false);
            ps.println(valueEncoded);
        }
        ps.flush();
        zipos.closeEntry();
    }

    /**
     * Puts a new resource file into a Jar file.
     */
    private void putResourceInZip(ZipOutputStream zipos, PrintStream ps, Set keys, String resourceFileName) throws IOException {
        zipos.putNextEntry(new ZipEntry(resourceFileName));
        for (Iterator iterator = keys.iterator(); iterator.hasNext(); ) {
            String value = (String) iterator.next();
            String keyEncoded = escape(value, true);
            ps.print(keyEncoded);
            ps.print("=");
            ps.println();
        }
        ps.flush();
        zipos.closeEntry();
    }

    /**
     * Returns the file which contains the translations for a locale.
     */
    private File getResourceFile(Locale locale) {
        return new File(getResourcesFolder(), getResourceFileName(locale));
    }

    /**
     * Returns the name of the file which contains the translations for a
     * locale.
     */
    private String getResourceFileName(Locale locale) {
        StringBuffer fileName = new StringBuffer("text");
        if (!(isEmpty(locale.getCountry()) && "es".equals(locale.getLanguage()))) {
            fileName.append('_').append(locale.getLanguage());
        }
        if (!isEmpty(locale.getCountry())) {
            fileName.append('_').append(locale.getCountry());
        }
        if (!isEmpty(locale.getVariant())) {
            fileName.append('_').append(locale.getVariant());
        }
        fileName.append(".properties");
        return fileName.toString();
    }

    /**
     * Returns the folder where to store the resource bundle files.
     */
    private File getResourcesFolder() {
        return PluginServices.getPluginServices("com.iver.cit.gvsig").getPluginDirectory();
    }

    /**
     * Returns the child XMLEntity with the RegisteredLocales.
     */
    private XMLEntity getRegisteredLocalesPersistence() {
        XMLEntity entity = getI18nPersistence();
        XMLEntity child = null;
        for (int i = entity.getChildrenCount() - 1; i >= 0; i--) {
            XMLEntity tmpchild = entity.getChild(i);
            if (tmpchild.getName().equals(REGISTERED_LOCALES_PERSISTENCE)) {
                child = tmpchild;
                break;
            }
        }
        return child;
    }

    /**
     * Returns the I18n Plugin persistence.
     */
    private XMLEntity getI18nPersistence() {
        XMLEntity entity = PluginServices.getPluginServices(I18N_EXTENSION).getPersistentXML();
        return entity;
    }

    /**
     * Returns the list of default locales bundled with gvSIG.
     */
    private Locale[] getDefaultLocales() {
        return defaultLocales;
    }

    /**
     * Stores the list of installed locales into the plugin persistence.
     */
    private void storeInstalledLocales() {
        XMLEntity localesEntity = getRegisteredLocalesPersistence();
        if (localesEntity != null) {
            XMLEntity i18nPersistence = getI18nPersistence();
            for (int i = i18nPersistence.getChildrenCount() - 1; i >= 0; i--) {
                XMLEntity child = i18nPersistence.getChild(i);
                if (child.getName().equals(REGISTERED_LOCALES_PERSISTENCE)) {
                    i18nPersistence.removeChild(i);
                    break;
                }
            }
        }
        localesEntity = new XMLEntity();
        localesEntity.setName(REGISTERED_LOCALES_PERSISTENCE);
        for (Iterator iterator = registeredLocales.iterator(); iterator.hasNext(); ) {
            Locale locale = (Locale) iterator.next();
            XMLEntity localeEntity = new XMLEntity();
            localeEntity.setName(locale.getDisplayName());
            localeEntity.putProperty(LANGUAGE, locale.getLanguage());
            localeEntity.putProperty(COUNTRY, locale.getCountry());
            localeEntity.putProperty(VARIANT, locale.getVariant());
            localesEntity.addChild(localeEntity);
        }
        getI18nPersistence().addChild(localesEntity);
    }

    private String escape(String value, boolean replaceBlanks) {
        if (replaceBlanks) {
            value = StringUtilities.replace(value, " ", "\\ ");
        }
        value = StringUtilities.replace(value, ":", "\\:");
        value = StringUtilities.replace(value, "\n", "\\n");
        value = StringUtilities.replace(value, "\t", "\\t");
        value = StringUtilities.replace(value, "\b", "\\b");
        value = StringUtilities.replace(value, "\f", "\\f");
        value = StringUtilities.replace(value, "\r", "\\r");
        return toRawUnicodeEncoded(value);
    }

    private String toRawUnicodeEncoded(String value) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c <= 0x80) {
                sb.append(c);
            } else {
                sb.append("\\u");
                String hexValue = Integer.toHexString((int) c);
                for (int j = hexValue.length(); j < 4; j++) {
                    sb.append('0');
                }
                sb.append(hexValue);
            }
        }
        return sb.toString();
    }
}
