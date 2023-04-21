package org.rakiura.rak;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Utility class for resolving URL/Files and other rak resources.
 * 
 *<br><br>
 * RakLocatorImpl.java<br>
 * Created: Thu May 31 17:52:32 2001<br>
 *
 * @author Mariusz Nowostawski   (mariusz@rakiura.org)
 * @version $Revision: 1.7 $ $Date: 2002/07/25 04:43:19 $
 */
class RakLocatorImpl extends RakLocator {

    /** */
    RakLocatorImpl(RakSystem system) {
        super(system);
    }

    public File getFile(String name) {
        if (isValidURL(name)) {
            return tmpFileFromURL(name);
        } else {
            if (isValidFilename(name)) {
                return tmpFileFromFile(name);
            } else return null;
        }
    }

    public File getFile(ModuleInfo info) {
        String[] refs = info.getJarURL();
        for (int i = 0; i < refs.length; i++) {
            File f = getFile(refs[i]);
            if (f != null) return f;
        }
        return null;
    }

    public ModuleInfo getModuleInfo(String name) {
        ModuleInfo info = null;
        try {
            InputStream is = null;
            if (isValidFilename(name)) {
                is = new FileInputStream(name);
            } else {
                if (isValidURL(name)) {
                    is = new URL(name).openStream();
                }
            }
            if (is != null) {
                try {
                    info = ModuleInfo.read(is);
                    is.close();
                } catch (IOException ioe) {
                    logger.warning("Error processing .rak resource: " + name);
                    logger.finer(".rak resource " + name + ": " + ioe.getMessage());
                }
            }
        } catch (Exception e) {
            logger.finer("RakLocatorImpl.getModuleInfo(): " + e);
        }
        return info;
    }

    private boolean isValidURL(String name) {
        try {
            final URL url = new URL(name);
            return true;
        } catch (MalformedURLException ex) {
            return false;
        }
    }

    private boolean isValidFilename(String name) {
        final File file = new File(name);
        if (!file.exists() || file.isDirectory()) return false; else return true;
    }

    private File tmpFileFromURL(String name) {
        if (name == null) {
            System.out.println("ERROR: the provided URL is invalid, aborting download!");
            return null;
        }
        try {
            final URL url = new URL(name);
            final InputStream in = url.openStream();
            final URLConnection conn = url.openConnection();
            final int total = conn.getContentLength();
            final String contentType = conn.getContentType();
            logger.fine("DOWNLOADING   Content-type: " + contentType);
            if (contentType.trim().toLowerCase().indexOf("html") != -1) {
                return tmpFileFromURL(extractRedirectURL(in));
            }
            final FileManager fileManager = system.getFileManager();
            final File dest = fileManager.createTmpModuleFile();
            final FileOutputStream out = new FileOutputStream(dest);
            final byte[] buf = new byte[2048];
            logger.fine("Total number of bytes to download: " + total);
            int len, current = 0;
            progress(new ProgressEvent(this, "Downloading " + name, 0));
            while ((len = in.read(buf)) > 0) {
                current += len;
                progress(new ProgressEvent(this, "Downloading " + name, (int) ((current * 100.0) / total)));
                out.write(buf, 0, len);
                out.flush();
            }
            in.close();
            out.flush();
            out.close();
            return dest;
        } catch (IOException ex) {
            progress(new ProgressEvent("  ERROR: downloading of " + name + " failed. URL does not exist!"));
            return null;
        }
    }

    private String extractRedirectURL(InputStream in) {
        String line = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            boolean meta = false;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().indexOf("meta") != -1 && line.toLowerCase().indexOf("refresh") != -1) {
                    meta = true;
                }
                if (meta) {
                    int i = line.toLowerCase().indexOf("url=");
                    if (i != -1) {
                        String s = line.substring(i + 4);
                        s = s.substring(0, s.indexOf("\""));
                        return s;
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private File tmpFileFromFile(String name) {
        FileManager fileManager = system.getFileManager();
        try {
            final File src = new File(name);
            final File dest = fileManager.createTmpModuleFile();
            fileManager.copy(src, dest);
            return dest;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
