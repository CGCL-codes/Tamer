package net.sourceforge.seqware.pipeline.util.filetools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import net.sourceforge.seqware.pipeline.module.ReturnValue;

public class FileTools {

    public static String md5sumFile(String filename) {
        DigestInputStream dis = null;
        try {
            dis = new DigestInputStream(new FileInputStream(filename), MessageDigest.getInstance("MD5"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        try {
            while (dis.read() > 0) {
                continue;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return byte2HexString(dis.getMessageDigest().digest());
    }

    public static String byte2HexString(byte[] b) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            result.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    public static long splitFile(File input, ArrayList<File> files, int linesPerStripe) {
        long linesProcessed = 0;
        int numFiles = files.size();
        BufferedWriter currentFile = null;
        BufferedReader in = null;
        ArrayList<BufferedWriter> fileOutputs = new ArrayList<BufferedWriter>();
        try {
            in = new BufferedReader(new FileReader(input));
        } catch (FileNotFoundException e1) {
            return -3;
        }
        for (int i = 0; i < numFiles; i++) {
            try {
                fileOutputs.add(new BufferedWriter(new FileWriter(files.get(i))));
            } catch (FileNotFoundException e) {
                return -1;
            } catch (IOException e) {
                return -2;
            }
        }
        if (numFiles <= 0) return -4;
        try {
            String line = null;
            while ((line = in.readLine()) != null) {
                if (linesProcessed % linesPerStripe == 0) {
                    currentFile = fileOutputs.get((int) (linesProcessed / linesPerStripe) % numFiles);
                }
                currentFile.write(line + System.getProperty("line.separator"));
                linesProcessed++;
            }
        } catch (IOException e) {
            return -3;
        }
        try {
            for (int i = 0; i < numFiles; i++) {
                fileOutputs.get(i).close();
            }
        } catch (IOException e) {
            return -3;
        }
        return linesProcessed;
    }

    public static ReturnValue verifyFile(File file) {
        String error = null;
        if (!file.exists()) {
            error = "File does nost exists";
        } else if (!file.canRead()) {
            error = "Cannot read file";
        } else if (file.length() == 0) {
            error = "File is zero length";
        } else {
            return new ReturnValue(null, null, 0);
        }
        return new ReturnValue(null, file.getAbsolutePath() + " " + error, 1);
    }

    public static ReturnValue dirPathExistsAndWritable(File file) {
        ReturnValue ret = new ReturnValue();
        ret.setExitStatus(ReturnValue.SUCCESS);
        if (!file.isDirectory() || !file.canRead() || !file.canWrite() || !file.exists()) {
            ret.setExitStatus(ReturnValue.DIRECTORYNOTWRITABLE);
        }
        return (ret);
    }

    public static ReturnValue dirPathExistsAndReadable(File file) {
        ReturnValue ret = new ReturnValue();
        ret.setExitStatus(ReturnValue.SUCCESS);
        if (!file.isDirectory() || !file.canRead() || !file.exists()) {
            ret.setExitStatus(ReturnValue.DIRECTORYNOTREADABLE);
        }
        return (ret);
    }

    public static ReturnValue fileExistsAndReadable(File file) {
        ReturnValue ret = new ReturnValue();
        ret.setExitStatus(ReturnValue.SUCCESS);
        if (!file.exists()) {
            ret.setStderr("File does not exist");
            ret.setExitStatus(ReturnValue.FILENOTREADABLE);
        } else if (!file.isFile()) {
            ret.setStderr("Is not a file");
            ret.setExitStatus(ReturnValue.FILENOTREADABLE);
        } else if (!file.canRead()) {
            ret.setStderr("Is not readable");
            ret.setExitStatus(ReturnValue.FILENOTREADABLE);
        }
        return (ret);
    }

    public static ReturnValue fileExistsAndExecutable(File file) {
        ReturnValue ret = new ReturnValue();
        ret.setExitStatus(ReturnValue.SUCCESS);
        if (!file.isFile() || !file.canRead() || !file.exists() || !file.canExecute()) {
            ret.setExitStatus(ReturnValue.FILENOTEXECUTABLE);
        }
        return (ret);
    }

    public static ReturnValue fileExistsAndNotEmpty(File file) {
        ReturnValue ret = new ReturnValue();
        ret.setExitStatus(ReturnValue.SUCCESS);
        if (!file.isFile() || !file.canRead() || !file.exists() || file.length() == 0) {
            ret.setExitStatus(ReturnValue.FILENOTREADABLE);
        }
        return (ret);
    }

    public static File createTempDirectory(File parentDir) throws IOException {
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        File tempDir = File.createTempFile("temp", Long.toString(System.nanoTime()), parentDir);
        if (!(tempDir.delete())) {
            throw new IOException("Could not delete temp file: " + tempDir.getAbsolutePath());
        }
        if (!(tempDir.mkdirs())) {
            throw new IOException("Could not create temp directory: " + tempDir.getAbsolutePath());
        }
        return (tempDir);
    }

    public static boolean deleteDirectoryRecursive(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectoryRecursive(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    public static boolean zipDirectoryRecursive(File path, File zipFileName, String excludeRegEx, boolean relative, boolean compress) {
        ArrayList<File> filesToZip = new ArrayList<File>();
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    FileTools.listFilesRecursive(files[i], filesToZip);
                } else {
                    filesToZip.add(files[i]);
                }
            }
        }
        try {
            byte[] buffer = new byte[18024];
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
            if (!compress) {
                out.setLevel(Deflater.NO_COMPRESSION);
            } else {
                out.setLevel(Deflater.DEFAULT_COMPRESSION);
            }
            for (int i = 0; i < filesToZip.size(); i++) {
                if (excludeRegEx == null || !filesToZip.get(i).getName().contains(excludeRegEx)) {
                    FileInputStream in = new FileInputStream(filesToZip.get(i));
                    String filePath = filesToZip.get(i).getPath();
                    if (relative) {
                        filePath = filePath.replaceFirst(path.getAbsolutePath() + File.separator, "");
                    }
                    System.out.println("Deflating: " + filePath);
                    out.putNextEntry(new ZipEntry(filePath));
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                    out.closeEntry();
                    in.close();
                }
            }
            out.close();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            return (false);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            return (false);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return (false);
        }
        return (true);
    }

    public static boolean unzipFile(File path, File outputDir) {
        int BUFFER = 2048;
        try {
            BufferedOutputStream dest = null;
            FileInputStream fis = new FileInputStream(path);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    File dir = new File(outputDir.getAbsolutePath() + File.separator + entry.getName());
                    dir.mkdirs();
                } else {
                    System.out.println("Extracting: " + entry);
                    int count;
                    byte data[] = new byte[BUFFER];
                    File dir = new File(outputDir.getAbsolutePath() + File.separator + entry.getName());
                    if (entry.getName().contains(File.separator)) {
                        String[] t = entry.getName().split(File.separator);
                        StringBuffer newDir = new StringBuffer();
                        for (int i = 0; i < t.length - 1; i++) {
                            newDir.append(t[i] + File.separator);
                        }
                        System.out.println("Creating Dir: " + outputDir.getAbsolutePath() + File.separator + newDir);
                        File newDirFile = new File(outputDir.getAbsolutePath() + File.separator + newDir);
                        newDirFile.mkdirs();
                    }
                    FileOutputStream fos = new FileOutputStream(dir.getAbsolutePath());
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();
                    File finalFile = new File(outputDir.getAbsolutePath() + File.separator + entry.getName());
                    finalFile.setExecutable(true);
                    finalFile.setWritable(true);
                    finalFile.setReadable(true);
                }
            }
            zis.close();
        } catch (IOException ioe) {
            System.err.println("Unhandled exception:");
            ioe.printStackTrace();
            return (false);
        }
        return (true);
    }

    private static void listFilesRecursive(File path, ArrayList<File> filesArray) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    FileTools.listFilesRecursive(files[i], filesArray);
                } else {
                    filesArray.add(files[i]);
                }
            }
        }
    }

    public static final void copyInputStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) >= 0) out.write(buffer, 0, len);
        in.close();
        out.close();
    }
}
