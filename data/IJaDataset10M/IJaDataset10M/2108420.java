package org.cubictest.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class URLList {

    private List<URLMapper> urls;

    public URLList() {
        urls = new ArrayList<URLMapper>();
    }

    public void addURL(URLMapper mapper) {
        urls.add(mapper);
    }

    public void removeURL(URLMapper mapper) {
        urls.remove(mapper);
    }

    public Object[] getAllUrls() {
        return urls.toArray();
    }

    public synchronized URLMapper createURL() {
        URLMapper mapper = new URLMapper();
        mapper.setId("sp" + System.currentTimeMillis());
        mapper.setName("name - change");
        mapper.setUrl("/URL.do");
        addURL(mapper);
        return mapper;
    }

    public URLMapper findByID(String id) {
        URLMapper result = null;
        Iterator it = urls.iterator();
        while (it.hasNext()) {
            URLMapper m = (URLMapper) it.next();
            if (m.getId().equals(id)) {
                result = m;
                break;
            }
        }
        return result;
    }
}
