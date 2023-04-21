package com.boxysystems.libraryfinder.view.intellij;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Siddique Hameed
 * Date: Mar 31, 2006
 * Time: 9:20:06 AM
 */
public class LibraryFinderHistory implements ApplicationComponent, JDOMExternalizable {

    private Map<String, Long> resourceMap = new TreeMap<String, Long>();

    private Map<String, Long> dirMap = new TreeMap<String, Long>();

    public static final String HISTORY_TAG = "LibFinderHistory";

    public static final String RESOURCES_TAG = "Resources";

    public static final String DIRS_TAG = "Paths";

    public static final String ELEMENT_TAG = "Element";

    public static final String VALUE_ATTRIB = "value";

    public static final String TIMESTAMP_ATTRIB = "timestamp";

    public static synchronized LibraryFinderHistory getInstance() {
        return ApplicationManager.getApplication().getComponent(LibraryFinderHistory.class);
    }

    @NotNull
    public String getComponentName() {
        return HISTORY_TAG;
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }

    public void readExternal(Element element) throws InvalidDataException {
        Element historyElement = element.getChild(HISTORY_TAG);
        if (historyElement != null) {
            Element filesElement = historyElement.getChild(RESOURCES_TAG);
            if (filesElement != null) {
                resourceMap = readXML(filesElement);
            }
            Element pathsElement = historyElement.getChild(DIRS_TAG);
            if (pathsElement != null) {
                dirMap = readXML(pathsElement);
            }
        }
    }

    private Map<String, Long> readXML(Element element) {
        Map<String, Long> map = new TreeMap<String, Long>();
        if (element != null) {
            final List<? extends Object> entries = element.getChildren(ELEMENT_TAG);
            Element entry;
            for (Object entry1 : entries) {
                entry = (Element) entry1;
                map.put(entry.getAttributeValue(VALUE_ATTRIB), getTimestamp(entry));
            }
        }
        return map;
    }

    private long getTimestamp(Element entry) {
        if (entry.getAttributeValue(TIMESTAMP_ATTRIB) != null) {
            return new Long(entry.getAttributeValue(TIMESTAMP_ATTRIB));
        }
        return (long) -1;
    }

    public void writeExternal(Element element) throws WriteExternalException {
        if (resourceMap.size() > 0 || dirMap.size() > 0) {
            Element historyElement = new Element(HISTORY_TAG);
            if (resourceMap.size() > 0) {
                Element filesElement = new Element(RESOURCES_TAG);
                writeToXML(filesElement, resourceMap);
                historyElement.addContent(filesElement);
            }
            if (dirMap.size() > 0) {
                Element pathsElement = new Element(DIRS_TAG);
                writeToXML(pathsElement, dirMap);
                historyElement.addContent(pathsElement);
            }
            element.addContent(historyElement);
        }
    }

    private void writeToXML(Element childElement, Map<String, Long> elements) {
        for (Object o : elements.keySet()) {
            String key = (String) o;
            final Element entryElement = new Element(ELEMENT_TAG);
            entryElement.setAttribute(VALUE_ATTRIB, key);
            entryElement.setAttribute(TIMESTAMP_ATTRIB, String.valueOf(elements.get(key)));
            childElement.addContent(entryElement);
        }
    }

    public void addDir(String dir) {
        if (dir != null) {
            dirMap.put(dir, System.currentTimeMillis());
        }
    }

    public List<String> getDirs() {
        ArrayList<String> dirList = new ArrayList<String>(dirMap.keySet());
        Collections.sort(dirList, new HistoryComparator(dirMap));
        return dirList;
    }

    public void addResource(String resource) {
        if (resource != null) {
            resourceMap.put(resource, System.currentTimeMillis());
        }
    }

    public List<String> getResources() {
        ArrayList<String> resourceList = new ArrayList<String>(resourceMap.keySet());
        Collections.sort(resourceList, new HistoryComparator(resourceMap));
        return resourceList;
    }

    public void clearDirMap() {
        dirMap.clear();
    }

    public void clearResourceMap() {
        resourceMap.clear();
    }

    private class HistoryComparator implements Comparator {

        private Map<String, Long> historyMap;

        public HistoryComparator(Map<String, Long> historyMap) {
            this.historyMap = historyMap;
        }

        public int compare(Object thisObj, Object thatObj) {
            if (thisObj instanceof String && thatObj instanceof String) {
                Long thisTimeStamp = historyMap.get(thisObj);
                Long thatTimeStamp = historyMap.get(thatObj);
                return (int) (thatTimeStamp - thisTimeStamp);
            }
            return -1;
        }
    }
}
