package com.ideo.sweetdevria.page;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Registry storing every user session page
 * 
 * @author Julien Maupoux
 *
 */
public class PageRegistry implements Serializable {

    private static final long serialVersionUID = -9187977641512313973L;

    private Map pages = Collections.synchronizedMap(new HashMap());

    private Set sessionsId = new HashSet();

    protected PageRegistry() {
    }

    public Page getPage(String pageId) {
        if (!pages.containsKey(pageId)) throw new IllegalArgumentException("The page required do not exist in the resitry for this session.");
        return (Page) pages.get(pageId);
    }

    public boolean contains(String pageId) {
        return pages.containsKey(pageId);
    }

    public void registerPage(Page page) {
        pages.put(page.getId(), page);
    }

    public void registerSession(String sessionId) {
        sessionsId.add(sessionId);
    }

    public boolean unregisterSession(String sessionId) {
        sessionsId.remove(sessionId);
        if (sessionsId.size() == 0) return true;
        return false;
    }

    public void unregisterPage(Page page) {
        pages.remove(page.getId());
    }

    public void unregisterPage(String pageId) {
        pages.remove(pageId);
    }

    public void clearPages() {
        pages.clear();
    }

    public String toString() {
        String text = "";
        text += " -PageRegistry contains :\n";
        Set keys = pages.keySet();
        Iterator k = keys.iterator();
        while (k.hasNext()) {
            String key = (String) k.next();
            Page p = (Page) pages.get(key);
            text += p.toString();
        }
        k = sessionsId.iterator();
        text += " -PageRegistry is linked to sessions :\n";
        while (k.hasNext()) {
            Object p = (String) k.next();
            text += "\n  - session id :" + p;
        }
        return text;
    }
}
