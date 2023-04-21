package com.croftsoft.core.net;

import java.io.*;
import java.net.*;
import java.util.*;
import com.croftsoft.core.lang.StringLib;

/*********************************************************************
     * Downloads the content of a URL to a local File.
     * 
     * <p />
     *
     * @version
     *   2003-11-06
     * @since
     *   1998-11-30
     * @author
     *   <a href="http://www.CroftSoft.com/">David Wallace Croft</a>
     *********************************************************************/
public final class Downloader {

    /*********************************************************************
     * Downloads files.
     *
     * <pre>
     * if ( args.length < 4 )
     * {
     *   validate ( args [ 0 ], args [ 1 ], true, true );
     * }
     * else
     * {
     *   download ( args [ 0 ], args [ 1 ],
     *     Integer.parseInt ( args [ 2 ] ),
     *     Integer.parseInt ( args [ 3 ] ) );
     * }
     * </pre>
     *********************************************************************/
    public static void main(String[] args) throws Exception {
        if (args.length < 4) {
            validate(args[0], args[1], true, true);
        } else {
            download(args[0], args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        }
    }

    /*********************************************************************
     * Downloads a sequence of files ending in a number.
     *********************************************************************/
    public static void download(String urlPrefix, String filenamePrefix, int start, int stop) throws IOException {
        for (int index = start; index <= stop; index++) {
            String url = urlPrefix + index;
            String filename = filenamePrefix + index;
            System.out.println("Downloading " + url + " to " + filename + "...");
            download(url, filename);
        }
    }

    public static void download(String urlString, String filename) throws IOException, MalformedURLException {
        download(new URL(urlString), new File(filename));
    }

    /*********************************************************************
     * Downloads the content at a URL to a local destination File.
     *********************************************************************/
    public static void download(URL url, File dest) throws IOException {
        download(url.openStream(), dest);
    }

    public static void download(InputStream unbufferedInputStream, File destinationFile) throws IOException {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(unbufferedInputStream);
            out = new BufferedOutputStream(new FileOutputStream(destinationFile));
            int i;
            while ((i = in.read()) > -1) {
                out.write((byte) i);
            }
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        }
    }

    public static boolean downloadResourceToDir(URL codebaseURL, String name, File destDir) {
        if ((codebaseURL == null) || (name == null) || (destDir == null)) return false;
        InputStream inputStream = null;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            File cacheFile = new File(destDir, name);
            cacheFile = new File(cacheFile.getCanonicalPath());
            File parentFile = new File(cacheFile.getParent());
            parentFile.mkdirs();
            inputStream = downloadResource(codebaseURL, name);
            if (inputStream == null) {
                System.out.println("  Download of \"" + name + "\" from\n" + "    \"" + codebaseURL + "\" failed.");
                return false;
            }
            in = new BufferedInputStream(inputStream);
            out = new BufferedOutputStream(new FileOutputStream(cacheFile));
            int i;
            while ((i = in.read()) > -1) out.write((byte) i);
            System.out.println("  Successfully downloaded and saved.");
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (Exception ex1) {
            }
            try {
                in.close();
            } catch (Exception ex1) {
            }
            try {
                inputStream.close();
            } catch (Exception ex1) {
            }
        }
        return false;
    }

    /*********************************************************************
     * Returns null if the codebaseURL is null.
     * Returns null upon failure.
     *********************************************************************/
    public static InputStream downloadResource(URL codebaseURL, String name) {
        if (codebaseURL == null) return null;
        InputStream inputStream = null;
        try {
            String urlName = replaceSpaces(replaceSeparators(name));
            URL url = new URL(codebaseURL, urlName);
            System.out.println("Downloading \"" + name + "\" from\n  \"" + url + "\"");
            URLConnection urlConnection = url.openConnection();
            inputStream = urlConnection.getInputStream();
            return inputStream;
        } catch (IOException ex) {
            try {
                inputStream.close();
            } catch (Exception ex1) {
            }
            return null;
        }
    }

    /*********************************************************************
     * Determines if the local file is valid.
     *
     * <p>
     * If both <i>compareContentLength</i> and <i>compareLastModified</i>
     * are false, this methods simply checks for the existence of the
     * local file.
     * </p>
     *********************************************************************/
    public static boolean isValid(URL sourceURL, File localFile, boolean compareContentLength, boolean compareLastModified) throws IOException, ProtocolException {
        if (!localFile.exists()) {
            return false;
        }
        if (!compareContentLength && !compareLastModified) {
            return true;
        }
        URLConnection urlConnection = sourceURL.openConnection();
        if (compareContentLength) {
            long localLength = localFile.length();
            int sourceLength = urlConnection.getContentLength();
            if (localLength != sourceLength) {
                return false;
            }
        }
        if (compareLastModified) {
            long localLastModified = localFile.lastModified();
            long sourceLastModified = urlConnection.getLastModified();
            if (localLastModified != sourceLastModified) {
                return false;
            }
        }
        return true;
    }

    /*********************************************************************
     * Determines if the local file is valid.
     *
     * <p>
     * If both <i>compareContentLength</i> and <i>compareLastModified</i>
     * are false, this methods simply checks for the existence of the
     * local file.
     * </p>
     *
     * @param  sourceURLName
     *
     *   An HTTP URL.
     *********************************************************************/
    public static boolean isValid(String sourceURLName, String localFilename, boolean compareContentLength, boolean compareLastModified) throws IOException, ProtocolException {
        return isValid(new URL(sourceURLName), new File(localFilename), compareContentLength, compareLastModified);
    }

    /*********************************************************************
     * Replaces every instance of File.separator with a forward slash
     * character.  This is used for converting a local path name to
     * a URL path name.
     *********************************************************************/
    public static String replaceSeparators(String localPath) {
        return StringLib.replace(localPath, File.separator, "/");
    }

    /*********************************************************************
     * Replaces every instance of space (" ") with the
     * x-www-form-urlencoded equivalent ("%20").
     * This is used for converting a remote filename with spaces so that
     * it can be downloaded via a HTTP request.
     *********************************************************************/
    public static String replaceSpaces(String remoteFilename) {
        return StringLib.replace(remoteFilename, " ", "%20");
    }

    /*********************************************************************
     * Downloads a file if the local copy is missing or outdated.
     *
     * @return
     *
     *   True if a new copy needed to be downloaded.
     *********************************************************************/
    public static boolean validate(String sourceURLName, String localFilename, boolean checkContentLength, boolean checkLastModified) throws IOException, MalformedURLException {
        return validate(new URL(sourceURLName), new File(localFilename), checkContentLength, checkLastModified);
    }

    /*********************************************************************
     * Downloads a file if the local copy is missing or outdated.
     *
     * @return
     *
     *   True if a new copy needed to be downloaded.
     *********************************************************************/
    public static boolean validate(URL sourceURL, File localFile, boolean checkContentLength, boolean checkLastModified) throws IOException {
        if (!localFile.exists()) {
            Downloader.download(sourceURL, localFile);
            return true;
        } else if (checkContentLength || checkLastModified) {
            long localLength = localFile.length();
            long localLastModified = localFile.lastModified();
            URLConnection urlConnection = sourceURL.openConnection();
            int sourceLength = urlConnection.getContentLength();
            long sourceLastModified = urlConnection.getLastModified();
            InputStream inputStream = null;
            try {
                inputStream = urlConnection.getInputStream();
                if (checkContentLength && (sourceLength != localLength) || checkLastModified && (sourceLastModified > localLastModified)) {
                    download(inputStream, localFile);
                    return true;
                }
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }
        return false;
    }

    private Downloader() {
    }
}
