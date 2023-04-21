package org.opengpx.lib;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * 
 * @author Martin Preishuber
 *
 */
public class SpoilerDownloader {

    /**
	 * 
	 * @author Martin Preishuber
	 *
	 */
    static class WebImage {

        private String mstrName;

        private String mstrURL;

        /**
		 * 
		 * @param name
		 * @param url
		 */
        public WebImage(String name, String url) {
            this.mstrName = name;
            this.mstrURL = url;
        }

        /**
		 * 
		 */
        @Override
        public String toString() {
            return String.format("%s (%s)", this.mstrName, this.mstrURL);
        }
    }

    private static final String CACHE_URL = "http://coord.info/";

    private String mstrCacheCode;

    private String mstrTargetFolder;

    private ArrayList<WebImage> mSpoilerImages = new ArrayList<WebImage>();

    /**
	 * 
	 * @param cacheCode
	 * @param targetFolder
	 */
    public SpoilerDownloader(String cacheCode, String targetFolder) {
        this.mstrCacheCode = cacheCode;
        this.mstrTargetFolder = targetFolder;
    }

    /**
	 * 
	 */
    public void run() {
        final String strURL = String.format("%s%s", CACHE_URL, this.mstrCacheCode);
        final String strWebPage = this.getWebPage(strURL);
        this.extractImageURLs(strWebPage);
        for (WebImage si : this.mSpoilerImages) {
            this.downloadImage(si);
        }
    }

    /**
	 * 
	 * @param url
	 * @return
	 */
    private String getWebPage(String strURL) {
        URL url = null;
        InputStream is = null;
        DataInputStream dis = null;
        String strLine;
        StringBuilder sbWebPage = new StringBuilder();
        ;
        try {
            url = new URL(strURL);
            is = url.openStream();
            dis = new DataInputStream(new BufferedInputStream(is));
            while ((strLine = dis.readLine()) != null) {
                sbWebPage.append(strLine + "\n");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sbWebPage.toString();
    }

    /**
	 * 
	 * @param strWebPage
	 */
    private void extractImageURLs(String strWebPage) {
        final int intStartImageSpan = strWebPage.indexOf("<span id=\"Images\">");
        String strImageURLs = "";
        if (intStartImageSpan != -1) strImageURLs = strWebPage.substring(intStartImageSpan + 18);
        final int intEndImageSpan = strImageURLs.indexOf("</span>");
        if (intEndImageSpan != -1) strImageURLs = strImageURLs.substring(0, intEndImageSpan);
        if (strImageURLs.length() > 0) {
            while (strImageURLs.indexOf("<a href=") != -1) {
                final int intStartHREF = strImageURLs.indexOf("<a href=");
                strImageURLs = strImageURLs.substring(intStartHREF + 9);
                final int intEndQuote = strImageURLs.indexOf("\"");
                final String strImageURL = strImageURLs.substring(0, intEndQuote);
                final int intEndHREF = strImageURLs.indexOf("</a>");
                final String strData = strImageURLs.substring(0, intEndHREF);
                final String strName = strData.substring(strData.lastIndexOf(">") + 1).trim();
                this.mSpoilerImages.add(new WebImage(strName, strImageURL));
            }
        }
    }

    /**
	 * 
	 * @param image
	 */
    private void downloadImage(WebImage image) {
        URL url;
        InputStream is = null;
        DataInputStream dis = null;
        FileOutputStream fos = null;
        String strFileName = String.format("%s%s%s_%s.jpg", this.mstrTargetFolder, File.separator, this.mstrCacheCode, image.mstrName);
        try {
            fos = new FileOutputStream(strFileName);
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(fos));
            url = new URL(image.mstrURL);
            is = url.openStream();
            dis = new DataInputStream(new BufferedInputStream(is));
            byte[] buf = new byte[1024];
            int numRead = 0;
            while ((numRead = dis.read(buf)) != -1) {
                dos.write(buf, 0, numRead);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        SpoilerDownloader sd = new SpoilerDownloader("GC1R9BX", "/Users/preisl/Downloads/x2");
        sd.run();
    }
}
