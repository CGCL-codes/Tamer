package com.classworld;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

public class JarJarURLConnection extends URLConnection {

    private URLConnection connection;

    public static final char SEPARATOR_CHAR = '^';

    public static final String SEPARATOR = SEPARATOR_CHAR + "/";

    public JarJarURLConnection(URL url) throws IOException {
        super(url);
        final String file = url.getFile();
        URL inner = new URL(file);
        connection = inner.openConnection();
    }

    public void connect() throws IOException {
        if (!connected) {
            connection.connect();
            connected = true;
        }
    }

    public InputStream getInputStream() throws IOException {
        connect();
        return connection.getInputStream();
    }

    public static void register() {
        URL.setURLStreamHandlerFactory(new JarJarURLStreamHandlerFactory());
    }

    public static class JarJarURLStreamHandlerFactory implements URLStreamHandlerFactory {

        public URLStreamHandler createURLStreamHandler(String protocol) {
            if (protocol.equals("jarjar")) {
                return new JarJarURLStreamHandler();
            }
            return null;
        }
    }

    public static class JarJarURLStreamHandler extends URLStreamHandler {

        protected URLConnection openConnection(URL u) throws IOException {
            return new JarJarURLConnection(u);
        }

        private int indexOfBangSlash(String spec) {
            int indexOfBang = spec.length();
            while ((indexOfBang = spec.lastIndexOf(SEPARATOR_CHAR, indexOfBang)) != -1) {
                if ((indexOfBang != (spec.length() - 1)) && (spec.charAt(indexOfBang + 1) == '/')) {
                    return indexOfBang + 1;
                } else {
                    indexOfBang--;
                }
            }
            return -1;
        }

        @SuppressWarnings({ "deprecation" })
        protected void parseURL(URL url, String spec, int start, int limit) {
            String file = null;
            String ref = null;
            int refPos = spec.indexOf('#', limit);
            boolean refOnly = refPos == start;
            if (refPos > -1) {
                ref = spec.substring(refPos + 1, spec.length());
                if (refOnly) {
                    file = url.getFile();
                }
            }
            boolean absoluteSpec = false;
            if (spec.length() >= 7) {
                absoluteSpec = spec.substring(0, 7).equalsIgnoreCase("jarjar:");
            }
            spec = spec.substring(start, limit);
            if (absoluteSpec) {
                file = parseAbsoluteSpec(spec);
            } else if (!refOnly) {
                file = parseContextSpec(url, spec);
                int bangSlash = indexOfBangSlash(file);
                String toBangSlash = file.substring(0, bangSlash);
                String afterBangSlash = file.substring(bangSlash);
                file = toBangSlash + afterBangSlash;
            }
            file = file != null ? "jar:" + file.replaceFirst("\\" + SEPARATOR, "!/") : null;
            setURL(url, "jarjar", "", -1, file, ref);
        }

        @SuppressWarnings({ "UnusedAssignment", "UnusedDeclaration" })
        private String parseAbsoluteSpec(String spec) {
            @SuppressWarnings("unused") URL url = null;
            int index = -1;
            if ((index = indexOfBangSlash(spec)) == -1) {
                throw new NullPointerException("no " + SEPARATOR + " in spec");
            }
            try {
                String innerSpec = spec.substring(0, index - 1);
                url = new URL(innerSpec);
            } catch (MalformedURLException e) {
                throw new NullPointerException("invalid url: " + spec + " (" + e + ")");
            }
            return spec;
        }

        private String parseContextSpec(URL url, String spec) {
            String ctxFile = url.getFile();
            if (spec.startsWith("/")) {
                int bangSlash = indexOfBangSlash(ctxFile);
                if (bangSlash == -1) {
                    throw new NullPointerException("malformed " + "context url:" + url + ": no " + SEPARATOR);
                }
                ctxFile = ctxFile.substring(0, bangSlash);
            }
            if (!ctxFile.endsWith("/") && (!spec.startsWith("/"))) {
                int lastSlash = ctxFile.lastIndexOf('/');
                if (lastSlash == -1) {
                    throw new NullPointerException("malformed " + "context url:" + url);
                }
                ctxFile = ctxFile.substring(0, lastSlash + 1);
            }
            return (ctxFile + spec);
        }
    }
}
