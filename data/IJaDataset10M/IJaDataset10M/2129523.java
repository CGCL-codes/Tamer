package org.rsbot.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class Extractor implements Runnable {

    private static void saveTo(InputStream in, String outPath) {
        try {
            OutputStream out = new FileOutputStream(new File(outPath));
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception ignored) {
        }
    }

    private final String[] args;

    public Extractor(String[] args) {
        this.args = args;
    }

    public void run() {
        ClassLoader loader = getClass().getClassLoader();
        String root = GlobalConfiguration.Paths.Resources.ROOT + "/";
        if (GlobalConfiguration.RUNNING_FROM_JAR) {
            try {
                if (GlobalConfiguration.getCurrentOperatingSystem() == GlobalConfiguration.OperatingSystem.WINDOWS) {
                    Extractor.saveTo(loader.getResourceAsStream(root + GlobalConfiguration.Paths.COMPILE_SCRIPTS_BAT), GlobalConfiguration.Paths.getHomeDirectory() + File.separator + GlobalConfiguration.Paths.COMPILE_SCRIPTS_BAT);
                    Extractor.saveTo(loader.getResourceAsStream(root + GlobalConfiguration.Paths.COMPILE_FIND_JDK), GlobalConfiguration.Paths.getHomeDirectory() + File.separator + GlobalConfiguration.Paths.COMPILE_FIND_JDK);
                } else {
                    Extractor.saveTo(loader.getResourceAsStream(root + GlobalConfiguration.Paths.COMPILE_SCRIPTS_SH), GlobalConfiguration.Paths.getHomeDirectory() + File.separator + GlobalConfiguration.Paths.COMPILE_SCRIPTS_SH);
                }
                URL version = GlobalConfiguration.class.getClassLoader().getResource(GlobalConfiguration.Paths.Resources.VERSION);
                String p = version.toString().replace("jar:file:", "").replace("!/" + GlobalConfiguration.Paths.Resources.VERSION, "");
                try {
                    p = URLDecoder.decode(p, "UTF-8");
                } catch (final UnsupportedEncodingException ignored) {
                }
                JarFile jar = new JarFile(new File(p));
                File out = new File(GlobalConfiguration.Paths.getScriptsExtractedCache());
                FileOutputStream fos = new FileOutputStream(out);
                JarOutputStream jos = new JarOutputStream(fos);
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry e = entries.nextElement();
                    if (e.getName().startsWith("scripts/")) {
                        InputStream in = loader.getResourceAsStream(e.getName());
                        jos.putNextEntry(new JarEntry(e.getName().substring(8)));
                        byte[] buffer = new byte[256];
                        while (true) {
                            int nRead = in.read(buffer, 0, buffer.length);
                            if (nRead < 0) {
                                break;
                            }
                            jos.write(buffer, 0, nRead);
                        }
                        in.close();
                    }
                }
                jos.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (args.length > 2) {
            if (args[0].toLowerCase().startsWith("delete")) {
                File jarOld = new File(args[1]);
                if (jarOld.exists()) {
                    jarOld.delete();
                }
            }
        }
    }

    public void clearDirectory(File path) {
        if (path.exists()) {
            for (File file : path.listFiles()) {
                if (file.isDirectory()) {
                    clearDirectory(file);
                }
                if (!file.delete()) {
                    System.err.println("Failed to delete file: " + file);
                }
            }
        }
    }
}
