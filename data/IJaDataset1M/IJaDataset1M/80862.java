package org.monet.kernel.model.definition;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "field-link")
public class LinkFieldDeclaration extends FieldDeclaration {

    public static class Link {

        @Attribute(name = "node")
        protected String node;

        @Attribute(name = "source")
        protected String source;

        public String getNode() {
            return node;
        }

        public String getSource() {
            return source;
        }
    }

    public static class AllowHistory {

        @Attribute(name = "datastore")
        protected String datastore;

        public String getDatastore() {
            return datastore;
        }
    }

    public static class AllowSearch {
    }

    @Element(name = "link", required = false)
    protected Link link;

    @Element(name = "allow-history", required = false)
    protected AllowHistory allowHistory;

    @Element(name = "allow-search", required = false)
    protected AllowSearch allowSearch;

    @Element
    protected LinkFieldViewDeclaration linkFieldViewDeclaration;

    public Link getLink() {
        return link;
    }

    public AllowHistory getAllowHistory() {
        return allowHistory;
    }

    public AllowSearch getAllowSearch() {
        return allowSearch;
    }

    public LinkFieldViewDeclaration getLinkFieldViewDeclaration() {
        return linkFieldViewDeclaration;
    }
}
