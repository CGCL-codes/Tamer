package com.csft.poker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarOutputStream;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

/**
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 * 
 * @author ywu
 */
public class FileHelper {

    public FileHelper() {
        super();
    }

    public void copyFile(File sourceFile, String toDir) throws FileNotFoundException, IOException {
        copyFile(sourceFile, toDir, false);
    }

    public void copyFile(File sourceFile, String toDir, boolean create) throws FileNotFoundException, IOException {
        copyFile(sourceFile, toDir, create, true);
    }

    public void copyFile(File sourceFile, String toDir, boolean create, boolean overwrite) throws FileNotFoundException, IOException {
        FileInputStream source = null;
        FileOutputStream destination = null;
        byte[] buffer;
        int bytes_read;
        File toFile = new File(toDir);
        if (create && !toFile.exists()) toFile.mkdirs();
        if (toFile.exists()) {
            File destFile = new File(toDir + "/" + sourceFile.getName());
            try {
                if (!destFile.exists() || overwrite) {
                    source = new FileInputStream(sourceFile);
                    destination = new FileOutputStream(destFile);
                    buffer = new byte[1024];
                    while (true) {
                        bytes_read = source.read(buffer);
                        if (bytes_read == -1) break;
                        destination.write(buffer, 0, bytes_read);
                    }
                }
            } catch (Exception exx) {
                exx.printStackTrace();
            } finally {
                if (source != null) try {
                    source.close();
                } catch (IOException e) {
                }
                if (destination != null) try {
                    destination.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public void copyDirContents(File srcDir, String outputDir) throws FileNotFoundException, IOException {
        File[] flist = srcDir.listFiles();
        if (flist != null) for (int x = 0; x < flist.length; x++) {
            copyFile(flist[x], outputDir, true);
        }
    }

    public void copyRecursiveChildren(File srcDir, String outputDir) throws FileNotFoundException, IOException {
        File[] flist = srcDir.listFiles();
        for (int x = 0; x < flist.length; x++) {
            copyRecursive(flist[x], outputDir);
        }
    }

    public void copyRecursive(File srcDir, String outputDir) throws FileNotFoundException, IOException {
        copyRecursive(srcDir, outputDir, true);
    }

    public void copyRecursive(File srcDir, String outputDir, boolean overwrite) throws FileNotFoundException, IOException {
        if (srcDir.isDirectory() && !srcDir.getName().equals("CVS")) {
            File[] flist = srcDir.listFiles();
            for (int x = 0; x < flist.length; x++) {
                copyRecursive(flist[x], outputDir + "/" + srcDir.getName(), overwrite);
            }
        } else {
            if (!srcDir.isDirectory()) copyFile(srcDir, outputDir, true, overwrite);
        }
    }

    public void copy(InputStream _in, OutputStream _out, boolean _closeInput) throws IOException {
        byte[] buffer = getCopyBuffer();
        int read;
        while (true) {
            read = _in.read(buffer);
            if (read == -1) break;
            _out.write(buffer, 0, read);
        }
        _out.flush();
        _out.close();
        if (_closeInput) _in.close();
    }

    public byte[] getCopyBuffer() {
        byte[] copyBuffer;
        copyBuffer = new byte[1024 * 1024];
        return copyBuffer;
    }

    public void deleteRecursive(File _file) throws IOException {
        if (_file.exists()) {
            if (_file.isDirectory()) {
                File[] files = _file.listFiles();
                for (int i = 0, l = files.length; i < l; ++i) deleteRecursive(files[i]);
            }
            _file.delete();
        }
    }

    /**
	 * Delete all files recursively except the ones whose names start with
	 * provided one
	 * 
	 * @param file
	 * @param excludeName
	 *            file name to start with to exclude from deletion
	 * @throws IOException
	 */
    public void deleteRecursiveWithExclusion(File file, String excludeName) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0, l = files.length; i < l; ++i) deleteRecursiveWithExclusion(files[i], excludeName);
            }
            if (!file.getName().startsWith(excludeName)) {
                file.delete();
            }
        }
    }

    /**
	 * Delete all files recursively except the ones whose name match one of the
	 * regex patterns.
	 * 
	 * @param file
	 * @param excludePattern
	 *            file name to start with to exclude from deletion
	 * @throws IOException
	 */
    public void deleteRecursiveExcludePattern(File file, String[] excludePattern) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0, l = files.length; i < l; ++i) deleteRecursiveExcludePattern(files[i], excludePattern);
            }
            boolean delete = true;
            for (int x = 0; x < excludePattern.length; x++) {
                if (Pattern.matches(excludePattern[x], file.getName())) {
                    delete = false;
                    break;
                }
            }
            if (delete) file.delete();
        }
    }

    public void deleteFiles(File rootFolder, String startsWith) {
        File[] files = rootFolder.listFiles();
        if (files != null) {
            for (int i = 0, l = files.length; i < l; ++i) {
                if (files[i].getName().startsWith(startsWith)) {
                    files[i].delete();
                }
            }
        }
    }

    public void jarDirectory(File dir, String jarFileName) throws IOException {
        JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFileName));
        zipDir(dir, "", jos);
        jos.close();
    }

    private void zipDir(File dir, String jarEntry, JarOutputStream jos) throws IOException {
        String[] dirList = dir.list();
        for (int i = 0; i < dirList.length; i++) {
            File f = new File(dir, dirList[i]);
            if (f.isDirectory()) {
                zipDir(f, jarEntry + dirList[i] + File.separatorChar, jos);
                continue;
            }
            FileInputStream fis = new FileInputStream(f);
            ZipEntry anEntry = new ZipEntry(jarEntry + dirList[i]);
            jos.putNextEntry(anEntry);
            byte[] readBuffer = new byte[2156];
            int bytesIn = 0;
            while ((bytesIn = fis.read(readBuffer)) != -1) {
                jos.write(readBuffer, 0, bytesIn);
            }
            fis.close();
        }
    }

    public byte[] read(String fileName) {
        byte[] buff = new byte[0];
        try {
            FileInputStream fis = new FileInputStream(new File(fileName));
            byte[] buf = new byte[1024];
            int len;
            while ((len = fis.read(buf)) > 0) {
                byte[] buff1 = new byte[buff.length + len];
                System.arraycopy(buff, 0, buff1, 0, buff.length);
                System.arraycopy(buf, 0, buff1, buff.length, len);
                buff = buff1;
            }
            fis.close();
        } catch (Exception e) {
        }
        return buff;
    }

    public String[] read(File f) {
        ArrayList<String> al = new ArrayList<String>();
        String currline = "";
        try {
            FileInputStream fis = new FileInputStream(f);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            while ((currline != null)) {
                currline = reader.readLine();
                if (currline != null) al.add(currline);
            }
            reader.close();
            isr.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (String[]) al.toArray(new String[0]);
    }

    public void write(String fileName, byte[] bis) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(fileName));
            fos.write(bis);
            fos.close();
        } catch (Exception e) {
        }
    }

    public void copy(String srcFile, String tgtFile) {
        byte[] content = read(srcFile);
        write(tgtFile, content);
    }

    /**
	 * <P>
	 * This method reads a local text file into memory. An non existing file
	 * will cause the file to be generated, the return to an empty array.
	 * 
	 * @param fileName
	 *            the file to load
	 * @return the string array of the whole file.
	 */
    public String[] readLineByLine(String fileName) {
        ArrayList<String> al = new ArrayList<String>();
        String currline = "";
        try {
            if (!new File(fileName).exists()) new File(fileName).createNewFile();
            FileInputStream fis = new FileInputStream(new File(fileName));
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            while ((currline != null)) {
                currline = reader.readLine();
                if (currline != null) al.add(currline);
            }
            reader.close();
            isr.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (String[]) al.toArray(new String[0]);
    }

    /**
	 * <P>
	 * This method dowloads a text file from an internet web site.
	 * 
	 * @param netAddress
	 *            the web address
	 * @param arguments
	 *            the posted parameters for the web address
	 * 
	 * @return the string array of the whole file.
	 * @throws IOException
	 * @exception IOException
	 *                will be thrown if the IO error happens. Normally a missing
	 *                file is the case.
	 */
    public String[] downloadLineByLine(String netAddress, String arguments) throws IOException {
        ArrayList<String> ret = new ArrayList<String>();
        URL u = new URL(netAddress);
        URLConnection uc = u.openConnection();
        uc.setDoOutput(true);
        PrintWriter pw = new PrintWriter(uc.getOutputStream());
        pw.println(arguments);
        pw.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        String res = in.readLine();
        while (res != null) {
            ret.add(res);
            res = in.readLine();
        }
        in.close();
        return (String[]) ret.toArray(new String[0]);
    }

    public static void writeLineByLine(final String fileName, final String[] writeList) {
        try {
            final PrintWriter pw = new PrintWriter(new FileWriter(fileName));
            for (final String element : writeList) {
                pw.println(element);
            }
            pw.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static void appendLineByLine(final String fileName, final String line) {
        try {
            final PrintWriter pw = new PrintWriter(new FileOutputStream(fileName, true));
            pw.println(line);
            pw.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static String readFile(String fileName) {
        StringBuffer sb = new StringBuffer();
        try {
            FileInputStream fis = new FileInputStream(new File(fileName));
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            String currline = "";
            while ((currline != null)) {
                currline = reader.readLine();
                if (currline != null) {
                    sb.append(currline);
                }
            }
            reader.close();
            isr.close();
            fis.close();
        } catch (Exception e) {
        }
        return sb.toString();
    }

    public static String[] readFileLineByLine(String fileName) {
        List<String> sl = new ArrayList<String>();
        try {
            if (!new File(fileName).exists()) {
                new File(fileName).createNewFile();
            }
            FileInputStream fis = new FileInputStream(new File(fileName));
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            String currline = "";
            while ((currline != null)) {
                currline = reader.readLine();
                if (currline != null) {
                    sl.add(currline);
                }
            }
            reader.close();
            isr.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] strs = sl.toArray(new String[0]);
        return strs;
    }

    public static void save(String path, String fileName, String text) {
        try {
            File d = new File(path);
            d.mkdirs();
            File f = new File(path + File.separator + fileName);
            FileWriter writer = new FileWriter(f);
            writer.append(text);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
