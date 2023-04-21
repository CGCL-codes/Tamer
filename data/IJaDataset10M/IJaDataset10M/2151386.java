package org.jacce;

import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Config {

    private static final int SECOND = 1000;

    private static final int MINUTE = SECOND * 60;

    private static final int HOUR = MINUTE * 60;

    private static final int KB = 1024;

    private static final int MB = KB * 1024;

    public static void configure(InputStream in) throws CacheException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(in);
            doc.getDocumentElement().normalize();
            NodeList cacheList = doc.getElementsByTagName("cache");
            for (int s = 0; s < cacheList.getLength(); s++) {
                Node node = cacheList.item(s);
                if (node instanceof Element) {
                    String id = ((Element) node).getAttribute("id");
                    String desc = ((Element) node).getAttribute("desc");
                    String cacheClassName = ((Element) node).getAttribute("cacheClassName");
                    int capacity = asInt(((Element) node).getAttribute("capacity"));
                    int memorySize = asMemorySize(((Element) node).getAttribute("memory-size"));
                    if (id == null || id.equals("")) {
                        throw new CacheException("id attribute is required");
                    }
                    if (cacheClassName == null || cacheClassName.equals("")) {
                        throw new CacheException("cacheClassName attribute is required");
                    }
                    if (capacity < -1) throw new CacheException("Invalid capacity specified for cache: " + id);
                    if (capacity == -1) capacity = CacheFactory.DEF_CAPACITY;
                    boolean cleanSettings = false;
                    int cleanInterval = 0;
                    int ttl = 0;
                    int idlePeriod = 0;
                    NodeList cacheChildren = node.getChildNodes();
                    for (int n = 0; n < cacheChildren.getLength(); n++) {
                        Node cleanup = cacheChildren.item(n);
                        if (cleanup instanceof Element) {
                            cleanInterval = asTime(((Element) cleanup).getAttribute("clean-interval"));
                            ttl = asTime(((Element) cleanup).getAttribute("ttl"));
                            idlePeriod = asTime(((Element) cleanup).getAttribute("idle-period"));
                            cleanSettings = true;
                        }
                    }
                    if (cleanSettings) CacheFactory.getInstance().addCacheTimed(id, desc, cacheClassName, capacity, memorySize, cleanInterval, ttl, idlePeriod); else CacheFactory.getInstance().addCache(id, desc, cacheClassName, capacity, memorySize);
                }
            }
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    private static int asInt(String value) {
        return (value == null || value.trim().length() == 0) ? -1 : Integer.parseInt(value);
    }

    private static int asMemorySize(String value) {
        if (value == null || value.trim().length() == 0) {
            return 0;
        }
        value = value.trim();
        int length = value.length();
        String modifier = value.substring(length - 1, length);
        if (modifier.equalsIgnoreCase("k")) {
            return Integer.parseInt(value.substring(0, length - 1)) * KB;
        } else if (modifier.equalsIgnoreCase("m")) {
            return Integer.parseInt(value.substring(0, length - 1)) * MB;
        } else {
            return Integer.parseInt(value);
        }
    }

    private static int asTime(String value) {
        if (value == null || value.trim().length() == 0) {
            return 0;
        }
        value = value.trim();
        int length = value.length();
        String modifier = value.substring(length - 1, length);
        if (modifier.equalsIgnoreCase("s")) {
            return Integer.parseInt(value.substring(0, length - 1)) * SECOND;
        } else if (modifier.equalsIgnoreCase("m")) {
            return Integer.parseInt(value.substring(0, length - 1)) * MINUTE;
        }
        if (modifier.equalsIgnoreCase("h")) {
            return Integer.parseInt(value.substring(0, length - 1)) * HOUR;
        } else {
            return Integer.parseInt(value);
        }
    }
}
