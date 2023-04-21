package mfb2.tools.obclipse.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import mfb2.tools.obclipse.exceptions.ObclipseException;
import mfb2.tools.obclipse.util.Msg;

public class FileOperations {

    public static String readFileToSting(File file) throws ObclipseException {
        char[] cbuff = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            cbuff = new char[(int) file.length()];
            reader.read(cbuff);
        } catch (FileNotFoundException e) {
            Msg.error("The file ''{0}'' does not exist!", file.getAbsolutePath());
        } catch (IOException e) {
            Msg.ioException(file, e);
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                Msg.ioException(file, e);
            }
        }
        return new String(cbuff);
    }

    public static void writeStingToFile(File file, String content) throws ObclipseException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
        } catch (IOException e) {
            Msg.ioException(file, e);
        }
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                Msg.ioException(file, e);
            }
        }
    }

    public static void copyFile(File in, File out) throws ObclipseException {
        try {
            FileChannel inChannel = null;
            FileChannel outChannel = null;
            try {
                inChannel = new FileInputStream(in).getChannel();
                outChannel = new FileOutputStream(out).getChannel();
                inChannel.transferTo(0, inChannel.size(), outChannel);
            } finally {
                if (inChannel != null) {
                    inChannel.close();
                }
                if (outChannel != null) {
                    outChannel.close();
                }
            }
        } catch (FileNotFoundException e) {
            Msg.error("The file ''{0}'' to copy does not exist!", e, in.getAbsolutePath());
        } catch (IOException e) {
            Msg.ioException(in, out, e);
        }
    }

    public static void copyDir(File srcDir, File dstDir) throws ObclipseException {
        dstDir.mkdir();
        File[] children = srcDir.listFiles();
        for (int i = 0; i < children.length; i++) {
            if (children[i].isDirectory()) {
                File subDir = new File(dstDir, children[i].getName());
                subDir.mkdir();
                copyDir(children[i], subDir);
            }
            if (children[i].isFile()) {
                File subFile = new File(dstDir, children[i].getName());
                copyFile(children[i], subFile);
            }
        }
    }

    public static boolean deleteDir(File dir, boolean deleteRootDir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            for (int i = 0; i < children.length; i++) {
                if (!deleteDir(children[i])) {
                    return false;
                }
            }
        }
        if (deleteRootDir) {
            return dir.delete();
        }
        return true;
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            for (int i = 0; i < children.length; i++) {
                if (!deleteDir(children[i])) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static List<File> getAllFiles(File dir, boolean includeSubdirs, FileFilter filter) {
        List<File> files = new ArrayList<File>();
        Stack<File> stack = new Stack<File>();
        stack.push(dir);
        while (!stack.empty()) {
            File currentDir = stack.pop();
            if (currentDir.isDirectory()) {
                File[] listFiles = currentDir.listFiles(filter);
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        if (includeSubdirs) {
                            stack.push(file);
                        }
                    } else {
                        files.add(file);
                    }
                }
            }
        }
        return files;
    }
}
