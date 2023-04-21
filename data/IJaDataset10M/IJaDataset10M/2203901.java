package com.beanstalktech.common.plugin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import com.beanstalktech.common.context.Application;
import com.beanstalktech.common.utility.SharedClass;

public class HTTPFunctions implements SharedClass {

    private Application m_application;

    public void initialize(Application application) {
        m_application = application;
    }

    public byte[] getURL(String urlName) {
        URL url;
        try {
            url = new URL(urlName);
        } catch (MalformedURLException e) {
            m_application.getLogger().logError(8, "getURL: Malformed URL: " + urlName);
            return null;
        }
        HttpURLConnection urlConnection;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDefaultUseCaches(true);
            urlConnection.setUseCaches(true);
            HttpURLConnection.setFollowRedirects(true);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                m_application.getLogger().logError(8, "getURL: HTTP Connection failed with status: " + statusCode);
                return null;
            }
            InputStream is = urlConnection.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                out.write(ch);
            }
            if (out.size() > 0) {
                return out.toByteArray();
            } else {
                return null;
            }
        } catch (Exception e) {
            m_application.getLogger().logError(8, "getURL: HTTP Connection failed with exception: " + e);
            return null;
        }
    }

    /**
	 * Get a zip file from local cache. If the zip is not currently cached,
	 * load it into cache from URL and return local zip file.
	 * 
	 * @param urlName - Fully qualified URL of content
	 * @return
	 */
    public ZipFile getCachedZip(String urlName) {
        CryptoFunctions cfunctions = new CryptoFunctions();
        cfunctions.initialize(m_application);
        String cacheName = cfunctions.encrypt(urlName);
        cacheName = cacheName.substring(cacheName.length() - 20, cacheName.length());
        cacheName = cacheName.replace('/', '_');
        String cacheFilePath = m_application.getApplicationContext().getProperty("cache_FilePath");
        String cacheFileName = cacheFilePath + cacheName;
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(new File(cacheFileName), ZipFile.OPEN_READ);
            return zipFile;
        } catch (Exception e) {
        }
        URL url;
        try {
            url = new URL(urlName);
        } catch (MalformedURLException e) {
            m_application.getLogger().logError(8, "getURLWithCache: Malformed URL: " + urlName);
            return null;
        }
        HttpURLConnection urlConnection;
        InputStream is;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDefaultUseCaches(true);
            urlConnection.setUseCaches(true);
            HttpURLConnection.setFollowRedirects(true);
            urlConnection.connect();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                m_application.getLogger().logError(8, "getURLWithCache: HTTP Connection failed with status: " + statusCode);
                return null;
            }
            is = urlConnection.getInputStream();
            FileOutputStream out = new FileOutputStream(cacheFileName);
            byte[] buffer = new byte[1024];
            int len = is.read(buffer);
            while (len >= 0) {
                out.write(buffer, 0, len);
                len = is.read(buffer);
            }
            is.close();
            out.close();
            m_application.getLogger().logMessage(0, "getCachedURL: wrote file to cache: " + cacheName);
        } catch (Exception e) {
            m_application.getLogger().logError(8, "getURLWithCache: HTTP Connection failed with exception: " + e);
            return null;
        }
        try {
            zipFile = new ZipFile(new File(cacheFileName), ZipFile.OPEN_READ);
            return zipFile;
        } catch (Exception e) {
            return null;
        }
    }

    public String getStringURL(String urlName) {
        byte[] byteResponse = getURL(urlName);
        if (byteResponse != null) {
            try {
                return new String(byteResponse, "ISO-8859-1");
            } catch (UnsupportedEncodingException e) {
                m_application.getLogger().logError(8, "getStringURL: Failed to get string from url. Exception: " + e);
                return null;
            }
        } else {
            return null;
        }
    }

    public int getZipEntryCount(String urlName) {
        ZipFile zip = this.getCachedZip(urlName);
        if (zip != null) {
            return zip.size();
        } else {
            return 0;
        }
    }

    public byte[] getZipEntryFromURL(String entryIndex, String urlName) {
        ZipFile zip = this.getCachedZip(urlName);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int index = Integer.valueOf(entryIndex).intValue() - 1;
        if (zip != null) {
            try {
                ZipEntry zipEntry = null;
                String[] entrynames = new String[zip.size()];
                Enumeration entries = zip.entries();
                int i = 0;
                while (entries.hasMoreElements()) {
                    entrynames[i++] = ((ZipEntry) entries.nextElement()).getName();
                }
                Arrays.sort(entrynames, String.CASE_INSENSITIVE_ORDER);
                zipEntry = zip.getEntry(entrynames[index]);
                InputStream is = zip.getInputStream(zipEntry);
                byte[] buffer = new byte[1024];
                int len = is.read(buffer);
                while (len >= 0) {
                    out.write(buffer, 0, len);
                    len = is.read(buffer);
                }
                is.close();
                zip.close();
                out.close();
                return out.toByteArray();
            } catch (Exception e) {
                m_application.getLogger().logError(8, "getZipEntryFromURL: failed to get entry from zip stream. Exception: " + e);
                return null;
            }
        } else {
            return null;
        }
    }
}
