package net.sourceforge.blogentis.rss;

public class RSSVersion {

    protected String name;

    protected String template;

    protected String version;

    RSSVersion(String name, String template, String version) {
        this.name = name;
        this.template = template;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getTemplate() {
        return template;
    }

    public String getVersion() {
        return version;
    }
}
