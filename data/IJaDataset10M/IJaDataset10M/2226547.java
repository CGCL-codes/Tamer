package org.mati.geotech.app;

public class PathTest {

    public static void main(String[] args) {
        String str = System.getProperty("java.library.path") + System.getProperty("path.separator") + "./lib";
        System.setProperty("java.library.path", str);
        System.out.println("java.library.path=" + System.getProperty("java.library.path"));
    }
}
