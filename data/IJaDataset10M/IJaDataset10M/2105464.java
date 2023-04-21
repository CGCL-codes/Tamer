package org.rapla.framework;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/** Helper Class for automated creation of the rapla-plugin.list in the
 * META-INF directory. Can be used in the build environment.
 */
public class ServiceListCreator {

    public static void main(String[] args) {
        try {
            String sourceDir = args[0];
            String destDir = (args.length > 1) ? args[1] : sourceDir;
            processDir(sourceDir, destDir);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void processDir(String srcDir, String destFile) throws ClassNotFoundException, IOException {
        File topDir = new File(srcDir);
        List<String> list = findPluginClasses(topDir);
        Writer writer = new BufferedWriter(new FileWriter(destFile));
        try {
            for (String className : list) {
                System.out.println("Found PluginDescriptor for " + className);
                writer.write(className);
                writer.write("\n");
            }
        } finally {
            writer.close();
        }
    }

    public static List<String> findPluginClasses(File topDir) throws ClassNotFoundException {
        List<String> list = new ArrayList<String>();
        Stack stack = new Stack();
        stack.push(topDir);
        while (!stack.empty()) {
            File file = (File) stack.pop();
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) stack.push(files[i]);
            } else {
                if (file.getName().endsWith("Plugin.class")) {
                    String absolut = file.getAbsolutePath();
                    String relativePath = absolut.substring(topDir.getAbsolutePath().length());
                    String pathName = relativePath.substring(1, relativePath.length() - ".class".length());
                    String className = pathName.replace(File.separatorChar, '.');
                    Class pluginClass = Class.forName(className);
                    if (!pluginClass.isInterface()) {
                        if (PluginDescriptor.class.isAssignableFrom(pluginClass)) {
                            list.add(className);
                        } else {
                            System.out.println("No PluginDescritor found for Class " + className);
                        }
                    }
                }
            }
        }
        return list;
    }
}
