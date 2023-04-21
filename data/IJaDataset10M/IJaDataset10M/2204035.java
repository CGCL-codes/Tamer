package com.googlecode.mycontainer.cpscanner;

import java.net.URL;

public abstract class ClassScannerListener implements ScannerListener {

    public void resourceFound(URL base, URL resource) {
        try {
            if (isClass(resource)) {
                String str = resource.toString();
                String baseStr = base.toString();
                StringBuilder builder = new StringBuilder(str);
                int idx = builder.indexOf("!");
                if (idx < 0) {
                    idx = baseStr.length();
                } else {
                    idx += 2;
                }
                builder.delete(0, idx);
                builder.delete(builder.length() - 6, builder.length());
                str = builder.toString().replaceAll("/", ".");
                Class<?> clazz = Class.forName(str);
                classFound(base, clazz);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isClass(URL url) {
        return url.toString().endsWith(".class");
    }

    public abstract void classFound(URL base, Class<?> clazz);
}
