package org.simpleframework.servlet;

import java.util.ArrayList;
import java.util.List;
import org.simpleframework.servlet.resolve.Pattern;
import org.simpleframework.servlet.resolve.PatternParser;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;

@Root(name = "servlet-mapping")
class ServletMapping {

    @Element(name = "servlet-name")
    private String filterName;

    @ElementList(entry = "url-pattern", inline = true)
    private List<String> urlPatterns;

    @ElementList(name = "dispatcher", entry = "dispatcher", inline = true, required = false, empty = false)
    private List<String> dispatcherList;

    private List<Pattern> patterns;

    public ServletMapping() {
        this.patterns = new ArrayList<Pattern>();
    }

    @Commit
    private void commit() {
        for (String pattern : urlPatterns) {
            patterns.add(new PatternParser(pattern));
        }
    }

    public List<String> getDispatcher() {
        return dispatcherList;
    }

    public String getServletName() {
        return filterName;
    }

    public List<Pattern> getPatterns() {
        return patterns;
    }
}
