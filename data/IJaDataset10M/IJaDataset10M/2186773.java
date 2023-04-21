package net.chowda.castcluster.provider;

import com.sun.syndication.fetcher.impl.SyndFeedInfo;
import com.sun.syndication.fetcher.impl.FeedFetcherCache;
import java.io.*;
import java.net.URL;

/**
 * stolen from ROME svn repo... THANKS!
 *
 * Disk based cache.
 */
public class DiskFeedInfoCache implements FeedFetcherCache {

    protected String cachePath = null;

    public DiskFeedInfoCache(String cachePath) {
        this.cachePath = cachePath;
    }

    public SyndFeedInfo getFeedInfo(URL url) {
        SyndFeedInfo info = null;
        new File(cachePath).mkdirs();
        String fileName = cachePath + File.separator + "feed_" + replaceNonAlphanumeric(url.toString(), '_').trim();
        FileInputStream fis;
        try {
            fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            info = (SyndFeedInfo) ois.readObject();
            fis.close();
        } catch (FileNotFoundException fnfe) {
        } catch (ClassNotFoundException cnfe) {
            throw new RuntimeException("Attempting to read from cache", cnfe);
        } catch (IOException fnfe) {
            throw new RuntimeException("Attempting to read from cache", fnfe);
        }
        return info;
    }

    public void setFeedInfo(URL url, SyndFeedInfo feedInfo) {
        String fileName = cachePath + File.separator + "feed_" + replaceNonAlphanumeric(url.toString(), '_').trim();
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(feedInfo);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            throw new RuntimeException("Attempting to write to cache", e);
        }
    }

    public static String replaceNonAlphanumeric(String str, char subst) {
        StringBuffer ret = new StringBuffer(str.length());
        char[] testChars = str.toCharArray();
        for (char testChar : testChars) {
            if (Character.isLetterOrDigit(testChar)) {
                ret.append(testChar);
            } else {
                ret.append(subst);
            }
        }
        return ret.toString();
    }
}
