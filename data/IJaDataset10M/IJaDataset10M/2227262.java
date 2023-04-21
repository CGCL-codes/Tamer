package dk.mirasola.systemtraining.server.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TextsFactory {

    private static class CacheKey<T> {

        private Class<T> textInterface;

        private String locale;

        private CacheKey(Class<T> textInterface, String locale) {
            this.textInterface = textInterface;
            this.locale = locale;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CacheKey cacheKey = (CacheKey) o;
            if (!locale.equals(cacheKey.locale)) return false;
            if (!textInterface.equals(cacheKey.textInterface)) return false;
            return true;
        }

        @Override
        public int hashCode() {
            int result = textInterface.hashCode();
            result = 31 * result + locale.hashCode();
            return result;
        }
    }

    private static List<String> supportedLocales = new ArrayList<String>();

    private static Map<CacheKey, Texts> cache = new HashMap<CacheKey, Texts>();

    private static Logger logger = Logger.getLogger(TextsFactory.class.getSimpleName());

    static {
        supportedLocales.add("en");
        supportedLocales.add("da");
    }

    public static <T extends Texts> T getTexts(final Class<T> textInterface, String locale) {
        synchronized (textInterface) {
            CacheKey<T> key = new CacheKey<T>(textInterface, locale);
            T t = (T) cache.get(key);
            if (t == null) {
                if (!supportedLocales.contains(locale) && logger.isLoggable(Level.WARNING)) {
                    logger.warning("getTexts called with unsupported locale " + locale + " for texts interface " + textInterface.getSimpleName() + ". Using fallback en locale.");
                    locale = "en";
                }
                Source source = textInterface.getAnnotation(Source.class);
                if (source == null) {
                    throw new RuntimeException("Missing @source annotation on " + textInterface.getSimpleName());
                }
                if (source.value().length() == 0) {
                    throw new RuntimeException("Empty value for @source annontation on " + textInterface.getSimpleName());
                }
                String resourceName = source.value() + "_" + locale + ".properties";
                InputStream is = TextsFactory.class.getClassLoader().getResourceAsStream(resourceName);
                if (is == null) {
                    throw new RuntimeException("Property file " + resourceName + " missing in classpath");
                }
                final Properties props = loadProperties(resourceName, is);
                t = (T) Proxy.newProxyInstance(TextsFactory.class.getClassLoader(), new Class[] { textInterface }, new InvocationHandler() {

                    @Override
                    public Object invoke(Object o, Method method, Object[] params) throws Throwable {
                        Key key = method.getAnnotation(Key.class);
                        if (key == null) {
                            throw new RuntimeException("Missing @Key annotation on method " + method.getName() + " for interface " + textInterface.getSimpleName());
                        }
                        String prop = props.getProperty(key.value());
                        if (params == null || params.length == 0) {
                            return prop;
                        } else {
                            return MessageFormat.format(prop, params);
                        }
                    }
                });
                cache.put(key, t);
            }
            return t;
        }
    }

    private static Properties loadProperties(String resourceName, InputStream is) {
        InputStreamReader reader = null;
        Properties props = new Properties();
        try {
            reader = new InputStreamReader(is, "utf-8");
            props.load(reader);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("huh ... utf-8 not supported", e);
        } catch (IOException e) {
            throw new RuntimeException("IO exception reading " + resourceName, e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.log(Level.WARNING, e.getMessage(), e);
                }
            }
        }
        return props;
    }
}
