package rabbit.installer;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;

/** This class packs a directory to a zipfile, for easy distribution
 *  of the RabbIT proxy.
 */
public class Packer {

    /** Should we run in verbose mode? */
    public static boolean verbose = false;

    /** Start a new Packer.
     */
    public static void main(String[] args) {
        if (args.length >= 2) {
            if (args.length > 2) verbose = true;
            new Packer(args[0], args[1]);
        } else new Packer();
        System.exit(0);
    }

    /** Create a new Packer that ask for directory and zip file.
     */
    public Packer() {
        FileDialog fd = new FileDialog(new Frame(), "Select directory to zip", FileDialog.SAVE);
        fd.setVisible(true);
        String dir = fd.getFile();
        if (dir == null) System.exit(0);
        File f = new File(fd.getDirectory() + fd.getFile());
        if (!f.isDirectory()) {
            System.err.println("That is not a directory, aborting");
            System.exit(-1);
        }
        FileDialog fd2 = new FileDialog(new Frame(), "select file to create", FileDialog.SAVE);
        fd2.setVisible(true);
        String dir2 = fd2.getFile();
        if (dir2 == null) System.exit(0);
        File f2 = new File(fd2.getDirectory() + fd2.getFile());
        if (f2.exists() && f2.isDirectory()) {
            System.err.println("File exist and is a directory, aborting");
            System.exit(-1);
        }
        try {
            createZip(f, f2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Create a new Packer with given directory and file.
     * @param dir the directory to pack.
     * @param file the name of the zip file.
     */
    public Packer(String dir, String file) {
        File d = new File(dir);
        File f = new File(file);
        try {
            createZip(d, f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Create the zipfile and add the directory to it.
     * @param dir the file of the zipfile.
     * @param zipfile the name of the zip file to create.
     * @throws IOException if creation of file fails.
     */
    public void createZip(File dir, File zipfile) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipfile));
        zos.setMethod(ZipOutputStream.DEFLATED);
        zos.setLevel(9);
        if (!(dir.getName().equals("."))) addDir("." + File.separatorChar + dir.getName() + File.separatorChar, dir, zos); else addDir("", dir, zos);
        zos.close();
    }

    /** Add a directory to a zipfile.
     * @param path the path to prepend to the files in this directory.
     * @param dir the directory to add.
     * @param zos the zipstream we are writing to.
     * @throws IOException if writing to the zipfile fails.
     */
    public void addDir(String path, File dir, ZipOutputStream zos) throws IOException {
        if (verbose) System.out.println("adding dir: " + path);
        String[] files = dir.list();
        for (int i = 0; i < files.length; i++) {
            File f = new File(dir, files[i]);
            if (f.isFile()) addFile(path, f, zos); else if (f.isDirectory()) addDir(path + f.getName() + f.separatorChar, f, zos);
        }
    }

    /** Add a file to a zipfile.
     * @param path the path to prepend to the filename.
     * @param f the File to add to the zip.
     * @param zos the zipstream we are writing to.
     * @throws IOException if writing to the zipfile fails.
     */
    public void addFile(String path, File f, ZipOutputStream zos) throws IOException {
        if (verbose) System.out.println("adding file: " + path + f.getName());
        ZipEntry ze = new ZipEntry(path + f.getName());
        ze.setSize(f.length());
        ze.setTime(f.lastModified());
        zos.putNextEntry(ze);
        FileInputStream fis = new FileInputStream(f);
        byte buf[] = new byte[1024];
        int read;
        while ((read = fis.read(buf)) >= 0) {
            zos.write(buf, 0, read);
        }
        fis.close();
        zos.flush();
        zos.closeEntry();
    }
}
