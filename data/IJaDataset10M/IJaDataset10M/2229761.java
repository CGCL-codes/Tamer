package org.aplikator.server.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class Configurator {

    public static final String STRUCTURE = "aplikator.structure";

    public static final String BUNDLE = "aplikator.bundle";

    public static final String HOME = "aplikator.home";

    public static final String CONFIG = "aplikator.config";

    public static final String DATASOURCE = "aplikator.datasourceName";

    private static final Logger LOG = Logger.getLogger(Configurator.class.getName());

    private static Configurator instance = instance();

    private Config config;

    private UTF8ClassLoader UTF8cl = new UTF8ClassLoader();

    private ResourceBundle.Control control = new Control();

    public static Configurator get() {
        return instance;
    }

    private static Configurator instance() {
        Configurator c = new Configurator();
        Config conf = ConfigFactory.load();
        Config userConf = ConfigFactory.parseFileAnySyntax(new File(conf.getString(CONFIG)));
        c.config = userConf.withFallback(conf);
        return c;
    }

    public Config getConfig() {
        return config;
    }

    public String getLocalizedString(String key, Locale locale) {
        if (key == null) return "null";
        ResourceBundle rb = null;
        try {
            rb = ResourceBundle.getBundle(config.getString(BUNDLE), locale, UTF8cl, control);
        } catch (MissingResourceException ex) {
            LOG.warning("Cannot find resource bundle:" + config.getString(BUNDLE));
        }
        if (rb != null) {
            try {
                return rb.getString(key);
            } catch (MissingResourceException ex) {
                return key;
            }
        }
        return key;
    }

    public class Control extends ResourceBundle.Control {

        public final List<String> FORMATS = Collections.unmodifiableList(Arrays.asList("user"));

        public List<String> getFormats(String baseName) {
            if (baseName == null) throw new NullPointerException();
            return FORMATS;
        }

        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {
            if (baseName == null || locale == null || format == null || loader == null) throw new NullPointerException();
            ResourceBundle bundle = null;
            if (format.equals("user")) {
                String bundleName = toBundleName(baseName, locale);
                String resourceName = "file://" + config.getString(HOME) + "/" + bundleName + ".properties";
                InputStream stream = null;
                try {
                    URL url = new URL(resourceName);
                    if (url != null) {
                        URLConnection connection = url.openConnection();
                        if (connection != null) {
                            if (reload) {
                                connection.setUseCaches(false);
                            }
                            stream = connection.getInputStream();
                        }
                    }
                } catch (Throwable t) {
                }
                if (stream != null) {
                    BufferedInputStream bis = new BufferedInputStream(stream);
                    bundle = new UserResourceBundle(UTF8ClassLoader.readUTFStreamToEscapedASCII(bis), super.newBundle(baseName, locale, "java.properties", loader, reload));
                    bis.close();
                } else {
                    return super.newBundle(baseName, locale, "java.properties", loader, reload);
                }
            }
            return bundle;
        }
    }

    public static class UTF8ClassLoader extends ClassLoader {

        /**
         * Charset used when reading a properties file.
         */
        private static final String CHARSET = "UTF-8";

        /**
         * Buffer size used when reading a properties file.
         */
        private static final int BUFFER_SIZE = 2000;

        public UTF8ClassLoader() {
            super(UTF8ClassLoader.class.getClassLoader());
        }

        @Override
        public InputStream getResourceAsStream(String name) {
            try {
                return readUTFStreamToEscapedASCII(super.getResourceAsStream(name));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * Reads a UTF-8 stream, performing a conversion to ASCII (i.e.,
         * ISO8859-1 encoding). Characters outside the normal range for
         * ISO8859-1 are converted to unicode escapes. In effect, it is
         * performing native2ascii on the files, on the fly.
         */
        private static InputStream readUTFStreamToEscapedASCII(InputStream is) throws IOException {
            Reader reader = new InputStreamReader(is, CHARSET);
            StringBuilder builder = new StringBuilder(BUFFER_SIZE);
            char[] buffer = new char[BUFFER_SIZE];
            while (true) {
                int length = reader.read(buffer);
                if (length < 0) break;
                for (int i = 0; i < length; i++) {
                    char ch = buffer[i];
                    if (ch <= '') {
                        builder.append(ch);
                        continue;
                    }
                    builder.append(String.format("\\u%04x", (int) ch));
                }
            }
            reader.close();
            byte[] resourceContent = builder.toString().getBytes();
            return new ByteArrayInputStream(resourceContent);
        }
    }

    public class UserResourceBundle extends PropertyResourceBundle {

        public UserResourceBundle(InputStream stream, ResourceBundle parent) throws IOException {
            super(stream);
            setParent(parent);
        }
    }
}
