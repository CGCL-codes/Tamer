package org.archive.processors.fetcher;

import it.unimi.dsi.mg4j.util.MutableString;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;
import org.apache.commons.httpclient.Cookie;
import org.archive.state.Immutable;
import org.archive.state.Initializable;
import org.archive.state.Key;
import org.archive.state.KeyManager;
import org.archive.state.StateProvider;
import org.archive.util.IoUtils;

/**
 * @author pjack
 *
 */
public class FileCookieStorage implements CookieStorage, Initializable, Closeable {

    private static final Logger LOGGER = Logger.getLogger(FileCookieStorage.class.getName());

    @Immutable
    public static final Key<String> LOAD_COOKIES_FROM_FILE = Key.make("");

    @Immutable
    public static final Key<String> SAVE_COOKIES_TO_FILE = Key.make("");

    static {
        KeyManager.addKeys(FileCookieStorage.class);
    }

    private SortedMap<String, Cookie> cookies;

    private String saveCookiesToFile;

    public void initialTasks(StateProvider provider) {
        String fname = provider.get(this, LOAD_COOKIES_FROM_FILE);
        this.cookies = loadCookies(fname);
        this.saveCookiesToFile = provider.get(this, SAVE_COOKIES_TO_FILE);
    }

    /**
     * Load cookies from a file before the first fetch.
     * <p>
     * The file is a text file in the Netscape's 'cookies.txt' file format.<br>
     * Example entry of cookies.txt file:<br>
     * <br>
     * www.archive.org FALSE / FALSE 1074567117 details-visit texts-cralond<br>
     * <br>
     * Each line has 7 tab-separated fields:<br>
     * <li>1. DOMAIN: The domain that created and have access to the cookie
     * value.
     * <li>2. FLAG: A TRUE or FALSE value indicating if hosts within the given
     * domain can access the cookie value.
     * <li>3. PATH: The path within the domain that the cookie value is valid
     * for.
     * <li>4. SECURE: A TRUE or FALSE value indicating if to use a secure
     * connection to access the cookie value.
     * <li>5. EXPIRATION: The expiration time of the cookie value (unix style.)
     * <li>6. NAME: The name of the cookie value
     * <li>7. VALUE: The cookie value
     * 
     * @param cookiesFile
     *            file in the Netscape's 'cookies.txt' format.
     */
    public static SortedMap<String, Cookie> loadCookies(String cookiesFile) {
        SortedMap<String, Cookie> result = new TreeMap<String, Cookie>();
        if (cookiesFile == null || cookiesFile.length() <= 0) {
            return result;
        }
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(cookiesFile, "r");
            String[] cookieParts;
            String line;
            Cookie cookie = null;
            while ((line = raf.readLine()) != null) {
                if (!line.startsWith("#")) {
                    cookieParts = line.split("\\t");
                    if (cookieParts.length == 7) {
                        cookie = new Cookie(cookieParts[0], cookieParts[5], cookieParts[6], cookieParts[2], -1, Boolean.valueOf(cookieParts[3]).booleanValue());
                        if (cookieParts[1].toLowerCase().equals("true")) {
                            cookie.setDomainAttributeSpecified(true);
                        } else {
                            cookie.setDomainAttributeSpecified(false);
                        }
                        LOGGER.fine("Adding cookie: " + cookie.toExternalForm());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not find file: " + cookiesFile + " (Element: " + LOAD_COOKIES_FROM_FILE.getFieldName() + ")");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtils.close(raf);
        }
        return result;
    }

    public static void saveCookies(String saveCookiesFile, Map<String, Cookie> cookies) {
        if (saveCookiesFile == null || saveCookiesFile.length() <= 0) {
            return;
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File(saveCookiesFile));
            String tab = "\t";
            out.write("# Heritrix Cookie File\n".getBytes());
            out.write("# This file is the Netscape cookies.txt format\n\n".getBytes());
            for (Cookie cookie : cookies.values()) {
                MutableString line = new MutableString(1024 * 2);
                line.append(cookie.getDomain());
                line.append(tab);
                line.append(cookie.isDomainAttributeSpecified() == true ? "TRUE" : "FALSE");
                line.append(tab);
                line.append(cookie.getPath());
                line.append(tab);
                line.append(cookie.getSecure() == true ? "TRUE" : "FALSE");
                line.append(tab);
                line.append(cookie.getName());
                line.append(tab);
                line.append(cookie.getValue());
                line.append("\n");
                out.write(line.toString().getBytes());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not find file: " + saveCookiesFile + " (Element: " + SAVE_COOKIES_TO_FILE.getFieldName() + ")");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public SortedMap<String, Cookie> loadCookiesMap() {
        return cookies;
    }

    public void saveCookiesMap(Map<String, Cookie> map) {
        saveCookies(saveCookiesToFile, map);
    }

    public void close() throws IOException {
    }
}
