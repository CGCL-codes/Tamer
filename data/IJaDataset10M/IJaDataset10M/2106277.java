package org.cumt.tools.importers;

import java.io.File;
import java.util.Set;
import org.junit.Test;

public class JavaClassPathImporterTestCase {

    @Test
    public void testLoadClassesFromJar() {
        JavaClassPathImporter importer = new JavaClassPathImporter();
        importer.setJarFileOrClassDirectory(new File(getClass().getResource("files.jar").getFile()));
        Set<Class<?>> classes = importer.loadClasses();
        for (Class<?> clazz : classes) {
            System.out.println(clazz.getName());
        }
    }

    @Test
    public void testLoadClassesFromDirectory() {
        JavaClassPathImporter importer = new JavaClassPathImporter();
        System.out.println(new File(".").getAbsolutePath());
        importer.setJarFileOrClassDirectory(new File("target", "classes"));
        Set<Class<?>> classes = importer.loadClasses();
        for (Class<?> clazz : classes) {
            System.out.println(clazz.getName());
        }
    }
}
