package com.googlecode.webduff.io;

import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.commons.io.FilenameUtils;

public class URI {

    public static final String DEFAULT_SEPARATOR = "/";

    private Vector<String> uriComponents;

    public URI() {
        uriComponents = new Vector<String>();
    }

    public URI(String uri) {
        this(uri, DEFAULT_SEPARATOR);
    }

    public URI(String uri, String delimiter) {
        this();
        String prefixLessUri = uri.substring(FilenameUtils.getPrefixLength(uri));
        StringTokenizer aTokenizer = new StringTokenizer(prefixLessUri, delimiter);
        while (aTokenizer.hasMoreTokens()) {
            uriComponents.add(aTokenizer.nextToken());
        }
    }

    public URI(URI anotherURI) {
        this();
        uriComponents.addAll(anotherURI.uriComponents);
    }

    public String getPath() {
        return getPath(DEFAULT_SEPARATOR);
    }

    public String getPath(String delimiter) {
        if (uriComponents.isEmpty()) {
            return delimiter;
        }
        StringBuffer aBuffer = new StringBuffer();
        Iterator<String> iterator = uriComponents.iterator();
        while (iterator.hasNext()) {
            aBuffer.append(delimiter + iterator.next());
        }
        return aBuffer.toString();
    }

    public String getFolderPath() {
        return getFolderPath(DEFAULT_SEPARATOR);
    }

    public String getFolderPath(String delimiter) {
        return (getPath(delimiter) + delimiter);
    }

    public String getNthComponent(int n) {
        try {
            return uriComponents.elementAt(n);
        } catch (ArrayIndexOutOfBoundsException e) {
            return "";
        }
    }

    public URI strip(int n) {
        URI anURI = new URI(this);
        if (anURI.uriComponents.elementAt(n) != null) {
            anURI.uriComponents.remove(n);
        }
        return anURI;
    }

    public URI stripFirst() {
        return strip(0);
    }

    public URI stripLast() {
        return strip(size() - 1);
    }

    public int size() {
        return uriComponents.size();
    }

    public URI add(int n, String component) {
        URI anURI = new URI(this);
        anURI.uriComponents.insertElementAt(component, n);
        return anURI;
    }

    public String toString() {
        return getPath();
    }

    public URI append(String uri) {
        return add(size(), uri);
    }

    public URI prepend(String uri) {
        return add(0, uri);
    }

    public URI append(URI uri) {
        URI anURI = new URI(this);
        for (String aComponent : uri.uriComponents) {
            anURI.uriComponents.add(aComponent);
        }
        return anURI;
    }

    public String getFirstComponent() {
        return getNthComponent(0);
    }

    public String getLastComponent() {
        return getNthComponent(size() - 1);
    }

    public URI getParent() {
        return stripLast();
    }

    public URI subset(int start, int end) {
        URI newURI = new URI(this);
        newURI.uriComponents.clear();
        newURI.uriComponents.addAll(uriComponents.subList(start, end));
        return newURI;
    }

    public URI subsetFromBegin(int n) {
        return subset(n, size());
    }

    public URI subsetToEnd(int n) {
        return subset(0, n);
    }
}
