package demo;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class DemoUtil {

    public static void escapeHtmlStream(Reader in, Writer out) throws IOException {
        int i;
        boolean lastWasBlankChar = false;
        while ((i = in.read()) != -1) {
            char c = (char) i;
            if (c == ' ') {
                if (lastWasBlankChar) {
                    lastWasBlankChar = false;
                    out.write("&nbsp;");
                } else {
                    lastWasBlankChar = true;
                    out.append(' ');
                }
            } else {
                lastWasBlankChar = false;
                if (c == '"') {
                    out.append("&quot;");
                } else if (c == '&') {
                    out.append("&amp;");
                } else if (c == '<') {
                    out.append("&lt;");
                } else if (c == '>') {
                    out.append("&gt;");
                } else if (c == '\n') {
                    out.append("<br>");
                } else {
                    int ci = 0xffff & c;
                    if (ci < 160) {
                        out.append(c);
                    } else {
                        out.append("&#");
                        out.append(new Integer(ci).toString());
                        out.append(';');
                    }
                }
            }
        }
    }

    /**
     * List directory contents for a resource folder. Not recursive.
     * This is basically a brute-force implementation.
     * Works for regular files and also JARs.
     * 
     * @author Greg Briggs
     * @param clazz Any java class that lives in the same place as the resources you want.
     * @param path Should end with "/", but not start with one.
     * @return Just the name of each member item, not the full paths.
     * @throws URISyntaxException 
     * @throws IOException
     * 
     * Credit where it's due: http://www.uofr.net/~greg/java/get-resource-listing.html
     */
    public static String[] getResourceListing(Class clazz, String path) throws URISyntaxException, IOException {
        URL dirURL = clazz.getClassLoader().getResource(path);
        if (dirURL != null && dirURL.getProtocol().equals("file")) {
            return new File(dirURL.toURI()).list();
        }
        if (dirURL == null) {
            String me = clazz.getName().replace(".", "/") + ".class";
            dirURL = clazz.getClassLoader().getResource(me);
        }
        if (dirURL.getProtocol().equals("jar")) {
            String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!"));
            JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
            try {
                Enumeration<JarEntry> entries = jar.entries();
                Set<String> result = new HashSet<String>();
                while (entries.hasMoreElements()) {
                    String name = entries.nextElement().getName();
                    if (name.startsWith(path)) {
                        String entry = name.substring(path.length());
                        int checkSubdir = entry.indexOf("/");
                        if (checkSubdir >= 0) {
                            entry = entry.substring(0, checkSubdir);
                        }
                        result.add(entry);
                    }
                }
                return result.toArray(new String[result.size()]);
            } finally {
                jar.close();
            }
        }
        throw new UnsupportedOperationException("Cannot list files for URL " + dirURL);
    }

    public static void getResources(String uri) throws Exception {
        Class c = DemoUtil.class;
    }
}
