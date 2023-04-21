package net.sf.jukebox.jmx;

/**
 * @author Copyright &copy; <a href="mailto:vt@freehold.crocodile.org">Vadim Tkachenko</a> 2007-2009
 */
public class JmxDescriptor {

    public final String domainName;

    public final String name;

    public final String instance;

    public final String description;

    public JmxDescriptor(String domainName, String name, String instance, String description) {
        this.domainName = domainName;
        this.name = name;
        this.instance = instance;
        this.description = description;
    }
}
