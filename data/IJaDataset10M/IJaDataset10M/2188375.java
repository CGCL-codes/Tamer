package org.jdmp.sigmen.resources;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public abstract class Resources {

    protected String PACKAGE = "org/jdmp/sigmen/resources/";

    public URL getResource(String name) {
        return ClassLoader.getSystemResource(PACKAGE + name);
    }

    protected void setPackage(String pack) {
        if (!pack.contains("/")) {
            pack.replace(".", "/");
        }
        if (!pack.endsWith("/")) {
            pack += "/";
        }
        PACKAGE = pack;
    }

    public String[] list() {
        try {
            String path = ClassLoader.getSystemResource(PACKAGE).getPath();
            path = path.substring(5, path.lastIndexOf("!/"));
            Enumeration<? extends ZipEntry> zeEnum = (new ZipFile(path)).entries();
            ZipEntry ze;
            String name;
            ArrayList<String> list = new ArrayList<String>();
            int l = PACKAGE.length();
            while (zeEnum.hasMoreElements()) {
                ze = zeEnum.nextElement();
                name = ze.getName();
                if (name.startsWith(PACKAGE) && !ze.isDirectory() && name.indexOf('/', l) == -1) {
                    list.add(name.substring(PACKAGE.length()));
                }
            }
            return list.toArray(new String[0]);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
            return new String[0];
        }
    }
}
