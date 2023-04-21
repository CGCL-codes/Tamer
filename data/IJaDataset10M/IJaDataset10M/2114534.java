package org.vrforcad.installer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * This class copy the files into the install path. 
 * 
 * @version 1.1 
 * @author Daniel Cioi <dan.cioi@vrforcad.org>
 */
public class UnPack {

    private final int BUFFER = 2048;

    private boolean writeFile;

    public UnPack(String installVersion) {
        makeTemp();
        unZip(installVersion);
    }

    public UnPack() {
    }

    /**
	 * Make temp directory to unpack the files from jar.
	 */
    public void makeTemp() {
        boolean successWrite = (new File("temp")).mkdirs();
        if (!successWrite) {
            System.out.println("Problems on writing a directory on HD");
        }
    }

    /**
	 * Delete temp directory.
	 */
    public void deleteTemp() {
        File dirTemp = new File("temp");
        boolean success = deleteDir(dirTemp);
        if (!success) {
            System.out.println("The temp folder can't be deleted.");
        }
        System.out.println("Temp files deleted? " + success);
    }

    /**
	 * Recursively delete all files and subdirectories in the temp directory.
	 * @param dir temp directory
	 * @return boolean
	 */
    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
	 * Unzip the VRforCAD install.
	 */
    private void unZip(String VRforCADversion) {
        try {
            BufferedOutputStream dest = null;
            BufferedInputStream is = null;
            ZipEntry entry;
            ZipFile zipfile = new ZipFile(VRforCADversion);
            @SuppressWarnings("rawtypes") Enumeration e = zipfile.entries();
            while (e.hasMoreElements()) {
                entry = (ZipEntry) e.nextElement();
                if (entry.isDirectory()) {
                    String dir = "temp/" + entry;
                    boolean successWrite = (new File(dir)).mkdirs();
                    if (!successWrite) {
                        System.out.println("Problems on writing a directory on HD");
                    }
                } else {
                    is = new BufferedInputStream(zipfile.getInputStream(entry));
                    int count;
                    byte data[] = new byte[BUFFER];
                    FileOutputStream fos = new FileOutputStream("temp/" + entry.getName());
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = is.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();
                    is.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Copy the files to the install path. 
	 */
    public boolean copyFiles(String installPath) {
        File sourceDir = new File("temp");
        File destinationDir = new File(installPath);
        try {
            copyDirectory(sourceDir, destinationDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writeFile;
    }

    /**
	 * This method copies directories from temp to the install path.
	 * @param srcDir source directory
	 * @param dstDir destination directory
	 * @throws IOException
	 */
    private void copyDirectory(File srcDir, File dstDir) throws IOException {
        if (srcDir.isDirectory()) {
            if (!dstDir.exists()) {
                writeFile = dstDir.mkdir();
            }
            if (writeFile) {
                String[] listDir = srcDir.list();
                for (int i = 0; i < listDir.length; i++) {
                    copyDirectory(new File(srcDir, listDir[i]), new File(dstDir, listDir[i]));
                }
            } else return;
        } else {
            copyFile(srcDir, dstDir);
        }
    }

    private void copyFile(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
}
