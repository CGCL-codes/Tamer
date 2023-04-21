package com.mentorgen.tools.profile.instrument.clfilter;

public class JettyClassLoaderFilter implements ClassLoaderFilter {

    public boolean canFilter() {
        return true;
    }

    public boolean accept(ClassLoader loader) {
        return loader.getClass().getName().equals("org.mortbay.jetty.webapp.WebAppClassLoader");
    }
}
