package org.jsmiparser.util.url;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompositeURLListFactory implements URLListFactory {

    private List<URLListFactory> children;

    public CompositeURLListFactory() {
        this(new ArrayList<URLListFactory>());
    }

    public CompositeURLListFactory(List<URLListFactory> children) {
        this.children = children;
    }

    public CompositeURLListFactory(URLListFactory... urlListFactories) {
        this(Arrays.asList(urlListFactories));
    }

    public List<URLListFactory> getChildren() {
        return children;
    }

    public void setChildren(List<URLListFactory> children) {
        this.children = children;
    }

    public List<URL> create() throws Exception {
        List<URL> result = new ArrayList<URL>();
        for (URLListFactory child : children) {
            result.addAll(child.create());
        }
        return result;
    }
}
