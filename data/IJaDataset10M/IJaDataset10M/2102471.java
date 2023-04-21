package de.tu_dortmund.cni.peper.tools;

import java.io.File;
import java.io.FileFilter;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ListPackages {

    private static PrintStream ps;

    private static void processFile(File jarFile) throws Exception {
        ps.println(jarFile.getName());
        for (int i = 0; i < jarFile.getName().length(); i++) {
            ps.print("=");
        }
        ps.println();
        ZipFile zf = new ZipFile(jarFile);
        Enumeration<? extends ZipEntry> zes = zf.entries();
        while (zes.hasMoreElements()) {
            ZipEntry ze = zes.nextElement();
            if (ze.isDirectory() && !ze.getName().startsWith("META-INF/")) {
                String name = ze.getName();
                if (name.endsWith("/")) name = name.substring(0, name.length() - 1);
                name = name.replaceAll("/", ".");
                ps.println(name);
            }
        }
        ps.println();
    }

    public static void main(String[] args) throws Exception {
        File f = new File("..\\Libs");
        File[] dirs = f.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                if (pathname.isDirectory()) return true;
                return false;
            }
        });
        for (File dir : dirs) {
            ps = new PrintStream(new File(dir, "_packages.txt"));
            File[] jars = dir.listFiles(new FileFilter() {

                public boolean accept(File pathname) {
                    if (pathname.getName().toLowerCase().endsWith(".jar")) return true;
                    return false;
                }
            });
            for (File file : jars) {
                processFile(file);
            }
            ps.close();
        }
    }
}
