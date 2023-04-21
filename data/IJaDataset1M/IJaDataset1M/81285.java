package com.ryanm.util.io;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * File utility
 * 
 * @author ryanm
 */
public class FileUtils {

    /**
	 * Finds the relative path from base to file. The file returned is
	 * such that file.getCanonicalPath().equals( new File( base,
	 * getRelativePath( base, file ).getPath() ).getCanonicalPath )
	 * 
	 * @param base
	 *           The base for the relative path
	 * @param file
	 *           The destination for the relative path
	 * @return The relative path betwixt base and file, or null if
	 *         there was a IOException
	 */
    public static File getRelativePath(File base, File file) {
        try {
            String absBase = base.getCanonicalPath();
            String absFile = file.getCanonicalPath();
            if (absBase.equals(absFile)) {
                return new File("");
            }
            int divergenceCharIndex = 0;
            int divergenceSeperatorIndex = 0;
            while (divergenceCharIndex < absBase.length() && divergenceCharIndex < absFile.length() && absBase.charAt(divergenceCharIndex) == absFile.charAt(divergenceCharIndex)) {
                if (absBase.charAt(divergenceCharIndex) == File.separatorChar || absFile.charAt(divergenceCharIndex) == File.separatorChar) {
                    divergenceSeperatorIndex = divergenceCharIndex;
                }
                divergenceCharIndex++;
            }
            if (divergenceCharIndex == absBase.length()) {
                return new File(absFile.substring(divergenceCharIndex + 1));
            } else {
                StringBuilder path = new StringBuilder();
                for (int i = divergenceSeperatorIndex; i < absBase.length(); i++) {
                    if (absBase.charAt(i) == File.separatorChar) {
                        path.append("..");
                        path.append(File.separatorChar);
                    }
                }
                path.append(absFile.substring(divergenceSeperatorIndex));
                return new File(path.toString());
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }

    /**
	 * Tests FileUtils methods
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        List<File> files = new LinkedList<File>();
        File base = new File(System.getProperty("user.dir"));
        files.add(base);
        addAllChildren(base, files);
        Random rand = new Random(632213912l);
        for (int i = 0; i < 400; i++) {
            System.err.println("\t\t" + i);
            File b = files.get(rand.nextInt(files.size()));
            File f = files.get(rand.nextInt(files.size()));
            File relPath = getRelativePath(b, f);
            assert !relPath.isAbsolute();
            System.err.println("Base = " + b.getPath());
            System.err.println("File = " + f.getPath());
            System.err.println("Relp = " + relPath.getPath());
            try {
                String fcan = f.getCanonicalPath();
                File rf = new File(b, relPath.getPath());
                String rcan = rf.getCanonicalPath();
                System.err.println("File abs = " + fcan);
                System.err.println("rela abs = " + rcan);
                assert fcan.equals(rcan);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.err.println();
        }
    }

    private static void addAllChildren(File base, List<File> files) {
        File[] children = base.listFiles();
        for (File f : children) {
            files.add(f);
        }
        for (File f : children) {
            if (f.isDirectory()) {
                addAllChildren(f, files);
            }
        }
    }
}
