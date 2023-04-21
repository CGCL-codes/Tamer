package org.apache.soap.util;

import java.io.*;
import java.util.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.beans.Introspector;

/**
 * Deals with strings (probably need to elaborate some more).
 *
 * @author Matthew J. Duftler
 */
public class StringUtils {

    public static final String lineSeparator = System.getProperty("line.separator", "\n");

    public static String URI_SEPARATION_CHAR = "@";

    public static String getClassName(Class targetClass) {
        String className = targetClass.getName();
        return targetClass.isArray() ? parseDescriptor(className) : className;
    }

    private static String parseDescriptor(String className) {
        char[] classNameChars = className.toCharArray();
        int arrayDim = 0;
        int i = 0;
        while (classNameChars[i] == '[') {
            arrayDim++;
            i++;
        }
        StringBuffer classNameBuf = new StringBuffer();
        switch(classNameChars[i++]) {
            case 'B':
                classNameBuf.append("byte");
                break;
            case 'C':
                classNameBuf.append("char");
                break;
            case 'D':
                classNameBuf.append("double");
                break;
            case 'F':
                classNameBuf.append("float");
                break;
            case 'I':
                classNameBuf.append("int");
                break;
            case 'J':
                classNameBuf.append("long");
                break;
            case 'S':
                classNameBuf.append("short");
                break;
            case 'Z':
                classNameBuf.append("boolean");
                break;
            case 'L':
                classNameBuf.append(classNameChars, i, classNameChars.length - i - 1);
                break;
        }
        for (i = 0; i < arrayDim; i++) classNameBuf.append("[]");
        return classNameBuf.toString();
    }

    private static URL getURL(URL contextURL, String spec, int recursiveDepth) throws MalformedURLException {
        URL url = null;
        try {
            url = new URL(contextURL, spec);
            try {
                url.openStream();
            } catch (IOException ioe1) {
                throw new MalformedURLException("This file was not found: " + url);
            }
        } catch (MalformedURLException e1) {
            url = new URL("file", "", spec);
            try {
                url.openStream();
            } catch (IOException ioe2) {
                if (contextURL != null) {
                    String contextFileName = contextURL.getFile();
                    String parentName = new File(contextFileName).getParent();
                    if (parentName != null && recursiveDepth < 3) {
                        return getURL(new URL("file", "", parentName + '/'), spec, recursiveDepth + 1);
                    }
                }
                throw new MalformedURLException("This file was not found: " + url);
            }
        }
        return url;
    }

    public static URL getURL(URL contextURL, String spec) throws MalformedURLException {
        return getURL(contextURL, spec, 1);
    }

    public static Reader getContentAsReader(URL url) throws SecurityException, IllegalArgumentException, IOException {
        if (url == null) {
            throw new IllegalArgumentException("URL cannot be null.");
        }
        try {
            Object content = url.getContent();
            if (content == null) {
                throw new IllegalArgumentException("No content.");
            }
            if (content instanceof InputStream) {
                Reader in = new InputStreamReader((InputStream) content);
                if (in.ready()) {
                    return in;
                } else {
                    throw new FileNotFoundException();
                }
            } else {
                throw new IllegalArgumentException((content instanceof String) ? (String) content : "This URL points to a: " + StringUtils.getClassName(content.getClass()));
            }
        } catch (SecurityException e) {
            throw new SecurityException("Your JVM's SecurityManager has disallowed this.");
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("This file was not found: " + url);
        }
    }

    public static String getContentAsString(URL url) throws SecurityException, IllegalArgumentException, IOException {
        return IOUtils.getStringFromReader(getContentAsReader(url));
    }

    /**
  *  This method will perform the splicing of a full URI. It is currently 
  *  the only place where the delimiting character in the URI that triggers the 
  *  splicing operation is specified. (This character should later be specified
  *  as a constant...
  *
  * Creation date: (10/23/00 2:54:33 PM)
  * @return java.lang.String
  * @param fullTargetObjectURI java.lang.String
  */
    public static String parseFullTargetObjectURI(String fullTargetObjectURI) {
        if (fullTargetObjectURI == null) return null;
        int delimIndex = fullTargetObjectURI.indexOf(URI_SEPARATION_CHAR);
        if ((fullTargetObjectURI != null) && (delimIndex != -1)) return fullTargetObjectURI.substring(0, delimIndex); else return fullTargetObjectURI;
    }
}
