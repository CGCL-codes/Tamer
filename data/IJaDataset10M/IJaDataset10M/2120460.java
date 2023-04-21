package net.hanjava.roas;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;

public class PropertiesBundleImportFilter implements BundleImportFilter {

    private String defaultLocale = "en";

    /**
     * @param what
     *            "C:/src/bundle" when there are "C:/src/bundle.properties",
     *            "C:/src/bundle_ko.properties",
     *            "C:/src/bundle_de.properties"...
     */
    public TextBundleModel importBundle(String what) {
        File basePropFile = new File(what + ".properties");
        if (!basePropFile.exists()) throw new IllegalArgumentException("can't find resource bundle for : " + what);
        String baseFileName = basePropFile.getName();
        final String basename = baseFileName.substring(0, baseFileName.length() - ".properties".length());
        File dir = basePropFile.getParentFile();
        FilenameFilter propNameFilter = new FilenameFilter() {

            public boolean accept(File dir, String name) {
                if (!name.startsWith(basename)) return false;
                return name.endsWith(".properties");
            }
        };
        File[] propFiles = dir.listFiles(propNameFilter);
        TextBundleModel model = new TextBundleModel(basename);
        for (File f : propFiles) {
            Translation t = createTranslation(f);
            model.addTranslation(t);
        }
        return model;
    }

    @SuppressWarnings("unchecked")
    private Translation createTranslation(File f) {
        String[] nal = nameAndLocale(f.getName());
        String localeStr = nal[1];
        Translation result = new Translation(parseLocale(localeStr));
        Properties prop = new Properties();
        FileInputStream fis;
        try {
            fis = new FileInputStream(f);
            prop.load(fis);
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (Enumeration keys = prop.keys(); keys.hasMoreElements(); ) {
            String key = keys.nextElement().toString();
            String value = prop.getProperty(key);
            result.set(key, value);
        }
        return result;
    }

    /**
     * @param fname
     *            "yugyum_ko.properties" or "yugyum.properties"
     */
    String[] nameAndLocale(String fname) {
        String name = fname.substring(0, fname.length() - ".properties".length());
        String[] result = new String[2];
        int sepIndex = name.indexOf('_');
        if (sepIndex < 0) {
            result[0] = name;
            result[1] = getDefaultLocale();
        } else {
            result[0] = name.substring(0, sepIndex);
            result[1] = name.substring(sepIndex + 1);
        }
        return result;
    }

    public void setDefaultLocale(String locale) {
        defaultLocale = locale;
    }

    public String getDefaultLocale() {
        return defaultLocale;
    }

    /**
     * @param localeStr
     *            can not be null or 0-length
     */
    public static Locale parseLocale(String localeStr) {
        String lang = null;
        int index = localeStr.indexOf('_');
        if (index < 0) {
            lang = localeStr;
            return new Locale(lang);
        }
        lang = localeStr.substring(0, index);
        localeStr = localeStr.substring(3);
        String country = null;
        index = localeStr.indexOf('_');
        if (index < 0) {
            country = localeStr;
            return new Locale(lang, country);
        }
        country = localeStr.substring(0, index);
        String variant = localeStr.substring(3);
        return new Locale(lang, country, variant);
    }

    public static void main(String[] args) throws Exception {
        String what = args[0];
        String xls = args[1];
        PropertiesBundleImportFilter importer = new PropertiesBundleImportFilter();
        TextBundleModel bundleModel = importer.importBundle(what);
        System.out.println("bundleMode : " + bundleModel);
        FileOutputStream fos = new FileOutputStream(xls);
        XlsExportFilter xlsFilter = new XlsExportFilter(fos);
        xlsFilter.exportBundle(bundleModel);
        fos.close();
    }
}
